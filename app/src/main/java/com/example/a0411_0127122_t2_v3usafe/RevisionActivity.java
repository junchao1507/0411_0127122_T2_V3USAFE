package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RevisionActivity extends AppCompatActivity {
    private DatabaseReference questionRef;
    private Question questionObj;
    private ArrayList<Question> questionList = new ArrayList<>();
    private TextView tvQuestion, tvAnswer;
    private int questionId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);

        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        tvQuestion = findViewById(R.id.tv_question);
        tvAnswer = findViewById(R.id.tv_answer);

        // Retrieving data
        questionRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Question");
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Question Node
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        // questionId Node
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
                        questionList.add(new Question(questionId, question, optionA, optionB, optionC, optionD, answer));
                    }

                    GridView gridView = findViewById(R.id.gv_question_list);
                    RevisionAdapter revisionAdapter = new RevisionAdapter(RevisionActivity.this, R.layout.revision_question_item, questionList);
                    gridView.setAdapter(revisionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}