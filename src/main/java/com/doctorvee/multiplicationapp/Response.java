package com.doctorvee.multiplicationapp;

import lombok.Data;

import java.util.List;

@Data
public class Response {
    private List<QuestionDetail> results;
}
