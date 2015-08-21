package com.github.lzenczuk.ngs.channel.inoutchannel;

import com.github.lzenczuk.ngs.channel.SingleConsumerChannel;
import com.github.lzenczuk.ngs.channel.SingleConsumerSingleQueueChannelImpl;
import com.github.lzenczuk.ngs.channel.inoutchannel.impl.InputOutputChannelImpl;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

/**
 * @author lzenczuk 19/08/2015
 */
public class InputOutputChannelImplTest {

    @Test
    public void subscribersShouldReceiveAllMessagesInCorrectOrder() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(6);
        List<Integer> numbers = Collections.synchronizedList(new LinkedList<>());
        List<String> strings = Collections.synchronizedList(new LinkedList<>());

        SingleConsumerChannel<String> inChannel = new SingleConsumerSingleQueueChannelImpl<>();
        SingleConsumerChannel<Integer> outChannel = new SingleConsumerSingleQueueChannelImpl<>();

        InputOutputChannelImpl<String, Integer> processor = new InputOutputChannelImpl<>(inChannel, outChannel);

        processor.setInputConsumer((s) -> {
            strings.add(s);
            int i = Integer.parseInt(s);
            processor.sendOutput(i);
        });

        processor.setOutputConsumer((i) -> {
            numbers.add(i);
            countDownLatch.countDown();
        });

        processor.sendInput("1");
        processor.sendInput("2");
        processor.sendInput("3");
        processor.sendInput("4");
        processor.sendInput("5");
        processor.sendInput("6");

        countDownLatch.await(2, TimeUnit.SECONDS);

        assertThat(strings, contains("1", "2", "3", "4", "5", "6"));
        assertThat(numbers, contains(1, 2, 3, 4, 5, 6));
    }
}
