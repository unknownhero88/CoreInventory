package com.example.backend.auth;

import com.example.backend.auth.dto.AuthResponse;
import com.example.backend.auth.dto.LoginRequest;
import com.example.backend.auth.dto.RegisterRequest;
import com.example.backend.auth.service.AuthService;
import com.example.backend.auth.service.OtpService;
import com.example.backend.config.JwtService;
import com.example.backend.user.entity.User;
import com.example.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock PasswordEncoder       passwordEncoder;
    @Mock
    JwtService jwtService;
    @Mock AuthenticationManager authenticationManager;
    @Mock
    OtpService otpService;

    @InjectMocks
    AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole("INVENTORY_MANAGER");
    }

    @Test
    void register_success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse res = authService.register(registerRequest);

        assertThat(res.getAccessToken()).isEqualTo("jwt-token");
        assertThat(res.getEmail()).isEqualTo("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throwsException() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already registered");
    }

    @Test
    void login_success() {
        User user = User.builder()
                .id(1L).name("Test User").email("test@example.com")
                .password("hashed").role(User.Role.INVENTORY_MANAGER).build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        LoginRequest req = new LoginRequest();
        req.setEmail("test@example.com");
        req.setPassword("password123");

        AuthResponse res = authService.login(req);

        assertThat(res.getAccessToken()).isEqualTo("jwt-token");
        assertThat(res.getUserId()).isEqualTo(1L);
    }

    @Test
    void requestOtp_unknownEmail_doesNotThrow() {
        when(userRepository.existsByEmail("unknown@example.com")).thenReturn(false);
        assertThatNoException().isThrownBy(() -> authService.requestOtp("unknown@example.com"));
        verify(otpService, never()).generateOtp(anyString());
    }
}

