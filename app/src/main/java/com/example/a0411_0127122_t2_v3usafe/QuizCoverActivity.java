package com.example.a0411_0127122_t2_v3usafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuizCoverActivity extends AppCompatActivity {

    private Button takeQuiz, skipQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_cover);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        takeQuiz = (Button)findViewById(R.id.btn_take_quiz);
        skipQuiz = (Button)findViewById(R.id.btn_skip_quiz);

        takeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = QuizCoverActivity.this;
                Intent intent = new Intent(context,QuizActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        skipQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = QuizCoverActivity.this;
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });
    }


}