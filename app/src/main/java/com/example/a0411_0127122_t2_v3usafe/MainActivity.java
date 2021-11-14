package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import android.widget.ImageView;

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
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference lessonRef, userRef;
    private StorageReference imgRef;
    private Lesson lesson;
    private GridView gridMenu, gridLessons;
    private CircleImageView profilePic;
    private ArrayList<BookingItem> bookingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

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
            }
        });



        List<MainMenuItem> itemList = new ArrayList<>();
        itemList.add(new MainMenuItem(R.drawable.screening, "Covid Risk Status"));
        itemList.add(new MainMenuItem(R.drawable.booking, "Book a Seat"));
        itemList.add(new MainMenuItem(R.drawable.revision, "Revise"));
        itemList.add(new MainMenuItem(R.drawable.quiz, "Quiz"));
        itemList.add(new MainMenuItem(R.drawable.report, "Report"));
        itemList.add(new MainMenuItem(R.drawable.history, "History"));

        gridMenu= findViewById(R.id.gv_menu);
        MainMenuAdapter menuAdapter = new MainMenuAdapter(this, R.layout.main_menu_item, itemList);
        gridMenu.setAdapter(menuAdapter);
        gridMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Context context = MainActivity.this;
                Intent intent;

                switch(position)
                {
                    case 0:
                        intent =  new Intent(context, UpdateCovidStatusActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        break;

                    case 1:
                        userRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User");
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String covidRisk = snapshot.child(user.getUserId()).child("covidRisk").getValue().toString();
                                String vacStatus = snapshot.child(user.getUserId()).child("vacStatus").getValue().toString();

                                // Check user's covid risk and vaccination status
                                if(covidRisk.equals("LOW RISK") && vacStatus.equals("COMPLETED")) {
                                    Intent intent = new Intent(context, BookingActivity.class);
                                    intent.putExtra("userObject", user);
                                    startActivity(intent);
                                }else{
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("ACCESS DENIED")
                                            .setMessage("You do not have access to this page unless you are a low risk individual and have completed TWO(2) doses of vaccination. Update your Info Now!")
                                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent =  new Intent(context, ProfileActivity.class);
                                                    intent.putExtra("userObject", user);
                                                    startActivity(intent);
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
                        intent =  new Intent(context, RevisionActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        break;

                    case 3:
                        intent =  new Intent(context, QuizCoverActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        break;

                    case 4:
                        intent =  new Intent(context, ReportActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        break;

                    case 5:
                        intent =  new Intent(context, HistoryActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });




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
                                        // If userId hasn't booked the seat
                                        // Then dont display the lesson on the list.
                                        String seat = snapshot3.getValue(String.class);
                                        if (!seat.equals(user.getUserId())) {
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

                            Log.d("ADebugTag", "i: " + i);
                            i++;
                            // Reset te value back to true
                            display = true;
                            active = true;
                        }
                    }


                    gridLessons= findViewById(R.id.gv_booked_lessons);
                    BookingAdapter bookingAdapter = new BookingAdapter(MainActivity.this, R.layout.booking_item, bookingList);
                    gridLessons.setAdapter(bookingAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}