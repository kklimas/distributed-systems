package client;

import command.CommandOption;
import command.CommandType;
import event.User;
import event.UserEvent;
import org.apache.commons.lang3.SerializationUtils;
import utils.SharedFields;
import utils.SocketUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import static java.lang.Thread.currentThread;
import static utils.SharedFields.*;

/**
 * Program Client przyjmuje dwa opcjonalne parametry jako program arguments:
 * - username - nazwa usera; domyslnie user-random_number(1-10000)
 * - belongToMulticastGroup - wystarczy wpisac dowolne slowo i klient bedzie nalezal do grupy na adresie 224.0.0.0; żeby uzupełnic pole nalezy tez podac username
 */
public class Client {
    private static final BufferedReader CONSOLE_READER = new BufferedReader(new InputStreamReader(System.in));
    private static final Random random = new Random();

    private static final String CONSOLE_MESSAGE = """
            Type one of the following commands
            1) exit
            2) send - with one of following options
                i) none to send tcp message
                ii) -U to send UDP message
                iii) -M to send UDP Multicast message to users with even id's
                e.g: send -U here is my message
                e.g: send -M here is my message
            3) none - to send default tcp message (no command is required)
                e.g: here is my message
            """;

    private static User user;
    private static Socket clientTcpSocket;
    private static DatagramSocket clientUdpSocket;
    private static MulticastSocket clientMulticastSocket;

    public static void main(String[] args) {
        var userId = random.nextInt(0, 10000);
        System.out.println("Client id: " + userId);

        var username = args.length == 0 ? "user-" + userId : args[0];
        var joinMulticast = args.length >= 2;

        System.out.printf("Username: %s | is in multicast group: %s%n", username, joinMulticast);
        System.out.println(CONSOLE_MESSAGE);
        user = new User(userId, username);
        try {
            var address = InetAddress.getByName(SharedFields.SERVER_HOST);
            var multicastAddress = InetAddress.getByName(MULTICAST_ADDRESS);

            clientTcpSocket = new Socket(SharedFields.SERVER_HOST, SharedFields.SERVER_PORT);
            clientUdpSocket = new DatagramSocket(clientTcpSocket.getLocalPort());

            var objectWriter = new ObjectOutputStream(clientTcpSocket.getOutputStream());

            var observeTcpThread = observeTcpSocket();
            var observeUdpThread = observeUdpSocket();

            observeTcpThread.start();
            observeUdpThread.ifPresent(Thread::start);

            if (joinMulticast) {
                var multicastObserver = observeMulticastSocket(multicastAddress);
                multicastObserver.ifPresent(Thread::start);
            }

            while (true) {
                var line = readLine();

                var commandType = getCommand(line);
                var commandOption = getSendCommandOption(line);
                var message = getMessage(line);

                var userEvent = user.toMessage(message, commandType);

                switch (commandOption) {
                    case CommandOption.SEND_UDP -> clientUdpSocket.send(getPacket(userEvent, SERVER_PORT, address));
                    case CommandOption.SEND_MULTICAST -> clientUdpSocket.send(getPacket(userEvent, MULTICAST_PORT, multicastAddress));
                    default -> objectWriter.writeObject(userEvent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SocketUtils.closeSocket(clientTcpSocket);
            SocketUtils.closeSocket(clientTcpSocket);
            SocketUtils.closeSocket(clientMulticastSocket);
        }
    }

    private static DatagramPacket getPacket(UserEvent userEvent, int port, InetAddress address) {
        var sendBuffer = SerializationUtils.serialize(userEvent);
        return new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
    }

    private static String readLine() {
        try {
            return CONSOLE_READER.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static CommandType getCommand(String consoleLine) {
        return CommandType.fromMessage(consoleLine);
    }

    private static CommandOption getSendCommandOption(String consoleLine) {
        return CommandOption.fromCommandLine(consoleLine);
    }

    private static String getMessage(String consoleLine) {
        var splitMessage = consoleLine.split(" ");
        Predicate<String> messagePredicate = (messageWord) -> !(messageWord.startsWith("-") || messageWord.equals("exit") || messageWord.equals("send"));
        var splitMessageList = Arrays.stream(splitMessage).filter(messagePredicate).toList();
        return String.join(" ", splitMessageList);
    }

    private static Thread observeTcpSocket() {
        return new Thread(() -> {
            try {
                var clientSocketReader = new ObjectInputStream(clientTcpSocket.getInputStream());
                while (true) {
                    var event = (UserEvent) clientSocketReader.readObject();

                    if (event.getCommand() == CommandType.EXIT) {
                        System.out.println("Exiting program...");
                        SocketUtils.closeSocket(clientTcpSocket);
                        System.exit(0);
                    }

                    System.out.println(event.getUser().username() + ": " + event.getMessage());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private static Optional<Thread> observeUdpSocket() {
        return Optional.of(observeUdpSocket(clientUdpSocket));
    }

    private static Optional<Thread> observeMulticastSocket(InetAddress multicastAddress) {
        try {
            clientMulticastSocket = new MulticastSocket(MULTICAST_PORT);
            var externalGroup = new InetSocketAddress(MULTICAST_ADDRESS, MULTICAST_PORT);
            clientMulticastSocket.joinGroup(externalGroup, NetworkInterface.getByInetAddress(multicastAddress));
            return Optional.of(observeUdpSocket(clientMulticastSocket));

        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static Thread observeUdpSocket(DatagramSocket socket) {
        return new Thread(() -> {
            try {
                var receiveBuffer = new byte[1024];
                while (true) {
                    var datagramPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(datagramPacket);
                    var event = (UserEvent) SerializationUtils.deserialize(datagramPacket.getData());
                    if (!event.getUser().username().equals(user.username())) {
                        System.out.println(event.getUser().username() + ": " + event.getMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
