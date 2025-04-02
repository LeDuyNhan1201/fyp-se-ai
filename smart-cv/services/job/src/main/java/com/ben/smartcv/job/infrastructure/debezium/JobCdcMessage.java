package com.ben.smartcv.job.infrastructure.debezium;

import com.ben.smartcv.job.domain.entity.MasterJob;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobCdcMessage {

    MasterJob after;

    MasterJob before;

    CdcOperation op;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Key {

        String id;

    }

}
