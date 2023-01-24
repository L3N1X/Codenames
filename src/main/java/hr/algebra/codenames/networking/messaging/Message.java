package hr.algebra.codenames.networking.messaging;

import hr.algebra.codenames.networking.response.StatusCode;

import java.io.Serializable;

public class Message implements Serializable {
    private final StatusCode statusCode;
    private final Object content;

    public Message(StatusCode statusCode, Object content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Object getContent() {
        return content;
    }
}
