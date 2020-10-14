package com.example.http;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Marcus lv
 * @date 2020/10/10 13:41
 */
@Component
public class NioHttpServer implements CommandLineRunner {
    
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.getClass().getName() + " start");
        
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket ss = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(8000);
        ss.bind(address);
        
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        Runnable runnable = () -> {
            for (; ; ) {
                try {
                    selector.select();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
                Set<SelectionKey> readKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    try {
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ, msg.duplicate());
                            System.out.println("Accepted connection from " + client);
                        }
                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                            int len;
                            StringBuilder builder = new StringBuilder();
                            for (len = client.read(byteBuffer); len != -1; ) {
                                builder.append(new String(byteBuffer.array(), 0, len));
                            }
                            System.out.println(builder.toString());
                            client.register(selector, SelectionKey.OP_WRITE, msg.duplicate());
                        }
                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            while (buffer.hasRemaining()) {
                                if (client.write(buffer) == 0) {
                                    break;
                                }
                            }
                            client.close();
                        }
                    } catch (IOException ex) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException ignore) {
                        }
                    }
                }
            }
        };
        new Thread(runnable).start();
    }
    
    public String header(String content) {
        String header =
                "HTTP/1.0 200 OK\r\n" + "Server: OneFile 1.0\r\n" + "Content-length: " + content.length() + "\r\n"
                        + "Content-type: text/html\r\n\r\n";
        return header;
        
    }
    
}
