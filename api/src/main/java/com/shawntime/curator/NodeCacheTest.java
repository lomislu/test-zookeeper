package com.shawntime.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * NodeCache：事件监听，监听节点本身数据的改变
 * 1）节点创建
 * 2）节点数据改变
 * 3）节点删除无法监听到
 * 4）子节点变法无法监听到
 */
public class NodeCacheTest {

    public static void main(String[] args) throws Exception {
         CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .namespace("curator")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(5000)
                .build();
        curator.start();
        String path = "/chapter05/data2";
        final NodeCache nodeCache = new NodeCache(curator, path, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("node is updated, path data : " + new String(nodeCache.getCurrentData().getData()));
            }
        });

        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        Thread.sleep(1000);
        curator.setData().withVersion(-1).forPath(path, "nihao".getBytes());
        Thread.sleep(1000);
        curator.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
