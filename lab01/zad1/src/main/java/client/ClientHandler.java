package client;

import command.CommandType;
import event.UserEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;
    private final ConcurrentHashMap<Long, ClientHandler> handlersMap;

    private ClientHandler(Socket socket, ObjectInputStream reader, ObjectOutputStream writer, ConcurrentHashMap<Long, ClientHandler> handlersMap) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.handlersMap = handlersMap;
    }

    public static ClientHandler of(Socket socket, ConcurrentHashMap<Long, ClientHandler> handlersMap) {
        try {
            var writer = new ObjectOutputStream(socket.getOutputStream());
            var reader = new ObjectInputStream(socket.getInputStream());
            return new ClientHandler(socket, reader, writer, handlersMap);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void run() {
        handlersMap.put(this.threadId(), this);
        try {
            while (true) {
                var userEvent = (UserEvent) reader.readObject();
                System.out.printf("Received request: %s from %s%n", userEvent.getCommand().toString(), userEvent.getUser().username());

                if (userEvent.getCommand() == CommandType.EXIT) {
                    handlersMap.remove(this.threadId());
                    var closeEvent = new UserEvent("Close confirm", CommandType.EXIT, userEvent.getUser());
                    writer.writeObject(closeEvent);
                    return;
                }

                tcpClients().forEach(handler -> {
                    try {
                        handler.getWriter().writeObject(userEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Stream<ClientHandler> tcpClients() {
        return handlersMap.values()
                .stream()
                .filter(handler -> handler.threadId() != currentThread().threadId());
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public int getPort() {
        return this.socket.getPort();
    }

}
