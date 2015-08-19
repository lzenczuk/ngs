package com.github.lzenczuk.ngs.message.processor;

/**
 * @author lzenczuk 19/08/2015
 */
public interface OutputPublisher<O> {
    boolean sendOutput(O message);
}
