package ir.jadeh.api.controllers.infrastructure;

import lombok.Data;

@Data
public class Message {

    private MessageType messageType;
    private String message;

    public Message() {
        messageType = MessageType.none;
    }

    public Message setSuccessfulMessage(String message) {
        messageType = MessageType.success;
        this.message = message;
        

        return this;
    }

    public Message setFailedMessage(String message) {
        messageType = MessageType.fail;
        this.message = message;

        return this;
    }
}
