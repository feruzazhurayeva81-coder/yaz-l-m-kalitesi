package org.example;

public class Question {
    private final int id;
    private final String language;   // "TR" veya "EN"
    private final String department; // Bölüm adı
    private final int grade;         // 1, 2, 3, 4
    private final String course;     // Ders adı (opsiyonel/filtre için)
    private final String questionText;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final String correctAnswer; // "A", "B", "C", "D"

    public Question(int id, String language, String department, int grade, String course,
                    String questionText, String optionA, String optionB,
                    String optionC, String optionD, String correctAnswer) {
        this.id = id;
        this.language = language;
        this.department = department;
        this.grade = grade;
        this.course = course;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    public int getId() { return id; }
    public String getLanguage() { return language; }
    public String getDepartment() { return department; }
    public int getGrade() { return grade; }
    public String getCourse() { return course; }
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
}