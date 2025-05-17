package david.yesayan.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String selectedTopic = "";
    private FirebaseFirestore db;
    private static final String TAG = "MainActivity";
    private static final String QUESTIONS_COLLECTION = "questions";
    private static final String CATEGORY_FIELD = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Инициализация Firestore
        db = FirebaseFirestore.getInstance();

        // Проверка подключения Firebase
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = task.getResult();
                        Log.d("Firebase", "Installation ID: " + id);
                        // Firebase работает правильно
                        //Toast.makeText(MainActivity.this, "Firebase успешно подключен", Toast.LENGTH_SHORT).show();

                        // Загружаем существующие вопросы или добавляем новые
                        checkAndUploadQuestions();
                    } else {
                        Log.e("Firebase", "Error getting Installation ID", task.getException());
                        // Проблема с Firebase
                        //Toast.makeText(MainActivity.this, "Ошибка подключения Firebase", Toast.LENGTH_SHORT).show();
                    }
                });

        final LinearLayout football = findViewById(R.id.footballLayout);
        final LinearLayout math = findViewById(R.id.mathLayout);
        final LinearLayout country = findViewById(R.id.countryLayout);
        final LinearLayout facts = findViewById(R.id.factsLayout);
        final AppCompatButton startQuizBtn = findViewById(R.id.startQuizBtn);

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "football";
                football.setBackgroundResource(R.drawable.round_back_white_stroke10);

                math.setBackgroundResource(R.drawable.round_back_white10);
                country.setBackgroundResource(R.drawable.round_back_white10);
                facts.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "math";
                math.setBackgroundResource(R.drawable.round_back_white_stroke10);

                football.setBackgroundResource(R.drawable.round_back_white10);
                country.setBackgroundResource(R.drawable.round_back_white10);
                facts.setBackgroundResource(R.drawable.round_back_white10);
            }
        });
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "country";
                country.setBackgroundResource(R.drawable.round_back_white_stroke10);

                math.setBackgroundResource(R.drawable.round_back_white10);
                football.setBackgroundResource(R.drawable.round_back_white10);
                facts.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        facts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "facts";
                facts.setBackgroundResource(R.drawable.round_back_white_stroke10);

                math.setBackgroundResource(R.drawable.round_back_white10);
                country.setBackgroundResource(R.drawable.round_back_white10);
                football.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTopic.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Выберите викторину", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("selectedTopic", selectedTopic);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * Проверяет наличие вопросов в Firestore и загружает их, если их нет
     */
    private void checkAndUploadQuestions() {
        Log.d(TAG, "Проверка наличия вопросов в Firestore...");
        db.collection(QUESTIONS_COLLECTION)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Коллекция пуста, загружаем вопросы
                            Log.d(TAG, "Вопросы не найдены в Firestore. Загружаем...");
                            uploadQuestionsToFirestore();
                        } else {
                            Log.d(TAG, "Вопросы уже есть в Firestore");
                        }
                    } else {
                        Log.e(TAG, "Ошибка при проверке вопросов", task.getException());
                        // Пробуем все равно загрузить вопросы
                        Log.d(TAG, "Попытка загрузки вопросов, несмотря на ошибку...");
                        uploadQuestionsToFirestore();
                    }
                });
    }

    /**
     * Загружает вопросы в Firestore из локальных данных
     */
    private void uploadQuestionsToFirestore() {
        Log.d(TAG, "Начало загрузки вопросов в Firestore...");
        // Загружаем вопросы по категориям
        uploadCategoryQuestions("football", QuestionsBank.qetQuestions("football"));
        uploadCategoryQuestions("math", QuestionsBank.qetQuestions("math"));
        uploadCategoryQuestions("country", QuestionsBank.qetQuestions("country"));
        uploadCategoryQuestions("facts", QuestionsBank.qetQuestions("facts"));
    }

    /**
     * Загружает вопросы определенной категории в Firestore
     * @param category категория вопросов
     * @param questionsList список вопросов
     */
    private void uploadCategoryQuestions(String category, List<QuestionsList> questionsList) {
        Log.d(TAG, "Загрузка " + questionsList.size() + " вопросов для категории: " + category);
        for (int i = 0; i < questionsList.size(); i++) {
            QuestionsList question = questionsList.get(i);
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("category", category);
            questionData.put("question", question.getQuestion());
            questionData.put("option1", question.getOption1());
            questionData.put("option2", question.getOption2());
            questionData.put("option3", question.getOption3());
            questionData.put("option4", question.getOption4());
            questionData.put("answer", question.getAnswer());
            questionData.put("index", i);

            // Добавляем документ в коллекцию
            db.collection(QUESTIONS_COLLECTION)
                    .add(questionData)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Вопрос добавлен с ID: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Ошибка при добавлении вопроса", e);
                    });
        }
    }

    /**
     * Получает вопросы из Firestore для определенной категории
     * Может использоваться в QuizActivity вместо локальных вопросов
     */
    public static void getQuestionsFromFirestore(FirebaseFirestore db, String category, QuestionCallback callback) {
        Log.d(TAG, "Запрос вопросов из Firestore для категории: " + category);
        db.collection(QUESTIONS_COLLECTION)
                .whereEqualTo(CATEGORY_FIELD, category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<QuestionsList> questionsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String question = document.getString("question");
                            String option1 = document.getString("option1");
                            String option2 = document.getString("option2");
                            String option3 = document.getString("option3");
                            String option4 = document.getString("option4");
                            String answer = document.getString("answer");

                            QuestionsList questionItem = new QuestionsList(
                                    question, option1, option2, option3, option4, answer, ""
                            );
                            questionsList.add(questionItem);
                        }
                        Log.d(TAG, "Загружено " + questionsList.size() + " вопросов из Firestore");
                        callback.onQuestionsLoaded(questionsList);
                    } else {
                        Log.e(TAG, "Ошибка при получении вопросов", task.getException());
                        callback.onError(task.getException());
                    }
                });
    }

    /**
     * Интерфейс для обратного вызова при загрузке вопросов
     */
    public interface QuestionCallback {
        void onQuestionsLoaded(List<QuestionsList> questions);
        void onError(Exception e);
    }
}