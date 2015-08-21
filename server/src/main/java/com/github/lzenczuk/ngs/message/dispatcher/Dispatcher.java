package com.github.lzenczuk.ngs.message.dispatcher;

import com.github.lzenczuk.ngs.engine.Engine;
import com.github.lzenczuk.ngs.message.Message;

import java.util.Optional;

/**
 * @author lzenczuk 21/08/2015
 */
public interface Dispatcher<InM extends Message, OutM extends Message> {
    void registerEngine(long engineId, Engine<InM, OutM> engine);

    Optional<OutM> process(InM message);
}
