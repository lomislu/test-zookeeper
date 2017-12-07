package com.shawntime.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by shma on 2017/11/1.
 */
public class ExistNodeTest {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        System.out.println(zkClient.exists("/chapter05/node"));
        zkClient.createEphemeral("/chapter05/node");
        System.out.println(zkClient.exists("/chapter05/node"));
    }
}
