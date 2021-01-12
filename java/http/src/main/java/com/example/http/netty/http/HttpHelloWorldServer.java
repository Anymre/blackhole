package com.example.http.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * An HTTP server that sends back the content of the received HTTP request in a pretty plaintext form.
 */
@Component
public class HttpHelloWorldServer implements CommandLineRunner {
    
    static final boolean SSL = System.getProperty("ssl") != null;
    
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));
    
    @Override
    public void run(String... args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }
        
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    if (sslCtx != null) {
                        p.addLast(sslCtx.newHandler(ch.alloc()));
                    }
                    p.addLast(new HttpServerCodec());
                    p.addLast(new HttpServerExpectContinueHandler());
                    p.addLast(new HttpHelloWorldServerHandler());
                }
            });
            
            Channel ch = b.bind(PORT).sync().channel();
            
            System.err.println(
                    "Open your web browser and navigate to " + (SSL ? "https" : "http") + "://127.0.0.1:" + PORT + '/');
            
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
