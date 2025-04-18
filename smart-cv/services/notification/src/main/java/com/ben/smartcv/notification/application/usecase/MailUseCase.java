package com.ben.smartcv.notification.application.usecase;

import com.ben.smartcv.common.auth.PreviewUser;
import com.ben.smartcv.common.contract.dto.Enum;
import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.job.JobInfo;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.notification.application.exception.NotificationError;
import com.ben.smartcv.notification.application.exception.NotificationHttpException;
import com.ben.smartcv.notification.infrastructure.grpc.GrpcClientAuthService;
import com.ben.smartcv.notification.infrastructure.grpc.GrpcClientJobService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailUseCase implements IMailUseCase {

    static String APPLICATION_NAME = "Smart CV";

    @Value("${spring.mail.from}")
    @NonFinal
    String emailFrom;

    JavaMailSender mailSender;

    SpringTemplateEngine templateEngine;

    GrpcClientJobService jobServiceClient;

    GrpcClientAuthService authServiceClient;

    @Override
    public void sendApprovalMail(NotificationEvent.ApprovalMailSent event) {
        LocaleContextHolder.setLocale(Locale.of(event.getLocale()));

        JobInfo jobInfo = jobServiceClient.callGetInfoById(event.getJobId());
        PreviewUser userInfo = authServiceClient.callGetById(event.getUserId());

        String to = userInfo.getEmail();

        String subject = (event.getTitle() != null && !event.getTitle().isEmpty())
                ? event.getTitle()
                : Translator.getMessage("MailTitle.Approval",
                jobInfo.getPosition(), jobInfo.getOrganizationName());

        String body = (event.getContent() != null && !event.getContent().isEmpty())
                ? event.getContent()
                : Translator.getMessage("MailContent.Approval",
                userInfo.getName(), jobInfo.getPosition(),
                jobInfo.getOrganizationName(), APPLICATION_NAME);

        log.debug("Sending mail to: {}, subject: {}, body: {}", to, subject, body);

        try {
            sendMail(Enum.MailType.APPROVAL, to, subject, body);
        } catch (Exception e) {
            log.error("Error sending mail: {}", e.getMessage(), e);
            throw new NotificationHttpException(
                    NotificationError.CAN_NOT_SEND_MAIL,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private void sendMail(Enum.MailType type, String to, String subject, String body) throws Exception {
        String templateName = switch (type) {
            case APPROVAL -> "approval-template.html";
            case REJECTION -> "rejection-template.html";
            default -> throw new NotificationHttpException(
                    NotificationError.INVALID_TYPE,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        };
        Map<String, Object> properties = new HashMap<>();
        properties.put("body", body);
        properties.put("subject", subject);

        Context context = new Context();
        context.setVariables(properties);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        helper.setFrom(emailFrom, APPLICATION_NAME);
        helper.setTo(to);
        helper.setSubject(subject);

        String html = templateEngine.process(templateName, context);
        helper.setText(html, true);

        mailSender.send(message);
    }

}
