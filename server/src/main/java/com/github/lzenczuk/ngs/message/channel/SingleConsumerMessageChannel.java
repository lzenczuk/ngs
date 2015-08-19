package com.github.lzenczuk.ngs.message.channel;

import java.util.function.Consumer;

/**
 * @author lzenczuk 19/08/2015
 */
public interface SingleConsumerMessageChannel<I> {
    boolean sendMessage(I message);
    boolean setConsumer(Consumer<I> consumer);
}
