package com.zookeeper.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

/**
 * {@code @description:}
 */
public class TreeCacheTest {
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
    
    @Test
    public void treeCacheTest() throws Exception {
        TreeCache treeCache = new TreeCache(curatorFramework, "/");
        treeCache.getListenable().addListener(new TreeCacheListener() {
            /**
             *
             * @param client 客户端
             * @param event  事件对象
             * @throws Exception
             */
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("节点或其子节点变化了");
                
                System.out.println(event);
                if (event.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
                    System.out.println("节点或其子节点更新后的数据：" + new String(event.getData().getData()));
                }
            }
        });
        treeCache.start();
        
        // 一直监听
        while (true) {
        
        }
    }
}