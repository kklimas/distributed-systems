package zad1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Optional;

public class UdpServer {
    private static DatagramSocket SERVER_SOCKET;

    public static void main(String[] args) {
        try {
            SERVER_SOCKET = new DatagramSocket(9876);
            while (true) {
                var clientPort = receive();
                sendResponse(clientPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (SERVER_SOCKET != null) SERVER_SOCKET.close();
        }
    }

    private static int receive() {
        try {
            byte[] receiveBuffer = new byte[1024];
            var receivePacket =
                    new DatagramPacket(receiveBuffer, receiveBuffer.length);
            SERVER_SOCKET.receive(receivePacket);
            var msg = new String(receivePacket.getData());
            var port = receivePacket.getPort();
            System.out.printf("Received message: %s.", msg);
            return port;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void sendResponse(int port) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            System.out.printf("\nSending response to: %d.", port);
            var address = InetAddress.getByName("localhost");
            byte[] sendBuffer = "Response".getBytes();
            var sendPacket =
                    new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Optional.ofNullable(socket).ifPresent(DatagramSocket::close);
        }
    }

}
