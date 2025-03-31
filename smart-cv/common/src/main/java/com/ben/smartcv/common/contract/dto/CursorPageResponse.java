package com.ben.smartcv.common.contract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CursorPageResponse<TCursor, TData> {

    List<TData> items;

    TCursor cursor;

    Integer limit;

    Boolean hasNextPage;

}
