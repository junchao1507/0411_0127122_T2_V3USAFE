package com.example.a0411_0127122_t2_v3usafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");
    }
}