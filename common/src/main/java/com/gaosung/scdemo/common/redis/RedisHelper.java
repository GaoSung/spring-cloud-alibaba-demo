package com.gaosung.scdemo.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Slf4j
public class RedisHelper {

    @Autowired
    private ReactiveRedisTemplate redisTemplate;
    private static final String LOCK_KEY_PREFIX = "lock_";
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @SuppressWarnings("unchecked")
    public <V> Mono<V> getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @SuppressWarnings("unchecked")
    public <V> Mono<Boolean> putValue(String key, V value, long time, ChronoUnit timeUnit) {
        return redisTemplate.opsForValue().set(key, value, Duration.of(time, timeUnit));
    }

    @SuppressWarnings("unchecked")
    public <V> Mono<Boolean> putValue(String key, V value) {
        return redisTemplate.opsForValue().set(key, value);
    }

    @SuppressWarnings("unchecked")
    public Mono<Boolean> delete(String key) {
        return redisTemplate.opsForValue().delete(key);
    }

    public <K, V> Mono<Boolean> putHashValue(String key, K hashKey, V hashValue) {
        @SuppressWarnings("unchecked")
        ReactiveHashOperations<String, K, V> opsForHash = redisTemplate.opsForHash();
        return opsForHash.put(key, hashKey, hashValue);
    }

    public <K, V> Mono<V> getHashValue(String key, K hashKey) {
        @SuppressWarnings("unchecked")
        ReactiveHashOperations<String, K, V> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hashKey);
    }

    public <K, V> Mono<List<V>> getMultiHashValue(String key, Collection<K> hashKeys) {
        @SuppressWarnings("unchecked")
        ReactiveHashOperations<String, K, V> opsForHash = redisTemplate.opsForHash();
        return opsForHash.multiGet(key, hashKeys);
    }

    public <K, V> Flux<K> getHashKeys(String key) {
        @SuppressWarnings("unchecked")
        ReactiveHashOperations<String, K, V> opsForHash = redisTemplate.opsForHash();
        return opsForHash.keys(key);
    }

    public <K, V> Mono<Long> deleteHashKeys(String key, Collection<K> hashKeys) {
        if (CollectionUtils.isEmpty(hashKeys)) {
            return Mono.just(0L);
        }
        @SuppressWarnings("unchecked")
        ReactiveHashOperations<String, K, V> opsForHash = redisTemplate.opsForHash();
        return opsForHash.remove(key, hashKeys.toArray());
    }

    /**
     * 执行lua脚本
     * @param luaScript 脚本内容
     * @param keys redis键列表
     * @param values 值列表
     * @return
     */
    @SuppressWarnings("unchecked")
    public Flux<Object> executeLuaScript(String luaScript, List<String> keys, List<String> values) {
        RedisScript redisScript = RedisScript.of(luaScript);
        return redisTemplate.execute(redisScript, keys, values);
    }

    /**
     * 执行lua脚本
     * @param luaScript 脚本内容
     * @param keys redis键列表
     * @param values 值列表
     * @param resultType 返回值类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Flux<T> executeLuaScript(String luaScript, List<String> keys, List<String> values, Class<T> resultType) {
        RedisScript redisScript = RedisScript.of(luaScript, resultType);
        return redisTemplate.execute(redisScript, keys, values);
    }

    /**
     * 获取分布式锁
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    @SuppressWarnings("unchecked")
    public Mono<Boolean> tryGetDistributedLock(String lockKey, String requestId, long expireTime) {
        Assert.hasLength(lockKey, "lockKey must not be empty");
        Assert.hasLength(requestId, "requestId must not be empty");
        return redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, Duration.ofSeconds(expireTime));
    }

    /**
     * 释放分布式锁
     * @param lockKey
     * @param requestId
     * @return
     */
    public Mono<Boolean> releaseDistributedLock(String lockKey, String requestId) {
        Assert.hasLength(lockKey, "lockKey must not be empty");
        Assert.hasLength(requestId, "requestId must not be empty");
        String innerLockKey = buildLockKey(lockKey);

        return this.executeLuaScript(RELEASE_LOCK_SCRIPT, Lists.newArrayList(innerLockKey), Lists.newArrayList(requestId), Long.class)
                .next()
                .map(count -> count == 1)
                .doOnError(t -> log.error("release lockkey: [{}] failure", innerLockKey, t))
                .onErrorResume(e -> Mono.just(false))
                ;
    }

    /**
     * 添加前缀
     * @param lockKey
     * @return
     */
    public String buildLockKey(String lockKey) {
        Assert.hasLength(lockKey, "lockKey must not be empty");
        return LOCK_KEY_PREFIX + lockKey;
    }

}
