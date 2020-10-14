package com.example.http;

import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

class HttpApplicationTests {
    @Test
    public void test(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put("abc".getBytes());
        byteBuffer.put("def".getBytes());
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes,0,bytes.length));
        byteBuffer.clear();
    }
    
    @Test
    void simple() throws Exception {
        long s = System.nanoTime();
        for (int i = 0; i < 500; i++) {
            new Thread(() -> send(80)).start();
        }
        System.out.println(System.nanoTime() - s);
    }
    @Test
    void nio() throws Exception {
        long s = System.nanoTime();
        for (int i = 0; i < 500; i++) {
            new Thread(() -> send(8000)).start();
        }
        System.out.println(System.nanoTime() - s);
    }
    
    private void send(int port) {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            OutputStream outputStream = socket.getOutputStream();
            String content = "{\n" + "  \"code\": 200,\n" + "  \"success\": true,\n" + "  \"data\": {\n"
                    + "    \"tenantCode\": \"2\",\n" + "    \"tenantName\": \"\",\n"
                    + "    \"token\": \"9e500e35a5104c4d8ed88ade849d9f2d\",\n" + "    \"ada\": \"34888\",\n"
                    + "    \"openId\": \"\",\n" + "    \"unionId\": \"\",\n" + "    \"masterName\": \"客服号\",\n"
                    + "    \"spouseName\": \"\",\n" + "    \"masterAmwayId\": \"\",\n"
                    + "    \"spouseAmwayId\": \"\",\n" + "    \"masterMobile\": \"\",\n"
                    + "    \"spouseMobile\": \"\",\n" + "    \"masterIdCard\": \"\",\n"
                    + "    \"spouseIdCard\": \"\",\n" + "    \"isSpouse\": -1,\n" + "    \"spouseFlag\": -1,\n"
                    + "    \"isBindPhone\": -1,\n" + "    \"role\": \"\",\n" + "    \"amwayRole\": \"\",\n"
                    + "    \"autoLogin\": 0,\n"
                    + "    \"avatarUrl\": \"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKQhibyVyxkGTwaZEh14cgtcCzoyibX718znnqNiaDvFGbEHSZTfETs2DvZ9wOYVkLTxvo9jJQAHL6VQ/132\"\n"
                    + "  },\n" + "  \"msg\": \"操作成功\"\n" + "}";
            outputStream.write(content.getBytes());
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
