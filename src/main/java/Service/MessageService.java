package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.*;


public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO MessageDAO){
        this.messageDAO = MessageDAO;
    }

    public Message createMessage(Message message) {
        return (!message.getMessage_text().isEmpty() && message.getMessage_text().length() <= 254) ? messageDAO.createMessage(message) : null;
        // if (!message.getMessage_text().isEmpty() && message.getMessage_text().length() <= 255) {
            // return ;
// 
        // }
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(String messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(String messageId, Message updatedMessage) {
        return (!updatedMessage.getMessage_text().isEmpty() && updatedMessage.getMessage_text().length() <= 254) ? messageDAO.updateMessage(messageId, updatedMessage) : null; //OR updatED
    }

    public List<Message> getMessagesByAccountId(String accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }


}

