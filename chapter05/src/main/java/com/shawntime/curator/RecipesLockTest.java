package com.shawntime.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Curator实现分布式锁
 */
public class RecipesLockTest {

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(4000)
                .namespace("curator")
                .connectString("127.0.0.1:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        String path = "/lock";
        final InterProcessMutex lock = new InterProcessMutex(client, path);
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i<10; ++i) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        lock.acquire();
                        System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            lock.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            countDownLatch.countDown();
        }
    }
}
