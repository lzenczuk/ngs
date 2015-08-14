package com.github.lzenczuk.ngs;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.BlockingQueue;

/**
 * @author lzenczuk 13/08/2015
 */
public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final Engine engine;

    public ServerHandler(EngineManager engineManager) {
        engine = engineManager.getEngine();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        engine.executeCommand(msg.text());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        final ChannelHandlerContext c = ctx;

        new Thread(() -> {
            BlockingQueue<String> outputQ = engine.getOutputQ();
            while (true){
                try {
                    String message = outputQ.take();
                    c.writeAndFlush(new TextWebSocketFrame(message));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
