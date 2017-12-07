package com.shawntime.api;

import java.io.IOException;
import java.util.List;
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
 * 异步获取子节点列表
 */
public class UsageChildrenNodeWithAsyncTest implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UsageChildrenNodeWithAsyncTest());
        countDownLatch.await();
        String node1 = zooKeeper.create("/chapter05/node1", "node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        zooKeeper.getChildren("/chapter05", true, new AsyncCallback.Children2Callback() {
            public void processResult(int returnCode, String path, Object ctx, List<String> childList, Stat stat) {
                System.out.println("returnCode : " + returnCode);
                System.out.println("path : " + path);
                System.out.println("ctx : " + ctx);
                System.out.println("childList : " + childList);
                System.out.println("stat : " + stat);
            }
        }, "I am content");
        String node2 = zooKeeper.create("/chapter05/node2", "node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (watchedEvent.getType() == Event.EventType.None
                    && watchedEvent.getPath() == null) {
                countDownLatch.countDown();
                System.out.println("Connected...");
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("children changed...");
                List<String> childrenList = null;
                try {
                    childrenList = zooKeeper.getChildren("/chapter05", true);
                    System.out.println("ReGet children:" + childrenList);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
