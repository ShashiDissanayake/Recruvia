package com.recruvia.backend.service.mail.impl;

import com.recruvia.backend.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Override
    public void sendVerificationEmail(String email, String token) {
        log.info("--------------------------------------------------");
        log.info("SENDING EMAIL VERIFICATION EMAIL");
        log.info("To: {}", email);
        log.info("Verification Link: http://localhost:5173/verify-email?token={}", token);
        log.info("--------------------------------------------------");
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        log.info("--------------------------------------------------");
        log.info("SENDING PASSWORD RESET EMAIL");
        log.info("To: {}", email);
        log.info("Reset Link: http://localhost:5173/reset-password?token={}", token);
        log.info("--------------------------------------------------");
    }
}
