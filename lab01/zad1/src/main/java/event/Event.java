package event;

import command.CommandType;

import java.io.Serializable;

public class Event implements Serializable {
    private final String message;
    private final CommandType command;

    public Event(String message, CommandType commandType) {
        this.message = message;
        this.command = commandType;
    }

    public CommandType getCommand() {
        return command;
    }

    public String getMessage() {
        return message;
    }
}
