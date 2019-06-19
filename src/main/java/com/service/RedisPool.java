package com.service;

import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

@Service
public class RedisPool {

    @Resource
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    public void safeClose(ShardedJedis shardedJedis) {
        try {
           if (shardedJedis != null) {
               shardedJedis.close();
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
