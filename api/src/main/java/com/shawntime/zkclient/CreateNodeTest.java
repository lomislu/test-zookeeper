package com.shawntime.zkclient;

import com.shawntime.JsonHelper;
import org.I0Itec.zkclient.ZkClient;

/**
 * zkClient创建会话，创建节点
 */
public class CreateNodeTest {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        zkClient.createEphemeral("/chapter05/node", JsonHelper.serialize("Hello world!!!"));
        Object o = zkClient.readData("/chapter05/node", true);
        System.out.println(JsonHelper.deSerialize(o.toString(), String.class));
    }
}
