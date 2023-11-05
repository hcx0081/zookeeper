package com.zookeeper.crud;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@code @description:}
 */
public class CreateTest {
    CuratorFramework curatorFramework;
    
    @Before
    public void connectTest() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        
        curatorFramework = CuratorFrameworkFactory.builder()
                                                  .connectString("192.168.106.100")
                                                  .sessionTimeoutMs(60 * 1000)
                                                  .connectionTimeoutMs(15 * 1000)
                                                  .retryPolicy(retryPolicy)
                                                  .namespace("hello")
                                                  .build();
        curatorFramework.start();
    }
    
    // create
    @Test
    public void createTest() throws Exception {
        String path = curatorFramework.create()
                                      .creatingParentsIfNeeded()
                                      .withMode(CreateMode.PERSISTENT)
                                      .forPath("/world/app", "hello，世界".getBytes());
        System.out.println(path);
    }
    
    @After
    public void close() {
        if (curatorFramework != null) {
            curatorFramework.close();
        }
    }
}