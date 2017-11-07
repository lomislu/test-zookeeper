package com.shawntime.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * Created by shma on 2017/11/7.
 */
public class GetAndSetDataTest {


    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(3000)
                .namespace("curator")
                .build();
        curator.start();

        Stat stat = new Stat();
        String path = curator.create().creatingParentsIfNeeded()
                .storingStatIn(stat)
                .forPath("/chapter05/data/node", "node".getBytes());

        String data = new String(curator.getData().storingStatIn(stat).forPath(path));
        System.out.println("old data : " + data + ", version : " + stat.getVersion());

        Stat stat1 = curator.setData().withVersion(stat.getVersion())
                .forPath(path, "data2".getBytes());
        data = new String(curator.getData().forPath(path));
        System.out.println("new data : " + data + ", version : " + stat1.getVersion());

        curator.setData().withVersion(stat.getVersion())
                .forPath(path);

    }
}
