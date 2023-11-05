package com.zookeeper.ticket;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * {@code @description:}
 */
public class Ticket12306 implements Runnable {
    // 数据库中的票数
    private int ticket = 10;
    
    private InterProcessLock lock;
    
    public Ticket12306() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                                                                   .connectString("192.168.106.100")
                                                                   .sessionTimeoutMs(60 * 1000)
                                                                   .connectionTimeoutMs(15 * 1000)
                                                                   .retryPolicy(retryPolicy)
                                                                   .build();
        curatorFramework.start();
        
        lock = new InterProcessMutex(curatorFramework, "/lock");
    }
    
    @Override
    public void run() {
        while (true) {
            // 加锁
            try {
                lock.acquire(3, TimeUnit.SECONDS);
                
                if (ticket > 0) {
                    System.out.println(Thread.currentThread() + "售出一张票，剩余票数：" + --ticket);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                // 释放锁
                try {
                    lock.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}