package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class QuizServiceTest {

    static class FakeQuizRepository extends QuizRepository {
        @Override
        public List<Question> findQuestions(String language, String department, int grade, String course) {
            return List.of(
                    new Question(1, language, department, grade, "Veri Yapıları",
                            "Stack hangi mantıkla çalışır?", "FIFO", "LIFO", "Random", "Queue", "B"),
                    new Question(2, language, department, grade, "Veritabanı",
                            "Primary Key ne işe yarar?", "Tekil kayıt", "Yedekleme", "Şifreleme", "Silme", "A")
            );
        }
    }

    @Test
    void shouldReturnFilteredQuestionsAndCalculateScore() {
        QuizService service = new QuizService(new FakeQuizRepository());

        List<Question> questions = service.getQuizQuestions("TR", "Yazılım Mühendisliği", 2, "TÜMÜ", false);
        assertEquals(2, questions.size());

        int score = service.calculateScore(questions, List.of("B", "A"));
        assertEquals(2, score);
    }

    @Test
    void shouldThrowExceptionForInvalidLanguage() {
        QuizService service = new QuizService(new FakeQuizRepository());

        assertThrows(IllegalArgumentException.class, () ->
                service.getQuizQuestions("DE", "Yazılım Mühendisliği", 1, "TÜMÜ", false));
    }
}