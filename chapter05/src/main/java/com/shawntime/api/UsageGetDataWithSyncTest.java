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
 * 同步获取节点数据
 */
public class UsageGetDataWithSyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageGetDataWithSyncTest());
        countDownLatch.await();
        String path = zooKeeper.create("/chapter05/node", "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        byte[] data = zooKeeper.getData("/chapter05/node", true, stat);
        System.out.println("data : " + new String(data));
        System.out.println("stat : " + stat);
        zooKeeper.setData("/chapter05/node", data, -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (watchedEvent.getType() == Event.EventType.None
                    && watchedEvent.getPath() == null) {
                countDownLatch.countDown();
                System.out.println("Connected....");
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                try {
                    byte[] data = zooKeeper.getData("/chapter05/node", true, stat);
                    System.out.println("data : " + new String(data));
                    System.out.println("stat : " + stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
