package com.techvora.controller;

import com.techvora.dto.NewsletterRequest;
import com.techvora.entity.NewsletterSubscriber;
import com.techvora.repository.NewsletterSubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterSubscriberRepository newsletterRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody NewsletterRequest request) {
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
        
        if (newsletterRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already subscribed.");
        }
        
        NewsletterSubscriber subscriber = NewsletterSubscriber.builder()
                .email(request.getEmail())
                .isActive(true)
                .build();
                
        newsletterRepository.save(subscriber);
        return ResponseEntity.ok("Successfully subscribed to newsletter.");
    }
}
