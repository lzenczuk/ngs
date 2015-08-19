package com.github.lzenczuk.ngs.message.processor.impl;

import com.github.lzenczuk.ngs.message.channel.SingleConsumerMessageChannel;
import com.github.lzenczuk.ngs.message.processor.InputPublisher;
import com.github.lzenczuk.ngs.message.processor.InputSingleConsumer;
import com.github.lzenczuk.ngs.message.processor.OutputPublisher;
import com.github.lzenczuk.ngs.message.processor.OutputSingleConsumer;

import java.util.function.Consumer;

/**
 * @author lzenczuk 19/08/2015
 */
public class MessageProcessorImpl<I, O> implements InputPublisher<I>,OutputPublisher<O>,InputSingleConsumer<I>,OutputSingleConsumer<O> {

    private final SingleConsumerMessageChannel<I> inChannel;
    private final SingleConsumerMessageChannel<O> outChannel;

    public MessageProcessorImpl(SingleConsumerMessageChannel<I> inChannel, SingleConsumerMessageChannel<O> outChannel) {
        this.inChannel = inChannel;
        this.outChannel = outChannel;
    }

    @Override
    public boolean sendInput(I message) {
        return inChannel.sendMessage(message);
    }

    @Override
    public boolean sendOutput(O message) {
        return outChannel.sendMessage(message);
    }

    @Override
    public void setInputConsumer(Consumer<I> consumer) {
        inChannel.setConsumer(consumer);
    }

    @Override
    public void setOutputConsumer(Consumer<O> consumer) {
        outChannel.setConsumer(consumer);
    }
}
