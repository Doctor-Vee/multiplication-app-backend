package com.doctorvee.multiplicationapp.entity;

import com.doctorvee.multiplicationapp.dto.QuestionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "question")
    @ApiModelProperty(notes = "The question itself")
    private String question;

    @Column(name = "difficulty")
    @ApiModelProperty(notes = "The difficulty of the given question")
    private String difficulty;

    @Column(name = "correct_answer")
    @ApiModelProperty(notes = "The correct answer to the question")
    private Integer correctAnswer;

    @OneToMany(targetEntity = IncorrectAnswer.class, fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "Other options different from the correct answer")
    private Set<IncorrectAnswer> incorrectAnswers = new HashSet<>();

    public Question(QuestionDTO questionDTO) {
        this.question = questionDTO.getQuestion();
        this.difficulty = questionDTO.getDifficulty();
        this.correctAnswer = questionDTO.getCorrectAnswer();
        questionDTO.getIncorrectAnswers().forEach(incorrectAnswer -> {
            this.incorrectAnswers.add(new IncorrectAnswer(this, incorrectAnswer));
        });
    }
}
