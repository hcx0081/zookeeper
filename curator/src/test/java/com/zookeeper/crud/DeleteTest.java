package com.zookeeper.crud;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@code @description:}
 */
public class DeleteTest {
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
    
    // delete
    @Test
    public void deleteTest() throws Exception {
        curatorFramework.delete().forPath("/");
    }
    
    // deleteall
    @Test
    public void deleteAllTest() throws Exception {
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/");
    }
    
    // 保证操作成功执行，防止网络抖动
    @Test
    public void deleteGuaranteedTest() throws Exception {
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath("/");
    }
    
    // 回调
    @Test
    public void deleteInBackgroundTest() throws Exception {
        curatorFramework.delete().deletingChildrenIfNeeded().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event);
            }
        }).forPath("/");
    }
    
    @After
    public void close() {
        if (curatorFramework != null) {
            curatorFramework.close();
        }
    }
}