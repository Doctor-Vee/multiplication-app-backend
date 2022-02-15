package com.doctorvee.multiplicationapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class IncorrectAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(targetEntity = Question.class, fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", referencedColumnName = "id", updatable = false, nullable = false)
    private Question question;

    @Column(name = "answer")
    private Integer answer;

    public IncorrectAnswer(Question question, Integer answer) {
        this.question = question;
        this.answer = answer;
    }
}
