package com.ben.smartcv.common.infrastructure.database;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareConfig implements AuditorAware<String> {

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("anonymous");
    }

}
