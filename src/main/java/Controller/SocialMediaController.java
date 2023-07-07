package Controller;

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
        app.post("/register", this::postUserRegistrationHandler);
        app.post("/login", this::postLoginHandler);

        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);

        app.get("/messages/{message_id}", this::getMessByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        app.get("accounts/{account_id}/messages", this::getMessagesFromAccIdHandler);

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
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages()).status(200);
        // ctx.status(200);
    }
    private void getMessByIdHandler(Context ctx) {
        int messageId = ctx.pathParamAsClass("messageId", Integer.class).get();
    Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message).status(200);
        } else {
            ctx.json("").status(200);
        }
    }

    private void deleteMessByIdHandler(Context ctx) {
        String messageId = ctx.pathParam("messageId");
        Message deletedMessage = messageService.deleteMessageById(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.json("").status(200);
        }
    }

    private void updateMessageHandler (Context ctx) throws JsonMappingException, JsonProcessingException { //Not sure if good DOUBLE CHECK
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(ctx.pathParam("messageId"), message);
        if (updatedMessage != null) {
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(updatedMessage));
        } else {
            ctx.status(400);
        }    
    }
    private void getMessagesFromAccIdHandler(Context ctx) {
        String accountId = ctx.pathParam("accountId");
        ctx.json(messageService.getMessagesByAccountId(accountId));
        ctx.status(200);
    }

    
}