package com.ben.smartcv.common.application.usecase;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public interface IBaseRedisUseCase<K, F, V> {

    void set(K key, V value);

    void setWithTTL(K key, V value, long timeout, TimeUnit unit);

    void setTimeToLive(K key, long timeoutInDays);

    V get(K key);

    List<V> multiGet(Collection<K> keys);

    void multiSet(Map<K, V> map);

    void delete(K key);

    void hashSet(K key, F field, V value);

    V hashGet(K key, F field);

}
