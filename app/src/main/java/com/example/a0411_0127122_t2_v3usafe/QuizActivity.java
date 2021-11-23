
package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private int timerNum = 20;
    private RoundedHorizontalProgressBar progressBar;
    private ArrayList<Question> list = new ArrayList<>();
    private ArrayList<Question> allQuestionList = new ArrayList<>();
    //private List<Question> allQuestionList;
    private Question questionClass;
    private int index = 0;
    private TextView tvQuestionNum, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private CardView cardOptionA, cardOptionB, cardOptionC, cardOptionD;
    private int correctCount = 0;
    private int wrongCount = 0;
    private LinearLayout btnNextQuiz;
    private DatabaseReference questionReference;

    //Variable
    private int questionId = 1;
    private String question = " ";
    private String optionA = " ";
    private String optionB = " ";
    private String optionC = " ";
    private String optionD = " ";
    private String answer = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent getUserIntent = getIntent();
        User user = (User) getUserIntent.getSerializableExtra("userObject");

        //timer
        progressBar = findViewById(R.id.timer_quiz);

        //TextView in activity_quiz.xml
        tvQuestionNum = findViewById(R.id.quiz_question_num);
        tvQuestion = findViewById(R.id.quiz_question);
        tvOptionA = findViewById(R.id.cardOptionA);
        tvOptionB = findViewById(R.id.cardOptionB);
        tvOptionC = findViewById(R.id.cardOptionC);
        tvOptionD = findViewById(R.id.cardOptionD);

        //CardView in activity_quiz.xml
        cardOptionA = findViewById(R.id.cardOA);
        cardOptionB = findViewById(R.id.cardOB);
        cardOptionC = findViewById(R.id.cardOC);
        cardOptionD = findViewById(R.id.cardOD);

        //Click next quiz question
        btnNextQuiz = findViewById(R.id.btn_next_quiz);


        // Retrieving data from firebase real-time db.
        questionReference = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Question");
        questionReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // Question Node
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        // questionId Node
                        //ArrayList<String> List = new ArrayList<>();

                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            //Get the current child node
                            String child2 = snapshot2.getKey();

                            if (child2.equals("questionId")) {
                                questionId = snapshot2.getValue(Integer.class);
                            } else if (child2.equals("question")) {
                                question = snapshot2.getValue(String.class);
                            } else if (child2.equals("optionA")) {
                                optionA = snapshot2.getValue(String.class);
                            } else if (child2.equals("optionB")) {
                                optionB = snapshot2.getValue(String.class);
                            } else if (child2.equals("optionC")) {
                                optionC = snapshot2.getValue(String.class);
                            } else if (child2.equals("optionD")) {
                                optionD = snapshot2.getValue(String.class);
                            } else if (child2.equals("answer")) {
                                answer = snapshot2.getValue(String.class);
                            }

                        }
                        // Add into the allQuestionList
                        allQuestionList.add(new Question(questionId, question, optionA, optionB, optionC, optionD, answer));
                    }

                   // allQuestionList = list;
                    Collections.shuffle(allQuestionList);
                    questionClass = allQuestionList.get(index);

                    //Set the CardView background colour to white
                    cardOptionA.setBackgroundColor(getResources().getColor(R.color.white));
                    cardOptionB.setBackgroundColor(getResources().getColor(R.color.white));
                    cardOptionC.setBackgroundColor(getResources().getColor(R.color.white));
                    cardOptionD.setBackgroundColor(getResources().getColor(R.color.white));

                    btnNextQuiz.setClickable(false);

                    //Quiz Timer
                    countDownTimer = new CountDownTimer(20000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timerNum = timerNum - 1;
                            progressBar.setProgress(timerNum);
                        }

                        @Override
                        public void onFinish() {
                            //Window Time out
                            Dialog dialog = new Dialog(QuizActivity.this,R.style.BlurTheme);
                            dialog.setContentView(R.layout.time_out_dialog);
                            dialog.findViewById(R.id.btn_quiz_try_again).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Close time out window & Next question
                                    dialog.dismiss();
                                    index++;
                                    questionClass = allQuestionList.get(index);
                                    colourReset();
                                    setAllData();
                                }
                            });
                            dialog.show();
                        }
                    }.start();

                    setAllData();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //Set Quiz Question
    private void setAllData() {
        int i = index + 1;
        tvQuestionNum.setText("Question " + i);
        tvQuestion.setText(questionClass.getQuestion());
        tvOptionA.setText(questionClass.getOptionA());
        tvOptionB.setText(questionClass.getOptionB());
        tvOptionC.setText(questionClass.getOptionC());
        tvOptionD.setText(questionClass.getOptionD());
        timerNum = 20;
        countDownTimer.cancel();
        countDownTimer.start();

    }


    public void correct(CardView cardView) {

        cardView.setBackgroundColor(getResources().getColor(R.color.dark_green));

        //Click button to the next question
        btnNextQuiz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                correctCount++;
                if (index < 4) {
                    index++;
                    questionClass = allQuestionList.get(index);
                    colourReset();
                    setAllData();
                }else{
                    quizResult();
                }
            }
        });
    }

    public void wrong(CardView cardOptionA) {

        cardOptionA.setBackgroundColor(getResources().getColor(R.color.dark_red));

        btnNextQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrongCount++;
                if (index < 4) {
                    index++;
                    questionClass = allQuestionList.get(index);
                    colourReset();
                    setAllData();
                } else {
                    quizResult();
                }
            }
        });

    }


    //Quiz Result
    private void quizResult() {
        Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);
        Intent getUserIntent = getIntent();
        startActivity(intent);
        finish();
    }

    public void enableButton() {
        cardOptionA.setClickable(true);
        cardOptionB.setClickable(true);
        cardOptionC.setClickable(true);
        cardOptionD.setClickable(true);
    }

    public void disableButton() {
        cardOptionA.setClickable(false);
        cardOptionB.setClickable(false);
        cardOptionC.setClickable(false);
        cardOptionD.setClickable(false);
    }

    //Set or change back the card view button to white colour
    public void colourReset() {
        cardOptionA.setBackgroundColor(getResources().getColor(R.color.white));
        cardOptionB.setBackgroundColor(getResources().getColor(R.color.white));
        cardOptionC.setBackgroundColor(getResources().getColor(R.color.white));
        cardOptionD.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void optionAClick(View view) {

        disableButton();

        btnNextQuiz.setClickable(true);

        if (questionClass.getOptionA().equals(questionClass.getAnswer())) {

            cardOptionA.setCardBackgroundColor(getResources().getColor(R.color.dark_green));


            if (index < 4) {
                correct(cardOptionA);
                enableButton();
            } else {
                correct(cardOptionA);
                                   }

        } else {
            wrong(cardOptionA);
            enableButton();
        }
    }

    public void optionBClick(View view) {

        disableButton();

        btnNextQuiz.setClickable(true);

        if (questionClass.getOptionB().equals(questionClass.getAnswer())) {

            cardOptionB.setCardBackgroundColor(getResources().getColor(R.color.dark_green));


            if (index < 4) {
                correct(cardOptionB);
                enableButton();
            } else {
                correct(cardOptionB);

            }

        } else {
            wrong(cardOptionB);
            enableButton();
        }
    }

    public void optionCClick(View view) {

        disableButton();

        btnNextQuiz.setClickable(true);

        if (questionClass.getOptionC().equals(questionClass.getAnswer())) {

            cardOptionC.setCardBackgroundColor(getResources().getColor(R.color.dark_green));


            if (index < 4) {
                correct(cardOptionC);
                enableButton();
            } else {
                correct(cardOptionC);

            }

        } else {
            wrong(cardOptionC);
            enableButton();
        }
    }

    public void optionDClick(View view) {

        disableButton();

        btnNextQuiz.setClickable(true);

        if (questionClass.getOptionD().equals(questionClass.getAnswer())) {

            cardOptionD.setCardBackgroundColor(getResources().getColor(R.color.dark_green));


            if (index < 4) {
                correct(cardOptionD);
                enableButton();
            } else {
                correct(cardOptionD);

            }

        } else {
            wrong(cardOptionD);
            enableButton();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
