package com.github.lzenczuk.ngs.server;

import com.github.lzenczuk.ngs.channel.inoutchannel.impl.InputOutputChannel;
import com.github.lzenczuk.ngs.channel.inoutchannel.impl.InputOutputChannelImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author lzenczuk 13/08/2015
 */
public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final InputOutputChannel<String, String> processor;

    public ServerHandler(InputOutputChannel<String, String> processor) {
        this.processor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        processor.sendInput(msg.text());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        processor.setOutputConsumer((message) -> ctx.writeAndFlush(new TextWebSocketFrame(message)));

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
