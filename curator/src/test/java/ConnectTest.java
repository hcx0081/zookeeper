import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

/**
 * @Description:
 */
public class ConnectTest {
    @Test
    public void connectTest1() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("192.168.106.100", 60 * 1000, 15 * 1000, retryPolicy);
        curatorFramework.start();
    }
    
    @Test
    public void connectTest2() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                                                                   .connectString("192.168.106.100")
                                                                   .sessionTimeoutMs(60 * 1000)
                                                                   .connectionTimeoutMs(15 * 1000)
                                                                   .retryPolicy(retryPolicy)
                                                                   .namespace("hello")
                                                                   .build();
        curatorFramework.start();
    }
}