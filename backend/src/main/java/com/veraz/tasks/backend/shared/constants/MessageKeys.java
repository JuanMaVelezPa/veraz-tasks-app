package com.veraz.tasks.backend.shared.constants;

/**
 * Message keys for internationalization
 * 
 * Applied principles:
 * - Centralize message keys to avoid typos
 * - Facilitate message maintenance
 * - Follow consistent naming conventions
 * - Standardized and reusable structure
 */
public final class MessageKeys {

    private MessageKeys() {
        // Private constructor to prevent instantiation
    }

    // ===========================================
    // COMMON VALIDATION KEYS
    // ===========================================

    public static final String COMMON_REQUIRED = "common.required";
    public static final String COMMON_NOT_BLANK = "common.not.blank";
    public static final String COMMON_NOT_NULL = "common.not.null";
    public static final String COMMON_INVALID_FORMAT = "common.invalid.format";
    public static final String COMMON_INVALID_LENGTH = "common.invalid.length";
    public static final String COMMON_INVALID_VALUE = "common.invalid.value";
    public static final String COMMON_ALREADY_EXISTS = "common.already.exists";
    public static final String COMMON_NOT_FOUND = "common.not.found";

    // ===========================================
    // FIELD VALIDATION KEYS
    // ===========================================

    public static final String VALIDATION_FIELD_REQUIRED = "validation.field.required";
    public static final String VALIDATION_FIELD_SIZE = "validation.field.size";
    public static final String VALIDATION_FIELD_MAX_LENGTH = "validation.field.max.length";
    public static final String VALIDATION_FIELD_MIN_LENGTH = "validation.field.min.length";
    public static final String VALIDATION_FIELD_EMAIL = "validation.field.email";
    public static final String VALIDATION_FIELD_PASSWORD = "validation.field.password";

    // ===========================================
    // AUTHENTICATION KEYS
    // ===========================================

    public static final String AUTH_SIGNIN_SUCCESS = "auth.signin.success";
    public static final String AUTH_SIGNUP_SUCCESS = "auth.signup.success";
    public static final String AUTH_LOGOUT_SUCCESS = "auth.logout.success";
    public static final String AUTH_INVALID_CREDENTIALS = "auth.invalid.credentials";
    public static final String AUTH_USER_INACTIVE = "auth.user.inactive";
    public static final String AUTH_USER_NOT_FOUND = "auth.user.not.found";
    public static final String AUTH_USER_ALREADY_EXISTS = "auth.user.already.exists";
    public static final String AUTH_USER_NOT_AUTHENTICATED = "auth.user.not.authenticated";

    // ===========================================
    // CRUD OPERATION KEYS
    // ===========================================

    public static final String CRUD_CREATED_SUCCESS = "crud.created.success";
    public static final String CRUD_UPDATED_SUCCESS = "crud.updated.success";
    public static final String CRUD_DELETED_SUCCESS = "crud.deleted.success";
    public static final String CRUD_RETRIEVED_SUCCESS = "crud.retrieved.success";
    public static final String CRUD_NOT_FOUND = "crud.not.found";
    public static final String CRUD_ALREADY_EXISTS = "crud.already.exists";
    public static final String CRUD_ERROR_CREATING = "crud.error.creating";
    public static final String CRUD_ERROR_UPDATING = "crud.error.updating";
    public static final String CRUD_ERROR_DELETING = "crud.error.deleting";
    public static final String CRUD_ERROR_GETTING = "crud.error.getting";

    // ===========================================
    // ENTITY KEYS
    // ===========================================

    // User entity
    public static final String ENTITY_USER = "entity.user";
    public static final String ENTITY_USER_USERNAME = "entity.user.username";
    public static final String ENTITY_USER_EMAIL = "entity.user.email";
    public static final String ENTITY_USER_PASSWORD = "entity.user.password";

    // Person entity
    public static final String ENTITY_PERSON = "entity.person";
    public static final String ENTITY_PERSON_IDENTIFICATION = "entity.person.identification";
    public static final String ENTITY_PERSON_FIRST_NAME = "entity.person.firstName";
    public static final String ENTITY_PERSON_LAST_NAME = "entity.person.lastName";
    public static final String ENTITY_PERSON_GENDER = "entity.person.gender";
    public static final String ENTITY_PERSON_NATIONALITY = "entity.person.nationality";
    public static final String ENTITY_PERSON_MOBILE = "entity.person.mobile";
    public static final String ENTITY_PERSON_ADDRESS = "entity.person.address";
    public static final String ENTITY_PERSON_CITY = "entity.person.city";
    public static final String ENTITY_PERSON_COUNTRY = "entity.person.country";
    public static final String ENTITY_PERSON_POSTAL_CODE = "entity.person.postalCode";
    public static final String ENTITY_PERSON_NOTES = "entity.person.notes";

