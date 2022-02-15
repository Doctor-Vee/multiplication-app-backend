package com.doctorvee.multiplicationapp.controller;

import com.doctorvee.multiplicationapp.dto.DownloadableResource;
import com.doctorvee.multiplicationapp.dto.HttpResponseBuilder;
import com.doctorvee.multiplicationapp.dto.Response;
import com.doctorvee.multiplicationapp.service.MultiplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/multiplication/questions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Api(tags = {"Multiplication Controller Endpoint"})
public class MultiplicationController {


    private final HttpResponseBuilder httpResponseBuilder;
    private final MultiplicationService multiplicationService;

    @GetMapping("")
    @ApiOperation(value = "Generate multiplication questions",
            notes = "Supply the parameters as you want or leave them blank and it will use default values")
    public ResponseEntity<Response> generateMultiplicationQuestions(
            @RequestParam(required = false, defaultValue = "easy") String difficulty,
            @RequestParam(required = false, defaultValue = "10") Integer noOfQuestions,
            @ApiParam(value = "The total number of options to be returned")
            @RequestParam(required = false, defaultValue = "4") Integer noOfAnswers) throws Exception {

        return ResponseEntity.ok(multiplicationService.generateQuestions(difficulty, noOfQuestions, noOfAnswers));
    }

    @GetMapping("/database")
    @ApiOperation(value = "Get multiplication questions from database",
            notes = "Supply the parameters as you want or leave them blank and it will use default values")
    public ResponseEntity<Response> getMultiplicationsFromDatabase() throws Exception {

        return ResponseEntity.ok(multiplicationService.getQuestionsFromDatabase());
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "Download multiplication questions")
    public ResponseEntity<Resource> downloadQuestionsUsingFastExcel(
            @RequestParam(required = false, defaultValue = "10") Integer noOfQuestions,
            @ApiParam(value = "The total number of options to be returned")
            @RequestParam(required = false, defaultValue = "4") Integer noOfAnswers) {

        DownloadableResource resource = multiplicationService.downloadQuestions(noOfQuestions, noOfAnswers);

        return httpResponseBuilder.fromDownloadableResource(resource);

    }

}
