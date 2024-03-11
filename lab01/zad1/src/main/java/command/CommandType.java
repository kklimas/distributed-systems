package command;

public enum CommandType {
    EXIT, SEND_MESSAGE;

    public static CommandType fromMessage(String message) {
        var command = message.split(" ")[0];
        if (command.equals("exit")) {
            return CommandType.EXIT;
        }
        return CommandType.SEND_MESSAGE;
    }
}
