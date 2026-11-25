package org.example;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizRepository quizRepository = new QuizRepository();
            QuizService quizService = new QuizService(quizRepository);
            QuizFrame frame = new QuizFrame(quizService);
            frame.setVisible(true);
        });
    }
}