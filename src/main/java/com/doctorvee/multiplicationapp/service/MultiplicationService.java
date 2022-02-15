package com.doctorvee.multiplicationapp.service;

import com.doctorvee.multiplicationapp.dto.DownloadableResource;
import com.doctorvee.multiplicationapp.dto.QuestionDTO;
import com.doctorvee.multiplicationapp.dto.Response;
import com.doctorvee.multiplicationapp.entity.Question;
import com.doctorvee.multiplicationapp.repository.QuestionRepository;
import com.doctorvee.multiplicationapp.util.Converter;
import lombok.RequiredArgsConstructor;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MultiplicationService {

    private final QuestionRepository questionRepository;
    private final Converter converter;

    Random rand = new Random();

    public Response generateQuestions(String difficulty, int noOfQuestions, int noOfAnswers) throws Exception {
        if (noOfQuestions <= 0) {
            throw new Exception("Invalid number of Questions");
        }
        if (noOfAnswers <= 1) {
            throw new Exception("Invalid number of Answers");
        }
        int lowerLimit;
        switch (difficulty) {
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
        List<QuestionDTO> questionDetailList = new ArrayList<>();
        for (int i = 0; i < noOfQuestions; i++) {
            QuestionDTO questionDTO = new QuestionDTO();
            int number1 = rand.nextInt(16) + lowerLimit;
            int number2 = rand.nextInt(16) + lowerLimit;
            questionDTO.setQuestion(number1 + " x " + number2);
            int correctAnswer = number1 * number2;
            questionDTO.setCorrectAnswer(correctAnswer);
            questionDTO.setDifficulty(difficulty);
            questionDTO.setIncorrectAnswers(generateWrongAnswers(correctAnswer, noOfAnswers, lowerLimit));
            questionDetailList.add(questionDTO);
            questionRepository.save(new Question(questionDTO));
        }
        response.setResults(questionDetailList);
        return response;
    }

    public Response getQuestionsFromDatabase() {
        Response response = new Response();
        response.setResults(questionRepository.findAll().stream().map(converter::convertQuestionToDTO)
                .collect(java.util.stream.Collectors.toList()));
        return response;
    }

    private List<Integer> generateWrongAnswers(Integer correctAnswer, int noOfAnswers, int lowerLimit) {
        List<Integer> incorrectAnswers = new ArrayList<>();
        int upperMultiplierBound = noOfAnswers + 16;
        for (int i = 1; i < noOfAnswers; i++) {
            int multiplier = rand.nextInt() % upperMultiplierBound;
            int numberToAdd = correctAnswer + multiplier * 10;
            if (numberToAdd != correctAnswer && !incorrectAnswers.contains(numberToAdd) && numberToAdd > 0) {
                incorrectAnswers.add(numberToAdd);
            } else {
                i--;
            }
        }
        return incorrectAnswers;
    }

    public DownloadableResource downloadQuestions(Integer noOfQuestions, Integer noOfAnswers) {
        List<String> difficulties = List.of("easy", "medium", "hard");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Workbook workbook = new Workbook(outputStream, "Multiplication App", "1.0");
            for (String difficulty : difficulties) {
                Response response = generateQuestions(difficulty, noOfQuestions, noOfAnswers);
                Worksheet ws = workbook.newWorksheet(String.format("%s Questions", difficulty.toUpperCase()));
                int rowCount = 0;
                int cellCount = 0;
                ws.value(rowCount, cellCount++, "No.");
                ws.value(rowCount, cellCount++, "Question");
                ws.value(rowCount, cellCount++, "Incorrect Answers");
                ws.value(rowCount, cellCount++, "Correct Answer");
                ws.value(rowCount, cellCount, "Difficulty");

                for (QuestionDTO detail : response.getResults()) {
                    rowCount++;
                    cellCount = 0;
                    ws.value(rowCount, cellCount++, rowCount);
                    ws.value(rowCount, cellCount++, detail.getQuestion());
                    ws.value(rowCount, cellCount++, detail.getIncorrectAnswers().toString());
                    ws.value(rowCount, cellCount++, detail.getCorrectAnswer());
                    ws.value(rowCount, cellCount, detail.getDifficulty());
                }
            }
            workbook.finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new DownloadableResource("multiplicationQuestions.xlsx", outputStream.toByteArray());
    }
}
