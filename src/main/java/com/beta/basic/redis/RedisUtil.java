package com.beta.basic.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaoyt on 17/4/21.
 *
 * @author yaoyt
 */
@Component("redisUtil")
public class RedisUtil {

    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    public void set(String k, Object v){
        logger.info("redis记录保存,key:{};value:{};",k,v);
        redisTemplate.opsForValue().set(k,v);
    }

    public void setex(String k, Object v, Long l){
        logger.info("redis记录保存,key:{};value:{};有效期:{};",k,v,l);
        redisTemplate.opsForValue().set(k,v,l, TimeUnit.SECONDS);
    }

    public void delete(String k){
        logger.info("redis记录删除,key:{};",k);
        redisTemplate.delete(k);
    }

    public Set<String> keys(String p){
        return redisTemplate.keys(p);
    }

    public void flushDB(String pattern){
        redisTemplate.delete(keys(pattern));
    }

    public Long dbSize(String pattern){
        return Long.valueOf(this.keys(pattern).size());
    }

    public Object get(String k){
        logger.info("redis记录查询,key:{};",k);
        return redisTemplate.opsForValue().get(k);
    }


}
