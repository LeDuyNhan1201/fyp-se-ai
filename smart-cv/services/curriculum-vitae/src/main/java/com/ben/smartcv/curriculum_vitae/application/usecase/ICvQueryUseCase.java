package com.ben.smartcv.curriculum_vitae.application.usecase;

import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICvQueryUseCase {

    List<ResponseDto.CvTag> findAllAfter(String lastId, int limit);

}
