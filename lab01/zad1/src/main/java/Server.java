import client.ClientHandler;
import event.UserEvent;
import org.apache.commons.lang3.SerializationUtils;
import utils.SharedFields;
import utils.SocketUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class Server {
    private static final ConcurrentHashMap<Long, ClientHandler> HANDLERS_MAP = new ConcurrentHashMap<>();
    private static ServerSocket serverTcpSocket;
    private static DatagramSocket serverUdpSocket;

    public static void main(String[] args) {
        var tcpServer = tcpServerThread();
        var udpServer = udpServerThread();
        tcpServer.start();
        udpServer.start();

    }

    private static Thread tcpServerThread() {
        return new Thread(() -> {
            try {
                serverTcpSocket = new ServerSocket(SharedFields.SERVER_PORT);

                while (true) {
                    var clientSocket = serverTcpSocket.accept();
                    System.out.println("Handling new tcp connection...");

                    var clientHandler = ClientHandler.of(clientSocket, HANDLERS_MAP);

                    ofNullable(clientHandler).ifPresent(Thread::start);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                SocketUtils.closeSocket(serverTcpSocket);
            }
        });
    }

    private static Thread udpServerThread() {
        return new Thread(() -> {
            var receiveBuffer = new byte[1024];
            try {
                var address = InetAddress.getByName(SharedFields.SERVER_HOST);
                serverUdpSocket = new DatagramSocket(SharedFields.SERVER_PORT);

                while (true) {
                    var datagramPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    serverUdpSocket.receive(datagramPacket);

                    var event = (UserEvent) SerializationUtils.deserialize(datagramPacket.getData());
                    System.out.println("Received udp request from: " + event.getUser().username());

                    HANDLERS_MAP.values()
                            .stream()
                            .map(ClientHandler::getPort)
                            .filter(port -> !port.equals(datagramPacket.getPort()))
                            .forEach(udPort -> {
                                var sendBuffer = SerializationUtils.serialize(event);
                                var sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, udPort);
                                try {
                                    serverUdpSocket.send(sendPacket);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                SocketUtils.closeSocket(serverTcpSocket);
                SocketUtils.closeSocket(serverUdpSocket);
            }
        });
    }

}
