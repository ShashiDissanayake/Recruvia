package com.recruvia.backend.service.mail;

public interface MailService {

    void sendVerificationEmail(String email, String token);

    void sendPasswordResetEmail(String email, String token);

}
