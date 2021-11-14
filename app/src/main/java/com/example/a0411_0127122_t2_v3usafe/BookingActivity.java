package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BookingActivity extends AppCompatActivity {
    private DatabaseReference lessonRef;
    private Lesson lesson;
    private ArrayList<BookingItem> bookingList = new ArrayList<>();
    //private ArrayList<Lesson> lessonlist = new ArrayList<>();
    private TextView tvModName, tvDayDate, tvTime, tvLoc, tvSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");


        // Retrieving data from firebase real-time db.
        lessonRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Lesson");
        lessonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Set time zone and format date and time
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                //Variables
                boolean display = true;
                boolean active = true;
                String lessonDate = "";
                String startTime = "";
                String startDateTime = "";
                Date now = new Date();
                if(snapshot.exists()) {
                    // Lesson Node
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String child1 = snapshot1.getKey();
                        Log.d("ADebugTag", "child1: " + child1);
                        // lessonId Node
                        int i = 0;
                        for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                            //Get the current child node
                            String child = snapshot2.getKey();

                            //Check if the lesson is still active
                            if(child.equals("active")){
                                active = snapshot2.getValue(Boolean.class);
                                //Log.d("ADebugTag", "active: " + active);
                            }

                            //Get the date of the lesson
                            if(child.equals("date")){
                                lessonDate = snapshot2.getValue(String.class);
                                //Log.d("ADebugTag", "lessonDate: " + lessonDate);
                            }

                            // Get the starting time of the lesson
                            if(child.equals("startTime")){
                                startTime = snapshot2.getValue(String.class);
                                //Log.d("ADebugTag", "startTime: " + startTime);
                                startDateTime = lessonDate + " " + startTime;
                                Date startDT = new Date();
                                try {
                                    startDT = dateTimeFormat.parse(startDateTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                // Check if the startDateTime has passed the current timestamp
                                Log.d("ADebugTag", "startDateTime: " + startDT);
                                Log.d("ADebugTag", "Now: " + now);
                                if (now.after(startDT)) {
                                    active = false;
                                    display = false;
                                }

                                Log.d("ADebugTag", "active: " + active);
                                // If the status is active
                                if(active) {
                                    // seatNo Node
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        //After booking, seat = userId
                                        // If userId has booked the seat
                                        // Then dont display the lesson on the list.
                                        String seat = snapshot3.getValue(String.class);
                                        if (seat.equals(user.getUserId())) {
                                            display = false;
                                        }
                                    }

                                    if(display) {
                                        lesson = snapshot1.getValue(Lesson.class);
                                        bookingList.add(new BookingItem(Integer.toString(lesson.getLessonId()), lesson.getModuleName(), lesson.getDay() + ", " + lesson.getDate(), lesson.getStartTime(), lesson.getLocation(), lesson.getCapacity(), lesson.getSeatNo()));
                                    }
                                }
                                else{
                                    // Update active status
                                    Lesson less = snapshot1.getValue(Lesson.class);
                                    lessonRef.child(Integer.toString(less.getLessonId())).child("active").setValue(false);
                                }
                            }

//                            Log.d("ADebugTag", "i: " + i);
//                            i++;
                            // Reset te value back to true
                            display = true;
                            active = true;
                        }
                    }

                    GridView gridView = findViewById(R.id.gv_lesson_list);
                    BookingAdapter bookingAdapter = new BookingAdapter(BookingActivity.this, R.layout.booking_item, bookingList);
                    gridView.setAdapter(bookingAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                            intent.putExtra("lessonId", bookingList.get(position).getLessonId());
                            intent.putExtra("moduleName", bookingList.get(position).getModuleName());
                            intent.putExtra("dayDate", bookingList.get(position).getDayDate());
                            intent.putExtra("time", bookingList.get(position).getTime());
                            intent.putExtra("location", bookingList.get(position).getLocation());
                            intent.putExtra("seatsAvailable", bookingList.get(position).getSeats());
                            intent.putExtra("selectSeat", bookingList.get(position).getSeatNo());
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