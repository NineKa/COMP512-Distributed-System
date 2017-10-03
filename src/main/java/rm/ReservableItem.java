package rm;

public abstract class ReservableItem {
    public int quota;
    public int reserved;
    public int price;

    public abstract String getKey();

    public static class Car extends ReservableItem {
        private final String location;

        public Car(String location) {
            this.location = location;
        }

        @Override
        public String getKey() {
            return buildKey(location);
        }

        public static String buildKey(String location) {
            return "Car@" + location;
        }
    }

    public static class Flight extends ReservableItem {
        private final int flightNumber;

        public Flight(int flightNumber) {
            this.flightNumber = flightNumber;
        }

        @Override
        public String getKey() {
            return buildKey(flightNumber);
        }

        public static String buildKey(int flightNumber) {
            return "Flight@" + Integer.toString(flightNumber);
        }
    }

    public static class Room extends ReservableItem {
        private final String location;

        public Room(String location) {
            this.location = location;
        }

        @Override
        public String getKey() {
            return buildKey(location);
        }

        public static String buildKey(String location) {
            return "Room@" + location;
        }
    }
}