    // Employee entity
    public static final String ENTITY_EMPLOYEE = "entity.employee";
    public static final String ENTITY_EMPLOYEE_POSITION = "entity.employee.position";
    public static final String ENTITY_EMPLOYEE_DEPARTMENT = "entity.employee.department";
    public static final String ENTITY_EMPLOYEE_HIRE_DATE = "entity.employee.hireDate";
    public static final String ENTITY_EMPLOYEE_CURRENCY = "entity.employee.currency";
    public static final String ENTITY_EMPLOYEE_EMPLOYMENT_TYPE = "entity.employee.employmentType";
    public static final String ENTITY_EMPLOYEE_STATUS = "entity.employee.status";
    public static final String ENTITY_EMPLOYEE_WORK_EMAIL = "entity.employee.workEmail";
    public static final String ENTITY_EMPLOYEE_WORK_MOBILE = "entity.employee.workMobile";
    public static final String ENTITY_EMPLOYEE_WORK_LOCATION = "entity.employee.workLocation";
    public static final String ENTITY_EMPLOYEE_WORK_SCHEDULE = "entity.employee.workSchedule";
    public static final String ENTITY_EMPLOYEE_SKILLS = "entity.employee.skills";
    public static final String ENTITY_EMPLOYEE_CERTIFICATIONS = "entity.employee.certifications";
    public static final String ENTITY_EMPLOYEE_EDUCATION = "entity.employee.education";
    public static final String ENTITY_EMPLOYEE_BENEFITS = "entity.employee.benefits";

    // Client entity
    public static final String ENTITY_CLIENT = "entity.client";
    public static final String ENTITY_CLIENT_CODE = "entity.client.code";
    public static final String ENTITY_CLIENT_TYPE = "entity.client.type";
    public static final String ENTITY_CLIENT_CATEGORY = "entity.client.category";
    public static final String ENTITY_CLIENT_SOURCE = "entity.client.source";
    public static final String ENTITY_CLIENT_COMPANY_NAME = "entity.client.companyName";
    public static final String ENTITY_CLIENT_COMPANY_WEBSITE = "entity.client.companyWebsite";
    public static final String ENTITY_CLIENT_COMPANY_INDUSTRY = "entity.client.companyIndustry";
    public static final String ENTITY_CLIENT_CONTACT_PERSON = "entity.client.contactPerson";
    public static final String ENTITY_CLIENT_CONTACT_POSITION = "entity.client.contactPosition";
    public static final String ENTITY_CLIENT_TAX_ID = "entity.client.taxId";
    public static final String ENTITY_CLIENT_PAYMENT_TERMS = "entity.client.paymentTerms";
    public static final String ENTITY_CLIENT_PAYMENT_METHOD = "entity.client.paymentMethod";
    public static final String ENTITY_CLIENT_PREFERENCES = "entity.client.preferences";
    public static final String ENTITY_CLIENT_TAGS = "entity.client.tags";

    // ===========================================
    // BUSINESS LOGIC KEYS
    // ===========================================

    public static final String BUSINESS_PERSON_HAS_USER = "business.person.has.user";
    public static final String BUSINESS_PERSON_HAS_EMPLOYEE = "business.person.has.employee";
    public static final String BUSINESS_PERSON_HAS_CLIENT = "business.person.has.client";
    public static final String BUSINESS_PERSON_IDENT_EXISTS = "business.person.ident.exists";
    public static final String BUSINESS_USER_HAS_PERSON = "business.user.has.person";
    public static final String BUSINESS_USER_HAS_PERSON_DELETE = "business.user.has.person.delete";

    // ===========================================
    // PAGINATION KEYS
    // ===========================================

    public static final String PAGINATION_INVALID_PAGE = "pagination.invalid.page";
    public static final String PAGINATION_INVALID_SIZE = "pagination.invalid.size";
    public static final String PAGINATION_INVALID_SORT_DIRECTION = "pagination.invalid.sort.direction";

    // ===========================================
    // GLOBAL EXCEPTION KEYS
    // ===========================================

    public static final String EXCEPTION_VALIDATION_FAILED = "exception.validation.failed";
    public static final String EXCEPTION_CONSTRAINT_VIOLATION = "exception.constraint.violation";
    public static final String EXCEPTION_INVALID_REQUEST_BODY = "exception.invalid.request.body";
    public static final String EXCEPTION_MISSING_PARAMETER = "exception.missing.parameter";
    public static final String EXCEPTION_INVALID_PARAMETER_TYPE = "exception.invalid.parameter.type";
    public static final String EXCEPTION_ENDPOINT_NOT_FOUND = "exception.endpoint.not.found";
    public static final String EXCEPTION_INTERNAL_SERVER_ERROR = "exception.internal.server.error";
    public static final String EXCEPTION_AUTHENTICATION_FAILED = "exception.authentication.failed";
    public static final String EXCEPTION_ACCESS_DENIED = "exception.access.denied";
    public static final String EXCEPTION_DATA_INTEGRITY_VIOLATION = "exception.data.integrity.violation";
}