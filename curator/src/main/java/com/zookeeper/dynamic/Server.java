package com.zookeeper.dynamic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;

/**
 * {@code @description:}
 */
public class Server {
    private final String connectString = "192.168.100.101:2181,192.168.100.102:2181,192.168.100.103:2181";
    private CuratorFramework curatorFramework;
    
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.connect();
        server.register(args[0]);
        server.business();
    }
    
    private void connect() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        
        curatorFramework = CuratorFrameworkFactory.builder()
                                                  .connectString(connectString)
                                                  .sessionTimeoutMs(60 * 1000)
                                                  .connectionTimeoutMs(15 * 1000)
                                                  .retryPolicy(retryPolicy)
                                                  .namespace("servers")
                                                  .build();
        curatorFramework.start();
    }
    
    private void register(String hostname) throws Exception {
        curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath("/" + hostname, hostname.getBytes(StandardCharsets.UTF_8));
        System.out.println(hostname + " online");
    }
    
    private void business() throws InterruptedException {
        Thread.sleep(50000);
    }
}
