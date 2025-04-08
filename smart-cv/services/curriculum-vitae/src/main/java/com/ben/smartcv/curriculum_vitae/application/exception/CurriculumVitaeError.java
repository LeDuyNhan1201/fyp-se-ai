package com.ben.smartcv.curriculum_vitae.application.exception;

import lombok.Getter;

@Getter
public enum CurriculumVitaeError {
    CAN_NOT_SEED_CVS("job/can-not-seed-cvs", "ErrorMsg.canNotSeedCvs"),
    CAN_NOT_SAVE_CV("job/can-not-save-cv", "ErrorMsg.canNotSaveCv"),
    ;

    CurriculumVitaeError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}