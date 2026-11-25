package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizRepository {

    public List<Question> findQuestions(String language, String department, int grade, String course) {
        List<Question> list = new ArrayList<>();
        boolean filterByCourse = course != null && !course.isBlank() && !course.equals("ALL") && !course.equals("TÜMÜ");

        String sql = filterByCourse
                ? "SELECT id, language, department, grade, course, question_text, option_a, option_b, option_c, option_d, correct_option " +
                "FROM questions WHERE language = ? AND department = ? AND grade = ? AND course = ?"
                : "SELECT id, language, department, grade, course, question_text, option_a, option_b, option_c, option_d, correct_option " +
                "FROM questions WHERE language = ? AND department = ? AND grade = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, language);
            ps.setString(2, department);
            ps.setInt(3, grade);
            if (filterByCourse) {
                ps.setString(4, course);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Question(
                            rs.getInt("id"),
                            rs.getString("language"),
                            rs.getString("department"),
                            rs.getInt("grade"),
                            rs.getString("course"),
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_option") // <-- Buraya
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Sorular çekilirken hata oluştu: " + e.getMessage(), e);
        }
        return list;
    }
}