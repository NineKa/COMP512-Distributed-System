package utils;

public class MessageBaseHandler<T> implements MessageHandler<T> {
    public T dispatch(Message message) {
        switch (message.messageType) {
            case ADD_FLIGHT:
                return caseAddFlight(((Message.AddFlight) message));
            case ADD_CARS:
                return caseAddCars(((Message.AddCars) message));
            case ADD_ROOMS:
                return caseAddRooms(((Message.AddRooms) message));
            case NEW_CUSTOMER:
                return caseNewCustomer(((Message.NewCustomer) message));
            case NEW_CUSTOMER_WITH_CID:
                return caseNewCustomerWithCID(((Message.NewCustomerWithCID) message));
            case DELETE_FLIGHT:
                return caseDeleteFlight(((Message.DeleteFlight) message));
            case DELETE_CARS:
                return caseDeleteCars(((Message.DeleteCars) message));
            case DELETE_ROOMS:
                return caseDeleteRooms(((Message.DeleteRooms) message));
            case DELETE_CUSTOMER:
                return caseDeleteCustomer(((Message.DeleteCustomer) message));
            case QUERY_FLIGHT:
                return caseQueryFlight(((Message.QueryFlight) message));
            case QUERY_CARS:
                return caseQueryCars(((Message.QueryCars) message));
            case QUERY_ROOMS:
                return caseQueryRooms(((Message.QueryRooms) message));
            case QUERY_CUSTOMER_INFO:
                return caseQueryCustomerInfo(((Message.QueryCustomerInfo) message));
            case QUERY_FLIGHT_PRICE:
                return caseQueryFlightPrice(((Message.QueryFlightPrice) message));
            case QUERY_CARS_PRICE:
                return caseQueryCarsPrice(((Message.QueryCarsPrice) message));
            case QUERY_ROOMS_PRICE:
                return caseQueryRoomsPrice(((Message.QueryRoomsPrice) message));
            case RESERVE_FLIGHT:
                return caseReserveFlight(((Message.ReserveFlight) message));
            case RESERVE_CAR:
                return caseReserveCar(((Message.ReserveCar) message));
            case RESERVE_ROOM:
                return caseReserveRoom(((Message.ReserveRoom) message));
            case RESERVE_ITINERARY:
                return caseReserveItinerary(((Message.ReserveItinerary) message));
            case CANCEL_RESERVE_FLIGHT:
                return caseCancelReserveFlight(((Message.CancelReserveFlight) message));
            case CANCEL_RESERVE_CAR:
                return caseCancelReserveCar(((Message.CancelReserveCar) message));
            case CANCEL_RESERVE_ROOM:
                return caseCancelReserveRoom(((Message.CancelReserveRoom) message));
        }
        return null;
    }

    @Override
    public T caseAddFlight(Message.AddFlight message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseAddCars(Message.AddCars message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseAddRooms(Message.AddRooms message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseNewCustomer(Message.NewCustomer message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseNewCustomerWithCID(Message.NewCustomerWithCID message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseDeleteFlight(Message.DeleteFlight message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseDeleteCars(Message.DeleteCars message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseDeleteRooms(Message.DeleteRooms message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseDeleteCustomer(Message.DeleteCustomer message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseQueryFlight(Message.QueryFlight message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseQueryCars(Message.QueryCars message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseQueryRooms(Message.QueryRooms message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseQueryCustomerInfo(Message.QueryCustomerInfo message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseQueryFlightPrice(Message.QueryFlightPrice message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseQueryCarsPrice(Message.QueryCarsPrice message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseQueryRoomsPrice(Message.QueryRoomsPrice message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseReserveFlight(Message.ReserveFlight message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseReserveCar(Message.ReserveCar message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseReserveRoom(Message.ReserveRoom message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseReserveItinerary(Message.ReserveItinerary message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseCancelReserveFlight(Message.CancelReserveFlight message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseCancelReserveCar(Message.CancelReserveCar message) {
        throw new RuntimeException("not yet implement");
    }

    @Override
    public T caseCancelReserveRoom(Message.CancelReserveRoom message) {
        throw new RuntimeException("not yet implement");
    }
}
