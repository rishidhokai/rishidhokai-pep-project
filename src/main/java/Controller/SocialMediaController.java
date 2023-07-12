package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postUserRegistrationHandler); //works
        app.post("/login", this::postLoginHandler); //works

        app.post("/messages", this::createMessageHandler); //works
        app.get("/messages", this::getAllMessagesHandler); //BROKEN 1/2

        app.get("/messages/{message_id}", this::getMessByIdHandler); //BROKEN
        app.delete("/messages/{message_id}", this::deleteMessByIdHandler); //BROKEN
        app.patch("/messages/{message_id}", this::updateMessageHandler); //works 4/5

        app.get("accounts/{account_id}/messages", this::getMessagesFromAccIdHandler); //BROKEN

        return app;
    }
    
    private void postUserRegistrationHandler(Context ctx) throws JsonProcessingException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerUser(account);
        if(addedAccount!=null){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
        else {
            ctx.status(400);
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.loginAccount(account);
        if(loginAccount!=null){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(loginAccount));
        }else{
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), (Message.class));
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.json(createdMessage).status(200);
        } else {
            ctx.status(400);
        }
    }

    // private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        // List<Message> messages = messageService.getAllMessages();
        // ObjectMapper mapper = new ObjectMapper();
        // String json = mapper.writeValueAsString(messages);
        // ctx.result(json).status(200);
        // ctx.json(new ArrayList<Message>()).status(200);
    // }

    private void getAllMessagesHandler(Context ctx) { //200 only
        ctx.json(messageService.getAllMessages()).status(200);    
    }
    
    private void getMessByIdHandler(Context ctx) { //200 only
        int messageId = ctx.pathParamAsClass("message_id", Integer.class).get();
        // int messageId = ctx.pathParam("message_id", Integer.class).get();
    Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message).status(200);
        } else {
            ctx.json("").status(200);
        }
    }

    private void deleteMessByIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException { //BROKEN
        int messageId = ctx.pathParamAsClass("message_id", Integer.class).get();
        Message deletedMessage = messageService.deleteMessageById(messageId);
        // Message deletedMessage = messageService.deleteMessageById(mapper.readValue(ctx.body(), Integer.class));
        if (deletedMessage != null) {
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.json("").status(200);
        }
    }

    private void updateMessageHandler (Context ctx) throws JsonMappingException, JsonProcessingException { //Not sure if good DOUBLE CHECK
        ObjectMapper mapper = new ObjectMapper();
        int messageId = ctx.pathParamAsClass("message_id", Integer.class).get();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(messageId, message);
        if (updatedMessage != null) {
            // ctx.result(mapper.writeValueAsString(updatedMessage)).status(200);
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(updatedMessage));
        } else {
            ctx.status(400);
        }    
    }
    private void getMessagesFromAccIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        int accountId = ctx.pathParamAsClass("account_id", Integer.class).get();
        ctx.json(messageService.getMessagesByAccountId(accountId));
        ctx.status(200);
    }

    
}