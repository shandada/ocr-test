package com.example.demo.redis;

import redis.clients.jedis.Jedis;

/**
 * @author: Endless 2020/3/23 11:18
 **/
public class JedisTest {

    public static void main(String[] args) {
        //1.创建连接jedis ,连接localhost:6379.
        Jedis jedis = new Jedis();

        //设置值
        jedis.set("school", "传智学院 !");
        //取值
        System.out.println(jedis.get("school"));
//        //删除内容

        jedis.del("user");


    }
}
