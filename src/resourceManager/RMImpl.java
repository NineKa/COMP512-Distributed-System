package resourceManager;

import Interfaces.CommandType;
import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;
import client.ClientSocket;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by emol on 10/1/17.
 */
public class RMImpl {
    protected RMHashtable m_itemHT;

    public RMImpl(RMHashtable m_itemHT) {
        this.m_itemHT = m_itemHT;
    }


    // Reads a data item
    public RMItem readData( int id, String key )
    {
        synchronized(m_itemHT) {
            return (RMItem) m_itemHT.get(key);
        }
    }

    // Writes a data item
    public void writeData( int id, String key, RMItem value )
    {
        synchronized(m_itemHT) {
            m_itemHT.put(key, value);
        }
    }

    // Remove the item out of storage
    public RMItem removeData(int id, String key) {
        synchronized(m_itemHT) {
            return (RMItem)m_itemHT.remove(key);
        }
    }


    // deletes the entire item
    protected boolean deleteItem(int id, String key)
    {
        Trace.info("RM::deleteItem(" + id + ", " + key + ") called" );
        ReservableItem curObj = (ReservableItem) readData( id, key );
        // Check if there is such an item in the storage
        if ( curObj == null ) {
            Trace.warn("RM::deleteItem(" + id + ", " + key + ") failed--item doesn't exist" );
            return false;
        } else {
            if (curObj.getReserved()==0) {
                removeData(id, curObj.getKey());
                Trace.info("RM::deleteItem(" + id + ", " + key + ") item deleted" );
                return true;
            }
            else {
                Trace.info("RM::deleteItem(" + id + ", " + key + ") item can't be deleted because some customers reserved it" );
                return false;
            }
        }
    }


    // query the number of available seats/rooms/cars
    protected int queryNum(int id, String key) {
        Trace.info("RM::queryNum(" + id + ", " + key + ") called" );
        ReservableItem curObj = (ReservableItem) readData( id, key);
        int value = 0;
        if ( curObj != null ) {
            value = curObj.getCount();
        } // else
        Trace.info("RM::queryNum(" + id + ", " + key + ") returns count=" + value);
        return value;
    }


    // query the price of an item
    protected int queryPrice(int id, String key) {
        Trace.info("RM::queryCarsPrice(" + id + ", " + key + ") called" );
        ReservableItem curObj = (ReservableItem) readData( id, key);
        int value = 0;
        if ( curObj != null ) {
            value = curObj.getPrice();
        } // else
        Trace.info("RM::queryCarsPrice(" + id + ", " + key + ") returns cost=$" + value );
        return value;
    }


    // called in middleware server
    public Reply customerReserve(ClientSocket cs, Msg m){
        int id = Integer.parseInt((String) m.arg.elementAt(1));

        int customerID = Integer.parseInt((String) m.arg.elementAt(2));
        String other = (String) m.arg.elementAt(3); // location for car/room, flightNum for flight

        Reply r = new Reply(false, new Vector());
        // Read customer object if it exists (and read lock it)
        Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
        if ( cust == null ) {{
            Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + other +")  failed--customer doesn't exist" );
            return r;
        }}

        r = cs.execute(m);

