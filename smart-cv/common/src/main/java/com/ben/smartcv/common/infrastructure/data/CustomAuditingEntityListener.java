package com.ben.smartcv.common.infrastructure.data;

import com.ben.smartcv.common.domain.AuditingEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Configurable
public class CustomAuditingEntityListener extends AuditingEntityListener {

    public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);
    }

    @Override
    @PrePersist
    public void touchForCreate(@NotNull Object target) {
        AuditingEntity entity = (AuditingEntity) target;
        if (entity.getCreatedAt() == null) {
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