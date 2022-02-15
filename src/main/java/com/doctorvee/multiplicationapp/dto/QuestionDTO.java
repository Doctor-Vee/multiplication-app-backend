package com.doctorvee.multiplicationapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "The necessary fields to be returned in the question object")
public class QuestionDTO {

    @ApiModelProperty(notes = "The question itself")
    private String question;

    @ApiModelProperty(notes = "The difficulty of the given question")
    private String difficulty;

    @ApiModelProperty(notes = "The correct answer to the question")
    private Integer correctAnswer;

    @ApiModelProperty(notes = "Other options different from the correct answer")
    private List<Integer> incorrectAnswers = new ArrayList<>();

}
