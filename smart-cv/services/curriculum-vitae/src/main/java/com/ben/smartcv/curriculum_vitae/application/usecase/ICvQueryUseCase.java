package com.ben.smartcv.curriculum_vitae.application.usecase;

import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICvQueryUseCase {

    List<ResponseDto.CvTag> search(String jobId,
                                   String createdBy,
                                   String lastId,
                                   int limit);

}
