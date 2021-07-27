package com.cristopher.redis.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.concurrent.TimeUnit;

@Service
public class RedisValueCash {
    private final ValueOperations<String , Object> valueOps ;

    public RedisValueCash(final RedisTemplate<String, Object> redisTemplate) {
        valueOps = redisTemplate.opsForValue();

    }

    public void cache (final String key , final Object data){
       // valueOps.set(key, data);
        valueOps.set(key , data , 30, TimeUnit.SECONDS);
    }

    public Object getCachedValue (final String key){
        return valueOps.get(key);
    }

    public void deleteCachedValue(final String key){
        valueOps.getOperations().delete(key);

    }

}
