package com.shawntime.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * PathChildrenCache：某节点的子节点监听
 * 1）子节点增加，删除，数据变更均监听
 * 2）该节点自身的变化不监听
 * 3）二级节点变化不监听
 */
public class PathChildrenCacheTest {

    public static void main(String[] args) throws Exception {
         CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("curator")
                .connectString("127.0.0.1:2181")
                .build();
        curator.start();
        final String path = "/chapter05/node3";
        final PathChildrenCache cache = new PathChildrenCache(curator, path, true);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event)
                    throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("child_add, path:" + event.getData().getPath() + ", data:" + new String(event
                                .getData().getData()));
                        break;
                    case CHILD_UPDATED:
                        System.out.println("child_update, path:" + event.getData().getPath() + ", data:" + new String
                                (event.getData().getData()));
                        break;
                    case CHILD_REMOVED:
                        System.out.println("child_removed, path:" + event.getData().getPath() + ", data:" + new String
                                (event.getData().getData()));
                        break;
                }
            }
        });
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        curator.create().withMode(CreateMode.PERSISTENT).forPath(path);
        Thread.sleep(100);
        curator.create().withMode(CreateMode.EPHEMERAL).forPath(path + "/c1", "name".getBytes());
        Thread.sleep(100);
        curator.setData().withVersion(-1).forPath(path + "/c1", "data".getBytes());
        Thread.sleep(100);
        curator.delete().withVersion(-1).forPath(path + "/c1");
        Thread.sleep(100);
        curator.delete().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
