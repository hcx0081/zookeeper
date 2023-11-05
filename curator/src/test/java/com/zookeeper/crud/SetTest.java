package com.zookeeper.crud;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@code @description:}
 */
public class SetTest {
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
    
    /**
     * 基本修改
     *
     * @throws Exception
     */
    // set
    @Test
    public void setTest() throws Exception {
        curatorFramework.setData().forPath("/", "hello，世界".getBytes());
    }
    
    /**
     * 根据版本号修改
     *
     * @throws Exception
     */
    // set
    @Test
    public void setWithVersionTest() throws Exception {
        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath("/");// 查看状态信息
        int version = stat.getVersion();// 获取版本号
        System.out.println(version);
        curatorFramework.setData().withVersion(version).forPath("/", "hello，xx".getBytes());
    }
    
    @After
    public void close() {
        if (curatorFramework != null) {
            curatorFramework.close();
        }
    }
}