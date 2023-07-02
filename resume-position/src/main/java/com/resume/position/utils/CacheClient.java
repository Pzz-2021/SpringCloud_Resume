package com.resume.position.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static com.resume.position.utils.RedisConstants.*;

@Slf4j
@Component
public class CacheClient {
    @Autowired
    private RedisUtil redisUtil;

    //自定义连接池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);


    /**
     * 方法1：将任意Java对象序列化为json并存储在string类型的key中，并且可以设置TTL过期时间
     *
     * @param key   key值
     * @param value value值
     * @param time  过期时间
     */
    public void set(String key, Object value, long time) {
        redisUtil.set(key, value, time);
    }

    /**
     * 方法2：将任意Java对象序列化为json并存储在string类型的key中，并且可以设置逻辑过期时间，用于处理缓存击穿问题
     *
     * @param key   key值
     * @param value value值
     * @param time  过期时间
     */
    public void setWithLogicalExpire(String key, Object value, Long time) {
        // 设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(Instant.now().getEpochSecond() + time);
        // 写入Redis
        redisUtil.set(key, redisData);
    }

    /**
     * 方法3：根据指定的key查询缓存，并反序列化为指定类型，利用缓存空值的方式解决缓存穿透问题
     *
     * @param keyPrefix  key值前缀名
     * @param id         商品id
     * @param type       要转换的类型
     * @param dbFallback 函数式编程
     * @param time       过期时间
     * @param <R>        返回值类型
     * @param <ID>       id的数据类型
     * @return
     */
    public <R, ID> R queryWithPassThrough(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, long time) {

        String key = keyPrefix + id;

        // 1.从 redis 查询缓存
        Object o = redisUtil.get(key);
        //判断是否存在
        if (o != null && !"".equals(o)) {
            // 3.存在，直接返回
            return type.cast(o);
        }

        // 判断命中的是否是空值
        if ("".equals(o))
            // 返回一个错误信息
            return null;

        // 4.不存在，根据id查询数据库
        R r = dbFallback.apply(id);
        // 5.不存在，返回错误
        if (r == null) {
            // 将空值写入redis
            redisUtil.set(key, "", CACHE_NULL_TTL);
            // 返回错误信息
            return null;
        }
        // 6.存在，写入redis
        this.set(key, r, time + new Random().nextInt(2 * 60));
        return r;
    }

    /**
     * 方法4：根据指定的key查询缓存，并反序列化为指定类型，需要利用逻辑过期解决缓存击穿问题
     *
     * @param keyPrefix  key值前缀名
     * @param id         商品id
     * @param type       要转换的类型
     * @param dbFallback 函数式编程
     * @param time       过期时间
     * @param <R>        返回值类型
     * @param <ID>       id的数据类型
     * @return
     */
    public <R, ID> R queryWithLogicalExpire(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time) {
        String key = keyPrefix + id;
        // 1.从redis查询商铺缓存
        Object o = redisUtil.get(key);
        // 2.判断是否存在
        if (o == null || "".equals(o)) {
            // 3.不存在，直接返回
            return null;
        }
        // 4.命中，需要先把json反序列化为对象
        RedisData redisData = (RedisData) o;
        R r = type.cast(redisData.getData());
        long expireTime = redisData.getExpireTime();
        // 5.判断是否过期
        if (expireTime > Instant.now().getEpochSecond()) {
            // 5.1.未过期，直接返回信息
            return r;
        }
        // 5.2.已过期，需要缓存重建
        // 6.缓存重建
        // 6.1.获取互斥锁
        String lockKey = LOCK_KEY + key;
        boolean isLock = tryLock(lockKey);
        // 6.2.判断是否获取锁成功
        if (isLock) {
            // 6.3.成功，开启独立线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查询数据库
                    R newR = dbFallback.apply(id);
                    // 重建缓存
                    this.setWithLogicalExpire(key, newR, time);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }
        // 6.4.返回过期的商铺信息
        return r;
    }


    /**
     * 方法5：利用互斥锁解决缓存击穿问题
     *
     * @param keyPrefix  key值前缀名
     * @param id         商品id
     * @param type       要转换的类型
     * @param dbFallback 函数式编程
     * @param time       过期时间
     * @param <R>        返回值类型
     * @param <ID>       id的数据类型
     * @return
     */
    public <R, ID> R queryWithMutex(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time) {
        String key = keyPrefix + id;
        // 1.从redis查询商铺缓存
        Object o = redisUtil.get(key);
        // 2.判断是否存在
        if (o != null && !"".equals(o)) {
            // 3.存在，直接返回
            return type.cast(o);
        }
        // 判断命中的是否是空值，o != null 则 o == ""
        if (o != null) {
            // 返回一个错误信息
            return null;
        }

        // 4.实现缓存重建
        // 4.1.获取互斥锁
        String lockKey = LOCK_KEY + key;
        R r = null;
        try {
            boolean isLock = tryLock(lockKey);
            // 4.2.判断是否获取成功
            if (!isLock) {
                // 4.3.获取锁失败，休眠并重试
                Thread.sleep(50);
                return queryWithMutex(keyPrefix, id, type, dbFallback, time);
            }
            // DoubleCheck
            o = redisUtil.get(key);
            if (o != null && !"".equals(o)) {
                // 3.存在，直接返回
                return type.cast(o);
            }
            // 判断命中的是否是空值，o != null 则 o == ""
            if (o != null) {
                // 返回一个错误信息
                return null;
            }

            // 4.4.获取锁成功，根据id查询数据库
            r = dbFallback.apply(id);
            // 5.不存在，返回错误
            if (r == null) {
                // 将空值写入redis
                redisUtil.set(key, "", CACHE_NULL_TTL);
                // 返回错误信息
                return null;
            }

            // 6.存在，写入redis
            this.set(key, r, time + new Random().nextInt(2 * 60));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 7.释放锁
            unlock(lockKey);
        }

        // 8.返回
        return r;
    }


    /**
     * 设置锁操作
     *
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        return redisUtil.setIfAbsent(key, "", 10);
    }

    /**
     * 释放锁操作
     *
     * @param key
     */
    private void unlock(String key) {
        redisUtil.del(key);
    }
}