package com.ben.smartcv.job.application.exception;

import lombok.Getter;

@Getter
public enum JobError {
    CAN_NOT_SEED_JOBS("job/can-not-seed-jobs", "ErrorMsg.canNotSeedJobs"),
    CAN_NOT_SAVE_JOB("job/can-not-save-job", "ErrorMsg.canNotSaveJob"),
    INVALID_SALARY_RANGE("job/invalid-salary-range", "ErrorMsg.invalidSalaryRange"),

    DEBEZIUM_CONNECT_FAILED("job/debezium-connect-failed", "ErrorMsg.debeziumConnectFailed"),
    ;

    JobError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}