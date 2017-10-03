package middleware;

import org.javatuples.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Middleware {
    public enum RMType {
        FLIGHT, CAR, ROOM
    }

    public static class Customer {
        public final List<Integer> flights = new LinkedList<>();
        public final List<String> cars = new LinkedList<>();
        public final List<String> rooms = new LinkedList<>();
    }

    private static Map<RMType, Pair<String, Integer>> rms = new HashMap<>();
    private static Map<Integer, Customer> customers = new HashMap<>();

    public static void main(String argv[]) throws IOException {
        String arg1[] = argv[0].split(":");
        String arg2[] = argv[1].split(":");
        String arg3[] = argv[2].split(":");

        rms.put(RMType.CAR, new Pair<>(arg1[0], Integer.parseInt(arg1[1])));
        rms.put(RMType.FLIGHT, new Pair<>(arg2[0], Integer.parseInt(arg2[1])));
        rms.put(RMType.ROOM, new Pair<>(arg3[0], Integer.parseInt(arg3[1])));

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(argv[3]));
        while (true) {
            Socket socket = serverSocket.accept();
            (new Thread(new MiddlewareThread(socket, customers, rms))).start();
        }
    }
}
