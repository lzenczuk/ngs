package com.github.lzenczuk.ngs.client;

import com.github.lzenczuk.ngs.message.ChannelMessage;
import com.github.lzenczuk.ngs.message.task.TaskAdded;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

/**
 * @author lzenczuk 26/08/2015
 */
public class WSClient {

    private WebSocketClient client;

    private Object openLock = new Object();

    private Consumer<String> messageConsumer = (String msg) -> {};

    public WSClient(String uri) throws URISyntaxException, InterruptedException {

        client = new WebSocketClient(new URI(uri), new Draft_10()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connection open");
                synchronized (openLock) {
                    openLock.notify();
                }
            }

            @Override
            public void onMessage(String message) {
                messageConsumer.accept(message);
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

        synchronized (openLock){
            System.out.println("Waiting client to connect");
            client.connect();
            openLock.wait();
        }
    }

    public void setMessageConsumer(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public void sendMessage(String message){
        client.send(message);
    }
}
