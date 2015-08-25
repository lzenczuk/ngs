package com.github.lzenczuk.ngs;

import com.github.lzenczuk.ngs.channel.SingleConsumerChannel;
import com.github.lzenczuk.ngs.channel.SingleConsumerSingleQueueChannelImpl;
import com.github.lzenczuk.ngs.channel.inoutchannel.impl.InputOutputChannelImpl;
import com.github.lzenczuk.ngs.engine.task.TaskEngine;
import com.github.lzenczuk.ngs.message.dispatcher.DispatcherImpl;
import com.github.lzenczuk.ngs.message.task.InTaskMessage;
import com.github.lzenczuk.ngs.message.task.OutTaskMessage;
import com.github.lzenczuk.ngs.server.Server;

/**
 * @author lzenczuk 13/08/2015
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        SingleConsumerChannel<InTaskMessage> inChannel = new SingleConsumerSingleQueueChannelImpl<>();
        SingleConsumerChannel<OutTaskMessage> outChannel = new SingleConsumerSingleQueueChannelImpl<>();
        InputOutputChannelImpl<InTaskMessage, OutTaskMessage> processor = new InputOutputChannelImpl<>(inChannel, outChannel);

        TaskEngine engine = new TaskEngine();

        DispatcherImpl<InTaskMessage, OutTaskMessage> dispatcher = new DispatcherImpl<>();
        dispatcher.registerEngine(1, engine);

        processor.setInputConsumer((inTaskMessage -> dispatcher.process(inTaskMessage).map(processor::sendOutput)));

        new Server(processor, InTaskMessage.class, OutTaskMessage.class).start();
    }
}
