package com.techvora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewQuestionResponse {
    private UUID id;
    private String title;
    private String slug;
    private String questionContent;
    private String answerContent;
    private String difficultyLevel;
}
