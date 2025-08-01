package com.veraz.tasks.backend.shared.util;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility for standardized message handling
 * 
 * Applied principles:
 * - Centralize message retrieval logic
 * - Facilitate standardized message usage
 * - Reduce code duplication
 * - Automatic MessageSource injection
 */
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * Gets a localized message
     */
    public static String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }

    /**
     * Gets a localized message with arguments
     */
    public static String getMessage(String code, Object... args) {
        if (messageSource == null) {
            throw new IllegalStateException(
                    "MessageSource has not been initialized. Make sure MessageUtils is being injected by Spring.");
        }
        return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
    }

    /**
     * Gets an entity not found message
     */
    public static String getEntityNotFound(String entityName) {
        String entityLocalizedName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CRUD_NOT_FOUND, entityLocalizedName);
    }

    /**
     * Gets an entity already exists message
     */
    public static String getEntityAlreadyExists(String entityName) {
        String entityLocalizedName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(MessageKeys.CRUD_ALREADY_EXISTS, entityLocalizedName);
    }

    /**
     * Gets a CRUD success message
     */
    public static String getCrudSuccess(String operation, String entityName) {
        String entityLocalizedName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(operation, entityLocalizedName);
    }

    /**
     * Gets a CRUD error message
     */
    public static String getCrudError(String operation, String entityName) {
        String entityLocalizedName = getMessage("entity." + entityName.toLowerCase(), entityName);
        return getMessage(operation, entityLocalizedName);
    }

    /**
     * Gets a field validation message
     */
    public static String getFieldValidation(String fieldName, String validationType, Object... args) {
        String fieldLocalizedName = getMessage("entity.field." + fieldName.toLowerCase(), fieldName);
        return getMessage("validation.field." + validationType, fieldLocalizedName, args);
    }

    /**
     * Checks if a message exists for the given key
     */
    public static boolean hasMessage(String code) {
        try {
            String message = getMessage(code);
            return !message.equals(code); // If message equals code, it doesn't exist
        } catch (Exception e) {
            return false;
        }
    }
}