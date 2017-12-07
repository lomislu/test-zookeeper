package com.shawntime.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Created by shma on 2017/11/7.
 */
public class CreateNodeWithBackgroundTest {

    private static final ExecutorService exec = Executors.newFixedThreadPool(2);

    private static final CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) throws Exception {
        final CuratorFramework curator = CuratorFrameworkFactory.builder()
                .namespace("curator")
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(3000, 100))
                .connectString("127.0.0.1:2181")
                .build();

        curator.start();
        String path = "/chapter05/node2";
        curator.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws
                            Exception {
                        String currThreadName = Thread.currentThread().getName();
                        System.out.println("threadName : " + currThreadName + ", returnCode : " + curatorEvent
                                .getResultCode());
                        System.out.println("threadName : " + currThreadName + ", eventTypeName : " + curatorEvent
                                .getType().name());
                        countDownLatch.countDown();
                    }
                }, exec).forPath(path, "data".getBytes());

        curator.create()
                .creatingParentsIfNeeded()
        .withMode(CreateMode.EPHEMERAL)
        .inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                String currThreadName = Thread.currentThread().getName();
                System.out.println("threadName : " + currThreadName + ", returnCode : " + curatorEvent
                        .getResultCode());
                System.out.println("threadName : " + currThreadName + ", eventTypeName : " + curatorEvent.getType().name());
                countDownLatch.countDown();
            }
        }).forPath(path,"data".getBytes());

        countDownLatch.await();
        exec.shutdown();
    }
}
