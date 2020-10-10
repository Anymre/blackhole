package com.example.http;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.smartcardio.TerminalFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Marcus lv
 * @date 2020/10/10 13:41
 */
@Component
public class SimpleHttpServer implements CommandLineRunner {
    
    private static final Executor exec = Executors.newFixedThreadPool(8);
    
    @Override
    public void run(String... args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(80);
        while (true) {
            Socket connection = serverSocket.accept();
            Runnable runnable = () -> handleSocket(connection);
            exec.execute(runnable);
        }
    }
    
    private void handleSocket(Socket connection) {
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            System.out.println("get message from client: " + is.readLine());
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write("hello world!".getBytes());
            outputStream.close();
            is.close();
            connection.close();
        } catch (Exception ignore) {
        }
    }
}