        if (r.isSuccess){
            String key = (String) r.response.elementAt(1);
            int price = (int) r.response.elementAt(0);
            Trace.info("RM::reserveItem( " + id + ", customer=" + customerID + ", " +key+ ", "+ other +" ) called" );
            cust.reserve( key, other, price);
            writeData( id, cust.getKey(), cust );
        }
        return r;
    }


    // called in middleware server
    public Reply customerReserveItinerary(HashMap<ServerType, ClientSocket> css, Msg m){
        int id = Integer.parseInt((String) m.arg.elementAt(1));

        int customerID = Integer.parseInt((String) m.arg.elementAt(2));
        int[] flightNums = new int[m.arg.size() - 6];

        for (int i = 0; i < m.arg.size() - 6; i++)
            flightNums[i] = Integer.parseInt((String) m.arg.elementAt(3 + i));

        String location = (String) m.arg.elementAt(m.arg.size() - 3); // location for car/room, flightNum for flight
        boolean carToBook = new Boolean((String) m.arg.elementAt(m.arg.size() - 2)).booleanValue();
        boolean roomToBook = new Boolean((String) m.arg.elementAt(m.arg.size() - 1)).booleanValue();



        Reply r = new Reply(false, new Vector());
        // Read customer object if it exists (and read lock it)
        Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
        if ( cust == null ) {{
            Trace.warn("RM::reserveItinerary( " + id + ", " + customerID + ", " + ")  failed--customer doesn't exist" );
            return r;
        }}

        // split msg to sub msgs
        boolean rollback = false;
        HashMap<Msg, Reply> subMsg = new HashMap<>();
        for (int i = 0; i< flightNums.length; i++){
            Msg subM = new Msg();
            subM.cmd = CommandType.reserveFlight;
            subM.arg = new Vector();
            subM.arg.add(CommandType.reserveFlight);
            subM.arg.add("" + id);
            subM.arg.add("" + customerID);
            subM.arg.add("" + flightNums[i]);
            Reply subR = css.get(ServerType.Flight).execute(subM);
            subMsg.put(subM, subR);
            if (!subR.isSuccess) {
                rollback = true;
                break;
            }
        }
        if (!rollback){
            if (carToBook){
                Msg subM = new Msg();
                subM.cmd = CommandType.reserveCar;
                subM.arg = new Vector();
                subM.arg.add(CommandType.reserveCar);
                subM.arg.add("" + id);
                subM.arg.add("" + customerID);
                subM.arg.add(location);
                Reply subR = css.get(ServerType.Car).execute(subM);
                subMsg.put(subM, subR);
                if (!subR.isSuccess) {
                    rollback = true;
                }
            }
            if (!rollback && roomToBook){
                Msg subM = new Msg();
                subM.cmd = CommandType.reserveRoom;
                subM.arg = new Vector();
                subM.arg.add(CommandType.reserveRoom);
                subM.arg.add("" + id);
                subM.arg.add("" + customerID);
                subM.arg.add(location);
                Reply subR = css.get(ServerType.Room).execute(subM);
                subMsg.put(subM, subR);
                if (!subR.isSuccess) {
                    rollback = true;
                }
            }
        }

        if (rollback){
            for (Msg msg : subMsg.keySet()){
                if (subMsg.get(msg).isSuccess){
                    ClientSocket cs = null;
                    // roll back -> send reverse cmd
                    Msg rMsg = new Msg();
                    switch (msg.cmd){
                        case reserveRoom:
                            rMsg.cmd = CommandType.unreserveRoom;
                            cs = css.get(ServerType.Room);
                            break;
                        case reserveFlight:
                            rMsg.cmd = CommandType.unreserveFlight;
                            cs = css.get(ServerType.Flight);
                            break;
                        case reserveCar:
                            rMsg.cmd = CommandType.unreserveCar;
                            cs = css.get(ServerType.Car);
                            break;
                    }
                    rMsg.arg = msg.arg;
                    Reply rr = cs.execute(rMsg);
                }
            }

            return new Reply(false, null);
        }

        else {
            // all success
            for (Msg msg : subMsg.keySet()){
                String key = (String) subMsg.get(msg).response.elementAt(1);
                int price = (int) subMsg.get(msg).response.elementAt(0);
                String other = (String) msg.arg.elementAt(3);
                Trace.info("RM::reserveItem( " + id + ", customer=" + customerID + ", " +key+ ", "+ other +" ) called" );
                cust.reserve( key, other, price);
                writeData( id, cust.getKey(), cust );
            }
        }
        return new Reply(true, null);

    }

    // called in middleware server
    public Reply customerUnreserve(ClientSocket cs, Msg m){
        int id = Integer.parseInt((String) m.arg.elementAt(1));

        int customerID = Integer.parseInt((String) m.arg.elementAt(2));
        String other = (String) m.arg.elementAt(3); // location for car/room, flightNum for flight

        Reply r = new Reply(false, new Vector());
        // Read customer object if it exists (and read lock it)
        Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
        if ( cust == null ) {{
            Trace.warn("RM::unreserveItem( " + id + ", " + customerID + ", " + other +")  failed--customer doesn't exist" );
            return r;
        }}

        r = cs.execute(m);

        if (r.isSuccess){
            String key = (String) r.response.elementAt(1);
            int price = (int) r.response.elementAt(0);
            Trace.info("RM::unreserveItem( " + id + ", customer=" + customerID + ", " +key+ ", "+ other +" ) called" );
            cust.reserve( key, other, price);
            writeData( id, cust.getKey(), cust );
        }
        return r;
    }

    // reserve an item
    protected Reply reserveItem(int id, int customerID, String key, String location) {
        Reply r = new Reply(false, new Vector());

        Trace.info("RM::reserveItem( " + id + ", customer=" + customerID + ", " +key+ ", "+location+" ) called" );


        // check if the item is available
        ReservableItem item = (ReservableItem)readData(id, key);
        if ( item == null ) {
            Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + key+", " +location+") failed--item doesn't exist" );
        } else if (item.getCount()==0) {
            Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + key+", " + location+") failed--No more items" );
        } else {

            // decrease the number of available items in the storage
            item.setCount(item.getCount() - 1);
            item.setReserved(item.getReserved()+1);

            Trace.info("RM::reserveItem( " + id + ", " + customerID + ", " + key + ", " +location+") succeeded" );
            r.isSuccess = true;
            r.response.add(item.getPrice());
            r.response.add(item.getKey());
        }

        return r;
    }

    // reserve an item
    protected Reply unreserveItem(int id, int customerID, String key, String location) {
        Reply r = new Reply(false, new Vector());

        Trace.info("RM::unreserveItem( " + id + ", customer=" + customerID + ", " +key+ ", "+location+" ) called" );


        // check if the item is available
        ReservableItem item = (ReservableItem)readData(id, key);
        if ( item == null ) {
            Trace.warn("RM::unreserveItem( " + id + ", " + customerID + ", " + key+", " +location+") failed--item doesn't exist" );
        }
        else {

            // increase the number of available items in the storage
            item.setCount(item.getCount() + 1);
            item.setReserved(item.getReserved() - 1);

            Trace.info("RM::unreserveItem( " + id + ", " + customerID + ", " + key + ", " +location+") succeeded" );
            r.isSuccess = true;
            r.response.add(item.getPrice());
            r.response.add(item.getKey());
        }

        return r;
    }

    // Create a new flight, or add seats to existing flight
    //  NOTE: if flightPrice <= 0 and the flight already exists, it maintains its current price
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice)
    {
        Trace.info("RM::addFlight(" + id + ", " + flightNum + ", $" + flightPrice + ", " + flightSeats + ") called" );
        Flight curObj = (Flight) readData( id, Flight.getKey(flightNum) );
        if ( curObj == null ) {
            // doesn't exist...add it
            Flight newObj = new Flight( flightNum, flightSeats, flightPrice );
            writeData( id, newObj.getKey(), newObj );
            Trace.info("RM::addFlight(" + id + ") created new flight " + flightNum + ", seats=" +
                    flightSeats + ", price=$" + flightPrice );
        } else {
            // add seats to existing flight and update the price...
            curObj.setCount( curObj.getCount() + flightSeats );
            if ( flightPrice > 0 ) {
                curObj.setPrice( flightPrice );
            } // if
            writeData( id, curObj.getKey(), curObj );
            Trace.info("RM::addFlight(" + id + ") modified existing flight " + flightNum + ", seats=" + curObj.getCount() + ", price=$" + flightPrice );
        } // else
        return(true);
    }



    public boolean deleteFlight(int id, int flightNum)
    {
        return deleteItem(id, Flight.getKey(flightNum));
    }



    // Create a new room location or add rooms to an existing location
    //  NOTE: if price <= 0 and the room location already exists, it maintains its current price
    public boolean addRooms(int id, String location, int count, int price)
    {
        Trace.info("RM::addRooms(" + id + ", " + location + ", " + count + ", $" + price + ") called" );
        Hotel curObj = (Hotel) readData( id, Hotel.getKey(location) );
        if ( curObj == null ) {
            // doesn't exist...add it
            Hotel newObj = new Hotel( location, count, price );
            writeData( id, newObj.getKey(), newObj );
            Trace.info("RM::addRooms(" + id + ") created new room location " + location + ", count=" + count + ", price=$" + price );
        } else {
            // add count to existing object and update price...
            curObj.setCount( curObj.getCount() + count );
            if ( price > 0 ) {
                curObj.setPrice( price );
            } // if
            writeData( id, curObj.getKey(), curObj );
            Trace.info("RM::addRooms(" + id + ") modified existing location " + location + ", count=" + curObj.getCount() + ", price=$" + price );
        } // else
        return(true);
    }

    // Delete rooms from a location
    public boolean deleteRooms(int id, String location)
    {
        return deleteItem(id, Hotel.getKey(location));

    }

    // Create a new car location or add cars to an existing location
    //  NOTE: if price <= 0 and the location already exists, it maintains its current price
    public boolean addCars(int id, String location, int count, int price)
    {
        Trace.info("RM::addCars(" + id + ", " + location + ", " + count + ", $" + price + ") called" );
        Car curObj = (Car) readData( id, Car.getKey(location) );
        if ( curObj == null ) {
            // car location doesn't exist...add it
            Car newObj = new Car( location, count, price );
            writeData( id, newObj.getKey(), newObj );
            Trace.info("RM::addCars(" + id + ") created new location " + location + ", count=" + count + ", price=$" + price );
        } else {
            // add count to existing car location and update price...
            curObj.setCount( curObj.getCount() + count );
            if ( price > 0 ) {
                curObj.setPrice( price );
            } // if
            writeData( id, curObj.getKey(), curObj );
            Trace.info("RM::addCars(" + id + ") modified existing location " + location + ", count=" + curObj.getCount() + ", price=$" + price );
        } // else
        return(true);
    }


    // Delete cars from a location
    public boolean deleteCars(int id, String location)
    {
        return deleteItem(id, Car.getKey(location));
    }



    // Returns the number of empty seats on this flight
    public int queryFlight(int id, int flightNum)
    {
        return queryNum(id, Flight.getKey(flightNum));
    }

    // Returns the number of reservations for this flight.
