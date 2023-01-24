package hr.algebra.codenames.networking.utils;

import hr.algebra.codenames.networking.messaging.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class NetworkUtils {
    private NetworkUtils(){}
    public static void SendMessage(Message message, ObjectOutputStream out) throws IOException {
        out.writeObject(message);
    }
}
