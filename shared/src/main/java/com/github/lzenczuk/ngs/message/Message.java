package com.github.lzenczuk.ngs.message;

/**
 * @author lzenczuk 21/08/2015
 */
public abstract class Message {

    private final long engineId;

    public Message() {
        this.engineId=-1;
    }

    public Message(long engineId) {
        this.engineId = engineId;
    }

    public long getEngineId() {
        return engineId;
    }
}
