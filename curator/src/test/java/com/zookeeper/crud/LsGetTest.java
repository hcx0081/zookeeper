package com.zookeeper.crud;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * {@code @description:}
 */
public class LsGetTest {
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
    
    // ls
    @Test
    public void lsTest() throws Exception {
        List<String> stringList = curatorFramework.getChildren().forPath("/");
        System.out.println(stringList);
    }
    
    // ls -s
    @Test
    public void lssTest() throws Exception {
        Stat stat = new Stat();
        curatorFramework.getChildren().storingStatIn(stat).forPath("/");
        System.out.println(stat);
    }
    
    // get
    @Test
    public void getTest() throws Exception {
        byte[] bytes = curatorFramework.getData().forPath("/");
        System.out.println(new String(bytes));
    }
    
    @After
    public void close() {
        if (curatorFramework != null) {
            curatorFramework.close();
        }
    }
}