package com.github.lzenczuk.ngs.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzenczuk.ngs.message.ChannelMessage;
import com.github.lzenczuk.ngs.message.task.AddTask;
import com.github.lzenczuk.ngs.message.task.TaskAdded;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lzenczuk 14/08/2015
 */
public class Main {

    public static final int NUMBER_OF_MESSAGES = 100000;

    public static final int messagesPerSecond = 10;
    public static final int timeInSeconds = 60;

    public static void main(String[] args) throws URISyntaxException, InterruptedException, JsonProcessingException {

        Object endLock = new Object();
        AtomicInteger messages = new AtomicInteger(0);
        AtomicLong delay = new AtomicLong(0);

        ObjectMapper objectMapper = new ObjectMapper();

        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:8088/ws"), new Draft_10()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connection open");
                synchronized (endLock) {
                    endLock.notify();
                }
            }

            @Override
            public void onMessage(String message) {
                try {
                    long currentNanoTime = System.nanoTime();
                    ChannelMessage channelMessage = objectMapper.readValue(message, ChannelMessage.class);

                    Object value = objectMapper.readValue(channelMessage.getMessage(), Class.forName(channelMessage.getMessageClass()));

                    if(value instanceof TaskAdded){
                        TaskAdded ta = (TaskAdded) value;

                        String[] strings = ta.getTaskTitle().split(" ");
                        long nanoTime = Long.parseLong(strings[1]);
                        long delta = currentNanoTime - nanoTime;
                        delay.addAndGet(delta);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                int receivedMessages = messages.incrementAndGet();
                if(receivedMessages ==NUMBER_OF_MESSAGES){
                    synchronized (endLock) {
                        endLock.notify();
                    }
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Connection close");
                System.exit(1);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                System.exit(1);
            }
        };

        synchronized (endLock){
            System.out.println("Waiting client to connect");
            client.connect();
            endLock.wait();
        }

        long numberOfMessages = messagesPerSecond*timeInSeconds;
        long messageProcessingTime = 1000000000/messagesPerSecond;

        System.out.println("message processing time: "+messageProcessingTime);

        for(int x=0;x< numberOfMessages;x++){
            long nanoTime = System.nanoTime();

            AddTask addTask = new AddTask(1, "Task " + nanoTime);

            String messageClass = addTask.getClass().getCanonicalName();
            String message = objectMapper.writeValueAsString(addTask);

            ChannelMessage channelMessage = new ChannelMessage(message, messageClass);
            String channelMessageString = objectMapper.writeValueAsString(channelMessage);

            client.send(channelMessageString);

            long currentNanoTime = System.nanoTime();

            long sendNanoTime = currentNanoTime-nanoTime;

            long waitingNanos = messageProcessingTime-sendNanoTime;

            long waitingMillis = 0;
            if(waitingNanos>999999){
                waitingMillis = waitingNanos/1000000;
                waitingNanos = waitingNanos-waitingMillis*1000000;
            }
            System.out.println("messageProcessingTime: "+messageProcessingTime+" processing nanos: "+sendNanoTime+" waiting: "+waitingMillis+":"+waitingNanos);

            Thread.sleep(waitingMillis, (int)waitingNanos);
        }

        synchronized (endLock){
            System.out.println("Waiting client to receive all messages");
            endLock.wait(30000);
        }

        long d = delay.get();
        int m = messages.get();

        double ad=d*1.0/m;

        System.out.println("Processed messages: "+m+" from "+numberOfMessages+". Average delay:"+ad);

        System.exit(0);
    }
}
