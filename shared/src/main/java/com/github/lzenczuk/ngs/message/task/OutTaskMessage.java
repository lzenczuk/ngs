package com.github.lzenczuk.ngs.message.task;

import com.github.lzenczuk.ngs.message.Message;

/**
 * @author lzenczuk 21/08/2015
 */
public abstract class OutTaskMessage extends Message {

    public OutTaskMessage() {}

    public OutTaskMessage(long engineId) {
        super(engineId);
    }
}
