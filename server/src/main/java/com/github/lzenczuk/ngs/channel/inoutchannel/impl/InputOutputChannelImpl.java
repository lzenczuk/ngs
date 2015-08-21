package com.github.lzenczuk.ngs.channel.inoutchannel.impl;

import com.github.lzenczuk.ngs.channel.SingleConsumerChannel;
import java.util.function.Consumer;

/**
 * @author lzenczuk 19/08/2015
 */
public class InputOutputChannelImpl<I, O> implements InputOutputChannel<I,O> {

    private final SingleConsumerChannel<I> inChannel;
    private final SingleConsumerChannel<O> outChannel;

    public InputOutputChannelImpl(SingleConsumerChannel<I> inChannel, SingleConsumerChannel<O> outChannel) {
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
