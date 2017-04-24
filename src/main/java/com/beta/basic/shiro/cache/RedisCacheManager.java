package com.beta.basic.shiro.cache;

import com.beta.basic.redis.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yaoyt on 17/4/21.
 *
 * @author yaoyt
 */
public class RedisCacheManager implements CacheManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

    @Autowired
    private RedisUtil redisUtil;

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        logger.debug("获取名称为{}的RedisShiroCache实例",name);
        Cache c = caches.get(name);
        if( null == c ){
            c = new RedisCache<K,V>(redisUtil);
            caches.put(name,c);
        }
        return c;
    }
}
