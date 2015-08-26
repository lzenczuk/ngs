package com.github.lzenczuk.ngs.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzenczuk.ngs.message.ChannelMessage;
import com.github.lzenczuk.ngs.message.task.AddTask;
import com.github.lzenczuk.ngs.message.task.TaskAdded;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lzenczuk 14/08/2015
 */
public class Main {

    public static final int messagesPerSecond = 10;
    public static final int timeInSeconds = 10;

    public static void main(String[] args) throws URISyntaxException, InterruptedException, JsonProcessingException, FileNotFoundException {

        Object endLock = new Object();
        AtomicInteger messages = new AtomicInteger(0);
        AtomicLong delay = new AtomicLong(0);

        ObjectMapper objectMapper = new ObjectMapper();

        WSClient client = new WSClient("ws://localhost:8088/ws");

        PerformanceMeter meter = new PerformanceMeter();
        client.setMessageConsumer(meter);

        Publisher publisher = new Publisher(client, timeInSeconds, messagesPerSecond);
        publisher.setProducer(() -> {

            try {
                AddTask addTask = new AddTask(1, "Task " + System.nanoTime());
                String messageClass = addTask.getClass().getCanonicalName();
                String message = null;
                message = objectMapper.writeValueAsString(addTask);
                ChannelMessage channelMessage = new ChannelMessage(message, messageClass);

                return objectMapper.writeValueAsString(channelMessage);

            } catch (JsonProcessingException e) {
                e.printStackTrace();

                return "";
            }
        });

        publisher.start();
        meter.waitForNumberOfMessagesOrSeconds(100, 6);

        PrintWriter printWriter = new PrintWriter("out.cvs");
        printWriter.println("nanos");
        printWriter.println(meter.toString());
        printWriter.close();

        System.out.println("Result saved");

        System.exit(0);
    }
}
