package com.recruvia.backend.dto.user;

import com.recruvia.backend.enums.AccountStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(

        UUID id,

        String firstName,

        String lastName,

        String email,

        String phone,

        String profileImage,

        String role,

        AccountStatus accountStatus,

        boolean emailVerified,

        LocalDateTime lastLogin,

        LocalDateTime createdAt

) {
}