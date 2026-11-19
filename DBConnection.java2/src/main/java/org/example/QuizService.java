package org.example;


public class QuizService {


    public int calculateScore(int correctAnswers, int totalQuestions) {
        if (totalQuestions <= 0) {
            return 0;
        }
        if (correctAnswers < 0) {
            throw new IllegalArgumentException("Doğru cevap sayısı negatif olamaz.");
        }
        if (correctAnswers > totalQuestions) {
            throw new IllegalArgumentException("Doğru sayısı toplam soru sayısını aşamaz.");
        }

        double score = ((double) correctAnswers / totalQuestions) * 100.0;
        return (int) Math.round(score);
    }

    public boolean isAnswerCorrect(String selectedOption, String correctOption) {
        if (selectedOption == null || correctOption == null) {
            return false;
        }
        return selectedOption.trim().equalsIgnoreCase(correctOption.trim());
    }
}