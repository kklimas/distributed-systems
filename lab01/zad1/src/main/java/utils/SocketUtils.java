package utils;

import java.io.Closeable;
import java.io.IOException;

public class SocketUtils {

    public static void closeSocket(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
