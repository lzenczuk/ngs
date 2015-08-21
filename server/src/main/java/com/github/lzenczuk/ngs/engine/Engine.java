package com.github.lzenczuk.ngs.engine;

import com.github.lzenczuk.ngs.message.Message;

import java.util.Optional;

/**
 * @author lzenczuk 13/08/2015
 */
public interface Engine<InM extends Message, OutM extends Message> {

    Optional<OutM> process(InM message);
}
