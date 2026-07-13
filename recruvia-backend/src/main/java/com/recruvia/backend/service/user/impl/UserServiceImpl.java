package com.recruvia.backend.service.user.impl;

import com.recruvia.backend.common.constants.ErrorMessages;
import com.recruvia.backend.dto.user.ChangePasswordRequest;
import com.recruvia.backend.dto.user.DashboardResponse;
import com.recruvia.backend.dto.user.LoginHistoryResponse;
import com.recruvia.backend.dto.user.NotificationPreferenceResponse;
import com.recruvia.backend.dto.user.ProfileCompletionResponse;
import com.recruvia.backend.dto.user.UpdateNotificationPreferenceRequest;
import com.recruvia.backend.dto.user.UpdateProfileRequest;
import com.recruvia.backend.dto.user.UpdateUserStatusRequest;
import com.recruvia.backend.dto.user.UserResponse;
import com.recruvia.backend.entity.AuditLog;
import com.recruvia.backend.entity.NotificationPreference;
import com.recruvia.backend.entity.User;
import com.recruvia.backend.enums.AccountStatus;
import com.recruvia.backend.exception.BadRequestException;
import com.recruvia.backend.exception.ResourceNotFoundException;
import com.recruvia.backend.mapper.NotificationPreferenceMapper;
import com.recruvia.backend.mapper.UserMapper;
import com.recruvia.backend.repository.ApplicationRepository;
import com.recruvia.backend.repository.AuditLogRepository;
import com.recruvia.backend.repository.InterviewRepository;
import com.recruvia.backend.repository.NotificationPreferenceRepository;
import com.recruvia.backend.repository.NotificationRepository;
import com.recruvia.backend.repository.ResumeRepository;
import com.recruvia.backend.repository.UserRepository;
import com.recruvia.backend.repository.UserSkillRepository;
import com.recruvia.backend.repository.specification.UserSpecification;
import com.recruvia.backend.security.CustomUserDetails;
import com.recruvia.backend.service.storage.StorageService;
import com.recruvia.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final NotificationRepository notificationRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final UserSkillRepository userSkillRepository;

    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    private final UserMapper userMapper;
    private final NotificationPreferenceMapper notificationPreferenceMapper;

    // =========================================================================
    // Helper: resolve authenticated user from SecurityContext
    // =========================================================================

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BadRequestException("No authenticated user found.");
        }

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND)
                );
    }

    // =========================================================================
    // 1. GET /api/v1/users/me
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile() {
        User user = getAuthenticatedUser();
        return userMapper.toResponse(user);
    }

    // =========================================================================
    // 2. PUT /api/v1/users/me
    // =========================================================================

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = getAuthenticatedUser();
        userMapper.updateEntity(request, user);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    // =========================================================================
    // 3. PUT /api/v1/users/change-password
    // =========================================================================

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = getAuthenticatedUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect.");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BadRequestException(
                    "New password must be different from the current password."
            );
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    // =========================================================================
    // 4. POST /api/v1/users/profile-image
    // =========================================================================

    @Override
    public UserResponse uploadProfileImage(MultipartFile file) {
        User user = getAuthenticatedUser();

        // Delete old profile image if present
        if (user.getProfileImage() != null && !user.getProfileImage().isBlank()) {
            storageService.deleteFile(user.getProfileImage());
        }

        String imageUrl = storageService.storeFile(file);
        user.setProfileImage(imageUrl);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    // =========================================================================
    // 5. GET /api/v1/users/{id}  (Admin)
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND)
                );
        return userMapper.toResponse(user);
    }

    // =========================================================================
    // 6. GET /api/v1/users  (Admin — paginated, filtered)
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(
            String keyword,
            String role,
            AccountStatus status,
            Boolean emailVerified,
            Pageable pageable
    ) {
        Specification<User> spec = UserSpecification.filter(keyword, role, status, emailVerified);
        return userRepository.findAll(spec, pageable)
                .map(userMapper::toResponse);
    }

    // =========================================================================
    // 7. GET /api/v1/users/search
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        Specification<User> spec = UserSpecification.filter(keyword, null, null, null);
        return userRepository.findAll(spec, pageable)
                .map(userMapper::toResponse);
    }

    // =========================================================================
    // 8. PATCH /api/v1/users/{id}/status  (Admin)
    // =========================================================================

    @Override
    public UserResponse updateUserStatus(UUID id, UpdateUserStatusRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND)
                );
        user.setAccountStatus(request.status());
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    // =========================================================================
    // 9. DELETE /api/v1/users/{id}  (Admin — Soft Delete)
    // =========================================================================

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND)
                );

        // Hibernate @SQLDelete hook will set deleted = true and suffix the email
        userRepository.delete(user);
    }

    // =========================================================================
    // 10. GET /api/v1/users/dashboard
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboardSummary() {
        User user = getAuthenticatedUser();

        long totalResumes = resumeRepository.findByUser(user).size();

        long totalJobApplications = applicationRepository.findByUser(user).size();

        long totalScheduledInterviews =
                interviewRepository.countScheduledInterviewsByUser(user);

        long totalNotifications = notificationRepository.findByUser(user).size();

        // Recent activities: last 5 login audit log entries
        List<String> recentActivities = auditLogRepository
                .findTop5ByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(log -> log.getAction() + " at " + log.getCreatedAt())
                .toList();

        return new DashboardResponse(
                totalResumes,
                totalJobApplications,
                totalScheduledInterviews,
                totalNotifications,
                recentActivities
        );
    }

    // =========================================================================
    // 11. GET /api/v1/users/me/profile-completion
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public ProfileCompletionResponse getProfileCompletion() {
        User user = getAuthenticatedUser();

        List<String> missingFields = new ArrayList<>();
        List<String> nextSteps = new ArrayList<>();

        int totalFields = 7;
        int completedFields = 0;

        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            completedFields++;
        } else {
            missingFields.add("firstName");
            nextSteps.add("Add your first name to personalize your profile.");
        }

        if (user.getLastName() != null && !user.getLastName().isBlank()) {
            completedFields++;
        } else {
            missingFields.add("lastName");
            nextSteps.add("Add your last name.");
        }

        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            completedFields++;
        } else {
            missingFields.add("phone");
            nextSteps.add("Add a phone number so recruiters can contact you.");
        }

        if (user.getProfileImage() != null && !user.getProfileImage().isBlank()) {
            completedFields++;
        } else {
            missingFields.add("profileImage");
            nextSteps.add("Upload a professional profile picture.");
        }

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            completedFields++;
        } else {
            missingFields.add("emailVerified");
            nextSteps.add("Verify your email address to unlock all features.");
        }

        boolean hasResume = !resumeRepository.findByUser(user).isEmpty();
        if (hasResume) {
            completedFields++;
        } else {
            missingFields.add("resume");
            nextSteps.add("Upload your resume to start applying for jobs.");
        }

        boolean hasSkills = !userSkillRepository.findByUser(user).isEmpty();
        if (hasSkills) {
            completedFields++;
        } else {
            missingFields.add("skills");
            nextSteps.add("Add your skills to improve job match recommendations.");
        }

        int completionPercentage = (completedFields * 100) / totalFields;

        return new ProfileCompletionResponse(completionPercentage, missingFields, nextSteps);
    }

    // =========================================================================
    // 12. GET /api/v1/users/login-history
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public List<LoginHistoryResponse> getLoginHistory() {
        User user = getAuthenticatedUser();

        return auditLogRepository
                .findTop20ByUserAndActionOrderByCreatedAtDesc(user, "LOGIN_SUCCESS")
                .stream()
                .map(log -> new LoginHistoryResponse(
                        log.getCreatedAt(),
                        log.getIpAddress(),
                        log.getUserAgent(),
                        log.getStatus()
                ))
                .toList();
    }

    // =========================================================================
    // 13. Notification Preferences
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public NotificationPreferenceResponse getNotificationPreferences() {
        User user = getAuthenticatedUser();
        NotificationPreference preference = getOrCreatePreference(user);
        return notificationPreferenceMapper.toResponse(preference);
    }

    @Override
    public NotificationPreferenceResponse updateNotificationPreferences(
            UpdateNotificationPreferenceRequest request
    ) {
        User user = getAuthenticatedUser();
        NotificationPreference preference = getOrCreatePreference(user);
        notificationPreferenceMapper.updateEntity(request, preference);
        NotificationPreference saved = notificationPreferenceRepository.save(preference);
        return notificationPreferenceMapper.toResponse(saved);
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    private NotificationPreference getOrCreatePreference(User user) {
        return notificationPreferenceRepository.findByUser(user)
                .orElseGet(() -> {
                    NotificationPreference pref = new NotificationPreference();
                    pref.setUser(user);
                    return notificationPreferenceRepository.save(pref);
                });
    }

}
