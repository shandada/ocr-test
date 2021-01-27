package com.example.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: Endless 2020/3/23 11:18
 * 使用jedis垃圾池
 **/
public class JedisTest02 {

    public static void main(String[] args) {

        //1.创建连接池config配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);//设置最大连接数
        config.setMinIdle(10);//空闲时最大连接数
        //2.创建连接池对象
        //第一个参数,配置对象,
        //第二个参数,redis服务器地址,
        //第三个参数,redis服务器端口号,
        JedisPool pool = new JedisPool(config, "127.0.0.1", 6379);
        //从连接池中连接
        Jedis jedis = pool.getResource();
        //创建值
        jedis.set("key", "传智大学");
        //取值
        String name2 = jedis.get("key");
        System.out.println(name2);

        String name = jedis.get("name");
        System.out.println(name);
        jedis.close();

    }
}
