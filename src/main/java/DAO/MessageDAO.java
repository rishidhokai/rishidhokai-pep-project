package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            // PreparedStatement ps = conn.prepareStatement(sql);
            // ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                // Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                
                int messageId = rs.getInt("message_id");
                // System.out.println("OOGA BOOGA Message ID: " + messageId);

                int postedBy = rs.getInt("posted_by");
                // System.out.println("Posted By: " + postedBy);

                String messageText = rs.getString("message_text");
                // System.out.println("Message text: " + messageText);

                long timePostedEpoch = rs.getLong("time_posted_epoch");
                // System.out.println("TP Epoch: " + timePostedEpoch);



                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                // System.out.println("Here is a message: " + message.toString());
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int messageId = generatedKeys.getInt(1);
                    message.setMessage_id(messageId);
                    return message;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message getMessageById(int messageId) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int messageIdValue = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");

                return new Message(messageIdValue, postedBy, messageText, timePostedEpoch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message deleteMessageById(int messageId) {
        Connection conn = ConnectionUtil.getConnection();
        Message m = getMessageById(messageId);    
        if(m != null) {
            try {
                String sql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, messageId);
                int rowsDeleted = ps.executeUpdate();
                if (rowsDeleted > 0) {
                    return m;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }

    public Message updateMessage(int messageId, Message updatedMessage) {
        Connection conn = ConnectionUtil.getConnection();
        
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            // Statement s = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, updatedMessage.getMessage_text());            
            ps.setInt(2, messageId);   
            
            int rowsUpdated = ps.executeUpdate();
            if(rowsUpdated > 0 ) {
                // System.out.println("OOGA BOOGAAAAAAAA");
                // System.out.println(updatedMessage.toString());
                // System.out.println("Get OOGA BOOGAAAAAAAADDDDDDDDDDDD");
                return updatedMessage;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public List<Message> getMessagesByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");

                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

}