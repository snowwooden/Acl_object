package com.service;

import com.google.common.base.Joiner;
import com.utils.CacheKeyConstants;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

@Service
public class SysCacheService {

    @Resource
    private RedisPool redisPool;

    /**
     *  保存
     * @param toSavedValue  值
     * @param timeoutSeconds 过期时间
     * @param prefix 指定前缀
     * @param keys 键
     */
    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {
        if (toSavedValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix,keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 获取
     * @param prefix
     * @param keys
     * @return
     */
    public String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix,keys);
        try {
            shardedJedis = redisPool.instance();
            String value = shardedJedis.get(cacheKey);
            return value;
        } catch (Exception e) {
           return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    public String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
