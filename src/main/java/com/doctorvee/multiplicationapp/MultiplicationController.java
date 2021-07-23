package com.doctorvee.multiplicationapp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/multiplication/questions")
@CrossOrigin(origins = "*")
@Api(tags = {"Multiplication Controller Endpoint"})
public class MultiplicationController {

    @Autowired
    MultiplicationService multiplicationService;

    @GetMapping("")
    @ApiOperation(value = "Generate multiplication Questions",
            notes = "Supply the parameters as you want or leave them blank and it will use default values")
    public ResponseEntity<Response> generateMultiplicationQuestions(
            @RequestParam(required = false, defaultValue = "easy") String difficulty,
            @RequestParam(required = false, defaultValue = "10") Integer noOfQuestions,
            @ApiParam(value = "The total number of options to be returned")
            @RequestParam(required = false, defaultValue = "4") Integer noOfAnswers) throws Exception {

        return ResponseEntity.ok(multiplicationService.generateQuestions(difficulty, noOfQuestions, noOfAnswers));
    }
}
