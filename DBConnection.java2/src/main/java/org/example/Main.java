package org.example;

import javax.swing.SwingUtilities;

/**
 * QuizFlow uygulamasının başlatıcı sınıfı.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizFrame().setVisible(true);
        });
    }
}