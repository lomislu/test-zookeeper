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
 * 异步方式更新节点数据
 */
public class UsageSetDataWithAsyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    private static Stat stat;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageSetDataWithAsyncTest());
        countDownLatch.await();
        String path = zooKeeper.create("/chapter05/node", "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        zooKeeper.setData(path, "node2".getBytes(), -1, new AsyncCallback.StatCallback() {
            public void processResult(int returnCode, String path, Object ctx, Stat stat) {
                if (returnCode == 0) {
                    System.out.println("update success...");
                    System.out.println("path : " + path);
                    System.out.println("ctx : " + ctx);
                    System.out.println("version : " + stat.getVersion());
                }
            }
        }, "I am Content");
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (watchedEvent.getType() == Event.EventType.None
                    && watchedEvent.getPath() == null) {
                countDownLatch.countDown();
            }
        }
    }
}
