package com.shawntime.api;

import org.apache.zookeeper.AsyncCallback;

/**
 * Created by shma on 2017/10/29.
 */
public class IStringCallback implements AsyncCallback.StringCallback {

    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("returnCode : " + rc);
        System.out.println("path : " + path);
        System.out.println("ctx : " + ctx);
        System.out.println("name : " + name);
    }
}
