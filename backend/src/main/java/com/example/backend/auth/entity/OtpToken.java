package com.example.backend.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens")
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used")
    private boolean used = false;

    // Constructor used by Builder
    private OtpToken(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.otp = builder.otp;
        this.expiresAt = builder.expiresAt;
        this.used = builder.used;
    }

    public OtpToken() {
    }

    // Expiry check
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String email;
        private String otp;
        private LocalDateTime expiresAt;
        private boolean used;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder otp(String otp) {
            this.otp = otp;
            return this;
        }

        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder used(boolean used) {
            this.used = used;
            return this;
        }

        public OtpToken build() {
            return new OtpToken(this);
        }
    }
}
