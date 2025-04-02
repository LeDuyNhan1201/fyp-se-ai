package com.ben.smartcv.job.application.usecase;

import com.ben.smartcv.job.application.dto.ResponseDto;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISlaveJobReadSideUseCase {

    List<ResponseDto.JobDescription> advancedSearch(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Integer page,
            Integer size);

}
