package ir.jadeh.api.controllers.infrastructure;

import lombok.Data;

@Data
public class SessionIdMessage extends Message {
    private String token;
}
