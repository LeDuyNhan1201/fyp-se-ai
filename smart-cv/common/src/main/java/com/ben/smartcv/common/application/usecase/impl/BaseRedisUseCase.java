package com.ben.smartcv.common.application.usecase.impl;

import com.ben.smartcv.common.application.usecase.IBaseRedisUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class BaseRedisUseCase<K, F, V> implements IBaseRedisUseCase<K, F, V> {

    RedisTemplate<K, V> redisTemplate;
    HashOperations<K, F, V> hashOperations;

    @Override
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setWithTTL(K key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void setTimeToLive(K key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public List<V> multiGet(Collection<K> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void multiSet(Map<K, V> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public V hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

}
