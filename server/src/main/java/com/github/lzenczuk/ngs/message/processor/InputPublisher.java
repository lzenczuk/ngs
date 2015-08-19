package com.github.lzenczuk.ngs.message.processor;

/**
 * @author lzenczuk 19/08/2015
 */
public interface InputPublisher<I> {
    boolean sendInput(I message);
}
