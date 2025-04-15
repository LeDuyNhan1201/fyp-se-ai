package com.ben.smartcv.user.infrastructure.data;

import com.ben.smartcv.common.infrastructure.data.ThreadLocalAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableTransactionManagement
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new ThreadLocalAuditorAware();
    }

}
