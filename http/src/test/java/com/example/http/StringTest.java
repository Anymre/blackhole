package com.example.http;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Marcus lv
 * @date 2020/10/14 14:18
 */
public class StringTest {
    
    private static final Executor exec = Executors.newFixedThreadPool(8);
    
    @Test
    public void simple() {
        StringBuilder builder = new StringBuilder();
        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                builder.append(0);
            }
            System.out.println(builder.toString().length());
        };
        exec.execute(runnable);
        exec.execute(runnable);
    }
    
    @Test
    public void simple2() {
        StringBuffer builder = new StringBuffer();
        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                builder.append(0);
            }
            System.out.println(builder.toString().length());
        };
        exec.execute(runnable);
        exec.execute(runnable);
    }
}
