package com.recruvia.backend.service.auth;

import com.recruvia.backend.dto.auth.AuthResponse;
import com.recruvia.backend.dto.auth.ForgotPasswordRequest;
import com.recruvia.backend.dto.auth.LoginRequest;
import com.recruvia.backend.dto.auth.RefreshTokenRequest;
import com.recruvia.backend.dto.auth.RegisterRequest;
import com.recruvia.backend.dto.auth.ResetPasswordRequest;
import com.recruvia.backend.dto.auth.VerifyEmailRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout();

    void verifyEmail(VerifyEmailRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}