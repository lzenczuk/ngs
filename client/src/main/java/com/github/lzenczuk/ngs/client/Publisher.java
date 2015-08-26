package com.github.lzenczuk.ngs.client;

import com.github.lzenczuk.ngs.message.ChannelMessage;
import com.github.lzenczuk.ngs.message.task.AddTask;

import java.util.function.Supplier;

/**
 * @author lzenczuk 26/08/2015
 */
public class Publisher {

    private final WSClient client;

    private final long publishingTime;
    private final long messagesPerSecond;

    private Supplier<String> producer = () -> "";

    public Publisher(WSClient client, long publishingTime, long messagesPerSecond) {
        this.client = client;
        this.publishingTime = publishingTime;
        this.messagesPerSecond = messagesPerSecond;
    }

    public void setProducer(Supplier<String> producer) {
        this.producer = producer;
    }

    public void start() throws InterruptedException {

        long numberOfMessages = messagesPerSecond*publishingTime;
        long messageProcessingTime = 1000000000/messagesPerSecond;

        for(int x=0;x< numberOfMessages;x++){
            long nanoTime = System.nanoTime();

            client.sendMessage(producer.get());

            long currentNanoTime = System.nanoTime();

            long sendNanoTime = currentNanoTime-nanoTime;

            long waitingNanos = messageProcessingTime-sendNanoTime;

            long waitingMillis = 0;
            if(waitingNanos>999999){
                waitingMillis = waitingNanos/1000000;
                waitingNanos = waitingNanos-waitingMillis*1000000;
            }
            System.out.println("Publisher messageProcessingTime: "+messageProcessingTime+" processing nanos: "+sendNanoTime+" waiting: "+waitingMillis+":"+waitingNanos);

            Thread.sleep(waitingMillis, (int)waitingNanos);
        }


    }
}
