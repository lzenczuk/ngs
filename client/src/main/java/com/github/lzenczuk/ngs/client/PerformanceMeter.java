package com.github.lzenczuk.ngs.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzenczuk.ngs.message.ChannelMessage;
import com.github.lzenczuk.ngs.message.task.TaskAdded;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author lzenczuk 26/08/2015
 */
public class PerformanceMeter implements Consumer<String>{

    private List<Long> delayLog = Collections.synchronizedList(new LinkedList<>());

    private ObjectMapper objectMapper = new ObjectMapper();

    private Object waitLock = new Object();

    private AtomicLong messageCounter = new AtomicLong();
    private AtomicLong expectedMessages = new AtomicLong(-1);

    @Override
    public void accept(String message) {

        try {
            long currentNanoTime = System.nanoTime();
            ChannelMessage channelMessage = objectMapper.readValue(message, ChannelMessage.class);

            Object value = objectMapper.readValue(channelMessage.getMessage(), Class.forName(channelMessage.getMessageClass()));

            if(value instanceof TaskAdded){
                TaskAdded ta = (TaskAdded) value;

                String[] strings = ta.getTaskTitle().split(" ");
                long nanoTime = Long.parseLong(strings[1]);
                long delta = currentNanoTime - nanoTime;
                delayLog.add(delta);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        long messages = messageCounter.incrementAndGet();
        long expected = expectedMessages.get();

        if(expected!=-1 && messages>=expected){
            synchronized (waitLock){
                waitLock.notify();
            }
        }
    }

    public void waitForNumberOfMessagesOrSeconds(long numberOfMessages, long time) throws InterruptedException {

        if(messageCounter.get()>=numberOfMessages) return;

        synchronized (waitLock){
            waitLock.wait(time * 1000);
        }

    }

    @Override
    public String toString() {

        return delayLog.stream().map(d -> d.toString()+";\n").reduce("", (acc, n) -> acc+n);
    }
}
