package david.yesayan.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {
    private TextView questions;
    private TextView question;
    private AppCompatButton option1, option2, option3, option4;
    private AppCompatButton nextBtn;
    private Timer quizTimer;
    private int seconds = 0;
    private int totalTimeInMins = 1;
    private List<QuestionsList> questionsList;
    private ProgressBar quizProgress;

    private int currentQuestionPosition = 0;
    private String selectedOptionByUser = "";

    // Переменные для подсчета баллов
    private int currentScore = 0;
    private int pointsPerQuestion = 1; // 1 балл за правильный ответ
    private TextView pointsTextView; // TextView для отображения баллов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView timer = findViewById(R.id.timer);
        final TextView selectedTopicName = findViewById(R.id.selectedTopicName);

        // Инициализация TextView для баллов
        pointsTextView = findViewById(R.id.points);
        if (pointsTextView != null) {
            pointsTextView.setText("0 баллов");
        }

        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        quizProgress = findViewById(R.id.quizProgress); // Initialize the progress bar

        nextBtn = findViewById(R.id.nextBtn);
        final String getSelectedTopic = getIntent().getStringExtra("selectedTopic");
        selectedTopicName.setText(getSelectedTopic);

        questionsList = QuestionsBank.qetQuestions(getSelectedTopic);

        // Set up the progress bar with the total question count
        quizProgress.setMax(100);
        // Initial progress (first question)
        updateProgressBar();

        startTimer(timer);

        questions.setText((currentQuestionPosition+1)+"/"+questionsList.size());
        question.setText(questionsList.get(0).getQuestion());
        option1.setText(questionsList.get(0).getOption1());
        option2.setText(questionsList.get(0).getOption2());
        option3.setText(questionsList.get(0).getOption3());
        option4.setText(questionsList.get(0).getOption4());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quizTimer.purge();
                    quizTimer.cancel();

                    startActivity(new Intent(QuizActivity.this, MainActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    startActivity(new Intent(QuizActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option1.getText().toString();
                        option1.setBackgroundResource(R.drawable.round_back_red10);
                        option1.setTextColor(Color.WHITE);

                        revealAnswer();
                        questionsList.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                        // Проверка правильности ответа и начисление баллов
                        checkAnswerAndUpdateScore(selectedOptionByUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(QuizActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option2.getText().toString();
                        option2.setBackgroundResource(R.drawable.round_back_red10);
                        option2.setTextColor(Color.WHITE);

                        revealAnswer();
                        questionsList.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                        // Проверка правильности ответа и начисление баллов
                        checkAnswerAndUpdateScore(selectedOptionByUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(QuizActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option3.getText().toString();
                        option3.setBackgroundResource(R.drawable.round_back_red10);
                        option3.setTextColor(Color.WHITE);

                        revealAnswer();
                        questionsList.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                        // Проверка правильности ответа и начисление баллов
                        checkAnswerAndUpdateScore(selectedOptionByUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(QuizActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option4.getText().toString();
                        option4.setBackgroundResource(R.drawable.round_back_red10);
                        option4.setTextColor(Color.WHITE);

                        revealAnswer();
                        questionsList.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                        // Проверка правильности ответа и начисление баллов
                        checkAnswerAndUpdateScore(selectedOptionByUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(QuizActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (selectedOptionByUser.isEmpty()) {
                        Toast.makeText(QuizActivity.this, "Пожалуйста, сделайте выбор", Toast.LENGTH_SHORT).show();
                    } else {
                        changeNextQuestion();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(QuizActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Метод для обновления прогресс бара
    private void updateProgressBar() {
        try {
            // Calculate progress percentage based on current question position
            int progressPercentage = (currentQuestionPosition + 1) * 100 / questionsList.size();
            quizProgress.setProgress(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для проверки ответа и обновления счета (1 балл за правильный ответ)
    private void checkAnswerAndUpdateScore(String selectedOption) {
        try {
            String correctAnswer = questionsList.get(currentQuestionPosition).getAnswer();

            if (selectedOption.equals(correctAnswer)) {
                // Добавление 1 балла к общему счету
                currentScore += pointsPerQuestion;

                // Обновление отображения баллов
                updateScoreDisplay();

                // Показ всплывающего сообщения о полученных баллах
                showScoreToast(pointsPerQuestion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для обновления отображения баллов
    private void updateScoreDisplay() {
        try {
            if (pointsTextView != null) {
                pointsTextView.setText(currentScore + " баллов");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для показа всплывающего сообщения о баллах
    private void showScoreToast(int points) {
        try {
            Toast.makeText(this, "+" + points + " балл!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer(TextView timerTextView) {
        totalTimeInMins = 3;
        seconds = 0;

        try {
            quizTimer = new Timer();
            quizTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (seconds == 0 && totalTimeInMins == 0) {
                        quizTimer.cancel();

                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(QuizActivity.this, "Время вышло", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(QuizActivity.this, QuizResults.class);
                                intent.putExtra("correct", getCorrectAnswers());
                                intent.putExtra("incorrect", getInCorrectAnswers());
                                intent.putExtra("score", currentScore);

                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    } else if (seconds == 0) {
                        totalTimeInMins--;
                        seconds = 59;
                    } else {
                        seconds--;
                    }

                    runOnUiThread(() -> {
                        try {
                            String finalMinutes = String.valueOf(totalTimeInMins);
                            String finalSeconds = String.valueOf(seconds);

                            if (finalMinutes.length() == 1) {
                                finalMinutes = "0" + finalMinutes;
                            }
                            if (finalSeconds.length() == 1) {
                                finalSeconds = "0" + finalSeconds;
                            }
                            timerTextView.setText(finalMinutes + ":" + finalSeconds);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }, 1000, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCorrectAnswers() {
        int correctAnswers = 0;

        try {
            for (int i = 0; i < questionsList.size(); i++) {
                final String getUserSelectedAnswer = questionsList.get(i).getUserSelectedAnswer();
                final String getAnswer = questionsList.get(i).getAnswer();

                if (getUserSelectedAnswer.equals(getAnswer)) {
                    correctAnswers++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return correctAnswers;
    }

    private int getInCorrectAnswers() {
        int incorrectAnswers = 0;

        try {
            for (int i = 0; i < questionsList.size(); i++) {
                final String getUserSelectedAnswer = questionsList.get(i).getUserSelectedAnswer();
                final String getAnswer = questionsList.get(i).getAnswer();

                if (!getUserSelectedAnswer.equals(getAnswer)) {
                    incorrectAnswers++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return incorrectAnswers;
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            quizTimer.purge();
            quizTimer.cancel();

            startActivity(new Intent(QuizActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(new Intent(QuizActivity.this, MainActivity.class));
            finish();
        }
    }

    private void revealAnswer() {
        try {
            final String getAnswer = questionsList.get(currentQuestionPosition).getAnswer();

            if (option1.getText().toString().equals(getAnswer)) {
                option1.setBackgroundResource(R.drawable.round_back_green10);
                option1.setTextColor(Color.WHITE);
            } else if (option2.getText().toString().equals(getAnswer)) {
                option2.setBackgroundResource(R.drawable.round_back_green10);
                option2.setTextColor(Color.WHITE);
            } else if (option3.getText().toString().equals(getAnswer)) {
                option3.setBackgroundResource(R.drawable.round_back_green10);
                option3.setTextColor(Color.WHITE);
            } else if (option4.getText().toString().equals(getAnswer)) {
                option4.setBackgroundResource(R.drawable.round_back_green10);
                option4.setTextColor(Color.WHITE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeNextQuestion() {
        try {
            currentQuestionPosition++;

            if ((currentQuestionPosition+1) == questionsList.size()) {
                nextBtn.setText("Готово");
            }

            if (currentQuestionPosition < questionsList.size()) {
                selectedOptionByUser = "";
                option1.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
                option1.setTextColor(Color.parseColor("#1F6BB8"));

                option2.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
                option2.setTextColor(Color.parseColor("#1F6BB8"));

                option3.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
                option3.setTextColor(Color.parseColor("#1F6BB8"));

                option4.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
                option4.setTextColor(Color.parseColor("#1F6BB8"));

                questions.setText((currentQuestionPosition+1)+"/"+questionsList.size());
                question.setText(questionsList.get(currentQuestionPosition).getQuestion());
                option1.setText(questionsList.get(currentQuestionPosition).getOption1());
                option2.setText(questionsList.get(currentQuestionPosition).getOption2());
                option3.setText(questionsList.get(currentQuestionPosition).getOption3());
                option4.setText(questionsList.get(currentQuestionPosition).getOption4());

                // Update the progress bar for the new question
                updateProgressBar();
            }
            else {
                Intent intent = new Intent(QuizActivity.this, QuizResults.class);
                intent.putExtra("correct", getCorrectAnswers());
                intent.putExtra("incorrect", getInCorrectAnswers());
                intent.putExtra("score", currentScore); // Передаем очки на экран с результатами

                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}