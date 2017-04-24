package com.beta.basic.shiro.cache;

import com.beta.basic.redis.RedisKeyPrefixs;
import com.beta.basic.redis.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by yaoyt on 17/4/21.
 *
 * @author yaoyt
 */
public class RedisCache<K,V> implements Cache<K,V> {

    private Logger logger = LoggerFactory.getLogger(RedisCache.class);


    private RedisUtil redisUtil;

    public RedisCache(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    /**
     *方法的功能描述:根据 key 从 redis 中获取对象
     *@author yaoyt
     *@time 17/4/21 下午2:04
     */
    @Override
    public V get(K k) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key[{}]",k);
        try{
            if(null == k){
                return null;
            }else{
                V value = (V) this.redisUtil.get(String.valueOf(k));
                return value;
            }
        }catch (Throwable t){
            throw new CacheException(t);
        }
    }

    @Override
    public V put(K k, V v) throws CacheException {
        logger.debug("根据key[]存储对应的值[{}]",k,v);
        try{
            this.redisUtil.set(String.valueOf(k),v);
            return v;
        }catch (Throwable t){
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K k) throws CacheException {
        logger.debug("从 redis 中删除 key [{}]",k);
        try{
            V previous = get(k);
            this.redisUtil.delete(String.valueOf(k));
            return previous;
        }catch (Throwable t){
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有元素");
        this.redisUtil.delete(RedisKeyPrefixs.REDIS_SHIRO_CACHE.concat("*"));
    }

    @Override
    public int size() {
        try {
            Long longSize = new Long(this.redisUtil.dbSize(RedisKeyPrefixs.REDIS_SHIRO_CACHE.concat("*")));
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }


    @Override
    public Set<K> keys() {
        Set<String> keys = this.redisUtil.keys(RedisKeyPrefixs.REDIS_SHIRO_CACHE.concat("*"));
        if(CollectionUtils.isEmpty(keys)){
            return Collections.emptySet();
        }else{
            Set<K> newKeys = new HashSet<K>();
            for(String key:keys){
                newKeys.add((K)key);
            }
            return newKeys;
        }
    }

    @Override
    public Collection<V> values() {
        Set<String> keys = this.redisUtil.keys(RedisKeyPrefixs.REDIS_SHIRO_CACHE.concat("*"));
        if( !CollectionUtils.isEmpty(keys) ){
            List<V> values = new ArrayList<V>(keys.size());
            for(String key: keys){
                V value = get((K)key);
                if(null != value){
                    values.add(value);
                }
            }
            return Collections.unmodifiableList(values);
        }else{
            return Collections.emptyList();
        }
    }
}
