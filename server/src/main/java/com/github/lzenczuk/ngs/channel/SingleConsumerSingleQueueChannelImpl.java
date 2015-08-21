package com.github.lzenczuk.ngs.channel;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class SingleConsumerSingleQueueChannelImpl<I> implements SingleConsumerChannel<I> {
    private  BlockingQueue<I> queue = new ArrayBlockingQueue<I>(1024);

    private Consumer<I> consumer;

    private void runPublisherThread() {
        new Thread(() -> {
            for(;;) {
                try {
                    consumer.accept(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean sendMessage(I message) {
        try {
            return queue.add(message);
        }catch (IllegalStateException e){
            return false;
        }
    }

    @Override
    public boolean setConsumer(Consumer<I> consumer) {
        if(this.consumer==null){
            this.consumer = consumer;
            runPublisherThread();
            return true;
        }else{
            return false;
        }
    }
}