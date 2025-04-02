package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.common.infrastructure.database.AuditorAwareConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareConfig();
    }

}
