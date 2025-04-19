package com.ben.smartcv.curriculum_vitae.application.usecase;

import com.ben.smartcv.common.application.usecase.IBaseRedisUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IDownloadUrlCacheUseCase extends IBaseRedisUseCase<String, String, String> {

    Map<String, String> getWithPrefix(String prefix, List<String> keys);

}
