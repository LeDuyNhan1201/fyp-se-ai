package com.ben.smartcv.common.component;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("anonymous");
    }

}
