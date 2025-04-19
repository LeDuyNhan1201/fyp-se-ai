package com.ben.smartcv.gateway.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Translator {

    static ResourceBundleMessageSource messageSource;

    @Autowired
    public Translator(ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    public static String getMessage(String messageKey, String... args) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            log.info("Getting message for key: {} and locale: {}", messageKey, locale);
            return messageSource.getMessage(messageKey, args, locale);

        } catch (NoSuchMessageException exception) {
            return exception.getMessage();
        }
    }

}