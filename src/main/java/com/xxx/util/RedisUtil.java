package com.xxx.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/28 1:07
 */
public class RedisUtil {
    private RedisTemplate<Serializable, Object> redisTemplate;

    //默认setter注入
    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * @description: 读取缓存
     * @param key
     * @return:
     */
    public Object get(final String key){
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public boolean set(final String key, Object value){
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean set(final String key, Object value, Long expireTime){
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, expireTime, TimeUnit.SECONDS);
//            operations.set(key, value);
//            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void remove(final String... keys){
//       直接 redisTemplate.delete(keys);不更好？
        for(String key : keys){
            remove(key);
        }
    }

    public void remove(final String key){
        if(exists(key)){//我不太明白，直接delete就好啦，为什么还要判断，就算不存在，返回0也没事啊
            redisTemplate.delete(key);
        }
    }

    /**
     * @description: 批量删除key
     * @param pattern 描述key的一个pattern
     */
    public void removePattern(final String pattern){
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if(keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }

    public boolean exists(final String key){
        return redisTemplate.hasKey(key);
    }


}
