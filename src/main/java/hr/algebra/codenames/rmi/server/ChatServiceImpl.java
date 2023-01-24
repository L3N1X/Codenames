package hr.algebra.codenames.rmi.server;

import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatService {

    List<String> chatHistoryMessageList;

    public ChatServiceImpl() {
        this.chatHistoryMessageList = new ArrayList<>() ;
    }

    @Override
    public void sendMessage(String newMessage) {
        this.chatHistoryMessageList.add(newMessage);
    }

    @Override
    public List<String> getChatHistory() {
        return this.chatHistoryMessageList;
    }
}
