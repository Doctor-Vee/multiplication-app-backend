package com.doctorvee.multiplicationapp.repository;

import com.doctorvee.multiplicationapp.entity.IncorrectAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncorrectAnswerRepository extends JpaRepository<IncorrectAnswer, Long> {
}
