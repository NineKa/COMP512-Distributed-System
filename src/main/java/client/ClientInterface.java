package client;

import utils.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class ClientInterface {
    private final String middleware;
    private final int middlewarePort;

    public ClientInterface(String middleware, int port) {
        this.middleware = middleware;
        this.middlewarePort = port;
    }

    private String sendAndReceive(Message message) throws IOException, IllegalArgumentException {
        Socket socket = new Socket(middleware, middlewarePort);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        writer.println(message.toJSONString());
        writer.flush();
        String result = reader.readLine();
        if (result.equals("<IllegalArgumentException>")) throw new IllegalArgumentException();
        if (result.equals("<IOException>")) throw new IOException();
        return result;
    }

    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice)
            throws IOException, IllegalArgumentException {
        Message.AddFlight addFlight = new Message.AddFlight();
        addFlight.id = id;
        addFlight.flightNum = flightNum;
        addFlight.flightSeats = flightSeats;
        addFlight.flightPrice = flightPrice;

        return Boolean.parseBoolean(sendAndReceive(addFlight));
    }

    public boolean addCars(int id, String location, int numCars, int price)
            throws IOException, IllegalArgumentException {
        Message.AddCars addCars = new Message.AddCars();
        addCars.id = id;
        addCars.location = location;
        addCars.carNum = numCars;
        addCars.carPrice = price;

        return Boolean.parseBoolean(sendAndReceive(addCars));
    }

    public boolean addRooms(int id, String location, int numRooms, int price)
            throws IOException, IllegalArgumentException {
        Message.AddRooms addRooms = new Message.AddRooms();
        addRooms.id = id;
        addRooms.location = location;
        addRooms.roomNum = numRooms;
        addRooms.roomPrice = price;

        return Boolean.parseBoolean(sendAndReceive(addRooms));
    }

    public int newCustomer(int id)
            throws IOException, IllegalArgumentException {
        Message.NewCustomer newCustomer = new Message.NewCustomer();
        newCustomer.id = id;

        return Integer.parseInt(sendAndReceive(newCustomer));
    }

    public boolean newCustomer(int id, int cid)
            throws IOException, IllegalArgumentException {
        Message.NewCustomerWithCID newCustomerWithCID = new Message.NewCustomerWithCID();
        newCustomerWithCID.id = id;
        newCustomerWithCID.cid = cid;

        return Boolean.parseBoolean(sendAndReceive(newCustomerWithCID));
    }

    public boolean deleteFlight(int id, int flightNum)
            throws IOException, IllegalArgumentException {
        Message.DeleteFlight deleteFlight = new Message.DeleteFlight();
        deleteFlight.id = id;
        deleteFlight.flightNum = flightNum;

        return Boolean.parseBoolean(sendAndReceive(deleteFlight));
    }

    public boolean deleteCars(int id, String location)
            throws IOException, IllegalArgumentException {
        Message.DeleteCars deleteCars = new Message.DeleteCars();
        deleteCars.id = id;
        deleteCars.location = location;

        return Boolean.parseBoolean(sendAndReceive(deleteCars));
    }

    public boolean deleteRooms(int id, String location)
            throws IOException, IllegalArgumentException {
        Message.DeleteRooms deleteRooms = new Message.DeleteRooms();
        deleteRooms.id = id;
        deleteRooms.location = location;

        return Boolean.parseBoolean(sendAndReceive(deleteRooms));
    }

    public boolean deleteCustomer(int id, int customer)
            throws IOException, IllegalArgumentException {
        Message.DeleteCustomer deleteCustomer = new Message.DeleteCustomer();
        deleteCustomer.id = id;
        deleteCustomer.customer = customer;

        return Boolean.parseBoolean(sendAndReceive(deleteCustomer));
    }

    public int queryFlight(int id, int flightNumber)
            throws IOException, IllegalArgumentException {
        Message.QueryFlight queryFlight = new Message.QueryFlight();
        queryFlight.id = id;
        queryFlight.flightNum = flightNumber;

        return Integer.parseInt(sendAndReceive(queryFlight));
    }

    public int queryCars(int id, String location)
            throws IOException, IllegalArgumentException {
        Message.QueryCars queryCars = new Message.QueryCars();
        queryCars.id = id;
        queryCars.location = location;

        return Integer.parseInt(sendAndReceive(queryCars));
    }

    public int queryRooms(int id, String location)
            throws IOException, IllegalArgumentException {
        Message.QueryRooms queryRooms = new Message.QueryRooms();
        queryRooms.id = id;
        queryRooms.location = location;

        return Integer.parseInt(sendAndReceive(queryRooms));
    }

    public String queryCustomerInfo(int id, int customer)
            throws IOException, IllegalArgumentException {
        Message.QueryCustomerInfo queryCustomerInfo = new Message.QueryCustomerInfo();
        queryCustomerInfo.id = id;
        queryCustomerInfo.customer = customer;

        return sendAndReceive(queryCustomerInfo);
    }

    public int queryFlightPrice(int id, int flightNumber)
            throws IOException, IllegalArgumentException {
        Message.QueryFlightPrice queryFlightPrice = new Message.QueryFlightPrice();
        queryFlightPrice.id = id;
        queryFlightPrice.flightNum = flightNumber;

        return Integer.parseInt(sendAndReceive(queryFlightPrice));
    }

    public int queryCarsPrice(int id, String location)
            throws IOException, IllegalArgumentException {
        Message.QueryCarsPrice queryCarsPrice = new Message.QueryCarsPrice();
        queryCarsPrice.id = id;
        queryCarsPrice.location = location;

        return Integer.parseInt(sendAndReceive(queryCarsPrice));
    }

    public int queryRoomsPrice(int id, String location)
            throws IOException, IllegalArgumentException {
        Message.QueryRoomsPrice queryRoomsPrice = new Message.QueryRoomsPrice();
        queryRoomsPrice.id = id;
        queryRoomsPrice.location = location;

        return Integer.parseInt(sendAndReceive(queryRoomsPrice));
    }

    public boolean reserveFlight(int id, int customer, int flightNumber)
            throws IOException, IllegalArgumentException {
        Message.ReserveFlight reserveFlight = new Message.ReserveFlight();
        reserveFlight.id = id;
        reserveFlight.customer = customer;
        reserveFlight.flightNum = flightNumber;

        return Boolean.parseBoolean(sendAndReceive(reserveFlight));
    }

    public boolean reserveCar(int id, int customer, String location)
            throws IOException, IllegalArgumentException {
        Message.ReserveCar reserveCar = new Message.ReserveCar();
        reserveCar.id = id;
        reserveCar.customer = customer;
        reserveCar.location = location;

        return Boolean.parseBoolean(sendAndReceive(reserveCar));
    }

    public boolean reserveRoom(int id, int customer, String location)
            throws IOException, IllegalArgumentException {
        Message.ReserveRoom reserveRoom = new Message.ReserveRoom();
        reserveRoom.id = id;
        reserveRoom.customer = customer;
        reserveRoom.location = location;

        return Boolean.parseBoolean(sendAndReceive(reserveRoom));
    }

    public boolean itinerary(int id, int customer, Vector flightNumbers,
                             String location, boolean car, boolean room)
            throws IOException, IllegalArgumentException {
        Message.ReserveItinerary reserveItinerary = new Message.ReserveItinerary();
        reserveItinerary.id = id;
        reserveItinerary.customer = customer;
        for (Object object : flightNumbers) {
            if (object instanceof Integer) reserveItinerary.flightNums.add(((Integer) object));
            reserveItinerary.flightNums.add(Integer.parseInt(object.toString()));
        }
        reserveItinerary.location = location;
        reserveItinerary.reserveCar = car;
        reserveItinerary.reserveRoom = room;

        return  Boolean.parseBoolean(sendAndReceive(reserveItinerary));
    }
}
