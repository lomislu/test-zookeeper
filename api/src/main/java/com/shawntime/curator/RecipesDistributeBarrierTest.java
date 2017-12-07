package com.shawntime.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 分布式barrier
 * 只要创建一个barrier，调用setBarrier，最后调用removeBarrier
 */
public class RecipesDistributeBarrierTest {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(1000)
                .namespace("curator")
                .connectString("127.0.0.1:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        String path = "/barrier";
        DistributedBarrier controlBarrier = new DistributedBarrier(client, path);
        controlBarrier.setBarrier();

        for (int i = 0; i < 10; ++i) {
            final DistributedBarrier barrier = new DistributedBarrier(client, path);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "准备就绪...");
                        barrier.waitOnBarrier();
                        System.out.println(Thread.currentThread().getName() + "启动....");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
        controlBarrier.removeBarrier();
    }
}
