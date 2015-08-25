package com.github.lzenczuk.ngs.message;

/**
 * @author lzenczuk 25/08/2015
 */
public class ChannelMessage {

    private String messageClass;
    private String message;

    public ChannelMessage() {
    }

    public ChannelMessage(String message, String messageClass) {
        this.message = message;
        this.messageClass = messageClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(String messageClass) {
        this.messageClass = messageClass;
    }
}
