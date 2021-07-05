package com.doctorvee.multiplicationapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/multiplication/questions")
public class MultiplicationController {

    @Autowired
    MultiplicationService multiplicationService;

    @GetMapping("")
    public ResponseEntity<Response> displayTotalActivity(
            @RequestParam(required = false, defaultValue = "easy") String difficulty,
            @RequestParam(required = false, defaultValue = "10") Integer noOfQuestions,
            @RequestParam(required = false, defaultValue = "4") Integer noOfAnswers) throws Exception {

        return ResponseEntity.ok(multiplicationService.generateQuestions(difficulty, noOfQuestions, noOfAnswers));
    }
}
