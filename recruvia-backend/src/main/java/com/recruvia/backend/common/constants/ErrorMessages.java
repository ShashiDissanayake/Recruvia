package com.recruvia.backend.common.constants;

public final class ErrorMessages {

    private ErrorMessages() {
        throw new UnsupportedOperationException("Utility class");
    }

    /*
     * Authentication
     */
    public static final String EMAIL_ALREADY_EXISTS =
            "Email address is already registered.";

    public static final String INVALID_CREDENTIALS =
            "Invalid email or password.";

    public static final String INVALID_REFRESH_TOKEN =
            "Invalid refresh token.";

    public static final String REFRESH_TOKEN_EXPIRED =
            "Refresh token has expired.";

    public static final String ACCOUNT_NOT_VERIFIED =
            "Email address has not been verified.";

    public static final String ROLE_NOT_FOUND =
            "Default role not found.";

    public static final String USER_NOT_FOUND =
            "User not found.";

    public static final String INVALID_RESET_TOKEN =
            "Invalid password reset token.";

    public static final String RESET_TOKEN_EXPIRED =
            "Password reset token has expired.";

    /*
     * Authorization
     */
    public static final String ACCESS_DENIED =
            "You do not have permission to access this resource.";

    /*
     * Resource
     */
    public static final String RESOURCE_NOT_FOUND =
            "Requested resource was not found.";

    public static final String DUPLICATE_RESOURCE =
            "Resource already exists.";

    /*
     * Validation
     */
    public static final String INVALID_REQUEST =
            "Invalid request.";

    /*
     * File Upload
     */
    public static final String FILE_UPLOAD_FAILED =
            "File upload failed.";

    public static final String UNSUPPORTED_FILE_TYPE =
            "Unsupported file type.";

    /*
     * AI Module
     */
    public static final String AI_PROCESSING_FAILED =
            "AI processing failed.";

    /*
     * General
     */
    public static final String INTERNAL_SERVER_ERROR =
            "An unexpected error occurred. Please try again later.";
}