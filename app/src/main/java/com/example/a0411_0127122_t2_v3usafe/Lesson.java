package com.example.a0411_0127122_t2_v3usafe;

import java.util.ArrayList;
import java.util.Collections;

public class Lesson {
    private int lessonId;
    private String week;
    private String location;
    private String capacity;
    private ArrayList<String> seatNo;
    private String day;
    private String date;
    private String startTime;
    private String endTime;
    private String moduleName;
    private String mode;
    private String lecturer;
    private String active;

//    public Lesson(){
//
//    }

    public Lesson(int lessonId, String week, String location, String capacity, ArrayList<String> seatNo, String day, String date, String startTime, String endTime, String moduleName, String mode, String lecturer, String active) {
        this.lessonId = lessonId;
        this.week = week;
        this.location = location;
        this.capacity = capacity;
        this.seatNo = seatNo;
        this.day = day;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.moduleName = moduleName;
        this.mode = mode;
        this.lecturer = lecturer;
        this.active = active;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String seats) {
        this.capacity = seats;
    }

    public ArrayList<String> getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(ArrayList<String> seatNo) {
        this.seatNo = seatNo;
    }

    public String isActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}

