package com.shawntime.zkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * Created by shma on 2017/11/1.
 */
public class GetChildrenTest {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        zkClient.subscribeChildChanges("/chapter05/node", new IZkChildListener() {
            public void handleChildChange(String path, List<String> childList) throws Exception {
                System.out.println("path : " + path);
                System.out.println("childList : " + childList);
            }
        });
        zkClient.createPersistent("/chapter05/node", "node");
        List<String> children = zkClient.getChildren("/chapter05/node");
        System.out.println("child : " + children);
        zkClient.createEphemeral("/chapter05/node/node1");
        zkClient.delete("/chapter05/node/node1");
        zkClient.delete("/chapter05/node");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
