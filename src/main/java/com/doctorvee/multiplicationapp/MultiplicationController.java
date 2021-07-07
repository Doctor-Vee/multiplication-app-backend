package com.doctorvee.multiplicationapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/multiplication/questions")
@CrossOrigin(origins = "*")
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
