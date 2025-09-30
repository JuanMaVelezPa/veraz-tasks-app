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
        return getMessage(MessageKeys.CONTROLLER_NOT_FOUND, localizedEntityName);
    }

    public static String getEntityAlreadyExistsMessage(String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CONTROLLER_ALREADY_EXISTS, localizedEntityName);
    }

    public static String getCrudSuccessMessage(String operation, String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(operation, localizedEntityName);
    }

    public static String getCrudErrorMessage(String operation, String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(operation, localizedEntityName);
    }

    // ===========================================
    // CONTROLLER MESSAGES (Unified CRUD + Controller)
    // ===========================================

    public static String getControllerSuccessMessage(String operation, String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(operation, localizedEntityName);
    }

    public static String getControllerNotFoundMessage(String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CONTROLLER_NOT_FOUND, localizedEntityName);
    }

    public static String getControllerNotFoundWithIdMessage(String entityName, String id) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CONTROLLER_NOT_FOUND_WITH_ID, localizedEntityName, id);
    }

    public static String getControllerAlreadyExistsMessage(String entityName) {
        String localizedEntityName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CONTROLLER_ALREADY_EXISTS, localizedEntityName);
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

    public static String getLocalizedEntityName(String entityName) {
        return getMessage("entity." + entityName.toLowerCase(), entityName);
    }

    // ===========================================
    // AUTHENTICATION MESSAGES (Unified)
    // ===========================================

    public static String getAuthSuccessMessage(String operation) {
        return getMessage("auth." + operation + ".success");
    }

    public static String getAuthErrorMessage(String errorType, Object... arguments) {
        return getMessage("auth." + errorType, arguments);
    }

    // ===========================================
    // EXCEPTION MESSAGES (Unified)
    // ===========================================

    public static String getExceptionMessage(String exceptionType, Object... arguments) {
        return getMessage("exception." + exceptionType, arguments);
    }

    public static String getValidationExceptionMessage(String validationType, Object... arguments) {
        return getMessage("exception." + validationType, arguments);
    }

    public static String getSystemExceptionMessage(String systemType, Object... arguments) {
        return getMessage("exception." + systemType, arguments);
    }
}