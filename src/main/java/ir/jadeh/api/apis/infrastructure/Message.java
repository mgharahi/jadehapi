package ir.jadeh.api.apis.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class Message {
    private MessageType messageType;
    private String message;

    public Message() {
        messageType = MessageType.none;
    }

    public Message SetSuccessfulMessage(String message) {
        setMessageType(MessageType.success);
        setMessage(message);

        return this;
    }

    public Message SetFailedMessage(String message) {
        setMessageType(MessageType.fail);
        setMessage(message);

        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String ToJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
