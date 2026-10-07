package com.techvora.controller;

import com.techvora.dto.InterviewQuestionResponse;
import com.techvora.entity.InterviewQuestion;
import com.techvora.repository.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class PublicInterviewController {

    private final InterviewQuestionRepository interviewQuestionRepository;

    @GetMapping
    public ResponseEntity<List<InterviewQuestionResponse>> getAllQuestions() {
        List<InterviewQuestionResponse> responses = interviewQuestionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private InterviewQuestionResponse mapToResponse(InterviewQuestion question) {
        return InterviewQuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .slug(question.getSlug())
                .questionContent(question.getQuestionContent())
                .answerContent(question.getAnswerContent())
                .difficultyLevel(question.getDifficultyLevel())
                .build();
    }
}
