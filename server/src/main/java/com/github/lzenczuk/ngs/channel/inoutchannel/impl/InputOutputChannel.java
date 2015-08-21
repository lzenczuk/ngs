package com.github.lzenczuk.ngs.channel.inoutchannel.impl;

import java.util.function.Consumer;

/**
 * @author lzenczuk 21/08/2015
 */
public interface InputOutputChannel<I, O> {
    boolean sendInput(I message);

    boolean sendOutput(O message);

    void setInputConsumer(Consumer<I> consumer);

    void setOutputConsumer(Consumer<O> consumer);
}
