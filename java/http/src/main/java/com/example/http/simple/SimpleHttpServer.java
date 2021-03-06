package com.example.http.simple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.smartcardio.TerminalFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

    private static final Executor exec = Executors.newFixedThreadPool(200);

    @Value("${simple.port}")
    private int port;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.getClass().getName() + " start");
        ServerSocket serverSocket = new ServerSocket(port);
        Runnable tomcat = () -> {
            while (true) {
                Socket connection = null;
                try {
                    connection = serverSocket.accept();
                    Socket finalConnection = connection;
                    Runnable runnable = () -> handleSocket(finalConnection);
                    exec.execute(runnable);
                } catch (Exception ignore) {
                }
            }
        };
        exec.execute(tomcat);
    }

    private void handleSocket(Socket socket) {
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("get message from client: ");
            StringBuilder builder = new StringBuilder();
            while (true) {
                String s = is.readLine();
                if (s == null || "".equals(s)) {
                    break;
                }
                builder.append(s).append("\n");
            }
            System.out.println(builder.toString());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String content = "hello world\n";
            out.println(header(content));
            out.println(content);

            is.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String header(String content) {
        return "HTTP/1.0 200 OK\r\n" + "Server: OneFile 1.0\r\n" + "Content-length: " + content.length() + "\r\n"
                + "Content-type: text/html\r\n\r\n";

    }

}
