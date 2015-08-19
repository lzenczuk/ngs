package com.github.lzenczuk.ngs.message.processor;

import java.util.function.Consumer;

/**
 * @author lzenczuk 19/08/2015
 */
public interface OutputSingleConsumer<O> {
    void setOutputConsumer(Consumer<O> consumer);
}
