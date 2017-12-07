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
 * 同步获取节点数据
 */
public class UsageGetDataWithAsyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageGetDataWithAsyncTest());
        countDownLatch.await();
        String node = zooKeeper.create("/chapter05/node", "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        zooKeeper.getData("/chapter05/node", true, new AsyncCallback.DataCallback() {
            public void processResult(int returnCode, String path, Object ctx, byte[] bytes, Stat stat) {
                System.out.println("returnCode : " + returnCode);
                System.out.println("path : " + path);
                System.out.println("ctx : " + ctx);
                System.out.println("data : " + new String(bytes));
                System.out.println("stat : " + stat);
            }
        }, stat);
        zooKeeper.setData("/chapter05/node", "node2".getBytes(), -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (watchedEvent.getType() == Event.EventType.None
                    && watchedEvent.getPath() == null) {
                System.out.println("Connected...");
                countDownLatch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                zooKeeper.getData("/chapter05/node", true, new AsyncCallback.DataCallback() {
                    public void processResult(int returnCode, String path, Object ctx, byte[] bytes, Stat stat) {
                        System.out.println("returnCode1 : " + returnCode);
                        System.out.println("path1 : " + path);
                        System.out.println("ctx1 : " + ctx);
                        System.out.println("data1 : " + new String(bytes));
                        System.out.println("stat1 : " + stat);
                    }
                }, stat);
            }
        }
    }
}
