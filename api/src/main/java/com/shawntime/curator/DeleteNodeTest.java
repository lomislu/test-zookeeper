package com.shawntime.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by shma on 2017/11/7.
 */
public class DeleteNodeTest {

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFrameWork = CuratorFrameworkFactory.builder()
                .namespace("curator")
                .connectionTimeoutMs(3000)
                .connectString("127.0.0.1:2181")
                .retryPolicy(retryPolicy)
                .build();
        curatorFrameWork.start();

        String path = curatorFrameWork.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/chapter05/data/node", "data".getBytes());
        Stat stat = new Stat();
        byte[] bytes = curatorFrameWork.getData().storingStatIn(stat).forPath(path);
        System.out.println(new String(bytes));
        curatorFrameWork.getData().storingStatIn(stat).forPath("/chapter05/data");
        curatorFrameWork.delete()
                .guaranteed()
                .deletingChildrenIfNeeded()
                .withVersion(stat.getVersion())
                .forPath("/chapter05/data");
        System.out.println("删除成功");
    }
}
