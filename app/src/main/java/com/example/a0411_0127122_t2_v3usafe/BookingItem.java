package com.example.a0411_0127122_t2_v3usafe;

import java.util.ArrayList;

public class BookingItem {
    private String lessonId;
    private String moduleName;
    private String dayDate;
    private String time;
    private String location;
    private String seats;
    private ArrayList<String> seatNo;

    public BookingItem(String lessonId, String moduleName, String dayDate, String time, String location, String seats, ArrayList<String> seatNo) {
        this.lessonId = lessonId;
        this.moduleName = moduleName;
        this.dayDate = dayDate;
        this.time = time;
        this.location = location;
        this.seats = seats;
        this.seatNo = seatNo;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public ArrayList<String> getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(ArrayList<String> seatNo) {
        this.seatNo = seatNo;
    }
}
