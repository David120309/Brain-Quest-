package david.yesayan.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    private String selectedTopic = "";

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
}