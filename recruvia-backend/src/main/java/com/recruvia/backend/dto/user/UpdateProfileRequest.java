package com.recruvia.backend.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @Size(max = 100, message = "First name cannot exceed 100 characters")
        String firstName,

        @Size(max = 100, message = "Last name cannot exceed 100 characters")
        String lastName,

        @Pattern(
                regexp = "^\\+?\\d{10,15}$",
                message = "Phone number must be between 10 and 15 digits"
        )
        String phone,

        @Size(max = 500, message = "Profile image URL cannot exceed 500 characters")
        String profileImage

) {
}