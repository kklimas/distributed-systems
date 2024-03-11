import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class TcpServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12345);
            while (true) {
                var clientSocket = serverSocket.accept();
                var out = new PrintWriter(clientSocket.getOutputStream(), true);
                var in = new BufferedReader(new
                        InputStreamReader(clientSocket.getInputStream()));
                String msg = in.readLine();
                System.out.println("received msg: " + msg);
                out.println("Pong");
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
