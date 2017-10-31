package com.shawntime.api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 同步检查节点是否存在
 */
public class UsageExistWithSyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    private static String path = "/chapter05/node";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageExistWithSyncTest());
        countDownLatch.await();

        Stat stat = zooKeeper.exists(path, true);
        String newPath = zooKeeper.create(path, "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.setData(path, "node".getBytes(), -1);
        zooKeeper.create("/chapter05/node/child", "child".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode
                .EPHEMERAL);
        zooKeeper.delete("/chapter05/node/child", -1);
        zooKeeper.delete(path, -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (watchedEvent.getPath() == null && watchedEvent.getType() == Event.EventType.None) {
                countDownLatch.countDown();
                System.out.println("Connected...");
            } else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                System.out.println("Node is Created...");
                try {
                    zooKeeper.exists(path, true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("Node is changed...");
                try {
                    zooKeeper.exists(path, true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                System.out.println("Node is deleted...");
                try {
                    zooKeeper.exists(path, true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("Node is NodeChildrenChanged...");
                try {
                    zooKeeper.exists(path, true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
