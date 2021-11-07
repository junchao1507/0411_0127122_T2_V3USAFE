package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private StorageReference imgRef;
    private TextView tvName, tvEmail, tvCovid, tvVaccine, tvCovidTitle, tvVaccineTitle;
    private ImageButton back, editProfile;
    private Button updateCovidStatus, updateVaccineStatus, logout;
    private CircleImageView profilePic;
    private CardView crdCovid, crdVaccine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        tvName = (TextView)findViewById(R.id.tv_name);
        tvEmail = (TextView)findViewById(R.id.tv_email);
        tvCovid = (TextView)findViewById(R.id.status_covidrisk);
        tvVaccine = (TextView)findViewById(R.id.status_vaccination);
        tvCovidTitle = (TextView)findViewById(R.id.text_status_covidrisk);
        tvVaccineTitle = (TextView)findViewById(R.id.text_status_vaccination);
        profilePic = (CircleImageView)findViewById(R.id.circle_profile_pic);
        editProfile = (ImageButton)findViewById(R.id.btn_edit_profile);
        back = (ImageButton) findViewById(R.id.btn_back);
        updateCovidStatus = (Button)findViewById(R.id.btn_update_covid);
        updateVaccineStatus = (Button)findViewById(R.id.btn_update_vacc);
        crdCovid = (CardView)findViewById(R.id.crd_covid_risk);
        crdVaccine = (CardView)findViewById(R.id.crd_vaccination);
        logout = (Button) findViewById(R.id.btn_logout);

        // Retrieving data from firebase real-time db.
        dbRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName =  snapshot.child(user.getUserId()).child("userName").getValue().toString();
                String email = snapshot.child(user.getUserId()).child("email").getValue().toString();
                String covidStatus = snapshot.child(user.getUserId()).child("covidRisk").getValue().toString();
                String vaccineStatus = snapshot.child(user.getUserId()).child("vacStatus").getValue().toString();

                tvName.setText(userName);
                tvEmail.setText(email);
                tvCovid.setText(covidStatus);
                tvVaccine.setText(vaccineStatus);

                if(covidStatus.equals("HIGH RISK")){
                    tvCovidTitle.setTextColor(Color.parseColor("#DC143C"));
                    tvCovid.setTextColor(Color.parseColor("#DC143C"));
                    crdCovid.setCardBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.light_red));
                }
                else if (covidStatus.equals("LOW RISK")){
                    tvCovidTitle.setTextColor(Color.parseColor("#008000"));
                    tvCovid.setTextColor(Color.parseColor("#008000"));
                    crdCovid.setCardBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.light_green));
                    updateCovidStatus.setText("Good job! You're at a lower risk.");
                    updateCovidStatus.setTextSize(16);
                    updateCovidStatus.setTextColor(Color.parseColor("#008000"));
                    updateCovidStatus.setBackgroundColor(Color.parseColor("#90EE90"));
                    updateCovidStatus.setEnabled(false);
                }

                if(vaccineStatus.equals("INCOMPLETE")){
                    tvVaccineTitle.setTextColor(Color.parseColor("#DC143C"));
                    tvVaccine.setTextColor(Color.parseColor("#DC143C"));
                    crdVaccine.setCardBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.light_red));

                }
                else if (vaccineStatus.equals("COMPLETED")){
                    tvVaccineTitle.setTextColor(Color.parseColor("#008000"));
                    tvVaccine.setTextColor(Color.parseColor("#008000"));
                    crdVaccine.setCardBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.light_green));
                    updateVaccineStatus.setText("You're safe! Welcome back.");
                    updateVaccineStatus.setTextSize(16);
                    updateVaccineStatus.setTextColor(Color.parseColor("#008000"));
                    updateVaccineStatus.setBackgroundColor(Color.parseColor("#90EE90"));
                    updateVaccineStatus.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Load saved image from firebase firestore database.
        imgRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = imgRef.child("User/" + user.getUserId() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ProfileActivity.this;
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });


        updateCovidStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ProfileActivity.this;
                Intent intent = new Intent(context, UpdateCovidStatusActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });


        updateVaccineStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ProfileActivity.this;
                Intent intent = new Intent(context, UpdateVaccinationStatusActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ProfileActivity.this;
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ProfileActivity.this;
                Intent intent = new Intent(context, CoverActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
                finish();
            }
        });
    }
}