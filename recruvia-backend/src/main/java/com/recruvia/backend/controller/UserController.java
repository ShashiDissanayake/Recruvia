package com.recruvia.backend.controller;

import com.recruvia.backend.common.response.ApiResponse;
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
import com.recruvia.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =========================================================================
    // Candidate / Authenticated User Endpoints
    // =========================================================================

    /**
     * GET /api/v1/users/me
     * Returns the currently authenticated user's profile.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserProfile() {
        UserResponse response = userService.getCurrentUserProfile();
        return ResponseEntity.ok(
                ApiResponse.success("User profile fetched successfully.", response)
        );
    }

    /**
     * PUT /api/v1/users/me
     * Updates firstName, lastName, phone, profileImage of the current user.
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        UserResponse response = userService.updateProfile(request);
        return ResponseEntity.ok(
                ApiResponse.success("Profile updated successfully.", response)
        );
    }

    /**
     * PUT /api/v1/users/change-password
     * Verifies the current password and sets a new one.
     */
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(request);
        return ResponseEntity.ok(
                ApiResponse.success("Password changed successfully.")
        );
    }

    /**
     * POST /api/v1/users/profile-image
     * Uploads a profile image and stores its URL in the user entity.
     */
    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserResponse>> uploadProfileImage(
            @RequestPart("file") MultipartFile file
    ) {
        UserResponse response = userService.uploadProfileImage(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Profile image uploaded successfully.", response)
        );
    }

    /**
     * GET /api/v1/users/dashboard
     * Returns a dashboard summary for the currently authenticated user.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardSummary() {
        DashboardResponse response = userService.getDashboardSummary();
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard summary fetched successfully.", response)
        );
    }

    /**
     * GET /api/v1/users/me/profile-completion
     * Returns LinkedIn-style profile completion percentage with missing fields.
     */
    @GetMapping("/me/profile-completion")
    public ResponseEntity<ApiResponse<ProfileCompletionResponse>> getProfileCompletion() {
        ProfileCompletionResponse response = userService.getProfileCompletion();
        return ResponseEntity.ok(
                ApiResponse.success("Profile completion fetched successfully.", response)
        );
    }

    /**
     * GET /api/v1/users/login-history
     * Returns the recent login history for the current user from audit_logs.
     */
    @GetMapping("/login-history")
    public ResponseEntity<ApiResponse<List<LoginHistoryResponse>>> getLoginHistory() {
        List<LoginHistoryResponse> response = userService.getLoginHistory();
        return ResponseEntity.ok(
                ApiResponse.success("Login history fetched successfully.", response)
        );
    }

    /**
     * GET /api/v1/users/me/notification-preferences
     * Returns the current user's notification preferences.
     */
    @GetMapping("/me/notification-preferences")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> getNotificationPreferences() {
        NotificationPreferenceResponse response = userService.getNotificationPreferences();
        return ResponseEntity.ok(
                ApiResponse.success("Notification preferences fetched successfully.", response)
        );
    }

    /**
     * PUT /api/v1/users/me/notification-preferences
     * Updates the current user's notification preferences.
     */
    @PutMapping("/me/notification-preferences")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> updateNotificationPreferences(
            @Valid @RequestBody UpdateNotificationPreferenceRequest request
    ) {
        NotificationPreferenceResponse response = userService.updateNotificationPreferences(request);
        return ResponseEntity.ok(
                ApiResponse.success("Notification preferences updated successfully.", response)
        );
    }

    // =========================================================================
    // Admin-Only Endpoints
    // =========================================================================

    /**
     * GET /api/v1/users/{id}
     * Admin: fetch any user by UUID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(
                ApiResponse.success("User fetched successfully.", response)
        );
    }

    /**
     * GET /api/v1/users?keyword=&role=&status=&emailVerified=&page=0&size=10&sort=createdAt,desc
     * Admin: get all users with pagination, sorting, searching, and filtering.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(required = false) Boolean emailVerified,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponse> response = userService.getAllUsers(keyword, role, status, emailVerified, pageable);
        return ResponseEntity.ok(
                ApiResponse.success("Users fetched successfully.", response)
        );
    }

    /**
     * GET /api/v1/users/search?keyword=&page=0&size=10
     * Keyword-based user search (accessible to authenticated users).
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserResponse> response = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(
                ApiResponse.success("Search results fetched successfully.", response)
        );
    }

    /**
     * PATCH /api/v1/users/{id}/status
     * Admin: change a user's account status (ACTIVE / INACTIVE / SUSPENDED).
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        UserResponse response = userService.updateUserStatus(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("User status updated successfully.", response)
        );
    }

    /**
     * DELETE /api/v1/users/{id}
     * Admin: soft-delete a user (sets deleted=true, suffixes email to free it up).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully.")
        );
    }

}
