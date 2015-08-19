package com.github.lzenczuk.ngs.message.processor;

import com.github.lzenczuk.ngs.message.channel.SingleConsumerMessageChannel;
import com.github.lzenczuk.ngs.message.processor.impl.MessageProcessorImpl;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * @author lzenczuk 19/08/2015
 */
public class MessageProcessorImplTest {

    @Test
    public void subscribersShouldReceiveAllMessagesInCorrectOrder() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(6);
        List<String> messages = Collections.synchronizedList(new LinkedList<>());

        SingleConsumerMessageChannel<String> inChannelMock = mock(SingleConsumerMessageChannel.class);
        SingleConsumerMessageChannel<Integer> outChannelMock = mock(SingleConsumerMessageChannel.class);

        MessageProcessorImpl<String, Integer> processor = new MessageProcessorImpl<>(inChannelMock, outChannelMock);


        /*boolean addingConsumerResult = channel.setConsumer((String msg) -> {
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
        assertThat(messages, contains("Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6"));*/
    }
}
