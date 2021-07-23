package com.doctorvee.multiplicationapp;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "The results returned after the questions are generated")
public class Response {
    private List<QuestionDetail> results;
}
