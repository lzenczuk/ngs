package com.github.lzenczuk.ngs.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzenczuk.ngs.channel.inoutchannel.impl.InputOutputChannel;
import com.github.lzenczuk.ngs.channel.inoutchannel.impl.InputOutputChannelImpl;
import com.github.lzenczuk.ngs.message.ChannelMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author lzenczuk 13/08/2015
 */
public class ServerHandler<InM, OutM> extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final InputOutputChannel<InM, OutM> processor;

    private final Class<InM> inMessageClass;
    private final Class<OutM> outMessageClass;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServerHandler(InputOutputChannel<InM, OutM> processor, Class<InM> inMessageClass, Class<OutM> outMessageClass) {
        this.inMessageClass = inMessageClass;
        this.processor = processor;
        this.outMessageClass = outMessageClass;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        ChannelMessage channelMessage = objectMapper.readValue(msg.text(), ChannelMessage.class);

        Class<?> messageClass = Class.forName(channelMessage.getMessageClass());

        // TODO - replace this with some matcher - creating instance in expensive
        if(inMessageClass.isInstance(messageClass.newInstance())){
            Object message = objectMapper.readValue(channelMessage.getMessage(), messageClass);
            processor.sendInput((InM) message);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        processor.setOutputConsumer((message) -> {
            try {
                String messageClass = message.getClass().getCanonicalName();
                String jsonMessage = objectMapper.writeValueAsString(message);

                String channelMessage = objectMapper.writeValueAsString(new ChannelMessage(jsonMessage, messageClass));

                ctx.writeAndFlush(new TextWebSocketFrame(channelMessage));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
