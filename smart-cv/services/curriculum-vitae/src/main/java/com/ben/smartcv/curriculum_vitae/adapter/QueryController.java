package com.ben.smartcv.curriculum_vitae.adapter;

import com.ben.smartcv.common.contract.dto.CursorPageResponse;
import com.ben.smartcv.common.contract.query.CvQuery;
import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Controller
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class QueryController {

    QueryGateway queryGateway;

    @QueryMapping
    public CursorPageResponse<ResponseDto.CvTag> search(
            @Argument String jobId,
            @Argument String createdBy,
            @Argument String cursor,
            @Argument Integer limit) {

        CvQuery.Search query = CvQuery.Search.builder()
                .jobId(jobId)
                .createdBy(createdBy)
                .cursor(cursor)
                .limit(Optional.ofNullable(limit).orElse(10))
                .build();

        List<ResponseDto.CvTag> result =
                queryGateway.query(query, ResponseTypes.multipleInstancesOf(ResponseDto.CvTag.class)).join();

        CursorPageResponse<ResponseDto.CvTag> cursorPageResponse = CursorPageResponse.<ResponseDto.CvTag>builder()
                .items(result)
                .nextCursor(result.getFirst().getNextCursor())
                .hasNextPage(result.getFirst().isHasNextPage())
                .build();
        return cursorPageResponse;
    }

}
