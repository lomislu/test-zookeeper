package com.shawntime.curator;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by shma on 2017/11/16.
 */
public class ZKPathsTest {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectString("127.0.0.1:2181")
                .namespace("curator")
                .connectionTimeoutMs(1000)
                .build();
        client.start();

        ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();
        ZKPaths.mkdirs(zooKeeper, "/chapter05/zkpath");
        ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode("/chapter05/zkpath");
        System.out.println(pathAndNode.getPath());
        System.out.println(pathAndNode.getNode());

        ZKPaths.mkdirs(zooKeeper, "/chapter05/zkpath/child1");
        ZKPaths.mkdirs(zooKeeper, "/chapter05/zkpath/child2");

        final List<String> sortedChildren = ZKPaths.getSortedChildren(zooKeeper, "/chapter05/zkpath");
        System.out.println(sortedChildren);

        ZKPaths.deleteChildren(zooKeeper, "/chapter05/zkpath", true);
    }
}
