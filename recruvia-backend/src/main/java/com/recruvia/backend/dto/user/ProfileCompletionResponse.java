package com.recruvia.backend.dto.user;

import java.util.List;

public record ProfileCompletionResponse(

        int completionPercentage,

        List<String> missingFields,

        List<String> recommendedNextSteps

) {
}
