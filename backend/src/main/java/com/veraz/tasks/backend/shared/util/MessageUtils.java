package com.veraz.tasks.backend.shared.util;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    public static String getMessage(String messageCode) {
        return getMessage(messageCode, (Object[]) null);
    }

    public static String getMessage(String messageCode, Object... arguments) {
        if (messageSource == null) {
            throw new IllegalStateException(
                    "MessageSource has not been initialized. Make sure MessageUtils is being injected by Spring.");
        }
        return messageSource.getMessage(messageCode, arguments, messageCode, LocaleContextHolder.getLocale());
    }

    public static String getEntityNotFoundMessage(String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CRUD_NOT_FOUND, localizedEntityName);
    }

    public static String getEntityAlreadyExistsMessage(String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CRUD_ALREADY_EXISTS, localizedEntityName);
    }

    public static String getCrudSuccessMessage(String operation, String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(operation, localizedEntityName);
    }

    public static String getCrudErrorMessage(String operation, String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(operation, localizedEntityName);
    }

    public static String getFieldValidationMessage(String fieldName, String validationType, Object... arguments) {
        String localizedFieldName = getMessage("entity.field." + fieldName.toLowerCase(), fieldName);
        return getMessage("validation.field." + validationType, localizedFieldName, arguments);
    }

    public static boolean hasMessage(String messageCode) {
        try {
            String message = getMessage(messageCode);
            return !message.equals(messageCode);
        } catch (Exception e) {
            return false;
        }
    }
}