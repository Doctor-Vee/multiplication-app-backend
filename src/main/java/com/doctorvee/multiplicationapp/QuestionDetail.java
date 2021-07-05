package com.doctorvee.multiplicationapp;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDetail {

    private String question;
    private String difficulty;
    private Integer correctAnswer;
    private List<Integer> incorrectAnswers;

}
