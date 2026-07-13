package com.recruvia.backend.dto.user;

import java.util.List;

public record DashboardResponse(

        long totalResumes,

        long totalJobApplications,

        long totalScheduledInterviews,

        long totalNotifications,

        List<String> recentActivities

) {
}
