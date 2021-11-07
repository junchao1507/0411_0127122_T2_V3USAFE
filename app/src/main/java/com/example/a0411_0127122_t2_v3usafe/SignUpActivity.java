package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    //private FirebaseDatabase rootNode;
    private DatabaseReference rootRef, userRef;
    private TextInputEditText edtId, edtEmail, edtPassword;
    private Button btnSignUp;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtId = findViewById(R.id.txtId);
        edtEmail = findViewById(R.id.txtEmail);
        edtPassword = findViewById(R.id.txtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Autofill Id edittext
        edtId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing...removing causes error
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing...removing causes error
            }

            public void afterTextChanged(Editable s) {
                edtEmail.setText(edtId.getText().toString() + "@kdu-online.com");
            };
        });


        // Sign Up Account
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                userRef = rootRef.child("User");

                String studId = edtId.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(studId)) {
                    edtId.setError("Student Id Required!");
                    edtId.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Email Required!");
                    edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Password Required!");
                    edtPassword.requestFocus();
                } else {
                    user = new User(studId, email, studId, password, "UNKNOWN", "UNKNOWN");

                    userRef.child(studId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Account Registration Successful!", Toast.LENGTH_SHORT).show();

                                Log.d("ADebugTag", "Email: " + email);
                                Log.d("ADebugTag", "Password: " + password);

                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}