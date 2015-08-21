package com.github.lzenczuk.ngs.message.dispatcher;

import com.github.lzenczuk.ngs.engine.Engine;
import com.github.lzenczuk.ngs.message.Message;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzenczuk 21/08/2015
 */
public class DispatcherImpl<InM extends Message, OutM extends Message> implements Dispatcher<InM, OutM> {

    private ConcurrentHashMap<Long, Engine> engineMap = new ConcurrentHashMap<>();

    @Override
    public void registerEngine(long engineId, Engine<InM, OutM> engine){
        engineMap.put(engineId, engine);
    }

    @Override
    public Optional<OutM> process(InM message){
        return Optional.ofNullable(engineMap.get(message.getEngineId()))
                .map(engine -> engine.process(message)).orElse(Optional.empty());
    }
}