//    public int queryFlightReservations(int id, int flightNum)
//        throws RemoteException
//    {
//        Trace.info("RM::queryFlightReservations(" + id + ", #" + flightNum + ") called" );
//        RMInteger numReservations = (RMInteger) readData( id, Flight.getNumReservationsKey(flightNum) );
//        if ( numReservations == null ) {
//            numReservations = new RMInteger(0);
//        } // if
//        Trace.info("RM::queryFlightReservations(" + id + ", #" + flightNum + ") returns " + numReservations );
//        return numReservations.getValue();
//    }


    // Returns price of this flight
    public int queryFlightPrice(int id, int flightNum )
    {
        return queryPrice(id, Flight.getKey(flightNum));
    }


    // Returns the number of rooms available at a location
    public int queryRooms(int id, String location)
    {
        return queryNum(id, Hotel.getKey(location));
    }




    // Returns room price at this location
    public int queryRoomsPrice(int id, String location)
    {
        return queryPrice(id, Hotel.getKey(location));
    }


    // Returns the number of cars available at a location
    public int queryCars(int id, String location)
    {
        return queryNum(id, Car.getKey(location));
    }


    // Returns price of cars at this location
    public int queryCarsPrice(int id, String location)
    {
        return queryPrice(id, Car.getKey(location));
    }

    // Returns data structure containing customer reservation info. Returns null if the
    //  customer doesn't exist. Returns empty RMHashtable if customer exists but has no
    //  reservations.
    public RMHashtable getCustomerReservations(int id, int customerID)
    {
        Trace.info("RM::getCustomerReservations(" + id + ", " + customerID + ") called" );
        Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
        if ( cust == null ) {
            Trace.warn("RM::getCustomerReservations failed(" + id + ", " + customerID + ") failed--customer doesn't exist" );
            return null;
        } else {
            return cust.getReservations();
        } // if
    }

    // return a bill
    public String queryCustomerInfo(int id, int customerID)
    {
        Trace.info("RM::queryCustomerInfo(" + id + ", " + customerID + ") called" );
        Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
        if ( cust == null ) {
            Trace.warn("RM::queryCustomerInfo(" + id + ", " + customerID + ") failed--customer doesn't exist" );
            return "";   // NOTE: don't change this--WC counts on this value indicating a customer does not exist...
        } else {
            String s = cust.printBill();
            Trace.info("RM::queryCustomerInfo(" + id + ", " + customerID + "), bill follows..." );
            System.out.println( s );
            return s;
        } // if
    }

    // customer functions
    // new customer just returns a unique customer identifier

    public int newCustomer(int id)
    {
        Trace.info("INFO: RM::newCustomer(" + id + ") called" );
        // Generate a globally unique ID for the new customer
        int cid = Integer.parseInt( String.valueOf(id) +
                String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
                String.valueOf( Math.round( Math.random() * 100 + 1 )));
        Customer cust = new Customer( cid );
        writeData( id, cust.getKey(), cust );
        Trace.info("RM::newCustomer(" + cid + ") returns ID=" + cid );
        return cid;
    }

    // I opted to pass in customerID instead. This makes testing easier
    public boolean newCustomer(int id, int customerID )
    {
        Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") called" );
        Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
        if ( cust == null ) {
            cust = new Customer(customerID);
            writeData( id, cust.getKey(), cust );
            Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") created a new customer" );
            return true;
        } else {
            Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") failed--customer already exists");
            return false;
        } // else
    }


    // Deletes customer from the database.
    public boolean deleteCustomer(int id, int customerID)
    {
        Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") called" );
        Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
        if ( cust == null ) {
            Trace.warn("RM::deleteCustomer(" + id + ", " + customerID + ") failed--customer doesn't exist" );
            return false;
        } else {
            // Increase the reserved numbers of all reservable items which the customer reserved.
            RMHashtable reservationHT = cust.getReservations();
            for (Enumeration e = reservationHT.keys(); e.hasMoreElements();) {
                String reservedkey = (String) (e.nextElement());
                ReservedItem reserveditem = cust.getReservedItem(reservedkey);
                Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + " " +  reserveditem.getCount() +  " times"  );
                ReservableItem item  = (ReservableItem) readData(id, reserveditem.getKey());
                Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
                item.setReserved(item.getReserved()-reserveditem.getCount());
                item.setCount(item.getCount()+reserveditem.getCount());
            }

            // remove the customer from the storage
            removeData(id, cust.getKey());

            Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") succeeded" );
            return true;
        } // if
    }



    /*
    // Frees flight reservation record. Flight reservation records help us make sure we
    // don't delete a flight if one or more customers are holding reservations
    public boolean freeFlightReservation(int id, int flightNum)
        throws RemoteException
    {
        Trace.info("RM::freeFlightReservations(" + id + ", " + flightNum + ") called" );
        RMInteger numReservations = (RMInteger) readData( id, Flight.getNumReservationsKey(flightNum) );
        if ( numReservations != null ) {
            numReservations = new RMInteger( Math.max( 0, numReservations.getValue()-1) );
        } // if
        writeData(id, Flight.getNumReservationsKey(flightNum), numReservations );
        Trace.info("RM::freeFlightReservations(" + id + ", " + flightNum + ") succeeded, this flight now has "
                + numReservations + " reservations" );
        return true;
    }
    */


    // Adds car reservation to this customer.
    public Reply reserveCar(int id, int customerID, String location)
    {
        return reserveItem(id, customerID, Car.getKey(location), location);
    }


    // Adds room reservation to this customer.
    public Reply reserveRoom(int id, int customerID, String location)
    {
        return reserveItem(id, customerID, Hotel.getKey(location), location);
    }
    // Adds flight reservation to this customer.
    public Reply reserveFlight(int id, int customerID, int flightNum)
    {
        return reserveItem(id, customerID, Flight.getKey(flightNum), String.valueOf(flightNum));
    }

    // Reserve an itinerary
    public boolean itinerary(int id, int customer, Vector flightNumbers, String location, boolean Car, boolean Room)
    {
        return false;
    }

}
