package com.ben.smartcv.curriculum_vitae.application.usecase.impl;

import com.ben.smartcv.common.application.usecase.impl.BaseRedisUseCase;
import com.ben.smartcv.curriculum_vitae.application.usecase.IDownloadUrlCacheUseCase;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Service
public class DownloadUrlCacheUseCase
        extends BaseRedisUseCase<String, String, String> implements IDownloadUrlCacheUseCase {

    public DownloadUrlCacheUseCase(
            RedisTemplate<String, String> redisTemplate,
            HashOperations<String, String, String> hashOperations) {
        super(redisTemplate, hashOperations);
    }

    @Override
    public Map<String, String> getWithPrefix(String prefix, List<String> keys) {
        List<String> redisKeys = keys.stream().map(k -> prefix + k).toList();
        List<String> values = redisTemplate.opsForValue().multiGet(redisKeys);
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            if (values != null && i < values.size()) {
                if (values.get(i) != null) {
                    result.put(keys.get(i), values.get(i));
                }
            }
        }
        return result;
    }

}
