package zad4;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Optional;

public class UdpClient {

    private static DatagramSocket CLIENT_SOCKET;

    public static void main(String[] args) {
        try {
            System.out.println("Sending message...");
            CLIENT_SOCKET = new DatagramSocket();
            var address = InetAddress.getByName("localhost");
            byte[] sendBuffer = "JPing Java".getBytes();
            var sendPacket =
                    new DatagramPacket(sendBuffer, sendBuffer.length, address, 9876);
            CLIENT_SOCKET.send(sendPacket);

            // wait for response
            System.out.println("Waiting for response...");
            byte[] receiveBuffer = new byte[1024];
            var receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            CLIENT_SOCKET.receive(receivePacket);
            System.out.println(new String(receivePacket.getData()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Optional.ofNullable(CLIENT_SOCKET).ifPresent(DatagramSocket::close);
        }
    }
}
