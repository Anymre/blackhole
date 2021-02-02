package com.example.redis;

import io.reactivex.rxjava3.functions.Action;
import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisApplicationTests {
    @Autowired
    RedissonClient redissonClient;

    int test = 0;


    @Test
    void contextLoads() throws Exception{
        RLock lock = redissonClient.getLock("T");

        Runnable action = () -> {
            for (int i = 0; i < 100; i++) {
                lock.lock();
                this.test++;
                lock.unlock();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable action2 = () -> {
            for (int i = 0; i < 100; i++) {
                lock.lock();
                this.test++;
                lock.unlock();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(action);
        Thread thread1 = new Thread(action2);

        thread.start();
        thread1.start();

        thread.join();
        thread1.join();
        System.out.println(this.test);
    }

}
