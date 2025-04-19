package com.ben.smartcv.notification.application.usecase;

import com.ben.smartcv.common.contract.event.NotificationEvent;

public interface IMailUseCase {

    void sendApprovalMail(NotificationEvent.ApprovalMailSent event);

}
