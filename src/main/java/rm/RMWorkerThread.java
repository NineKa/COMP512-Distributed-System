package rm;

import utils.Message;
import utils.MessageBaseHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class RMWorkerThread implements Runnable {

    public class RMMessageDispatcher extends MessageBaseHandler<String> {
        private boolean addReservableItems(ReservableItem reservableItem) {
            try {
                synchronized (reservations) {
                    if (reservations.containsKey(reservableItem.getKey())) {
                        ReservableItem item = reservations.get(reservableItem.getKey());
                        item.quota = item.quota + reservableItem.quota;
                        item.price = reservableItem.price;
                    } else {
                        reservations.put(reservableItem.getKey(), reservableItem);
                    }
                }
                return true;
            } catch (Exception exception) {
                return false;
            }
        }

        private boolean removeReservableItems(String key) {
            try {
                synchronized (reservations) {
                    if (!reservations.containsKey(key)) return false;
                    ReservableItem item = reservations.get(key);
                    if (item.reserved != 0) return false;
                    reservations.remove(key);
                }
                return true;
            } catch (Exception exception) {
                return false;
            }
        }

        private int queryReservableItems(String key) {
            synchronized (reservations) {
                if (!reservations.containsKey(key)) throw new IllegalArgumentException();
                ReservableItem item = reservations.get(key);
                return item.quota - item.reserved;
            }
        }

        private int queryReservableItemsPrice(String key) {
            synchronized (reservations) {
                if (!reservations.containsKey(key)) throw new IllegalArgumentException();
                ReservableItem item = reservations.get(key);
                return item.price;
            }
        }

        private boolean reserveReservableItem(String key) {
            synchronized (reservations) {
                if (!reservations.containsKey(key)) throw new IllegalArgumentException();
                ReservableItem item = reservations.get(key);
                if (item.reserved >= item.quota) {
                    return false;
                } else {
                    item.reserved = item.reserved + 1;
                    return true;
                }
            }
        }

        private boolean cancelReservableItem(String key) {
            synchronized (reservations) {
                if (!reservations.containsKey(key)) throw new IllegalArgumentException();
                ReservableItem item = reservations.get(key);
                if (item.reserved <= 0) {
                    return false;
                } else {
                    item.reserved = item.reserved - 1;
                    return true;
                }
            }
        }

        @Override
        public String caseAddFlight(Message.AddFlight message) {
            ReservableItem.Flight flight = new ReservableItem.Flight(message.flightNum);
            flight.reserved = 0;
            flight.quota = message.flightSeats;
            flight.price = message.flightPrice;
            return Boolean.toString(addReservableItems(flight));
        }

        @Override
        public String caseAddCars(Message.AddCars message) {
            ReservableItem.Car car = new ReservableItem.Car(message.location);
            car.reserved = 0;
            car.quota = message.carNum;
            car.price = message.carPrice;
            return Boolean.toString(addReservableItems(car));
        }

        @Override
        public String caseAddRooms(Message.AddRooms message) {
            ReservableItem.Room room = new ReservableItem.Room(message.location);
            room.reserved = 0;
            room.quota = message.roomNum;
            room.price = message.roomPrice;
            return Boolean.toString(addReservableItems(room));
        }

        @Override
        public String caseDeleteFlight(Message.DeleteFlight message) {
            return Boolean.toString(
                    removeReservableItems(ReservableItem.Flight.buildKey(message.flightNum))
            );
        }

        @Override
        public String caseDeleteCars(Message.DeleteCars message) {
            return Boolean.toString(
                    removeReservableItems(ReservableItem.Car.buildKey(message.location))
            );
        }

        @Override
        public String caseDeleteRooms(Message.DeleteRooms message) {
            return Boolean.toString(
                    removeReservableItems(ReservableItem.Room.buildKey(message.location))
            );
        }

        @Override
        public String caseQueryFlight(Message.QueryFlight message) {
            try {
                return Integer.toString(
                        queryReservableItems(ReservableItem.Flight.buildKey(message.flightNum))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseQueryCars(Message.QueryCars message) {
            try {
                return Integer.toString(
                        queryReservableItems(ReservableItem.Car.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseQueryRooms(Message.QueryRooms message) {
            try {
                return Integer.toString(
                        queryReservableItems(ReservableItem.Room.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseQueryFlightPrice(Message.QueryFlightPrice message) {
            try {
                return Integer.toString(
                        queryReservableItemsPrice(ReservableItem.Flight.buildKey(message.flightNum))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseQueryCarsPrice(Message.QueryCarsPrice message) {
            try {
                return Integer.toString(
                        queryReservableItemsPrice(ReservableItem.Car.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseQueryRoomsPrice(Message.QueryRoomsPrice message) {
            try {
                return Integer.toString(
                        queryReservableItemsPrice(ReservableItem.Room.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseReserveFlight(Message.ReserveFlight message) {
            try {
                return Boolean.toString(
                        reserveReservableItem(ReservableItem.Flight.buildKey(message.flightNum))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseReserveCar(Message.ReserveCar message) {
            try {
                return Boolean.toString(
                        reserveReservableItem(ReservableItem.Car.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseReserveRoom(Message.ReserveRoom message) {
            try {
                return Boolean.toString(
                        reserveReservableItem(ReservableItem.Room.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseCancelReserveFlight(Message.CancelReserveFlight message) {
            try {
                return Boolean.toString(
                        cancelReservableItem(ReservableItem.Flight.buildKey(message.flightNum))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseCancelReserveCar(Message.CancelReserveCar message) {
            try {
                return Boolean.toString(
                        cancelReservableItem(ReservableItem.Car.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }

        @Override
        public String caseCancelReserveRoom(Message.CancelReserveRoom message) {
            try {
                return Boolean.toString(
                        cancelReservableItem(ReservableItem.Room.buildKey(message.location))
                );
            } catch (IllegalArgumentException exception) {
                return "<IllegalArgumentException>";
            }
        }
    }

    private Socket socket;
    private RMMessageDispatcher messageDispatcher = new RMMessageDispatcher();
    private final Map<String, ReservableItem> reservations;

    public RMWorkerThread(Socket socket, Map<String, ReservableItem> reservations) {
        this.socket = socket;
        this.reservations = reservations;
    }

    @Override
    public void run() {
        try {
            BufferedReader socketReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(socketReader.readLine());
            System.out.println(stringBuilder.toString());
            Message message = Message.fromJSONString(stringBuilder.toString());

            socketWriter.println(messageDispatcher.dispatch(message));
            socketWriter.flush();
        } catch (Exception exception) {
            /* ignore the exceptions */
            System.out.println(exception.toString());
        }
    }
}
