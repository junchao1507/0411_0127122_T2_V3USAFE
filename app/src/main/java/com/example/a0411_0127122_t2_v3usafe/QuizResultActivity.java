package com.example.a0411_0127122_t2_v3usafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class QuizResultActivity extends AppCompatActivity {

    private CircularProgressBar circularProgressBar;
    private TextView quizResult;
    private int correct, wrong;
    private Button btnCloseQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        Intent getUserIntent = getIntent();
        User user = (User) getUserIntent.getSerializableExtra("userObject");

        //Correct count and wrong count of quiz questions
        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);

        circularProgressBar = findViewById(R.id.quizResultProgressBar);
        quizResult = findViewById(R.id.txt_quiz_result);
        btnCloseQuiz = findViewById(R.id.btn_close_quiz);
//        btnNewQuiz = findViewById(R.id.btn_new_quiz);

        circularProgressBar.setProgress(correct);
        quizResult.setText(correct + "/5");

//        btnNewQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = QuizResultActivity.this;
//                Intent intent = new Intent(context,QuizCoverActivity.class);
//                intent.putExtra("userObject", user);
//                startActivity(intent);
//                finish();
//            }
//        });

        btnCloseQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}