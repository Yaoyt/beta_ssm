package com.beta.basic.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by yaoyt on 17/4/11.
 * stringRedisTemplate只能操作 String , String
 * @author yaoyt
 */
@Component("stringRedisUtil")
public class StringRedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set(String k, String v){
        stringRedisTemplate.opsForValue().set(k,v);
    }

    public void setex(String k, String v, Long l){
        stringRedisTemplate.opsForValue().set(k,v,l, TimeUnit.SECONDS);
    }

    public void delete(String k){
        stringRedisTemplate.delete(k);
    }

    public String get(String k){
        return stringRedisTemplate.opsForValue().get(k);
    }
}
