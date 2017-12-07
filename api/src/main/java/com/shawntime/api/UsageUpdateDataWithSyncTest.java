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
 * 同步方式更新节点数据
 */
public class UsageUpdateDataWithSyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageUpdateDataWithSyncTest());
        countDownLatch.await();
        String path = zooKeeper.create("/chapter05/node", "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        byte[] data = zooKeeper.getData(path, true, null);
        System.out.println(new String(data));
        Stat stat = zooKeeper.setData(path, "node2".getBytes(), -1);
        Stat stat1 = zooKeeper.setData(path, "node3".getBytes(), stat.getVersion());
        zooKeeper.setData(path, "node3".getBytes(), stat.getVersion());
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
            System.out.println("Connected...");
        }
    }
}
