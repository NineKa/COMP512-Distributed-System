package middleware;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Message;
import utils.MessageBaseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class MiddlewareThread implements Runnable {

    public class MiddlewareDispatcher extends MessageBaseHandler<String> {
        private String sendAndReceive(Middleware.RMType rmType, Message message) throws IOException {
            Pair<String, Integer> rmInfo = rms.get(rmType);
            Socket socket = new Socket(rmInfo.getValue0(), rmInfo.getValue1());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println(message.toJSONString());
            writer.flush();
            return reader.readLine();
        }

        @Override
        public String caseAddFlight(Message.AddFlight message) {
            try {
                return sendAndReceive(Middleware.RMType.FLIGHT, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseAddCars(Message.AddCars message) {
            try {
                return sendAndReceive(Middleware.RMType.CAR, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseAddRooms(Message.AddRooms message) {
            try {
                return sendAndReceive(Middleware.RMType.ROOM, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseNewCustomer(Message.NewCustomer message) {
            Middleware.Customer newCustomer = new Middleware.Customer();
            Set<Integer> cids = customers.keySet();
            int max = 0;
            for (int cid : cids)
                if (cid > max) max = cid;
            customers.put(max + 1, newCustomer);
            return Integer.toString(max + 1);
        }

        @Override
        public String caseNewCustomerWithCID(Message.NewCustomerWithCID message) {
            if (customers.containsKey(message.cid)) return Boolean.toString(false);
            Middleware.Customer newCustomer = new Middleware.Customer();
            customers.put(message.cid, newCustomer);
            return Boolean.toString(true);
        }

        @Override
        public String caseDeleteFlight(Message.DeleteFlight message) {
            try {
                return sendAndReceive(Middleware.RMType.FLIGHT, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseDeleteCars(Message.DeleteCars message) {
            try {
                return sendAndReceive(Middleware.RMType.CAR, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseDeleteRooms(Message.DeleteRooms message) {
            try {
                return sendAndReceive(Middleware.RMType.ROOM, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseDeleteCustomer(Message.DeleteCustomer message) {
            if (!customers.containsKey(message.customer)) return "<IllegalArgumentException>";
            boolean success = true;
            try {
                Middleware.Customer customer = customers.get(message.customer);
                for (int flight : customer.flights) {
                    Message.CancelReserveFlight cancelReserveFlight = new Message.CancelReserveFlight();
                    cancelReserveFlight.flightNum = flight;
                    cancelReserveFlight.id = message.id;
                    cancelReserveFlight.customer = message.customer;
                    String result = sendAndReceive(Middleware.RMType.FLIGHT, cancelReserveFlight);
                    if (!result.equals(Boolean.toString(true))) success = false;
                }
                for (String car : customer.cars) {
                    Message.CancelReserveCar cancelReserveCar = new Message.CancelReserveCar();
                    cancelReserveCar.location = car;
                    cancelReserveCar.id = message.id;
                    cancelReserveCar.customer = message.customer;
                    String result = sendAndReceive(Middleware.RMType.CAR, cancelReserveCar);
                    if (!result.equals(Boolean.toString(true))) success = false;
                }
                for (String room : customer.rooms) {
                    Message.CancelReserveRoom cancelReserveRoom = new Message.CancelReserveRoom();
                    cancelReserveRoom.location = room;
                    cancelReserveRoom.id = message.id;
                    cancelReserveRoom.customer = message.customer;
                    String result = sendAndReceive(Middleware.RMType.ROOM, cancelReserveRoom);
                    if (!result.equals(Boolean.toString(true))) success = false;
                }
                customers.remove(message.customer);
                return Boolean.toString(success);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseQueryFlight(Message.QueryFlight message) {
            try {
                return sendAndReceive(Middleware.RMType.FLIGHT, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseQueryCars(Message.QueryCars message) {
            try {
                return sendAndReceive(Middleware.RMType.CAR, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseQueryRooms(Message.QueryRooms message) {
            try {
                return sendAndReceive(Middleware.RMType.ROOM, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseQueryCustomerInfo(Message.QueryCustomerInfo message) {
            if (!customers.containsKey(message.customer)) return "<IllegalArgumentException>";
            Middleware.Customer customer = customers.get(message.customer);
            JSONArray flightsArray = new JSONArray();
            for (int flight : customer.flights) {
                JSONObject flightObject = new JSONObject();
                flightObject.accumulate("flight_number", flight);

                Message.QueryFlightPrice flightPrice = new Message.QueryFlightPrice();
                flightPrice.id = message.id;
                flightPrice.flightNum = flight;

                try {
                    String result = sendAndReceive(Middleware.RMType.FLIGHT, flightPrice);
                    flightObject.accumulate("price", Integer.parseInt(result));
                } catch (IOException exception) {
                    return "<IOException>";
                }

                flightsArray.put(flightObject);
            }

            JSONArray carsArray = new JSONArray();
            for (String car : customer.cars) {
                JSONObject carObject = new JSONObject();
                carObject.accumulate("location", car);

                Message.QueryCarsPrice carsPrice = new Message.QueryCarsPrice();
                carsPrice.id = message.id;
                carsPrice.location = car;

                try {
                    String result = sendAndReceive(Middleware.RMType.CAR, carsPrice);
                    carObject.accumulate("price", Integer.parseInt(result));
                } catch (IOException exception) {
                    return "<IOException>";
                }

                carsArray.put(carObject);
            }

            JSONArray roomsArray = new JSONArray();
            for (String room : customer.rooms) {
                JSONObject roomObject = new JSONObject();
                roomObject.accumulate("location", room);

                Message.QueryRoomsPrice roomsPrice = new Message.QueryRoomsPrice();
                roomsPrice.id = message.id;
                roomsPrice.location = room;

                try {
                    String result = sendAndReceive(Middleware.RMType.ROOM, roomsPrice);
                    roomObject.accumulate("price", Integer.parseInt(result));
                } catch (IOException exception) {
                    return "<IOException>";
                }
                roomsArray.put(roomObject);
            }

            return (new JSONObject())
                    .put("flights", flightsArray)
                    .put("cars", carsArray)
                    .put("rooms", roomsArray)
                    .toString();
        }

        @Override
        public String caseQueryFlightPrice(Message.QueryFlightPrice message) {
            try {
                return sendAndReceive(Middleware.RMType.FLIGHT, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseQueryCarsPrice(Message.QueryCarsPrice message) {
            try {
                return sendAndReceive(Middleware.RMType.CAR, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseQueryRoomsPrice(Message.QueryRoomsPrice message) {
            try {
                return sendAndReceive(Middleware.RMType.ROOM, message);
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseReserveFlight(Message.ReserveFlight message) {
            if (!customers.containsKey(message.customer)) return "<IllegalArgumentException>";
            try {
                String result = sendAndReceive(Middleware.RMType.FLIGHT, message);
                if (result.equals(Boolean.toString(true))) {
                    customers.get(message.customer).flights.add(message.flightNum);
                }
                return result;
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseReserveCar(Message.ReserveCar message) {
            if (!customers.containsKey(message.customer)) return "<IllegalArgumentException>";
            try {
                String result = sendAndReceive(Middleware.RMType.CAR, message);
                if (result.equals(Boolean.toString(true))) {
                    customers.get(message.customer).cars.add(message.location);
                }
                return result;
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseReserveRoom(Message.ReserveRoom message) {
            if (!customers.containsKey(message.customer)) return "<IllegalArgumentException>";
            try {
                String result = sendAndReceive(Middleware.RMType.ROOM, message);
                if (result.equals(Boolean.toString(true))) {
                    customers.get(message.customer).rooms.add(message.location);
                }
                return result;
            } catch (IOException exception) {
                return "<IOException>";
            }
        }

        @Override
        public String caseReserveItinerary(Message.ReserveItinerary message) {
            if (!customers.containsKey(message.customer)) return "<IllegalArgumentException>";
            Middleware.Customer customer = customers.get(message.customer);
            for (int flight : message.flightNums) {
                try {
                    Message.ReserveFlight reserveFlight = new Message.ReserveFlight();
                    reserveFlight.flightNum = flight;
                    reserveFlight.customer = message.customer;
                    reserveFlight.id = message.id;

                    String result = sendAndReceive(Middleware.RMType.FLIGHT, reserveFlight);
                    if (result.equals(Boolean.toString(true))) customer.flights.add(flight);
                } catch (IOException exception) {
                    return "<IOException>";
                }
            }
            if (message.reserveRoom) {
                Message.ReserveRoom reserveRoom = new Message.ReserveRoom();
                reserveRoom.location = message.location;
                reserveRoom.customer = message.customer;
                reserveRoom.id = message.id;
                try {
                    String result = sendAndReceive(Middleware.RMType.ROOM, reserveRoom);
                    if (result.equals(Boolean.toString(true))) customer.rooms.add(message.location);
                } catch (IOException exception) {
                    return "<IOException>";
                }
            }
            if (message.reserveCar) {
                Message.ReserveCar reserveCar = new Message.ReserveCar();
                reserveCar.location = message.location;
                reserveCar.customer = message.customer;
                reserveCar.id = message.id;
                try {
                    String result = sendAndReceive(Middleware.RMType.CAR, reserveCar);
                    if (result.equals(Boolean.toString(true))) customer.cars.add(message.location);
                } catch (IOException exception) {
                    return "<IOException>";
                }
            }
            return Boolean.toString(true);
        }
    }

    private final Map<Integer, Middleware.Customer> customers;
    private final Map<Middleware.RMType, Pair<String, Integer>> rms;
    private Socket socket;
    private MiddlewareDispatcher middlewareDispatcher = new MiddlewareDispatcher();

    public MiddlewareThread(Socket socket,
                            Map<Integer, Middleware.Customer> customers,
                            Map<Middleware.RMType, Pair<String, Integer>> rms) {
        this.socket = socket;
        this.customers = customers;
        this.rms = rms;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            Message message = Message.fromJSONString(reader.readLine());
            writer.println(middlewareDispatcher.dispatch(message));
            writer.flush();
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
