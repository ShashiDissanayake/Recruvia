package com.recruvia.backend.dto.user;

public record NotificationPreferenceResponse(

        boolean emailNotifications,

        boolean interviewNotifications,

        boolean jobRecommendationNotifications,

        boolean marketingEmails

) {
}
