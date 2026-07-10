package com.recruvia.backend.common.constants;

public final class ApiMessages {

    private ApiMessages() {
        throw new UnsupportedOperationException("Utility class");
    }

    /*
     * Authentication
     */
    public static final String USER_REGISTERED_SUCCESSFULLY =
            "User registered successfully.";

    public static final String LOGIN_SUCCESS =
            "Login successful.";

    public static final String LOGOUT_SUCCESS =
            "Logout successful.";

    public static final String TOKEN_REFRESHED =
            "Access token refreshed successfully.";

    public static final String EMAIL_VERIFIED =
            "Email verified successfully.";

    public static final String PASSWORD_RESET_EMAIL_SENT =
            "Password reset instructions have been sent to your email.";

    public static final String PASSWORD_RESET_SUCCESS =
            "Password reset successfully.";

    /*
     * User
     */
    public static final String PROFILE_UPDATED =
            "Profile updated successfully.";

    /*
     * Company
     */
    public static final String COMPANY_CREATED =
            "Company created successfully.";

    public static final String COMPANY_UPDATED =
            "Company updated successfully.";

    public static final String COMPANY_DELETED =
            "Company deleted successfully.";

    /*
     * Job
     */
    public static final String JOB_CREATED =
            "Job created successfully.";

    public static final String JOB_UPDATED =
            "Job updated successfully.";

    public static final String JOB_DELETED =
            "Job deleted successfully.";

    /*
     * Resume
     */
    public static final String RESUME_UPLOADED =
            "Resume uploaded successfully.";

    /*
     * Application
     */
    public static final String APPLICATION_SUBMITTED =
            "Application submitted successfully.";

    public static final String APPLICATION_WITHDRAWN =
            "Application withdrawn successfully.";

    /*
     * Interview
     */
    public static final String INTERVIEW_SCHEDULED =
            "Interview scheduled successfully.";
}