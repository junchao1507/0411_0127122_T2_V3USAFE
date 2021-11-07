package com.example.a0411_0127122_t2_v3usafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private StorageReference imgRef;
    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        List<MainMenuItem> itemList = new ArrayList<>();
        itemList.add(new MainMenuItem(R.drawable.screening, "Covid Risk Status"));
        itemList.add(new MainMenuItem(R.drawable.booking, "Book a Seat"));
        itemList.add(new MainMenuItem(R.drawable.revision, "Revise"));
        itemList.add(new MainMenuItem(R.drawable.quiz, "Quiz"));
        itemList.add(new MainMenuItem(R.drawable.report, "Report"));
        itemList.add(new MainMenuItem(R.drawable.history, "History"));

        profilePic = (CircleImageView) findViewById(R.id.circle_prof_pic);
        GridView gridView = findViewById(R.id.gv_menu);
        MainMenuAdapter menuAdapter = new MainMenuAdapter(this, R.layout.main_menu_item, itemList);
        gridView.setAdapter(menuAdapter);

        //Load saved image from firebase firestore database.
        imgRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = imgRef.child("User/" + user.getUserId() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        if(user.getCovidRisk().equals("LOW RISK") && user.getVacStatus().equals("COMPLETED")) {
                            intent = new Intent(context, BookingActivity.class);
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
                        break;

                    case 2:
                        intent =  new Intent(context, RevisionActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                        break;

                    case 3:
                        intent =  new Intent(context, QuizActivity.class);
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
    }
}