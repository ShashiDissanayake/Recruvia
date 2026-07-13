package com.recruvia.backend.dto.user;

import java.time.LocalDateTime;

public record LoginHistoryResponse(

        LocalDateTime loginTime,

        String ipAddress,

        String browserDevice,

        String status

) {
}
