package com.ben.smartcv.job.application.exception;

import lombok.Getter;

@Getter
public enum JobError {
    CAN_NOT_SAVE_JOB("job/can-not-save-job", "ErrorMsg.canNotSaveJob"),
    INVALID_SALARY_RANGE("job/invalid-salary-range", "ErrorMsg.invalidSalaryRange"),
    ;

    JobError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}