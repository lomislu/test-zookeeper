package com.shawntime.api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 异步检查节点是否存在
 */
public class UsageExistWithAsyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    private static String path = "/chapter05/node";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageExistWithAsyncTest());
        countDownLatch.await();
        zooKeeper.exists(path, true, new AsyncCallback.StatCallback() {
            public void processResult(int returnCode, String path, Object ctx, Stat stat) {
                System.out.println("returnCode : " + returnCode);
                System.out.println("path : " + path);
                System.out.println("ctx : " + ctx);
                System.out.println("stat : " + stat);
            }
        }, "I am Content...");
        zooKeeper.create(path, "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.exists(path, true, new AsyncCallback.StatCallback() {
            public void processResult(int returnCode, String path, Object ctx, Stat stat) {
                System.out.println("returnCode : " + returnCode);
                System.out.println("path : " + path);
                System.out.println("ctx : " + ctx);
                System.out.println("stat : " + stat);
            }
        }, "I am Content...");
    }
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (watchedEvent.getPath() == null && watchedEvent.getType() == Event.EventType.None) {
                countDownLatch.countDown();
                System.out.println("Connected...");
            }
        }
    }
}
