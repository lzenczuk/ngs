package com.github.lzenczuk.ngs.message.channel;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author lzenczuk 19/08/2015
 */
public class SingleQueueMessageChannelTest {

    @Test
    public void subscriberShouldReceiveAllMessagesInCorrectOrder() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(6);
        List<String> messages = Collections.synchronizedList(new LinkedList<>());

        SingleQueueMessageChannel<String> channel = new SingleQueueMessageChannel<>();

        boolean addingConsumerResult = channel.setConsumer((String msg) -> {
            System.out.println("Consumer 1. Receive message: " + msg);
            messages.add(msg);
            countDownLatch.countDown();
        });

        channel.sendMessage("Test 1");
        channel.sendMessage("Test 2");
        channel.sendMessage("Test 3");
        channel.sendMessage("Test 4");
        channel.sendMessage("Test 5");
        channel.sendMessage("Test 6");

        countDownLatch.await(2, TimeUnit.SECONDS);

        assertThat(addingConsumerResult, is(true));
        assertThat(messages, contains("Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6"));
    }
}
