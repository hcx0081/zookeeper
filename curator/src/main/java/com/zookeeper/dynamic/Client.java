package com.zookeeper.dynamic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * {@code @description:}
 */
public class Client {
    private final String connectString = "192.168.100.101:2181,192.168.100.102:2181,192.168.100.103:2181";
    private CuratorFramework curatorFramework;
    
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connect();
        client.getServerList();
    }
    
    private void connect() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        
        curatorFramework = CuratorFrameworkFactory.builder()
                                                  .connectString(connectString)
                                                  .sessionTimeoutMs(60 * 1000)
                                                  .connectionTimeoutMs(15 * 1000)
                                                  .retryPolicy(retryPolicy)
                                                  .build();
        curatorFramework.start();
    }
    
    private void getServerList() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, "/servers", true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            /**
             * @param client 客户端
             * @param event 事件对象
             */
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED || event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                    ArrayList<String> list = new ArrayList<>(client.getChildren().watched().forPath("/servers"));
                    System.out.println("server list: " + Arrays.toString(list.toArray()));
                }
            }
        });
        pathChildrenCache.start();
        
        // 一直监听
        while (true) {
        
        }
    }
}
