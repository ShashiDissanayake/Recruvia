package com.recruvia.backend.service.user;

import com.recruvia.backend.dto.user.UpdateProfileRequest;
import com.recruvia.backend.dto.user.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID userId);

    UserResponse updateProfile(UUID userId, UpdateProfileRequest request);

}