package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UpdateCovidStatusActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private CheckBox[] cbQ1 = new CheckBox[10];
    private CheckBox[] cbQ2 = new CheckBox[4];
    private RadioButton q1n, q2n, q3n, q4n, q5n, q3y,q4y, q5y;
    private Button btnSubmit;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_covid_status);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        cbQ1[0] = (CheckBox)findViewById(R.id.cb1q1);
        cbQ1[1] = (CheckBox)findViewById(R.id.cb2q1);
        cbQ1[2] = (CheckBox)findViewById(R.id.cb3q1);
        cbQ1[3] = (CheckBox)findViewById(R.id.cb4q1);
        cbQ1[4] = (CheckBox)findViewById(R.id.cb5q1);
        cbQ1[5] = (CheckBox)findViewById(R.id.cb6q1);
        cbQ1[6] = (CheckBox)findViewById(R.id.cb7q1);
        cbQ1[7] = (CheckBox)findViewById(R.id.cb8q1);
        cbQ1[8] = (CheckBox)findViewById(R.id.cb9q1);
        cbQ1[9] = (CheckBox)findViewById(R.id.cb10q1);
        cbQ2[0] = (CheckBox)findViewById(R.id.cb1q2);
        cbQ2[1] = (CheckBox)findViewById(R.id.cb2q2);
        cbQ2[2] = (CheckBox)findViewById(R.id.cb3q2);
        cbQ2[3] = (CheckBox)findViewById(R.id.cb4q2);
        q1n = (RadioButton)findViewById(R.id.rbtn_q1None);
        q2n = (RadioButton)findViewById(R.id.rbtn_q2None);
        q3n = (RadioButton)findViewById(R.id.rbtn_q3No);
        q4n = (RadioButton)findViewById(R.id.rbtn_q4No);
        q5n = (RadioButton)findViewById(R.id.rbtn_q5No);
        q3y = (RadioButton)findViewById(R.id.rbtn_q3Yes);
        q4y = (RadioButton)findViewById(R.id.rbtn_q4Yes);
        q5y = (RadioButton)findViewById(R.id.rbtn_q5Yes);
        btnSubmit = (Button)findViewById(R.id.btn_submit_covid_status);
        btnBack = (ImageButton)findViewById(R.id.btn_back_covid_status);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateCovidStatusActivity.this, MainActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countQ1 = 0;
                for(int i = 0; i < 10; i++){
                    if(cbQ1[i].isChecked()){
                        countQ1 ++;
                    }
                }

                int countQ2 = 0;
                for(int i = 0; i < 4; i++){
                    if(cbQ2[i].isChecked()){
                        countQ2 ++;
                    }
                }

                if(countQ1 == 0 && !q1n.isChecked() || countQ2 == 0 && !q2n.isChecked() || !(q3y.isChecked() || q3n.isChecked()) || !(q4y.isChecked() || q4n.isChecked()) || !(q5y.isChecked() || q5n.isChecked())){
                    new AlertDialog.Builder(UpdateCovidStatusActivity.this)
                            .setTitle("INCOMPLETE SUBMISSION.")
                            .setMessage("Make sure you have answered all questions.")
                            .setNegativeButton("Close", null).show();
                }else {
                    if (countQ1 >= 2 || countQ2 >= 1 || q3y.isChecked() || q4y.isChecked() || q5y.isChecked()) {
                        updateCovidRisk("HIGH RISK");
                    } else {
                        updateCovidRisk("LOW RISK");
                    }

                    Intent intent = new Intent(UpdateCovidStatusActivity.this, MainActivity.class);
                    intent.putExtra("userObject", user);
                    startActivity(intent);
                }
            }
        });
    }

    public void updateCovidRisk(String covidRisk){
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");
        Date now = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        HashMap UpdateUser = new HashMap();
        UpdateUser.put("covidRisk", covidRisk);
        UpdateUser.put("covidRiskUpdateTime", dateTimeFormat.format(now));


        ref = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
        ref.child(user.getUserId()).updateChildren(UpdateUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    user.setCovidRisk(covidRisk);
                }else{
                    Toast.makeText(UpdateCovidStatusActivity.this, "Update Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}