package com.awt.signin.signin.service;

import com.awt.signin.signin.entity.Quiz;
import com.awt.signin.signin.entity.QuizHistory;

import java.util.List;
import java.util.Map;

public interface QuizService {
    public Quiz createQuiz(Quiz quiz);
    public Quiz getQuizById(Long quizId);
    public Quiz updateQuiz(Quiz updatedQuiz);
    public void deleteQuiz(Long quizId);
    public List<Quiz> getAllQuizzes();
    public List<Quiz> getAllQuizzesBySubSectionId(Long subSectionId);
    public QuizHistory submitQuiz(Long quizId, Long userId, Map<Long, Integer> questionToOptionMap);
}
