package event;

import command.CommandType;

import java.io.Serializable;

public record User(long id, String username) implements Serializable {

    public UserEvent toMessage(String message, CommandType commandType) {
        return new UserEvent(message, commandType, this);
    }
}
