package com.recruvia.backend.dto.user;

import jakarta.validation.constraints.NotNull;

public record UpdateNotificationPreferenceRequest(

        @NotNull(message = "Email notifications preference is required.")
        Boolean emailNotifications,

        @NotNull(message = "Interview notifications preference is required.")
        Boolean interviewNotifications,

        @NotNull(message = "Job recommendation notifications preference is required.")
        Boolean jobRecommendationNotifications,

        @NotNull(message = "Marketing emails preference is required.")
        Boolean marketingEmails

) {
}
