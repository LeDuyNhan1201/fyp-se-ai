package com.ben.smartcv.curriculum_vitae.application.exception;

import lombok.Getter;

@Getter
public enum CurriculumVitaeError {
    CAN_NOT_SEED_CVS("cv/can-not-seed-cvs", "ErrorMsg.CanNotSeedCvs"),
    CAN_NOT_SAVE_CV("cv/can-not-save-cv", "ErrorMsg.CanNotSaveCv"),
    CAN_NOT_UPDATE_CV("cv/can-not-update-cv", "ErrorMsg.CanNotUpdateCv"),
    ;

    CurriculumVitaeError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}