package com.shawntime.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by shma on 2017/11/6.
 */
public class CreateSessionWithFluentTest {

    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(3000)
                .connectString("127.0.01.:2181")
                .retryPolicy(retryPolicy)
                .namespace("curator")
                .build();
        curatorFramework.start();
    }
}
