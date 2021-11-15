package com.example.a0411_0127122_t2_v3usafe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private DatabaseReference lessonRef;
    private Lesson lesson;
    private ArrayList<BookedLesson> bookingList = new ArrayList<>();
    private ArrayList<Lesson> lessonList = new ArrayList<>();
    private ArrayList<Lesson> tempLessonList = new ArrayList<>();
    private ArrayList<BookedLesson> bookedLessonsList = new ArrayList<>();
    private TextView tvModName, tvDayDate, tvTime, tvLoc, tvSeats;

    // Set time zone and format date and time
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
    //Variables
    private String active = "true";
    private int lessonId = 0;
    private String lessonDate = "";
    private String startTime = "";
    private String endTime = "";
    private String endDateTime = "";
    private String week = "";
    private String location = "";
    private String capacity = "";
    private String day = "";
    private String date = "";
    private String moduleName = "";
    private String mode = "";
    private String lecturer = "";
    private Date now = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        // Retrieving data from firebase real-time db.
        lessonRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Lesson");
        lessonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    // Lesson Node
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        // lessonId Node
                        ArrayList<String> seatList = new ArrayList<>();

                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            //Get the current child node
                            String child2 = snapshot2.getKey();

                            if(child2.equals("lessonId")){
                                lessonId = snapshot2.getValue(Integer.class);
                            }
                            else if(child2.equals("week")){
                                week = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("location")){
                                location = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("capacity")){
                                capacity = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("seatNo")){
                                seatList.clear();
                                for(DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                    String child3 = snapshot3.getKey();
                                    String seat = snapshot3.getValue(String.class);
                                    seatList.add(seat);
                                }
                            }else if(child2.equals("day")){
                                day = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("date")){
                                date = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("startTime")){
                                startTime = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("endTime")){
                                endTime = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("moduleName")){
                                moduleName = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("mode")){
                                mode = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("lecturer")){
                                lecturer = snapshot2.getValue(String.class);
                            }
                            else if(child2.equals("active")){
                                active = snapshot2.getValue(String.class);
                            }
                        }

                        // Add into the tempLessonList
                        tempLessonList.add(new Lesson(lessonId, week, location,  capacity, seatList,  day,  date,  startTime,  endTime,  moduleName,  mode,  lecturer,  active));
                    }


                    for (Lesson lesson : tempLessonList) {
                        lessonDate = lesson.getDate();
                        endTime = lesson.getEndTime();
                        ArrayList<String> seatList = lesson.getSeatNo();
                        endDateTime = lessonDate + " " + endTime;
                        Date endDT = new Date();

                        // Formatting the date and time of the lesson
                        try {
                            endDT = dateTimeFormat.parse(endDateTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // To check if the lesson is over
                        boolean display = true;
                        // To check if the lesson is over
                        if (now.after(endDT)) {
                            // To display the seat number selected by the user
                            int countSeat = 1;
                            // Loop through the seatList to check if the user has booked any seat.
                            for (String seat : seatList) {
                                // If yes, then add the lesson into the bookedLessonList to display on the history page.
                                if (seat.equals(user.getUserId())) {
                                    bookedLessonsList.add(new BookedLesson(lesson.getLessonId(), lesson.getModuleName(), lesson.getDay() + ", " + lesson.getDate(), lesson.getStartTime(), lesson.getEndTime(), lesson.getLocation(), Integer.toString(countSeat)));
                                    break;
                                }
                                countSeat++;
                            }
                        }
                    }

                    GridView gridView = findViewById(R.id.gv_history_list);
                    BookedSeatAdapter bookedSeatAdapter = new BookedSeatAdapter(HistoryActivity.this, R.layout.booked_seat, bookedLessonsList);
                    gridView.setAdapter(bookedSeatAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                            intent.putExtra("lessonId", bookedLessonsList.get(position).getLessonId());
                            intent.putExtra("moduleName", bookedLessonsList.get(position).getModuleName());
                            intent.putExtra("dayDate", bookedLessonsList.get(position).getDayDate());
                            intent.putExtra("startTime", bookedLessonsList.get(position).getStartTime());
                            intent.putExtra("endTime", bookedLessonsList.get(position).getEndTime());
                            intent.putExtra("location", bookedLessonsList.get(position).getLocation());
                            intent.putExtra("seat", bookedLessonsList.get(position).getSeat());
                            intent.putExtra("userObject", user);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}