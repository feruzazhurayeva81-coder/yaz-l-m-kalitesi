package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * QuizFlow uygulamasının görsel arayüz sınıfı.
 */
public class QuizFrame extends JFrame {

    private QuizRepository repository = new QuizRepository();
    private QuizService service = new QuizService();

    private int userId;
    private String currentUsername;
    private List<Question> questionList;
    private int currentIndex = 0;
    private int correctCount = 0;

    private JLabel questionLabel;
    private JLabel statusLabel;
    private JRadioButton rbA, rbB, rbC, rbD;
    private ButtonGroup optionsGroup;
    private JButton submitButton;
    private JButton nextButton;

    public QuizFrame() {
        setTitle("QuizFlow - Mini Quiz Uygulaması");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginScreen();
    }

    private void showLoginScreen() {
        String username = JOptionPane.showInputDialog(this, "Lütfen Kullanıcı Adınızı Giriniz:", "QuizFlow Giriş", JOptionPane.PLAIN_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Geçerli bir kullanıcı adı girmelisiniz!");
            System.exit(0);
        }

        this.currentUsername = username.trim();
        this.userId = repository.getOrCreateUser(currentUsername);
        initQuizScreen();
    }

    private void initQuizScreen() {
        questionList = repository.findAllQuestions();
        if (questionList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veritabanında hiç soru bulunamadı!");
            System.exit(0);
        }

        getContentPane().removeAll();
        setLayout(new BorderLayout(15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JLabel userLabel = new JLabel("Kullanıcı: " + currentUsername, SwingConstants.RIGHT);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(userLabel, BorderLayout.NORTH);

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(questionLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 8, 8));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        rbA = new JRadioButton();
        rbB = new JRadioButton();
        rbC = new JRadioButton();
        rbD = new JRadioButton();

        optionsGroup = new ButtonGroup();
        optionsGroup.add(rbA);
        optionsGroup.add(rbB);
        optionsGroup.add(rbC);
        optionsGroup.add(rbD);

        centerPanel.add(rbA);
        centerPanel.add(rbB);
        centerPanel.add(rbC);
        centerPanel.add(rbD);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.add(statusLabel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        submitButton = new JButton("Cevabı Gönder");
        nextButton = new JButton("Sonraki Soru");
        nextButton.setEnabled(false);

        bottomPanel.add(submitButton);
        bottomPanel.add(nextButton);
        add(bottomPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> evaluateAnswer());
        nextButton.addActionListener(e -> moveToNextQuestion());

        loadCurrentQuestion();
        revalidate();
        repaint();
    }

    private void loadCurrentQuestion() {
        optionsGroup.clearSelection();
        resetColors();

        Question q = questionList.get(currentIndex);
        questionLabel.setText("<html><div style='text-align: center; width: 450px;'>" +
                "<b>Soru " + (currentIndex + 1) + " / " + questionList.size() + ":</b> " + q.getQuestionText() + "</div></html>");

        rbA.setText("A) " + q.getOptionA());
        rbB.setText("B) " + q.getOptionB());
        rbC.setText("C) " + q.getOptionC());
        rbD.setText("D) " + q.getOptionD());

        setOptionsEnabled(true);
        submitButton.setEnabled(true);
        nextButton.setEnabled(false);
        statusLabel.setText("");
    }

    private void evaluateAnswer() {
        String selected = "";
        JRadioButton selectedButton = null;

        if (rbA.isSelected()) { selected = "A"; selectedButton = rbA; }
        else if (rbB.isSelected()) { selected = "B"; selectedButton = rbB; }
        else if (rbC.isSelected()) { selected = "C"; selectedButton = rbC; }
        else if (rbD.isSelected()) { selected = "D"; selectedButton = rbD; }

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen bir seçenek işaretleyiniz!");
            return;
        }

        Question q = questionList.get(currentIndex);
        boolean isCorrect = service.isAnswerCorrect(selected, q.getCorrectOption());

        setOptionsEnabled(false);
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);

        highlightCorrect(q.getCorrectOption());

        if (isCorrect) {
            correctCount++;
            statusLabel.setForeground(new Color(34, 139, 34));
            statusLabel.setText("✓ Doğru!");
        } else {
            if (selectedButton != null) {
                selectedButton.setForeground(Color.RED);
            }
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("✗ Yanlış! Doğru Şık: " + q.getCorrectOption());
        }
    }

    private void moveToNextQuestion() {
        currentIndex++;
        if (currentIndex < questionList.size()) {
            loadCurrentQuestion();
        } else {
            int finalScore = service.calculateScore(correctCount, questionList.size());
            repository.saveResult(userId, finalScore);

            JOptionPane.showMessageDialog(this,
                    "Quiz Tamamlandı!\nKullanıcı: " + currentUsername +
                            "\nToplam Doğru: " + correctCount + " / " + questionList.size() +
                            "\nFinal Skoru (100 üzerinden): " + finalScore + "\n(Sonuç veritabanına kaydedildi)",
                    "Quiz Sonucu", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    private void highlightCorrect(String correctOption) {
        Color green = new Color(34, 139, 34);
        if ("A".equalsIgnoreCase(correctOption)) rbA.setForeground(green);
        else if ("B".equalsIgnoreCase(correctOption)) rbB.setForeground(green);
        else if ("C".equalsIgnoreCase(correctOption)) rbC.setForeground(green);
        else if ("D".equalsIgnoreCase(correctOption)) rbD.setForeground(green);
    }

    private void resetColors() {
        rbA.setForeground(Color.BLACK);
        rbB.setForeground(Color.BLACK);
        rbC.setForeground(Color.BLACK);
        rbD.setForeground(Color.BLACK);
    }

    private void setOptionsEnabled(boolean enabled) {
        rbA.setEnabled(enabled);
        rbB.setEnabled(enabled);
        rbC.setEnabled(enabled);
        rbD.setEnabled(enabled);
    }
}
