package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class QuizResultRepository {

    public void saveResult(
            String studentName,
            String language,
            String department,
            int grade,
            String course,
            int score,
            int totalQuestions
    ) throws SQLException {
        String sql = """
                INSERT INTO quiz_results
                (student_name, language, department, grade, course, score, total_questions)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentName);
            pstmt.setString(2, language);
            pstmt.setString(3, department);
            pstmt.setInt(4, grade);
            pstmt.setString(5, course);
            pstmt.setInt(6, score);
            pstmt.setInt(7, totalQuestions);
            pstmt.executeUpdate();
        }
    }
}