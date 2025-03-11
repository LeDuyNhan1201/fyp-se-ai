package com.ben.smartcv.common.component;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseDocument<TId> {

    TId id;

    @CreatedBy
    String createdBy;

    Date createdAt;

    @LastModifiedBy
    String updatedBy;

    @LastModifiedDate
    Date updatedAt;

    Boolean isDeleted = false;

    String deletedBy;

    Date deletedAt;

    Long version;

}