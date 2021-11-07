package com.example.a0411_0127122_t2_v3usafe;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {
    private static ArrayList<User> userList = new ArrayList<>();

    private String userId;
    private String email;
    private String userName;
    private String password;
    private String vacStatus;
    private String covidRisk;

    public User(String userId, String email, String userName, String password, String vacStatus, String covidRisk) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.vacStatus = vacStatus;
        this.covidRisk = covidRisk;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVacStatus() {
        return vacStatus;
    }

    public void setVacStatus(String vacStatus) {
        this.vacStatus = vacStatus;
    }

    public String getCovidRisk() {
        return covidRisk;
    }

    public void setCovidRisk(String covidRisk) {
        this.covidRisk = covidRisk;
    }

    //Load account(s)
    public static ArrayList<User> getUserList() {
        return userList;
    }

    //Login Account

    //Update account

    //Delete Account
}