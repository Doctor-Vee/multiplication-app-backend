package com.doctorvee.multiplicationapp.util;

import com.doctorvee.multiplicationapp.dto.QuestionDTO;
import com.doctorvee.multiplicationapp.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    public QuestionDTO convertQuestionToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestion(question.getQuestion());
        dto.setDifficulty(question.getDifficulty());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        question.getIncorrectAnswers().forEach(answer -> dto.getIncorrectAnswers().add(answer.getAnswer()));
        return dto;
    }
}
