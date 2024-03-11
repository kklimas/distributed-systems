package command;

public enum CommandOption {
    SEND_UDP,
    SEND_MULTICAST,
    UNKNOWN;

    public static CommandOption fromCommandLine(String commandLine) {
        var startOptionStart = commandLine.indexOf("-");
        if (startOptionStart == -1 || startOptionStart == commandLine.length() - 1) {
            return UNKNOWN;
        }

        return switch (commandLine.charAt(startOptionStart + 1)) {
            case 'U' -> SEND_UDP;
            case 'M' -> SEND_MULTICAST;
            default -> UNKNOWN;
        };
    }

}
