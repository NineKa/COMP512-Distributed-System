import org.junit.Assert;
import org.junit.Test;
import utils.Message;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class MessageTest {
    private final int TEST_STR_LEN = 10;
    private final int TEST_INT_MAX = 1000;

    private String generateRandomString(int length) {
        final String alphabet = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int index = ThreadLocalRandom.current().nextInt(alphabet.length());
            stringBuilder.append(alphabet.charAt(index));
        }
        return stringBuilder.toString();
    }

    private <T extends Message> void genericTest(Supplier<T> messageSupplier) {
        T message = messageSupplier.get();
        Message parsedMessage = Message.fromJSONString(message.toJSONString());
        Assert.assertTrue(parsedMessage != null);
        Assert.assertTrue(
                parsedMessage.messageType == message.messageType &&
                        parsedMessage.toString().equals(message.toString())
        );
    }

    @Test
    public void testAddFlight() {
        genericTest(() -> {
            Message.AddFlight addFlight = new Message.AddFlight();
            addFlight.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            addFlight.flightNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            addFlight.flightSeats = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            addFlight.flightPrice = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return addFlight;
        });
    }

    @Test
    public void testAddCars() {
        genericTest(() -> {
            Message.AddCars addCars = new Message.AddCars();
            addCars.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            addCars.location = generateRandomString(TEST_STR_LEN);
            addCars.carNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            addCars.carPrice = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return addCars;
        });
    }

    @Test
    public void testAddRooms() {
        genericTest(() -> {
            Message.AddRooms addRooms = new Message.AddRooms();
            addRooms.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            addRooms.location = generateRandomString(TEST_STR_LEN);
            addRooms.roomNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            addRooms.roomPrice = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return addRooms;
        });
    }

    @Test
    public void testNewCustomer() {
        genericTest(() -> {
            Message.NewCustomer newCustomer = new Message.NewCustomer();
            newCustomer.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return newCustomer;
        });
    }

    @Test
    public void testNewCustomerWithCID() {
        genericTest(() -> {
            Message.NewCustomerWithCID newCustomerWithCID = new Message.NewCustomerWithCID();
            newCustomerWithCID.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            newCustomerWithCID.cid = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return newCustomerWithCID;
        });
    }

    @Test
    public void testDeleteFlight() {
        genericTest(() -> {
            Message.DeleteFlight deleteFlight = new Message.DeleteFlight();
            deleteFlight.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            deleteFlight.flightNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return deleteFlight;
        });
    }

    @Test
    public void testDeleteCars() {
        genericTest(() -> {
            Message.DeleteCars deleteCars = new Message.DeleteCars();
            deleteCars.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            deleteCars.location = generateRandomString(TEST_STR_LEN);
            return deleteCars;
        });
    }

    @Test
    public void testDeleteRooms() {
        genericTest(() -> {
            Message.DeleteRooms deleteRooms = new Message.DeleteRooms();
            deleteRooms.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            deleteRooms.location = generateRandomString(TEST_STR_LEN);
            return deleteRooms;
        });
    }

    @Test
    public void testDeleteCustomer() {
        genericTest(() -> {
            Message.DeleteCustomer deleteCustomer = new Message.DeleteCustomer();
            deleteCustomer.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            deleteCustomer.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return deleteCustomer;
        });
    }

    @Test
    public void testQueryFlight() {
        genericTest(() -> {
            Message.QueryFlight queryFlight = new Message.QueryFlight();
            queryFlight.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            queryFlight.flightNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return queryFlight;
        });
    }

    @Test
    public void testQueryCars() {
        genericTest(() -> {
            Message.QueryCars queryCars = new Message.QueryCars();
            queryCars.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            queryCars.location = generateRandomString(TEST_STR_LEN);
            return queryCars;
        });
    }

    @Test
    public void testQueryRooms() {
        genericTest(() -> {
            Message.QueryRooms queryRooms = new Message.QueryRooms();
            queryRooms.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            queryRooms.location = generateRandomString(TEST_STR_LEN);
            return queryRooms;
        });
    }

    @Test
    public void testQueryCustomerInfo() {
        genericTest(() -> {
            Message.QueryCustomerInfo queryCustomerInfo = new Message.QueryCustomerInfo();
            queryCustomerInfo.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            queryCustomerInfo.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return queryCustomerInfo;
        });
    }

    @Test
    public void testQueryFlightPrice() {
        genericTest(() -> {
            Message.QueryFlightPrice queryFlightPrice = new Message.QueryFlightPrice();
            queryFlightPrice.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            queryFlightPrice.flightNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return queryFlightPrice;
        });
    }

    @Test
    public void testQueryCarsPrice() {
        genericTest(() -> {
            Message.QueryCarsPrice queryCarsPrice = new Message.QueryCarsPrice();
            queryCarsPrice.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            queryCarsPrice.location = generateRandomString(TEST_STR_LEN);
            return queryCarsPrice;
        });
    }

    @Test
    public void testQueryRoomsPrice() {
        genericTest(() -> {
            Message.QueryRoomsPrice queryRoomsPrice = new Message.QueryRoomsPrice();
            queryRoomsPrice.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            queryRoomsPrice.location = generateRandomString(TEST_STR_LEN);
            return queryRoomsPrice;
        });
    }

    @Test
    public void testReserveFlight() {
        genericTest(() -> {
            Message.ReserveFlight reserveFlight = new Message.ReserveFlight();
            reserveFlight.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveFlight.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveFlight.flightNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return reserveFlight;
        });
    }

    @Test
    public void testReserveCar() {
        genericTest(() -> {
            Message.ReserveCar reserveCar = new Message.ReserveCar();
            reserveCar.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveCar.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveCar.location = generateRandomString(TEST_STR_LEN);
            return reserveCar;
        });
    }

    @Test
    public void testReserveRoom() {
        genericTest(() -> {
            Message.ReserveRoom reserveRoom = new Message.ReserveRoom();
            reserveRoom.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveRoom.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveRoom.location = generateRandomString(TEST_STR_LEN);
            return reserveRoom;
        });
    }

    @Test
    public void testReserveItinerary() {
        genericTest(() -> {
            Message.ReserveItinerary reserveItinerary = new Message.ReserveItinerary();
            reserveItinerary.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveItinerary.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            reserveItinerary.location = generateRandomString(TEST_STR_LEN);
            reserveItinerary.reserveCar = ThreadLocalRandom.current().nextBoolean();
            reserveItinerary.reserveRoom = ThreadLocalRandom.current().nextBoolean();

            final int numFlights = ThreadLocalRandom.current().nextInt(10);
            for (int i = 0; i < numFlights; ++i) {
                reserveItinerary.flightNums.add(ThreadLocalRandom.current().nextInt(TEST_INT_MAX));
            }

            return reserveItinerary;
        });
    }

    @Test
    public void testCancelReserveFlight() {
        genericTest(() -> {
            Message.CancelReserveFlight cancelReserveFlight = new Message.CancelReserveFlight();
            cancelReserveFlight.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            cancelReserveFlight.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            cancelReserveFlight.flightNum = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            return cancelReserveFlight;
        });
    }

    @Test
    public void testCancelReserveCar() {
        genericTest(() -> {
            Message.CancelReserveCar cancelReserveCar = new Message.CancelReserveCar();
            cancelReserveCar.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            cancelReserveCar.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            cancelReserveCar.location = generateRandomString(TEST_STR_LEN);
            return cancelReserveCar;
        });
    }

    @Test
    public void testCancelReserveRoom() {
        genericTest(() -> {
            Message.CancelReserveRoom cancelReserveRoom = new Message.CancelReserveRoom();
            cancelReserveRoom.id = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            cancelReserveRoom.customer = ThreadLocalRandom.current().nextInt(TEST_INT_MAX);
            cancelReserveRoom.location = generateRandomString(TEST_STR_LEN);
            return cancelReserveRoom;
        });
    }
}
