package com.ben.smartcv.common.infrastructure.database;

import com.ben.smartcv.common.domain.AuditingEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Configurable
@AllArgsConstructor
public class CustomAuditingEntityListener extends AuditingEntityListener {

    @Override
    @PrePersist
    public void touchForCreate(@NotNull Object target) {
        AuditingEntity entity = (AuditingEntity) target;
        if (entity.getCreatedBy() == null && entity.getCreatedAt() == null) {
            super.touchForCreate(target);
        }
    }

    @Override
    @PreUpdate
    public void touchForUpdate(@NotNull Object target) {
        AuditingEntity entity = (AuditingEntity) target;
        super.touchForUpdate(target);
        if (entity.isDeleted()) {
            entity.setDeletedAt(Instant.now());
            entity.setDeletedBy(entity.getUpdatedBy());
        }
    }

}