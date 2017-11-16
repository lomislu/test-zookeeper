package com.shawntime.curator;

import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 分布式计数器实现
 */
public class RecipesDistributeAtomicIntegerTest {

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectString("127.0.0.1:2181")
                .namespace("curator")
                .connectionTimeoutMs(1000)
                .build();
        client.start();
        String path = "/atomic";
        final DistributedAtomicInteger atomicInteger =
                new DistributedAtomicInteger(client, path, new ExponentialBackoffRetry(1000, 3));
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        AtomicValue<Integer> increment = atomicInteger.increment();
                        System.out.println(Thread.currentThread().getName() + ": " + increment.succeeded() + ", " +
                                increment.preValue() + ", " + increment.postValue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            countDownLatch.countDown();
        }
    }
}
