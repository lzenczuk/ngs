package com.github.lzenczuk.ngs.channel;

import java.util.function.Consumer;

/**
 * @author lzenczuk 19/08/2015
 */
public interface SingleConsumerChannel<I> {
    boolean sendMessage(I message);
    boolean setConsumer(Consumer<I> consumer);
}
