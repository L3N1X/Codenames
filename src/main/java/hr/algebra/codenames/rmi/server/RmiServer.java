package hr.algebra.codenames.rmi.server;

import hr.algebra.jndi.ServerConfigurationKey;
import hr.algebra.jndi.helper.JndiHelper;

import javax.naming.NamingException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {
    public static final int RANDOM_PORT_HINT = 0;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(JndiHelper.getConfigurationParameter(ServerConfigurationKey.RMI_SERVER_PORT.getKey())));
            ChatService chatService = new ChatServiceImpl();
            ChatService skeleton = (ChatService) UnicastRemoteObject.exportObject(chatService, RANDOM_PORT_HINT);
            registry.rebind(ChatService.REMOTE_OBJECT_NAME, skeleton);
            System.out.println("Object registered in RMI registry!");
        } catch (NamingException | IOException e) {
            e.printStackTrace();
        }
    }
}
