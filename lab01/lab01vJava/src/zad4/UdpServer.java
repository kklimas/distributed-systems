package zad4;

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
                var message = new String(receivePacket.getData());

                String responseMessage = "Ping unknown";
                if (message.length() > 0) {
                    if (message.charAt(0) == 'P') {
                        responseMessage = "Ping python";
                    } else {
                        responseMessage = "Ping java";
                    }
                }

                var port = receivePacket.getPort();
                System.out.printf("\n\nReceived message: %s.", message);

                sendResponse(port, responseMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (SERVER_SOCKET != null) SERVER_SOCKET.close();
        }
    }

    private static void sendResponse(int port, String value) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            System.out.printf("\nSending response to: %d.", port);
            var address = InetAddress.getByName("localhost");
            var message = value.getBytes();
            var sendPacket =
                    new DatagramPacket(message, message.length, address, port);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Optional.ofNullable(socket).ifPresent(DatagramSocket::close);
        }
    }

}
