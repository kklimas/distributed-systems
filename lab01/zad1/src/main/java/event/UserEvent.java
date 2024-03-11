package event;

import command.CommandType;

import java.io.Serializable;

public class UserEvent extends Event implements Serializable {
    private final User user;

    public UserEvent(String message, CommandType commandType, User user) {
        super(message, commandType);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
