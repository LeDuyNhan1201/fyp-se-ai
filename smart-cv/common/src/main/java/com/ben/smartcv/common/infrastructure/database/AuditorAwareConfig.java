package com.ben.smartcv.common.infrastructure.database;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareConfig implements AuditorAware<String> {

    private static final ThreadLocal<String> currentAuditor = new ThreadLocal<>();

    public static void set(String auditor) {
        currentAuditor.set(auditor);
    }

    public static void clear() {
        currentAuditor.remove();
    }

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(currentAuditor.get());
    }

}
