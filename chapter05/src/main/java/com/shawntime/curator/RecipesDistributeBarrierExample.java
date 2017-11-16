package com.shawntime.curator;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by shma on 2017/11/16.
 */
public class RecipesDistributeBarrierExample {

    private static final ExecutorService exec = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectString("127.0.0.1:2181")
                .namespace("curator")
                .connectionTimeoutMs(1000)
                .build();
        client.start();

        String path = "/barrier2";
        for (int i = 0; i < 10; ++i) {
            final DistributedDoubleBarrier distributedBarrier = new DistributedDoubleBarrier(client, path, 10);
            exec.submit(new Callable<String>() {
                public String call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + "准备就绪...");
                    distributedBarrier.enter();
                    Thread.sleep((long) (Math.random() * 10000));
                    System.out.println(Thread.currentThread().getName() + "处理...");
                    distributedBarrier.leave();
                    System.out.println(Thread.currentThread().getName() + "释放...");
                    return null;
                }
            });
        }

        Thread.sleep(2000);
        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.MINUTES);

    }
}
