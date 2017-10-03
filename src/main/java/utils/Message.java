package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class Message {
    private static final String MESSAGE_TYPE_TAG = "message_type";
    private static final String MESSAGE_CONTENT_TAG = "message_content";

    public enum MessageType {
        ADD_FLIGHT, ADD_CARS, ADD_ROOMS,
        NEW_CUSTOMER, NEW_CUSTOMER_WITH_CID,
        DELETE_FLIGHT, DELETE_CARS, DELETE_ROOMS, DELETE_CUSTOMER,
        QUERY_FLIGHT, QUERY_CARS, QUERY_ROOMS, QUERY_CUSTOMER_INFO,
        QUERY_FLIGHT_PRICE, QUERY_CARS_PRICE, QUERY_ROOMS_PRICE,
        RESERVE_FLIGHT, RESERVE_CAR, RESERVE_ROOM, RESERVE_ITINERARY,
        CANCEL_RESERVE_FLIGHT, CANCEL_RESERVE_CAR, CANCEL_RESERVE_ROOM;

        @Override
        public String toString() {
            switch (this) {
                case ADD_FLIGHT:
                    return "ADD_FLIGHT";
                case ADD_CARS:
                    return "ADD_CARS";
                case ADD_ROOMS:
                    return "ADD_ROOMS";
                case NEW_CUSTOMER:
                    return "NEW_CUSTOMER";
                case NEW_CUSTOMER_WITH_CID:
                    return "NEW_CUSTOMER_WITH_CID";
                case DELETE_FLIGHT:
                    return "DELETE_FLIGHT";
                case DELETE_CARS:
                    return "DELETE_CARS";
                case DELETE_ROOMS:
                    return "DELETE_ROOMS";
                case DELETE_CUSTOMER:
                    return "DELETE_CUSTOMER";
                case QUERY_FLIGHT:
                    return "QUERY_FLIGHT";
                case QUERY_CARS:
                    return "QUERY_CARS";
                case QUERY_ROOMS:
                    return "QUERY_ROOMS";
                case QUERY_CUSTOMER_INFO:
                    return "QUERY_CUSTOMER_INFO";
                case QUERY_FLIGHT_PRICE:
                    return "QUERY_FLIGHT_PRICE";
                case QUERY_CARS_PRICE:
                    return "QUERY_CARS_PRICE";
                case QUERY_ROOMS_PRICE:
                    return "QUERY_ROOMS_PRICE";
                case RESERVE_FLIGHT:
                    return "RESERVE_FLIGHT";
                case RESERVE_CAR:
                    return "RESERVE_CAR";
                case RESERVE_ROOM:
                    return "RESERVE_ROOM";
                case RESERVE_ITINERARY:
                    return "RESERVE_ITINERARY";
                case CANCEL_RESERVE_FLIGHT:
                    return "CANCEL_RESERVE_FLIGHT";
                case CANCEL_RESERVE_CAR:
                    return "CANCEL_RESERVE_CAR";
                case CANCEL_RESERVE_ROOM:
                    return "CANCEL_RESERVE_ROOM";
            }
            return null;
        }

        public static MessageType fromString(String string) {
            if (string.equals("ADD_FLIGHT")) return MessageType.ADD_FLIGHT;
            if (string.equals("ADD_CARS")) return MessageType.ADD_CARS;
            if (string.equals("ADD_ROOMS")) return MessageType.ADD_ROOMS;
            if (string.equals("NEW_CUSTOMER")) return MessageType.NEW_CUSTOMER;
            if (string.equals("NEW_CUSTOMER_WITH_CID")) return MessageType.NEW_CUSTOMER_WITH_CID;
            if (string.equals("DELETE_FLIGHT")) return MessageType.DELETE_FLIGHT;
            if (string.equals("DELETE_CARS")) return MessageType.DELETE_CARS;
            if (string.equals("DELETE_ROOMS")) return MessageType.DELETE_ROOMS;
            if (string.equals("DELETE_CUSTOMER")) return MessageType.DELETE_CUSTOMER;
            if (string.equals("QUERY_FLIGHT")) return MessageType.QUERY_FLIGHT;
            if (string.equals("QUERY_CARS")) return MessageType.QUERY_CARS;
            if (string.equals("QUERY_ROOMS")) return MessageType.QUERY_ROOMS;
            if (string.equals("QUERY_CUSTOMER_INFO")) return MessageType.QUERY_CUSTOMER_INFO;
            if (string.equals("QUERY_FLIGHT_PRICE")) return MessageType.QUERY_FLIGHT_PRICE;
            if (string.equals("QUERY_CARS_PRICE")) return MessageType.QUERY_CARS_PRICE;
            if (string.equals("QUERY_ROOMS_PRICE")) return MessageType.QUERY_ROOMS_PRICE;
            if (string.equals("RESERVE_FLIGHT")) return MessageType.RESERVE_FLIGHT;
            if (string.equals("RESERVE_CAR")) return MessageType.RESERVE_CAR;
            if (string.equals("RESERVE_ROOM")) return MessageType.RESERVE_ROOM;
            if (string.equals("RESERVE_ITINERARY")) return MessageType.RESERVE_ITINERARY;
            if (string.equals("CANCEL_RESERVE_FLIGHT")) return MessageType.CANCEL_RESERVE_FLIGHT;
            if (string.equals("CANCEL_RESERVE_CAR")) return MessageType.CANCEL_RESERVE_CAR;
            if (string.equals("CANCEL_RESERVE_ROOM")) return MessageType.CANCEL_RESERVE_ROOM;
            return null;
        }
    }

    public MessageType messageType;

    private Message(MessageType messageType) {
        this.messageType = messageType;
    }

    protected abstract Object toSerializedObject();

    public String toJSONString() {
        return (new JSONObject())
                .accumulate(MESSAGE_TYPE_TAG, messageType.toString())
                .accumulate(MESSAGE_CONTENT_TAG, this.toSerializedObject())
                .toString();
    }

    public static Message fromJSONString(String string) {
        JSONObject jsonObject = new JSONObject(string);
        MessageType messageType = MessageType.fromString(jsonObject.getString(MESSAGE_TYPE_TAG));
        if (messageType == null) throw new IllegalArgumentException();
        switch (messageType) {
            case ADD_FLIGHT:
                return AddFlight.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case ADD_CARS:
                return AddCars.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case ADD_ROOMS:
                return AddRooms.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case NEW_CUSTOMER:
                return NewCustomer.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case NEW_CUSTOMER_WITH_CID:
                return NewCustomerWithCID.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case DELETE_FLIGHT:
                return DeleteFlight.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case DELETE_CARS:
                return DeleteCars.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case DELETE_ROOMS:
                return DeleteRooms.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case DELETE_CUSTOMER:
                return DeleteCustomer.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case QUERY_FLIGHT:
                return QueryFlight.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case QUERY_CARS:
                return QueryCars.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case QUERY_ROOMS:
                return QueryRooms.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case QUERY_CUSTOMER_INFO:
                return QueryCustomerInfo.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case QUERY_FLIGHT_PRICE:
                return QueryFlightPrice.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case QUERY_CARS_PRICE:
                return QueryCarsPrice.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case QUERY_ROOMS_PRICE:
                return QueryRoomsPrice.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case RESERVE_FLIGHT:
                return ReserveFlight.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case RESERVE_CAR:
                return ReserveCar.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case RESERVE_ROOM:
                return ReserveRoom.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case RESERVE_ITINERARY:
                return ReserveItinerary.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case CANCEL_RESERVE_FLIGHT:
                return CancelReserveFlight.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case CANCEL_RESERVE_CAR:
                return CancelReserveCar.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
            case CANCEL_RESERVE_ROOM:
                return CancelReserveRoom.fromSerializedObject(jsonObject.get(MESSAGE_CONTENT_TAG));
        }
        return null;
    }

    @Override
    public String toString() {
        return toSerializedObject().toString();
    }

    public static class AddFlight extends Message {
        public int id;
        public int flightNum;
        public int flightSeats;
        public int flightPrice;

        public AddFlight() {
            super(MessageType.ADD_FLIGHT);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("flight_number", flightNum)
                    .accumulate("flight_seats", flightSeats)
                    .accumulate("flight_price", flightPrice);
        }

        private static AddFlight fromSerializedObject(Object object) {
            Message.AddFlight addFlight = new Message.AddFlight();
            addFlight.id = ((JSONObject) object).getInt("id");
            addFlight.flightNum = ((JSONObject) object).getInt("flight_number");
            addFlight.flightSeats = ((JSONObject) object).getInt("flight_seats");
            addFlight.flightPrice = ((JSONObject) object).getInt("flight_price");
            return addFlight;
        }
    }

    public static class AddCars extends Message {
        public int id;
        public String location;
        public int carNum;
        public int carPrice;

        public AddCars() {
            super(MessageType.ADD_CARS);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location)
                    .accumulate("number", carNum)
                    .accumulate("price", carPrice);
        }

        private static AddCars fromSerializedObject(Object object) {
            Message.AddCars addCars = new AddCars();
            addCars.id = ((JSONObject) object).getInt("id");
            addCars.location = ((JSONObject) object).getString("location");
            addCars.carNum = ((JSONObject) object).getInt("number");
            addCars.carPrice = ((JSONObject) object).getInt("price");
            return addCars;
        }
    }

    public static class AddRooms extends Message {
        public int id;
        public String location;
        public int roomNum;
        public int roomPrice;

        public AddRooms() {
            super(MessageType.ADD_ROOMS);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location)
                    .accumulate("number", roomNum)
                    .accumulate("price", roomPrice);
        }

        private static AddRooms fromSerializedObject(Object object) {
            Message.AddRooms addRooms = new AddRooms();
            addRooms.id = ((JSONObject) object).getInt("id");
            addRooms.location = ((JSONObject) object).getString("location");
            addRooms.roomNum = ((JSONObject) object).getInt("number");
            addRooms.roomPrice = ((JSONObject) object).getInt("price");
            return addRooms;
        }
    }

    public static class NewCustomer extends Message {
        public int id;

        public NewCustomer() {
            super(MessageType.NEW_CUSTOMER);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject()).accumulate("id", id);
        }

        private static NewCustomer fromSerializedObject(Object object) {
            Message.NewCustomer newCustomer = new Message.NewCustomer();
            newCustomer.id = ((JSONObject) object).getInt("id");
            return newCustomer;
        }
    }

    public static class NewCustomerWithCID extends Message {
        public int id;
        public int cid;

        public NewCustomerWithCID() {
            super(MessageType.NEW_CUSTOMER_WITH_CID);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("cid", cid);
        }

        private static NewCustomerWithCID fromSerializedObject(Object object) {
            Message.NewCustomerWithCID newCustomerWithCID = new Message.NewCustomerWithCID();
            newCustomerWithCID.id = ((JSONObject) object).getInt("id");
            newCustomerWithCID.cid = ((JSONObject) object).getInt("cid");
            return newCustomerWithCID;
        }
    }

    public static class DeleteFlight extends Message {
        public int id;
        public int flightNum;

        public DeleteFlight() {
            super(MessageType.DELETE_FLIGHT);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("flight_number", flightNum);
        }

        private static DeleteFlight fromSerializedObject(Object object) {
            Message.DeleteFlight deleteFlight = new Message.DeleteFlight();
            deleteFlight.id = ((JSONObject) object).getInt("id");
            deleteFlight.flightNum = ((JSONObject) object).getInt("flight_number");
            return deleteFlight;
        }
    }

    public static class DeleteCars extends Message {
        public int id;
        public String location;

        public DeleteCars() {
            super(MessageType.DELETE_CARS);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location);
        }

        private static DeleteCars fromSerializedObject(Object object) {
            Message.DeleteCars deleteCars = new Message.DeleteCars();
            deleteCars.id = ((JSONObject) object).getInt("id");
            deleteCars.location = ((JSONObject) object).getString("location");
            return deleteCars;
        }
    }

    public static class DeleteRooms extends Message {
        public int id;
        public String location;

        public DeleteRooms() {
            super(MessageType.DELETE_ROOMS);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location);
        }

        private static DeleteRooms fromSerializedObject(Object object) {
            Message.DeleteRooms deleteRooms = new Message.DeleteRooms();
            deleteRooms.id = ((JSONObject) object).getInt("id");
            deleteRooms.location = ((JSONObject) object).getString("location");
            return deleteRooms;
        }
    }

    public static class DeleteCustomer extends Message {
        public int id;
        public int customer;

        public DeleteCustomer() {
            super(MessageType.DELETE_CUSTOMER);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer);
        }

        private static DeleteCustomer fromSerializedObject(Object object) {
            Message.DeleteCustomer deleteCustomer = new Message.DeleteCustomer();
            deleteCustomer.id = ((JSONObject) object).getInt("id");
            deleteCustomer.customer = ((JSONObject) object).getInt("customer");
            return deleteCustomer;
        }
    }

    public static class QueryFlight extends Message {
        public int id;
        public int flightNum;

        public QueryFlight() {
            super(MessageType.QUERY_FLIGHT);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("flight_number", flightNum);
        }

        private static QueryFlight fromSerializedObject(Object object) {
            Message.QueryFlight queryFlight = new Message.QueryFlight();
            queryFlight.id = ((JSONObject) object).getInt("id");
            queryFlight.flightNum = ((JSONObject) object).getInt("flight_number");
            return queryFlight;
        }
    }

    public static class QueryCars extends Message {
        public int id;
        public String location;

        public QueryCars() {
            super(MessageType.QUERY_CARS);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location);
        }

        private static QueryCars fromSerializedObject(Object object) {
            Message.QueryCars queryCars = new Message.QueryCars();
            queryCars.id = ((JSONObject) object).getInt("id");
            queryCars.location = ((JSONObject) object).getString("location");
            return queryCars;
        }
    }

    public static class QueryRooms extends Message {
        public int id;
        public String location;

        public QueryRooms() {
            super(MessageType.QUERY_ROOMS);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location);
        }

        private static QueryRooms fromSerializedObject(Object object) {
            Message.QueryRooms queryRooms = new Message.QueryRooms();
            queryRooms.id = ((JSONObject) object).getInt("id");
            queryRooms.location = ((JSONObject) object).getString("location");
            return queryRooms;
        }
    }

    public static class QueryCustomerInfo extends Message {
        public int id;
        public int customer;

        public QueryCustomerInfo() {
            super(MessageType.QUERY_CUSTOMER_INFO);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer);
        }

        private static QueryCustomerInfo fromSerializedObject(Object object) {
            Message.QueryCustomerInfo queryCustomerInfo = new Message.QueryCustomerInfo();
            queryCustomerInfo.id = ((JSONObject) object).getInt("id");
            queryCustomerInfo.customer = ((JSONObject) object).getInt("customer");
            return queryCustomerInfo;
        }
    }

    public static class QueryFlightPrice extends Message {
        public int id;
        public int flightNum;

        public QueryFlightPrice() {
            super(MessageType.QUERY_FLIGHT_PRICE);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("flight_number", flightNum);
        }

        private static QueryFlightPrice fromSerializedObject(Object object) {
            Message.QueryFlightPrice queryFlightPrice = new Message.QueryFlightPrice();
            queryFlightPrice.id = ((JSONObject) object).getInt("id");
            queryFlightPrice.flightNum = ((JSONObject) object).getInt("flight_number");
            return queryFlightPrice;
        }
    }

    public static class QueryCarsPrice extends Message {
        public int id;
        public String location;

        public QueryCarsPrice() {
            super(MessageType.QUERY_CARS_PRICE);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location);
        }

        private static QueryCarsPrice fromSerializedObject(Object object) {
            Message.QueryCarsPrice queryCarsPrice = new Message.QueryCarsPrice();
            queryCarsPrice.id = ((JSONObject) object).getInt("id");
            queryCarsPrice.location = ((JSONObject) object).getString("location");
            return queryCarsPrice;
        }
    }

    public static class QueryRoomsPrice extends Message {
        public int id;
        public String location;

        public QueryRoomsPrice() {
            super(MessageType.QUERY_ROOMS_PRICE);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("location", location);
        }

        private static QueryRoomsPrice fromSerializedObject(Object object) {
            Message.QueryRoomsPrice queryRoomsPrice = new Message.QueryRoomsPrice();
            queryRoomsPrice.id = ((JSONObject) object).getInt("id");
            queryRoomsPrice.location = ((JSONObject) object).getString("location");
            return queryRoomsPrice;
        }
    }

    public static class ReserveFlight extends Message {
        public int id;
        public int customer;
        public int flightNum;

        public ReserveFlight() {
            super(MessageType.RESERVE_FLIGHT);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer)
                    .accumulate("flight_number", flightNum);
        }

        private static ReserveFlight fromSerializedObject(Object object) {
            Message.ReserveFlight reserveFlight = new Message.ReserveFlight();
            reserveFlight.id = ((JSONObject) object).getInt("id");
            reserveFlight.customer = ((JSONObject) object).getInt("customer");
            reserveFlight.flightNum = ((JSONObject) object).getInt("flight_number");
            return reserveFlight;
        }
    }

    public static class ReserveCar extends Message {
        public int id;
        public int customer;
        public String location;

        public ReserveCar() {
            super(MessageType.RESERVE_CAR);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer)
                    .accumulate("location", location);
        }

        private static ReserveCar fromSerializedObject(Object object) {
            Message.ReserveCar reserveCar = new Message.ReserveCar();
            reserveCar.id = ((JSONObject) object).getInt("id");
            reserveCar.customer = ((JSONObject) object).getInt("customer");
            reserveCar.location = ((JSONObject) object).getString("location");
            return reserveCar;
        }
    }

    public static class ReserveRoom extends Message {
        public int id;
        public int customer;
        public String location;

        public ReserveRoom() {
            super(MessageType.RESERVE_ROOM);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer)
                    .accumulate("location", location);
        }

        private static ReserveRoom fromSerializedObject(Object object) {
            Message.ReserveRoom reserveRoom = new Message.ReserveRoom();
            reserveRoom.id = ((JSONObject) object).getInt("id");
            reserveRoom.customer = ((JSONObject) object).getInt("customer");
            reserveRoom.location = ((JSONObject) object).getString("location");
            return reserveRoom;
        }
    }

    public static class ReserveItinerary extends Message {
        public int id;
        public int customer;
        public ArrayList<Integer> flightNums = new ArrayList<>();
        public String location;
        public boolean reserveCar;
        public boolean reserveRoom;

        public ReserveItinerary() {
            super(MessageType.RESERVE_ITINERARY);
        }

        @Override
        protected Object toSerializedObject() {
            JSONArray flightNumsArray = new JSONArray();
            for (int flightNum : flightNums) flightNumsArray.put(flightNum);
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer)
                    .accumulate("location", location)
                    .put("flight_numbers", flightNumsArray)
                    .accumulate("reserve_car", reserveCar)
                    .accumulate("reserve_room", reserveRoom);

        }

        private static ReserveItinerary fromSerializedObject(Object object) {
            Message.ReserveItinerary reserveItinerary = new Message.ReserveItinerary();
            reserveItinerary.id = ((JSONObject) object).getInt("id");
            reserveItinerary.customer = ((JSONObject) object).getInt("customer");
            JSONArray flightNumsArray = ((JSONObject) object).getJSONArray("flight_numbers");
            for (int i = 0; i < flightNumsArray.length(); ++i) {
                reserveItinerary.flightNums.add(flightNumsArray.getInt(i));
            }
            reserveItinerary.location = ((JSONObject) object).getString("location");
            reserveItinerary.reserveCar = ((JSONObject) object).getBoolean("reserve_car");
            reserveItinerary.reserveRoom = ((JSONObject) object).getBoolean("reserve_room");
            return reserveItinerary;
        }
    }

    public static class CancelReserveFlight extends Message {
        public int id;
        public int customer;
        public int flightNum;

        public CancelReserveFlight() {
            super(MessageType.CANCEL_RESERVE_FLIGHT);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer)
                    .accumulate("flight_number", flightNum);
        }

        private static CancelReserveFlight fromSerializedObject(Object object) {
            Message.CancelReserveFlight cancelReserveFlight = new Message.CancelReserveFlight();
            cancelReserveFlight.id = ((JSONObject) object).getInt("id");
            cancelReserveFlight.customer = ((JSONObject) object).getInt("customer");
            cancelReserveFlight.flightNum = ((JSONObject) object).getInt("flight_number");
            return cancelReserveFlight;
        }
    }

    public static class CancelReserveCar extends Message {
        public int id;
        public int customer;
        public String location;

        public CancelReserveCar() {
            super(MessageType.CANCEL_RESERVE_CAR);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer)
                    .accumulate("location", location);
        }

        private static CancelReserveCar fromSerializedObject(Object object) {
            Message.CancelReserveCar cancelReserveCar = new Message.CancelReserveCar();
            cancelReserveCar.id = ((JSONObject) object).getInt("id");
            cancelReserveCar.customer = ((JSONObject) object).getInt("customer");
            cancelReserveCar.location = ((JSONObject) object).getString("location");
            return cancelReserveCar;
        }
    }

    public static class CancelReserveRoom extends Message {
        public int id;
        public int customer;
        public String location;

        public CancelReserveRoom() {
            super(MessageType.CANCEL_RESERVE_ROOM);
        }

        @Override
        protected Object toSerializedObject() {
            return (new JSONObject())
                    .accumulate("id", id)
                    .accumulate("customer", customer)
                    .accumulate("location", location);
        }

        private static CancelReserveRoom fromSerializedObject(Object object) {
            Message.CancelReserveRoom cancelReserveRoom = new Message.CancelReserveRoom();
            cancelReserveRoom.id = ((JSONObject) object).getInt("id");
            cancelReserveRoom.customer = ((JSONObject) object).getInt("customer");
            cancelReserveRoom.location = ((JSONObject) object).getString("location");
            return cancelReserveRoom;
        }
    }
}
