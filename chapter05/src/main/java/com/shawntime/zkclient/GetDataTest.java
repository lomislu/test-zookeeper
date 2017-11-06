package com.shawntime.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * Created by shma on 2017/11/1.
 */
public class GetDataTest {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        zkClient.createEphemeral("/chapter05/node", "node");
        zkClient.subscribeDataChanges("/chapter05/node", new IZkDataListener() {

            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("dataPath : " + dataPath);
                System.out.println("data : " + data);
            }

            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("dataPath : " + dataPath);
            }
        });
        System.out.println(zkClient.readData("/chapter05/node"));
        zkClient.writeData("/chapter05/node", "node2", -1);
        Thread.sleep(1000);
        zkClient.deleteRecursive("/chapter05/node");
        Thread.sleep(Integer.MAX_VALUE);
    }

}
