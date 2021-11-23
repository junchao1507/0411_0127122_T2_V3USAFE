package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference lessonRef, seatRef, userRef;
    private StorageReference imgRef;
    private GridView gridMenu, gridLessons;
    private CircleImageView profilePic;
    private ArrayList<Lesson> tempLessonList = new ArrayList<>();
    private ArrayList<BookedLesson> bookedLessonsList = new ArrayList<>();
    private TextView noClass;


    // Set time zone and format date and time
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
    //Variables
    private String active = "true";
    private String lessonId = "0";
    private String lessonDate = "";
    private String startTime = "";
    private String endTime = "";
    private String startDateTime = "";
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
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);
        Intent getUserIntent = getIntent();
        User user = (User) getUserIntent.getSerializableExtra("userObject");

        //Load saved image from firebase firestore database.
        imgRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = imgRef.child("User/" + user.getUserId() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });

        profilePic = (CircleImageView) findViewById(R.id.circle_prof_pic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
                noClass = findViewById(R.id.txtIfNoClass);
                noClass.setVisibility(View.INVISIBLE);
            }
        });


        List<MainMenuItem> itemList = new ArrayList<>();
        itemList.add(new MainMenuItem(R.drawable.screening, "Covid Risk Status"));
        itemList.add(new MainMenuItem(R.drawable.booking, "Book a Seat"));
        itemList.add(new MainMenuItem(R.drawable.revision, "Revise"));
        itemList.add(new MainMenuItem(R.drawable.quiz, "Quiz"));
        itemList.add(new MainMenuItem(R.drawable.report, "Report"));
        itemList.add(new MainMenuItem(R.drawable.history, "History"));

        gridMenu = findViewById(R.id.gv_menu);
        MainMenuAdapter menuAdapter = new MainMenuAdapter(this, R.layout.main_menu_item, itemList);
        gridMenu.setAdapter(menuAdapter);
        gridMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Context context = MainActivity.this;
                Intent intent;

                switch (position) {
                    case 0:
                        intent = new Intent(context, UpdateCovidStatusActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        noClass = findViewById(R.id.txtIfNoClass);
                        noClass.setVisibility(View.INVISIBLE);
                        break;

                    case 1:
                        userRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User");
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String covidRisk = snapshot.child(user.getUserId()).child("covidRisk").getValue().toString();
                                String vacStatus = snapshot.child(user.getUserId()).child("vacStatus").getValue().toString();

                                // Check user's covid risk and vaccination status
                                if (covidRisk.equals("LOW RISK") && vacStatus.equals("COMPLETED")) {
                                    Intent intent = new Intent(context, BookingActivity.class);
                                    intent.putExtra("userObject", user);
                                    startActivity(intent);
                                    noClass = findViewById(R.id.txtIfNoClass);
                                    noClass.setVisibility(View.INVISIBLE);
                                } else {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("ACCESS DENIED")
                                            .setMessage("You do not have access to this page unless you are a low risk individual and have completed TWO(2) doses of vaccination. Update your Info Now!")
                                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(context, ProfileActivity.class);
                                                    intent.putExtra("userObject", user);
                                                    startActivity(intent);
                                                    noClass = findViewById(R.id.txtIfNoClass);
                                                    noClass.setVisibility(View.INVISIBLE);
                                                }
                                            }).setNegativeButton("Close", null).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;

                    case 2:
                        intent = new Intent(context, RevisionActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        noClass = findViewById(R.id.txtIfNoClass);
                        noClass.setVisibility(View.INVISIBLE);
                        break;

                    case 3:
                        intent = new Intent(context, QuizCoverActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        noClass = findViewById(R.id.txtIfNoClass);
                        noClass.setVisibility(View.INVISIBLE);
                        break;

                    case 4:
                        intent = new Intent(context, ReportActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        noClass = findViewById(R.id.txtIfNoClass);
                        noClass.setVisibility(View.INVISIBLE);
                        break;

                    case 5:
                        intent = new Intent(context, HistoryActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        noClass = findViewById(R.id.txtIfNoClass);
                        noClass.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        break;
                }
            }
        });


        // Retrieving data from firebase real-time db.
        lessonRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Lesson");
        lessonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // Lesson Node
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        // lessonId Node
                        ArrayList<String> seatList = new ArrayList<>();

                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            //Get the current child node
                            String child2 = snapshot2.getKey();

                            if(child2.equals("lessonId")){
                                lessonId = snapshot2.getValue(String.class);
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
                        startTime = lesson.getStartTime();
                        ArrayList<String> seatList = lesson.getSeatNo();
                        startDateTime = lessonDate + " " + startTime;
                        Date startDT = new Date();

                        // Formatting the date and time of the lesson
                        try {
                            startDT = dateTimeFormat.parse(startDateTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // To check if the lesson is still active
                        if (startDT.after(now)) {
                            // To display the seat number selected by the user
                            int countSeat = 1;
                            // Loop through the seatList to check if the user has booked any seat.
                            for (String seat : seatList) {
                                // If yes, then add the lesson into the bookedLessonList to display on the main page.
                                if (seat.equals(user.getUserId())) {
                                    bookedLessonsList.add(new BookedLesson(lesson.getLessonId(), lesson.getModuleName(), lesson.getDay() + ", " + lesson.getDate(), lesson.getStartTime(), lesson.getEndTime(), lesson.getLocation(), Integer.toString(countSeat)));
                                    break;
                                }
                                countSeat++;
                            }
                        }
                        else{
                            // Update active status -> false
                            lessonRef.child(lesson.getLessonId()).child("active").setValue("false");
                            Log.d("ADebugTag", "active(after): " + false);
                        }

                    }


                    gridLessons = findViewById(R.id.gv_booked_lessons);
                    BookedSeatAdapter bookedSeatAdapter = new BookedSeatAdapter(MainActivity.this, R.layout.booked_seat, bookedLessonsList);
                    gridLessons.setAdapter(bookedSeatAdapter);
                    if (gridLessons.getAdapter().isEmpty()) {
                        noClass = findViewById(R.id.txtIfNoClass);
                        noClass.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }

        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}