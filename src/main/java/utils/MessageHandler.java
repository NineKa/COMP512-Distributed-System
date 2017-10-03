package utils;

public interface MessageHandler<T> {
    T caseAddFlight(Message.AddFlight message);
    T caseAddCars(Message.AddCars message);
    T caseAddRooms(Message.AddRooms message);

    T caseNewCustomer(Message.NewCustomer message);
    T caseNewCustomerWithCID(Message.NewCustomerWithCID message);

    T caseDeleteFlight(Message.DeleteFlight message);
    T caseDeleteCars(Message.DeleteCars message);
    T caseDeleteRooms(Message.DeleteRooms message);
    T caseDeleteCustomer(Message.DeleteCustomer message);

    T caseQueryFlight(Message.QueryFlight message);
    T caseQueryCars(Message.QueryCars message);
    T caseQueryRooms(Message.QueryRooms message);
    T caseQueryCustomerInfo(Message.QueryCustomerInfo message);

    T caseQueryFlightPrice(Message.QueryFlightPrice message);
    T caseQueryCarsPrice(Message.QueryCarsPrice message);
    T caseQueryRoomsPrice(Message.QueryRoomsPrice message);

    T caseReserveFlight(Message.ReserveFlight message);
    T caseReserveCar(Message.ReserveCar message);
    T caseReserveRoom(Message.ReserveRoom message);
    T caseReserveItinerary(Message.ReserveItinerary message);

    T caseCancelReserveFlight(Message.CancelReserveFlight message);
    T caseCancelReserveCar(Message.CancelReserveCar message);
    T caseCancelReserveRoom(Message.CancelReserveRoom message);
}
