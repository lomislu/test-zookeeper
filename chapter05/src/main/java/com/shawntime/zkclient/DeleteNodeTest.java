package com.shawntime.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by shma on 2017/11/1.
 */
public class DeleteNodeTest {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        zkClient.createPersistent("/chapter05/node/node1", true);
        zkClient.writeData("/chapter05/node/node1", "node", -1);
        Object data = zkClient.readData("/chapter05/node/node1");
        System.out.println(data);
        zkClient.deleteRecursive("/chapter05/node");
        data = zkClient.readData("/chapter05/node/node1", true);
        System.out.println(data);
    }
}
