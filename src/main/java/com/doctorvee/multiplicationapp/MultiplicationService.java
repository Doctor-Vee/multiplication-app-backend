package com.doctorvee.multiplicationapp;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MultiplicationService {
    Random rand = new Random();

    public Response generateQuestions(String difficulty, int noOfQuestions, int noOfAnswers) throws Exception {
        if (noOfQuestions <= 0) {
            throw new Exception("Invalid number of Questions");
        }
        if (noOfAnswers <= 1){
            throw new Exception("Invalid number of Answers");
        }
        int lowerLimit;
        switch(difficulty) {
            case "easy":
                lowerLimit = 3;
                break;
            case "medium":
                lowerLimit = 11;
                break;
            case "hard":
                lowerLimit = 21;
                break;
            default:
                throw new Exception("Invalid difficulty set");
        }
        Response response = new Response();
        List<QuestionDetail> questionDetailList = new ArrayList<>();
        for (int i = 0; i < noOfQuestions; i++) {
            QuestionDetail questionDetail = new QuestionDetail();
            int number1 = rand.nextInt(16) + lowerLimit;
            int number2 = rand.nextInt(16) + lowerLimit;
            questionDetail.setQuestion(number1 + " x " + number2);
            int correctAnswer = number1 * number2;
            questionDetail.setCorrectAnswer(correctAnswer);
            questionDetail.setDifficulty(difficulty);
            questionDetail.setIncorrectAnswers(generateWrongAnswers(correctAnswer, noOfAnswers, lowerLimit));
            questionDetailList.add(questionDetail);
        }
        response.setResults(questionDetailList);
        return response;
    }

    private List<Integer> generateWrongAnswers(Integer correctAnswer, int noOfAnswers, int lowerLimit){
        List<Integer> incorrectAnswers = new ArrayList<>();
        for(int i = 1; i < noOfAnswers; i++){
            int number1 = rand.nextInt(16) + lowerLimit;
            int number2 = rand.nextInt(16) + lowerLimit;
            int numberToAdd = number1 * number2;
            if(numberToAdd != correctAnswer){
                incorrectAnswers.add(numberToAdd);
            } else {
                i--;
            }
        }
        return incorrectAnswers;
    }
}
