package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuizServiceTest {

    private QuizService service;

    @BeforeEach
    void setUp() {
        service = new QuizService();
    }

    @Test
    @DisplayName("Tam puan testi (4 soruda 4 doğru 100 puan)")
    void testCalculateScore_FullScore() {
        int score = service.calculateScore(4, 4);
        assertEquals(100, score);
    }

    @Test
    @DisplayName("Sıfır puan testi")
    void testCalculateScore_ZeroScore() {
        int score = service.calculateScore(0, 4);
        assertEquals(0, score);
    }

    @Test
    @DisplayName("Cevap kontrol testi")
    void testIsAnswerCorrect() {
        assertTrue(service.isAnswerCorrect("A", "A"));
        assertTrue(service.isAnswerCorrect("b", "B"));
        assertFalse(service.isAnswerCorrect("C", "D"));
    }
}
