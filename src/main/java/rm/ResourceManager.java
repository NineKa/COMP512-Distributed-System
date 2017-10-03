package rm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    public static Map<String, ReservableItem> reservations = new HashMap<>();

    public static void main(String argv[]) throws IOException {

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(argv[0]));
        while (true) {
            Socket socket = serverSocket.accept();
            (new Thread(new RMWorkerThread(socket, reservations))).start();
        }
    }
}
