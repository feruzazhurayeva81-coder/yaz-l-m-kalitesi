package org.example;

import java.util.Collections;
import java.util.List;

public class QuizService {
    private final QuizRepository repository;

    public QuizService(QuizRepository repository) {
        this.repository = repository;
    }

    public List<Question> getQuizQuestions(String language, String department, int grade, String course, boolean shuffle) {
        if (language == null || (!language.equals("TR") && !language.equals("EN"))) {
            throw new IllegalArgumentException("Dil 'TR' veya 'EN' olmalıdır.");
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Bölüm seçilmelidir.");
        }
        if (grade < 1 || grade > 4) {
            throw new IllegalArgumentException("Sınıf 1 ile 4 arasında olmalıdır.");
        }

        List<Question> questions = repository.findQuestions(language, department, grade, course);
        if (shuffle) {
            Collections.shuffle(questions);
        }
        return questions;
    }

    public int calculateScore(List<Question> questions, List<String> userAnswers) {
        int score = 0;
        int limit = Math.min(questions.size(), userAnswers.size());
        for (int i = 0; i < limit; i++) {
            String correct = questions.get(i).getCorrectAnswer();
            String given = userAnswers.get(i);
            if (correct != null && correct.equalsIgnoreCase(given)) {
                score++;
            }
        }
        return score;
    }
}