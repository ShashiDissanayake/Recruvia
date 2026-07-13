package com.recruvia.backend.service.user;

import com.recruvia.backend.dto.user.ChangePasswordRequest;
import com.recruvia.backend.dto.user.DashboardResponse;
import com.recruvia.backend.dto.user.LoginHistoryResponse;
import com.recruvia.backend.dto.user.NotificationPreferenceResponse;
import com.recruvia.backend.dto.user.ProfileCompletionResponse;
import com.recruvia.backend.dto.user.UpdateNotificationPreferenceRequest;
import com.recruvia.backend.dto.user.UpdateProfileRequest;
import com.recruvia.backend.dto.user.UpdateUserStatusRequest;
import com.recruvia.backend.dto.user.UserResponse;
import com.recruvia.backend.enums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse getCurrentUserProfile();

    UserResponse updateProfile(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);

    UserResponse uploadProfileImage(MultipartFile file);

    UserResponse getUserById(UUID id);

    Page<UserResponse> getAllUsers(
            String keyword,
            String role,
            AccountStatus status,
            Boolean emailVerified,
            Pageable pageable
    );

    Page<UserResponse> searchUsers(String keyword, Pageable pageable);

    UserResponse updateUserStatus(UUID id, UpdateUserStatusRequest request);

    void deleteUser(UUID id);

    DashboardResponse getDashboardSummary();

    ProfileCompletionResponse getProfileCompletion();

    List<LoginHistoryResponse> getLoginHistory();

    NotificationPreferenceResponse getNotificationPreferences();

    NotificationPreferenceResponse updateNotificationPreferences(UpdateNotificationPreferenceRequest request);

}