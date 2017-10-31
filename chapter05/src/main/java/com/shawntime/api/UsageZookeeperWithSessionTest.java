package com.shawntime.api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * SessionId，SessionPasswd，恢复会话
 */
public class UsageZookeeperWithSessionTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageZookeeperWithSessionTest());
        countDownLatch.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageZookeeperWithSessionTest(), 1, "test".getBytes());
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageZookeeperWithSessionTest(), sessionId,
                sessionPasswd);
        Thread.sleep(Integer.MAX_VALUE);

    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Connected...");
            countDownLatch.countDown();
        }
    }
}
