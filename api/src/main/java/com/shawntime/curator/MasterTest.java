package com.shawntime.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Master选举实现
 */
public class MasterTest {

    public static void main(String[] args) throws InterruptedException {
        final CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2183")
                .namespace("curator")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(4000)
                .build();
        curator.start();

        String path = "/chapter05/master";
        LeaderSelector leaderSelector = new LeaderSelector(curator, path, new LeaderSelectorListener() {

            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println(curatorFramework.getZookeeperClient().getCurrentConnectionString() + "成为Master角色");
                Thread.sleep(30000);
                System.out.println("完成任务，释放Master角色");
            }

            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                if (connectionState == ConnectionState.LOST
                        || connectionState == ConnectionState.SUSPENDED) {
                    throw new CancelLeadershipException();
                }
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
