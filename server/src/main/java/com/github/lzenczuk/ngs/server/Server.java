package com.github.lzenczuk.ngs.server;

import com.github.lzenczuk.ngs.channel.inoutchannel.impl.InputOutputChannel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author lzenczuk 13/08/2015
 */
public class Server<InM, OutM> {

    private final InputOutputChannel<InM, OutM> channel;

    private final Class<InM> inMessageClass;
    private final Class<OutM> outMessageClass;

    public Server(InputOutputChannel<InM, OutM> channel, Class<InM> inMessageClass, Class<OutM> outMessageClass) {
        this.channel = channel;
        this.inMessageClass = inMessageClass;
        this.outMessageClass = outMessageClass;
    }

    public void start()  throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(2));
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            pipeline.addLast(new ServerHandler(channel, inMessageClass, outMessageClass));
                        }
                    });

            Channel ch = b.bind(8088).sync().channel();

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
