package com.recruvia.backend.service.auth.impl;

import com.recruvia.backend.common.constants.ErrorMessages;
import com.recruvia.backend.dto.auth.AuthResponse;
import com.recruvia.backend.dto.auth.ForgotPasswordRequest;
import com.recruvia.backend.dto.auth.LoginRequest;
import com.recruvia.backend.dto.auth.RefreshTokenRequest;
import com.recruvia.backend.dto.auth.RegisterRequest;
import com.recruvia.backend.dto.auth.ResetPasswordRequest;
import com.recruvia.backend.dto.auth.VerifyEmailRequest;
import com.recruvia.backend.entity.EmailVerificationToken;
import com.recruvia.backend.entity.PasswordResetToken;
import com.recruvia.backend.entity.RefreshToken;
import com.recruvia.backend.entity.Role;
import com.recruvia.backend.entity.User;
import com.recruvia.backend.enums.AccountStatus;
import com.recruvia.backend.exception.BadRequestException;
import com.recruvia.backend.exception.InvalidTokenException;
import com.recruvia.backend.exception.ResourceNotFoundException;
import com.recruvia.backend.mapper.AuthMapper;
import com.recruvia.backend.mapper.UserMapper;
import com.recruvia.backend.repository.EmailVerificationTokenRepository;
import com.recruvia.backend.repository.PasswordResetTokenRepository;
import com.recruvia.backend.repository.RefreshTokenRepository;
import com.recruvia.backend.repository.RoleRepository;
import com.recruvia.backend.repository.UserRepository;
import com.recruvia.backend.security.CustomUserDetails;
import com.recruvia.backend.security.JwtService;
import com.recruvia.backend.service.auth.AuthService;
import com.recruvia.backend.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "CANDIDATE";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final MailService mailService;

    private final AuthMapper authMapper;
    private final UserMapper userMapper;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private String generateRefreshToken() {

        byte[] randomBytes = new byte[64];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private Role getDefaultRole() {

        return roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorMessages.ROLE_NOT_FOUND + ": " + DEFAULT_ROLE
                        )
                );
    }

    private CustomUserDetails buildUserDetails(User user) {

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getEmailVerified(),
                user.getRole().getName()
        );
    }

    private RefreshToken createOrUpdateRefreshToken(User user) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByUser(user)
                .orElseGet(RefreshToken::new);

        refreshToken.setUser(user);
        refreshToken.setToken(generateRefreshToken());
        refreshToken.setExpiresAt(
                LocalDateTime.now().plus(java.time.Duration.ofMillis(refreshTokenExpiration))
        );
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    private EmailVerificationToken createOrUpdateEmailVerificationToken(User user) {

        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByUser(user)
                .orElseGet(EmailVerificationToken::new);

        verificationToken.setUser(user);
        verificationToken.setToken(UUID.randomUUID().toString());
        // Verification token expires in 24 hours
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(24));

        return emailVerificationTokenRepository.save(verificationToken);
    }

    private PasswordResetToken createOrUpdatePasswordResetToken(User user) {

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByUser(user)
                .orElseGet(PasswordResetToken::new);

        resetToken.setUser(user);
        resetToken.setToken(UUID.randomUUID().toString());
        // Reset token expires in 2 hours
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(2));

        return passwordResetTokenRepository.save(resetToken);
    }

    private AuthResponse buildAuthResponse(
            User user,
            RefreshToken refreshToken
    ) {

        String accessToken = jwtService.generateToken(
                buildUserDetails(user)
        );

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtService.getAccessTokenExpiration()
        );
    }

    private RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new InvalidTokenException(ErrorMessages.INVALID_REFRESH_TOKEN)
                );

        if (Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new InvalidTokenException("Refresh token has been revoked.");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException(ErrorMessages.REFRESH_TOKEN_EXPIRED);
        }

        return refreshToken;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }

        Role role = getDefaultRole();

        User user = authMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        user.setRole(role);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        // Generate email verification token and send verification email
        EmailVerificationToken verificationToken = createOrUpdateEmailVerificationToken(savedUser);
        mailService.sendVerificationEmail(savedUser.getEmail(), verificationToken.getToken());

        RefreshToken refreshToken = createOrUpdateRefreshToken(savedUser);

        return buildAuthResponse(savedUser, refreshToken);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND)
                );

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Your account is not active.");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new BadRequestException(ErrorMessages.ACCOUNT_NOT_VERIFIED);
        }

        user.setLastLogin(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        RefreshToken refreshToken = createOrUpdateRefreshToken(updatedUser);

        return buildAuthResponse(updatedUser, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = validateRefreshToken(
                request.refreshToken()
        );

        User user = refreshToken.getUser();

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Your account is not active.");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new BadRequestException(ErrorMessages.ACCOUNT_NOT_VERIFIED);
        }

        RefreshToken updatedRefreshToken = createOrUpdateRefreshToken(user);

        return buildAuthResponse(user, updatedRefreshToken);
    }

    @Override
    public void logout() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BadRequestException("No authenticated user found.");
        }

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND)
                );

        refreshTokenRepository.deleteByUser(user);

        SecurityContextHolder.clearContext();
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {

        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByToken(request.token())
                .orElseThrow(() ->
                        new InvalidTokenException("Invalid verification token.")
                );

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Verification token has expired.");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        emailVerificationTokenRepository.delete(verificationToken);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND)
                );

        PasswordResetToken resetToken = createOrUpdatePasswordResetToken(user);
        mailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(request.token())
                .orElseThrow(() ->
                        new InvalidTokenException(ErrorMessages.INVALID_RESET_TOKEN)
                );

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException(ErrorMessages.RESET_TOKEN_EXPIRED);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}