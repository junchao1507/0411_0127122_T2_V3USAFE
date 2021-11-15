package com.example.a0411_0127122_t2_v3usafe;

import java.util.ArrayList;

public class BookedLesson {
    private int lessonId;
    private String moduleName;
    private String dayDate;
    private String startTime;
    private String endTime;
    private String location;
    private String seat;

    public BookedLesson(int lessonId, String moduleName, String dayDate, String startTime, String endTime, String location, String seat) {
        this.lessonId = lessonId;
        this.moduleName = moduleName;
        this.dayDate = dayDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.seat = seat;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String day) {
        this.dayDate = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
