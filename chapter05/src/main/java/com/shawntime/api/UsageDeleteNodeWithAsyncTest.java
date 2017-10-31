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

/**
 * 异步删除节点
 */
public class UsageDeleteNodeWithAsyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageDeleteNodeWithAsyncTest());
        countDownLatch.await();
        String nodePath = zooKeeper.create("/chapter05/node", "node".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.delete(nodePath, 0, new AsyncCallback.VoidCallback() {
            public void processResult(int returnCode, String path, Object ctx) {
                System.out.println("returnCode : " + returnCode);
                System.out.println("path : " + path);
                System.out.println("ctx : " + ctx);
            }
        }, "I am content....");
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Connected...");
            countDownLatch.countDown();
        }
    }
}
