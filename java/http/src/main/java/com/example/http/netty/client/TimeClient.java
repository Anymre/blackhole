package com.example.http.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.Socket;

/**
 * @author Marcus lv
 * @date 2020/10/21 13:36
 */
public class TimeClient {
    
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        Bootstrap b = new Bootstrap();
        b.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                    }
                });
        ChannelFuture f = b.connect("127.0.0.1", 8081).sync();
        f.channel().closeFuture().sync();
        
        workerGroup.shutdownGracefully();
    }
}
