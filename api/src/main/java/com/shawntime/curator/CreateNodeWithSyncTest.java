package com.shawntime.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Created by shma on 2017/11/7.
 */
public class CreateNodeWithSyncTest {

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFrameWork = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .connectionTimeoutMs(3000)
                .retryPolicy(retryPolicy)
                .namespace("curator")
                .build();
        curatorFrameWork.start();

        String path = curatorFrameWork.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/chapter05/node/node1", "data".getBytes());
        System.out.println(path);
        System.out.println(curatorFrameWork.checkExists().forPath(path));;
    }
}
