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
        System.out.println("Receive message: " + msg);

        engine.executeCommand(msg.text());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");

        new Thread(() -> {
            BlockingQueue<String> outputQ = engine.getOutputQ();
            while (true){
                try {
                    String message = outputQ.take();
                    ctx.writeAndFlush(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
    }
}
