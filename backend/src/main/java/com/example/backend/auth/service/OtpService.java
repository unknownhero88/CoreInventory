package com.example.backend.auth.service;




import com.example.backend.auth.Repository.OtpTokenRepository;
import com.example.backend.auth.entity.OtpToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service

public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private final OtpTokenRepository otpTokenRepository;

    public OtpService(OtpTokenRepository otpTokenRepository) {
        this.otpTokenRepository = otpTokenRepository;
    }

    private static final int OTP_EXPIRY_MINUTES = 10;

    @Transactional
    public String generateOtp(String email) {
        // Invalidate any existing OTPs for this email
        otpTokenRepository.deleteAllByEmail(email);

        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpToken token = OtpToken.builder()
                .email(email)
                .otp(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
                .used(false)
                .build();

        otpTokenRepository.save(token);

        // In production: send via email service (e.g. SendGrid, JavaMailSender)
        // For now, log it (remove in production!)
        log.info("OTP for {}: {}", email, otp);

        return otp;
    }

    @Transactional
    public boolean verifyOtp(String email, String otp) {
        OtpToken token = otpTokenRepository
                .findTopByEmailAndUsedFalseOrderByExpiresAtDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("No active OTP found for this email"));

        if (token.isExpired()) {
            throw new IllegalArgumentException("OTP has expired. Please request a new one.");
        }

        if (!token.getOtp().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP.");
        }

        token.setUsed(true);
        otpTokenRepository.save(token);
        return true;
    }


}
