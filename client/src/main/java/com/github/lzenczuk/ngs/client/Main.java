package com.github.lzenczuk.ngs.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lzenczuk 14/08/2015
 */
public class Main {

    public static final int NUMBER_OF_MESSAGES = 10000;

    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        Object endLock = new Object();
        AtomicInteger messages = new AtomicInteger(0);
        AtomicLong delay = new AtomicLong(0);

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
                long currentNanoTime = System.nanoTime();
                long nanoTime = Long.parseLong(message);

                long delta = currentNanoTime - nanoTime;
                delay.addAndGet(delta);

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

        for(int x=0;x< NUMBER_OF_MESSAGES;x++){
            long nanoTime = System.nanoTime();

            client.send(Long.toString(nanoTime));
            //client.send(x+":"+Long.toString(nanoTime));
        }

        synchronized (endLock){
            System.out.println("Waiting client to receive all messages");
            endLock.wait(30000);
        }

        long d = delay.get();
        int m = messages.get();

        double ad=d*1.0/m;

        System.out.println("Processed messages: "+m+" from "+NUMBER_OF_MESSAGES+". Average delay:"+ad);

        System.exit(0);
    }
}
