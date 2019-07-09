package Q.W.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class JedisUtilsTest {
@Test
    public void test(){

    //2.获取连接
    Jedis jedis = JedisUtils.getJedis();

    //3. ping pong 测试
    //如果有密码
    //jedis.auth("密码");
    String ping = jedis.ping();
    System.out.println("ping = " + ping);
}
}