package com.techvora.service;

import com.techvora.dto.AuthenticationRequest;
import com.techvora.dto.AuthenticationResponse;
import com.techvora.dto.RegisterRequest;
import com.techvora.entity.Role;
import com.techvora.entity.User;
import com.techvora.repository.UserRepository;
import com.techvora.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.techvora.entity.PasswordResetOtp;
import com.techvora.repository.PasswordResetOtpRepository;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetOtpRepository otpRepository;
    private final EmailService emailService;
    
    @org.springframework.beans.factory.annotation.Value("${otp.expiry.minutes:10}")
    private int otpExpiryMinutes;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();
        repository.save(user);
        var extraClaims = new java.util.HashMap<String, Object>();
        extraClaims.put("role", user.getRole().name());
        var jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
                
        if (request.getRole() != null && !user.getRole().name().equalsIgnoreCase(request.getRole())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Access Denied: Incorrect role selected.");
        }
        
        var extraClaims = new java.util.HashMap<String, Object>();
        extraClaims.put("role", user.getRole().name());
        var jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void forgotPassword(String email) {
        var user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        
        String otp = String.format("%06d", new Random().nextInt(999999));
        var resetOtp = PasswordResetOtp.builder()
                .email(user.getEmail())
                .otp(otp)
                .expiryDate(LocalDateTime.now().plusMinutes(otpExpiryMinutes))
                .isUsed(false)
                .build();
                
        otpRepository.save(resetOtp);
        emailService.sendPasswordResetOtp(user.getEmail(), otp);
    }

    public boolean verifyOtp(String email, String otp) {
        var optRecord = otpRepository.findByEmailAndOtpAndIsUsedFalse(email, otp);
        if (optRecord.isEmpty()) return false;
        
        var record = optRecord.get();
        if (record.getExpiryDate().isBefore(LocalDateTime.now())) return false;
        
        return true;
    }

    public void resetPassword(String email, String otp, String newPassword) {
        var optRecord = otpRepository.findByEmailAndOtpAndIsUsedFalse(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));
                
        if (optRecord.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
        
        var user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
        
        optRecord.setUsed(true);
        otpRepository.save(optRecord);
    }
}
