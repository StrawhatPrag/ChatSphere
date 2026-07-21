package gui.model;

import java.time.LocalTime;

public class ChatMessage {

    public enum Type {
        CHAT,
        PRIVATE,
        SYSTEM
    }

    private final String sender;
    private final String text;
    private final LocalTime time;
    private final Type type;

    public ChatMessage(String sender,
                       String text,
                       LocalTime time,
                       Type type) {

        this.sender = sender;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalTime getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }
}