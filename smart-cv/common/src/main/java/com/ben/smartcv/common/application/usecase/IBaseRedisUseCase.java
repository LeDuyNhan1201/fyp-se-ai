package com.ben.smartcv.common.application.usecase;

import org.springframework.stereotype.Service;

@Service
public interface IBaseRedisUseCase<K, F, V> {

    void set(K key, V value);

    void setTimeToLive(K key, long timeoutInDays);

    void hashSet(K key, F field, V value);

    V get(K key);

}