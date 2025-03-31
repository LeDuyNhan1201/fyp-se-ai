package com.ben.smartcv.job.util;

import com.ben.smartcv.job.application.exception.JobError;
import com.ben.smartcv.job.application.exception.JobHttpException;
import org.springframework.http.HttpStatus;

public final class ValidationHelper {

    public static void validateSalaryRange(Double fromSalary, Double toSalary) {
        if (fromSalary == null && toSalary == null) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }
        if (fromSalary != null && toSalary != null && fromSalary > toSalary) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }
    }

}
