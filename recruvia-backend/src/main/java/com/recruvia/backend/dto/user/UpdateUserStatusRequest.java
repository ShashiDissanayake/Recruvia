package com.recruvia.backend.dto.user;

import com.recruvia.backend.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(

        @NotNull(message = "Account status is required.")
        AccountStatus status

) {
}
