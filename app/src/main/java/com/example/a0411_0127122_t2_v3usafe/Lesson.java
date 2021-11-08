package com.example.a0411_0127122_t2_v3usafe;

import java.util.ArrayList;

public class Lesson {
    private static ArrayList<Lesson> lessonList = new ArrayList<>();

    private int lessonId;
    private int week;
    private String location;
    private String day;
    private String date;
    private String startTime;
    private String endTime;
    private String moduleName;
    private String mode;
    private String lecturer;

    public Lesson(int lessonId, int week, String location, String day, String date, String startTime, String endTime, String moduleName, String mode, String lecturer) {
        this.lessonId = lessonId;
        this.week = week;
        this.location = location;
        this.day = day;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.moduleName = moduleName;
        this.mode = mode;
        this.lecturer = lecturer;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
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

    public static void initLessons(){
        lessonList.add(new Lesson(
                1,
                11,
                "GF-LRT-01",
                "Wednesday",
                "24-11-2021",
                "12:00 P.M.",
                "02:00 P.M.",
                "XBMC3024 Mobile Programming & Screen Design 2",
                "Tutorial 1",
                "Dr. Law Foong Li"));

        lessonList.add(new Lesson(
                2,
                11,
                "GF-LRT-01",
                "Wednesday",
                "24-11-2021",
                "02:00 P.M.",
                "04:00 P.M.",
                "XBMC3024 Mobile Programming & Screen Design 2",
                "Tutorial 2",
                "Dr. Law Foong Li"));

        lessonList.add(new Lesson(
                3,
                11,
                "GF-LRT-02",
                "Wednesday",
                "24-11-2021",
                "04:00 P.M.",
                "06:00 P.M.",
                "XBDS3024 Natural Language Processing",
                "Lecture",
                "Mr. Phua Yeong Tsann"));


        lessonList.add(new Lesson(
                4,
                11,
                "GF-LRT-01",
                "Thursday",
                "24-11-2021",
                "08:00 A.M.",
                "10:00 A.M.",
                "XBDS3024 Natural Language Processing",
                "Tutorial",
                "Mr. Phua Yeong Tsann"));
    }

    public ArrayList<Lesson> getLessonList() {
        return lessonList;
    }
}
