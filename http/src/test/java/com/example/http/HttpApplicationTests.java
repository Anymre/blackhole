package com.example.http;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class HttpApplicationTests {
    
    @Test
    void simple() throws Exception {
        long s = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> send(80)).start();
        }
        System.out.println(System.nanoTime() - s);
    }
    @Test
    void nio() throws Exception {
        long s = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> send(8000)).start();
        }
        System.out.println(System.nanoTime() - s);
    }
    
    private void send(int port) {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("hello world\n".getBytes());
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
