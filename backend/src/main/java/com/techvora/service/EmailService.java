package com.techvora.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetOtp(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Techvora - Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + "\n\nIt is valid for 10 minutes.\nIf you did not request this, please ignore this email.");
        message.setFrom("trainerhub90@gmail.com");

        mailSender.send(message);
        
        System.out.println("OTP Email successfully sent to " + to);
    }
}
