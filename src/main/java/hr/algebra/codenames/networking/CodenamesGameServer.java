package hr.algebra.codenames.networking;

import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.Player;
import hr.algebra.codenames.model.SerializableTurnLog;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.singleton.GameLogger;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.networking.messaging.Message;
import hr.algebra.codenames.networking.response.StatusCode;
import hr.algebra.codenames.repository.factories.RepositoryFactory;
import hr.algebra.codenames.rmi.server.ChatService;
import hr.algebra.codenames.rmi.server.ChatServiceImpl;
import hr.algebra.codenames.xml.jaxb.XMLConverter;
import hr.algebra.codenames.xml.model.XmlTurnListWrapper;
import hr.algebra.jndi.ServerConfigurationKey;
import hr.algebra.jndi.helper.JndiHelper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CodenamesGameServer {
    private static int clientCounter = 0;
    private static final List<ObjectOutputStream> clientOutputs = new ArrayList<>();
    private static final List<Player> verifiedPlayers = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Codenames Java Edition (Server)");
        System.out.println("Made by Leon Krušlin @ Algebra University College | Vlaada Chvatil original board game author\n");
        loadPreviousGameLogs();
        runRmiService();
        try {
            int serverPort = Integer.parseInt(JndiHelper.getConfigurationParameter(ServerConfigurationKey.GAMESERVER_PORT.getKey()));
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.err.println("[Server]: Game server is listening on: " +  serverSocket.getInetAddress() + ":" + serverPort) ;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCounter++;
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                clientOutputs.add(out);
                System.out.println("[Server]: " + clientCounter + " clients connected to the server!");
                if(clientCounter <= GameSettings.PLAYER_COUNT) {
                    new Thread(new ClientHandler(clientSocket, in, out)).start();
                } else {
                    System.out.println("[Server]: Server is full. Connection refused.");
                    clientSocket.close();
                }
            }
        } catch (IOException | NamingException e) {
            e.printStackTrace();
        }
    }

    private static void runRmiService() {
        System.out.println("[RMIServer]: Running RMI service...");
        try {
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(JndiHelper.getConfigurationParameter(ServerConfigurationKey.RMI_SERVER_PORT.getKey())));
            ChatService chatService = new ChatServiceImpl();
            ChatService skeleton = (ChatService) UnicastRemoteObject.exportObject(chatService, 0);
            registry.rebind(ChatService.REMOTE_OBJECT_NAME, skeleton);
            System.out.println("[RMIServer]: Object registered in RMI registry!");
        } catch (NamingException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(Object message) {
        for (ObjectOutputStream out : clientOutputs) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean canAddPlayer(Player requestedPlayer) {
        boolean canAddPlayer = true;
        for (Player player  : verifiedPlayers) {
            if(requestedPlayer.getCardColor() == player.getCardColor()
                    && requestedPlayer.getRole() == player.getRole()){
                canAddPlayer = false;
                break;
            }
        }
        return canAddPlayer;
    }

    private static void saveGameLogs(){
        XmlTurnListWrapper wrapper = new XmlTurnListWrapper(GameLogger.getInstance().getTurnLogs());
        try {
            String xml = XMLConverter.ConvertToXml(wrapper);
            RepositoryFactory.getRepository().writeGameLogs(xml);
        } catch (Exception e) {
            System.out.println("[SERVER] - [ERROR] - Failed to output game logs to xml");
        }
    }

    private static void loadPreviousGameLogs() {
        try {
            String xml = RepositoryFactory.getRepository().readGameLogs();
            Serializer serializer = new Persister();
            XmlTurnListWrapper wrapper = serializer.read(XmlTurnListWrapper.class, xml);
            for (SerializableTurnLog turnLog : wrapper.getTurnLogs()) {
                GameLogger.getInstance().addTurnLog(turnLog);
            }
            System.out.println("FOUND LOGS: " + GameLogger.getInstance().getTurnLogs().size());
        } catch (Exception e) {
            System.out.println("[SERVER]: No previous game logs found.");
        }
    }

    //THIS METHOD IS ONLY USED TO SHOW SYNCHRONIZED KEYWORD FOR I4, NO POINT LOADING IT AGAIN IF IT'S ALREADY IN GameLogger
    private static synchronized Optional<XmlTurnListWrapper> getPreviousTurnLogs(){
        try {
            String xml = RepositoryFactory.getRepository().readGameLogs();
            Serializer serializer = new Persister();
            XmlTurnListWrapper wrapper = serializer.read(XmlTurnListWrapper.class, xml);
            return Optional.of(wrapper);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static void initializeGameState(){
        String redSpymasterName = null;
        String redOperativeName = null;
        String blueSpymasterName = null;
        String blueOperativeName = null;
        for (Player player : verifiedPlayers) {
            if (player.getCardColor() == CardColor.Red && player.getRole() == PlayerRole.Spymaster)
                redSpymasterName = player.getName();
            else if (player.getCardColor() == CardColor.Red && player.getRole() == PlayerRole.Operative)
                redOperativeName = player.getName();
            else if (player.getCardColor() == CardColor.Blue && player.getRole() == PlayerRole.Spymaster)
                blueSpymasterName = player.getName();
            else if (player.getCardColor() == CardColor.Blue && player.getRole() == PlayerRole.Operative)
                blueOperativeName = player.getName();
        }
        GameState initialGameState = new GameState();
        try {
            initialGameState.initialize(redSpymasterName, redOperativeName, blueSpymasterName, blueOperativeName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameHolder.GAMESTATE.setValue(initialGameState);
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ObjectInputStream currentIn;
        private final ObjectOutputStream currentOut;

        public ClientHandler(Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) {
            this.clientSocket = clientSocket;
            this.currentIn = in;
            this.currentOut = out;
        }

        @Override
        public void run() {
            try {

                //SEND LOGS TO PLAYERS IF THEY EXIST!
                Optional<XmlTurnListWrapper> optLogs = getPreviousTurnLogs();
                if(optLogs.isPresent()) {
                    currentOut.writeObject(new Message(StatusCode.INITIAL_GAME_LOGS, optLogs.get()));
                }

                while (true) {

                    /*Object input = currentIn.readObject();
                    if (input instanceof String) {
                        String message = (String) input;
                        if (message.equals("Send to all")) {
                            broadcast(message);
                        }
                    }*/

                    Object recievedObject = currentIn.readObject();

                    if (recievedObject instanceof Message recievedMessage) {
                        if (recievedMessage.getStatusCode() == StatusCode.REQUEST_REGISTRATION) {
                            Player requestedPlayer = (Player) recievedMessage.getContent();
                            System.out.println("[Client]: " + recievedMessage.getStatusCode() + " " + requestedPlayer + " requested registration!");
                            if (canAddPlayer(requestedPlayer)) {
                                System.out.println("[Server]: Approved");
                                verifiedPlayers.add(requestedPlayer);
                                 if (verifiedPlayers.size() == GameSettings.PLAYER_COUNT) {
                                    System.out.println("[Server]: All players registered, starting the game...");
                                    initializeGameState();
                                    broadcast(new Message(StatusCode.START_GAME, GameHolder.GAMESTATE.getValue()));
                                } else {
                                    currentOut.writeObject(new Message(StatusCode.ACCEPT_REGISTRATION, null));
                                    currentOut.flush();
                                }
                            } else {
                                System.out.println("[Server]: Refused");
                                currentOut.writeObject(new Message(StatusCode.REFUSE_REGISTRATION, null));
                            }
                        } else if (recievedMessage.getStatusCode() == StatusCode.DEBUG_MESSAGE) {
                            System.out.println("[Server]: Got a debug message, sending to everyone!");
                            broadcast(new Message(StatusCode.DEBUG_MESSAGE, null));
                        } else if (recievedMessage.getStatusCode() == StatusCode.GAME_MOVE) {
                            System.out.println("[Client]: " + recievedMessage.getStatusCode() + " - Committed a game move");
                            GameState recievedGameState = (GameState) recievedMessage.getContent();
                            System.out.println("[Server]: Broadcasting updated game state to every player... ");

                            if(recievedGameState.getLastTurnLog() != null){
                                GameLogger.getInstance().addTurnLog(recievedGameState.getLastTurnLog());
                                if(recievedGameState.getHasWinner()){
                                    saveGameLogs();
                                }
                            }

                            broadcast(new Message(StatusCode.GAME_MOVE, recievedGameState));
                        } else if (recievedMessage.getStatusCode() == StatusCode.CHAT_MESSAGE) {
                            System.out.println("[Client]: " + recievedMessage.getStatusCode() + " - Sent chat message");
                            System.out.println("[Server]: RMI Server handled chat. Requesting to refresh chat area for every player");
                            broadcast(new Message(StatusCode.CHAT_MESSAGE, null));
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    currentIn.close();
                    clientSocket.close();
                    clientOutputs.remove(currentOut);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
