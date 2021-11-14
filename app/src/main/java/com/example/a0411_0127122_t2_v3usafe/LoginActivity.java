package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference rootRef, userRef;
    private TextInputEditText edtId, edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtId = findViewById(R.id.txtUserId);
        edtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCredentials();
            }
        });
    }

    private void verifyCredentials(){
        String enteredId = edtId.getText().toString().trim();
        String enteredPassword = edtPassword.getText().toString().trim();

        rootRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        userRef = rootRef.child("User");

        // Check if the email exists in the database.
        Query checkUser = userRef.orderByChild("userId").equalTo(enteredId);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Email/Account exist
                if(snapshot.exists()){
                    // Get the actual password from the database
                    String actualPassword = snapshot.child(enteredId).child("password").getValue(String.class);

                    //If the entered password == actual password
                    if(actualPassword.equals(enteredPassword)){
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        String userId =  snapshot.child(enteredId).child("userId").getValue(String.class);
                        String email =  snapshot.child(enteredId).child("email").getValue(String.class);
                        String userName =  snapshot.child(enteredId).child("userName").getValue(String.class);
                        String vacStatus = snapshot.child(enteredId).child("vacStatus").getValue(String.class);
                        String covidStatus = snapshot.child(enteredId).child("covidRisk").getValue(String.class);
                        String covidRiskUpdateTime = snapshot.child(enteredId).child("covidRiskUpdateTime").getValue(String.class);
                        User user = new User(userId, email, userName, actualPassword, vacStatus, covidStatus, covidRiskUpdateTime);

                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                        Date updatedTimestamp = new Date();

                        try {
                            updatedTimestamp = dateTimeFormat.parse(covidRiskUpdateTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(!DateUtils.isToday(updatedTimestamp.getTime())){
                            HashMap UpdateUser = new HashMap();
                            String covidRisk = "UNKNOWN";
                            UpdateUser.put("covidRisk", covidRisk);
                            UpdateUser.put("covidRiskUpdateTime", "");



                            userRef.child(user.getUserId()).updateChildren(UpdateUser).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        user.setCovidRisk(covidRisk);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Update Failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Email and password does not match.", Toast.LENGTH_SHORT).show();
                        edtPassword.requestFocus();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Account does not exist. Register one today!", Toast.LENGTH_SHORT).show();
                    edtId.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}