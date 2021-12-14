package com.doctorvee.multiplicationapp;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        int upperMultiplierBound = noOfAnswers + 16;
        for(int i = 1; i < noOfAnswers; i++){
            int multiplier = rand.nextInt()%upperMultiplierBound;
            int numberToAdd = correctAnswer + multiplier * 10;
            if(numberToAdd != correctAnswer && !incorrectAnswers.contains(numberToAdd) && numberToAdd > 0){
                incorrectAnswers.add(numberToAdd);
            } else {
                i--;
            }
        }
        return incorrectAnswers;
    }

    public void downloadQuestions(HttpServletResponse servletResponse, String difficulty, Integer noOfQuestions, Integer noOfAnswers) {

        String fileName = "multiplicationQuestions.xls";

        try{
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("multiplicationQuestions");
            int rowCount = 0;
            int cellCount = 0;

            Response response = generateQuestions(difficulty, noOfQuestions, noOfAnswers);

            HSSFRow titleRow = sheet.createRow((short) rowCount);
            titleRow.setHeightInPoints((short) 20);
            titleRow.createCell(cellCount++).setCellValue("No.");
            titleRow.createCell(cellCount++).setCellValue("Question");
            titleRow.createCell(cellCount++).setCellValue("Incorrect Answers");
            titleRow.createCell(cellCount++).setCellValue("Correct Answer");
            titleRow.createCell(cellCount).setCellValue("Difficulty");

            for (QuestionDetail detail: response.getResults()){
                rowCount++;
                cellCount = 0;
                HSSFRow row = sheet.createRow((short) rowCount);
                row.createCell(cellCount++).setCellValue(rowCount);
                row.createCell(cellCount++).setCellValue(detail.getQuestion());
                row.createCell(cellCount++).setCellValue(detail.getIncorrectAnswers().toString());
                row.createCell(cellCount++).setCellValue(detail.getCorrectAnswer());
                row.createCell(cellCount).setCellValue(detail.getDifficulty());
            }

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("The excel file has been generated!");
            processExcelSheetDownload(fileName, servletResponse);

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void processExcelSheetDownload(String fileName, HttpServletResponse response){
        try{
            File fileToDownload = new File(fileName);
            InputStream in = new FileInputStream(fileToDownload);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLength((int) fileToDownload.length());
            String headerValue = String.format("attachment; filename=\"%s\"", fileToDownload.getName());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[4096];

            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }
            in.close();
            outputStream.close();
            System.out.println("File downloaded at client successfully");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
