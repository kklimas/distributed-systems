package zad3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Optional;

public class UdpServer {
    private static DatagramSocket SERVER_SOCKET;

    public static void main(String[] args) {
        try {
            SERVER_SOCKET = new DatagramSocket(9876);
            while (true) {

                byte[] receiveBuffer = new byte[1024];
                var receivePacket =
                        new DatagramPacket(receiveBuffer, receiveBuffer.length);
                SERVER_SOCKET.receive(receivePacket);
                var nb = ByteBuffer.wrap(receivePacket.getData()).getInt();
                var port = receivePacket.getPort();
                System.out.printf("\n\nReceived message: %d.", nb);

                sendResponse(port, nb + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (SERVER_SOCKET != null) SERVER_SOCKET.close();
        }
    }

    private static void sendResponse(int port, int value) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            System.out.printf("\nSending response to: %d.", port);
            var address = InetAddress.getByName("localhost");
            byte[] sendBuffer = ByteBuffer.allocate(4).putInt(value).array();
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
