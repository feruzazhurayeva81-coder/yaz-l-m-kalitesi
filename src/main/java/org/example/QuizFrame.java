package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizFrame extends JFrame {
    private final QuizService quizService;
    private final QuizResultRepository resultRepository = new QuizResultRepository();

    // Renk Paleti (Modern Koyu Lacivert & Turkuaz/Amber)
    private static final Color BG_MAIN = new Color(18, 24, 38);
    private static final Color BG_PANEL = new Color(28, 37, 58);
    private static final Color TEXT_LIGHT = new Color(241, 245, 249);
    private static final Color ACCENT_BLUE = new Color(56, 189, 248);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_AMBER = new Color(245, 158, 11);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color INPUT_BG = new Color(255, 255, 255);
    private static final Color INPUT_FG = new Color(15, 23, 42);

    private final JTextField studentNameField = new JTextField();
    private final JComboBox<String> langBox = new JComboBox<>(new String[]{"TR", "EN"});
    private final JComboBox<String> deptBox = new JComboBox<>();
    private final JComboBox<Integer> gradeBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
    private final JComboBox<String> courseBox = new JComboBox<>();

    private final JLabel questionLabel = new JLabel(
            "Lütfen yukarıdan adınızı ve filtreleri seçip 'Sınavı Başlat' butonuna basın."
    );
    private final JRadioButton optA = new JRadioButton();
    private final JRadioButton optB = new JRadioButton();
    private final JRadioButton optC = new JRadioButton();
    private final JRadioButton optD = new JRadioButton();
    private final ButtonGroup optionGroup = new ButtonGroup();
    private final JLabel feedbackLabel = new JLabel(" ");

    private final JButton startBtn = new JButton("Sınavı Başlat / Start Quiz");
    private final JButton finishBtn = new JButton("Sınavı Bitir / Finish");
    private final JButton nextBtn = new JButton("Sonraki / Next");
    private final JButton exitBtn = new JButton("Çık / Exit");

    private final Map<String, List<String>> trDeptCourses = Map.of(
            "Yazılım Mühendisliği", List.of("TÜMÜ", "Programlamaya Giriş", "Veri Yapıları", "Yazılım Kalitesi", "Veritabanı"),
            "Bilgisayar Mühendisliği", List.of("TÜMÜ", "Algoritmalar", "Bilgisayar Mimarisi", "İşletim Sistemleri")
    );

    private final Map<String, List<String>> enDeptCourses = Map.of(
            "Software Engineering", List.of("ALL", "Intro to Programming", "Data Structures", "Software Quality", "Database Systems"),
            "Computer Engineering", List.of("ALL", "Algorithms", "Computer Architecture", "Operating Systems")
    );

    private List<Question> currentQuestions = new ArrayList<>();
    private List<String> userAnswers = new ArrayList<>();
    private int currentIndex = 0;

    public QuizFrame(QuizService quizService) {
        this.quizService = quizService;
        setTitle("QuizFlow - Online Mini Quiz");
        setSize(860, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);

        optionGroup.add(optA);
        optionGroup.add(optB);
        optionGroup.add(optC);
        optionGroup.add(optD);

        JPanel topPanel = new JPanel(new GridLayout(2, 5, 10, 8));
        topPanel.setBackground(BG_PANEL);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 12, 10, 12),
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1)
        ));

        addStyledLabel(topPanel, "Öğrenci Adı:");
        addStyledLabel(topPanel, "Dil / Language:");
        addStyledLabel(topPanel, "Bölüm / Department:");
        addStyledLabel(topPanel, "Sınıf / Grade:");
        addStyledLabel(topPanel, "Ders / Course:");

        styleInput(studentNameField);
        studentNameField.setToolTipText("Ad Soyad girin");
        styleCombo(langBox);
        styleCombo(deptBox);
        styleCombo(gradeBox);
        styleCombo(courseBox);

        topPanel.add(studentNameField);
        topPanel.add(langBox);
        topPanel.add(deptBox);
        topPanel.add(gradeBox);
        topPanel.add(courseBox);

        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 8, 8));
        centerPanel.setBackground(BG_MAIN);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));

        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        questionLabel.setForeground(ACCENT_BLUE);

        styleRadio(optA);
        styleRadio(optB);
        styleRadio(optC);
        styleRadio(optD);

        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        feedbackLabel.setForeground(TEXT_LIGHT);

        centerPanel.add(questionLabel);
        centerPanel.add(optA);
        centerPanel.add(optB);
        centerPanel.add(optC);
        centerPanel.add(optD);
        centerPanel.add(feedbackLabel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        bottomPanel.setBackground(BG_PANEL);

        styleButton(startBtn, ACCENT_GREEN, Color.BLACK);
        styleButton(finishBtn, ACCENT_AMBER, Color.BLACK);
        styleButton(nextBtn, ACCENT_BLUE, Color.BLACK);
        styleButton(exitBtn, ACCENT_RED, Color.WHITE);

        finishBtn.setEnabled(false);
        nextBtn.setEnabled(false);

        bottomPanel.add(startBtn);
        bottomPanel.add(finishBtn);
        bottomPanel.add(nextBtn);
        bottomPanel.add(exitBtn);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        ActionListener checkAnswerListener = e -> showInstantFeedback();
        optA.addActionListener(checkAnswerListener);
        optB.addActionListener(checkAnswerListener);
        optC.addActionListener(checkAnswerListener);
        optD.addActionListener(checkAnswerListener);

        langBox.addActionListener(e -> refreshDepartments());
        deptBox.addActionListener(e -> refreshCourses());
        startBtn.addActionListener(e -> startQuiz());
        finishBtn.addActionListener(e -> finishQuiz());
        nextBtn.addActionListener(e -> handleNext());
        exitBtn.addActionListener(e -> exitApplication());

        refreshDepartments();
    }

    private void showInstantFeedback() {
        if (currentQuestions == null || currentIndex >= currentQuestions.size()) return;

        String selected = getSelectedOption();
        if (selected.isEmpty()) {
            feedbackLabel.setText(" ");
            return;
        }

        String correct = currentQuestions.get(currentIndex).getCorrectAnswer();
        if (selected.equalsIgnoreCase(correct)) {
            feedbackLabel.setText("✓ Doğru! / Correct! (" + correct + ")");
            feedbackLabel.setForeground(ACCENT_GREEN);
        } else {
            feedbackLabel.setText("✕ Yanlış! / Wrong! Doğru cevap: " + correct);
            feedbackLabel.setForeground(ACCENT_RED);
        }
    }

    private String getSelectedOption() {
        return optA.isSelected() ? "A" :
                optB.isSelected() ? "B" :
                        optC.isSelected() ? "C" :
                                optD.isSelected() ? "D" : "";
    }

    private void addStyledLabel(JPanel panel, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_LIGHT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(lbl);
    }

    private void styleInput(JTextField field) {
        field.setBackground(INPUT_BG);
        field.setForeground(INPUT_FG);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private <T> void styleCombo(JComboBox<T> box) {
        box.setBackground(INPUT_BG);
        box.setForeground(INPUT_FG);
        box.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private void styleRadio(JRadioButton rb) {
        rb.setBackground(BG_MAIN);
        rb.setForeground(TEXT_LIGHT);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rb.setFocusPainted(false);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
    }

    private void refreshDepartments() {
        String lang = (String) langBox.getSelectedItem();
        deptBox.removeAllItems();
        Map<String, List<String>> map = "EN".equals(lang) ? enDeptCourses : trDeptCourses;
        for (String dept : map.keySet()) {
            deptBox.addItem(dept);
        }
        refreshCourses();
    }

    private void refreshCourses() {
        String lang = (String) langBox.getSelectedItem();
        String dept = (String) deptBox.getSelectedItem();
        courseBox.removeAllItems();
        if (dept == null) return;
        Map<String, List<String>> map = "EN".equals(lang) ? enDeptCourses : trDeptCourses;
        for (String c : map.getOrDefault(dept, List.of())) {
            courseBox.addItem(c);
        }
    }

    private void startQuiz() {
        String studentName = studentNameField.getText().trim();
        if (studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen önce öğrenci adını girin.");
            return;
        }

        String lang = (String) langBox.getSelectedItem();
        String dept = (String) deptBox.getSelectedItem();
        Integer grade = (Integer) gradeBox.getSelectedItem();
        String course = (String) courseBox.getSelectedItem();

        try {
            currentQuestions = quizService.getQuizQuestions(lang, dept, grade != null ? grade : 1, course, true);
            userAnswers = new ArrayList<>();
            currentIndex = 0;

            if (currentQuestions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seçilen filtreye uygun soru bulunamadı.");
                finishBtn.setEnabled(false);
                nextBtn.setEnabled(false);
                return;
            }
            finishBtn.setEnabled(true);
            nextBtn.setEnabled(true);
            showQuestion(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showQuestion(int index) {
        Question q = currentQuestions.get(index);
        questionLabel.setText((index + 1) + "/" + currentQuestions.size() + " - " + q.getQuestionText());
        optA.setText("A) " + q.getOptionA());
        optB.setText("B) " + q.getOptionB());
        optC.setText("C) " + q.getOptionC());
        optD.setText("D) " + q.getOptionD());
        optionGroup.clearSelection();
        feedbackLabel.setText(" ");
    }

    private void handleNext() {
        String selected = getSelectedOption();
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen bir şık seçin.");
            return;
        }

        userAnswers.add(selected);
        currentIndex++;

        if (currentIndex < currentQuestions.size()) {
            showQuestion(currentIndex);
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        if (currentQuestions == null || currentQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aktif bir sınav bulunmuyor.");
            return;
        }

        // Aktif soruda şık seçildiyse ve henüz listeye eklenmediyse kaydet
        if (currentIndex < currentQuestions.size() && userAnswers.size() == currentIndex) {
            String selected = getSelectedOption();
            if (!selected.isEmpty()) {
                userAnswers.add(selected);
            }
        }

        int score = quizService.calculateScore(currentQuestions, userAnswers);
        finishBtn.setEnabled(false);
        nextBtn.setEnabled(false);

        String studentName = studentNameField.getText().trim();
        String lang = (String) langBox.getSelectedItem();
        String dept = (String) deptBox.getSelectedItem();
        int grade = gradeBox.getSelectedItem() != null ? (Integer) gradeBox.getSelectedItem() : 1;
        String course = (String) courseBox.getSelectedItem();

        String dbStatusMessage;
        try {
            resultRepository.saveResult(
                    studentName,
                    lang != null ? lang : "TR",
                    dept != null ? dept : "",
                    grade,
                    course != null ? course : "",
                    score,
                    currentQuestions.size()
            );
            dbStatusMessage = "\nSonuç veritabanına kaydedildi.";
        } catch (Exception ex) {
            dbStatusMessage = "\nKayıt uyarısı: " + ex.getMessage();
        }

        JOptionPane.showMessageDialog(
                this,
                "Öğrenci: " + studentName +
                        "\nYanıtlanan Soru: " + userAnswers.size() + " / " + currentQuestions.size() +
                        "\nSkor: " + score + " / " + currentQuestions.size() +
                        dbStatusMessage,
                "Sınav Sonucu",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Uygulamadan çıkmak istediğinize emin misiniz?",
                "Çıkış / Exit",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}
