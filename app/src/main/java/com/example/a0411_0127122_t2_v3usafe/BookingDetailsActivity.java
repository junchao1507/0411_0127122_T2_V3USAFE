package com.example.a0411_0127122_t2_v3usafe;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.reginald.editspinner.EditSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BookingDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseReference rootRef, lessonRef, seatRef;
    private ArrayAdapter<String> listSeats;
    private ArrayList<String> seatNo = new ArrayList<>();;
    private TextView tvLessonId, tvModuleName, tvDayDate, tvTime, tvLocation, tvSubjectLecturer, tvSeatsAvailable;
    private EditSpinner spnSelectSeat;
    private Button btnBook, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);

        tvLessonId = findViewById(R.id.tv_lesson_id_details);
        tvModuleName = findViewById(R.id.tv_module_name_details);
        tvDayDate = findViewById(R.id.tv_day_date_details);
        tvTime = findViewById(R.id.tv_time_details);
        tvLocation = findViewById(R.id.tv_location_details);
        tvSeatsAvailable = findViewById(R.id.tv_seat_details);
        tvSubjectLecturer = findViewById(R.id.tv_lecturer);
        spnSelectSeat = findViewById(R.id.spn_select_seat);
        btnBook = findViewById(R.id.btn_book_booking_details);
        btnBack = findViewById(R.id.btn_back_booking_details);

        String lessonId = "";
        String moduleName = "";
        String dayDate = "";
        String time = "";
        String location = "";
        String lecturer = "";
        int seatsAvailable = 0;
        //String seatList = "";
        ArrayList<String> seatList = new ArrayList<>();


        Bundle extras = getIntent().getExtras();
        lessonId = extras.getString("lessonId");
        moduleName = extras.getString("moduleName");
        dayDate = extras.getString("dayDate");
        time = extras.getString("time");
        location = extras.getString("location");
        lecturer = extras.getString("lecturer");
        seatList = (ArrayList<String>)getIntent().getExtras().getSerializable("seatNo");

        Log.d("ADebugTag", "seatList: " + seatList);

        for (int i = 0; i < seatList.size(); i++) {
            if (seatList.get(i).equals("0")){
                seatNo.add(Integer.toString(i + 1));
                seatsAvailable++;
            }
        }

        tvLessonId.setText(lessonId);
        tvModuleName.setText(moduleName);
        tvDayDate.setText(dayDate);
        tvTime.setText(time);
        tvLocation.setText(location);
        tvSubjectLecturer.setText(lecturer);
        tvSeatsAvailable.setText(Integer.toString(seatsAvailable));

        // Week Spinner
        listSeats = new ArrayAdapter<String>(BookingDetailsActivity.this, android.R.layout.simple_spinner_item, seatNo);
        listSeats.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectSeat.setAdapter(listSeats);


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMsg = "This field cannot be left empty!";
                String lessId = tvLessonId.getText().toString();
                String seat = spnSelectSeat.getText().toString();

                if (TextUtils.isEmpty(seat)) {
                    spnSelectSeat.setError(errorMsg);
                    spnSelectSeat.requestFocus();
                } else{
                    rootRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                    lessonRef = rootRef.child("Lesson").child(lessId);
                    seatRef = lessonRef.child("seatNo");

                    // Seat number starts from 1 but index starts from 0.
                    int sId = Integer.parseInt(seat) - 1;
                    String seatId = Integer.toString(sId);
                    seatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                // Get the seat index
                                String seat = snapshot1.getKey();
                                // If seat index == seatId
                                if(seat.equals(seatId)){
                                    Intent getUserIntent = getIntent();
                                    User user = (User)getUserIntent.getSerializableExtra("userObject");

                                    // Update capacity
                                    seatRef.child(seatId).setValue(user.getUserId());
                                    String newCap = Integer.toString(Integer.parseInt(tvSeatsAvailable.getText().toString()) - 1);
                                    lessonRef.child("capacity").setValue(newCap);
                                    tvSeatsAvailable.setText(newCap);

                                    // Alert dialog
                                    new AlertDialog.Builder(BookingDetailsActivity.this)
                                            .setTitle("SUCCESS!")
                                            .setMessage("Congrats! Your seat has been reserved!")
                                            .setNegativeButton("Close", null).show();
                                    Toast.makeText(BookingDetailsActivity.this, "Booking was Successful!",Toast.LENGTH_SHORT).show();

                                    // Disable the booking button
                                    btnBook.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(BookingDetailsActivity.this, BookingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}