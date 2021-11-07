package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateVaccinationStatusActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private StorageReference imgRef;
    private RadioButton q1SecDose, q1FirstDose, q1NotYet;
    private Button btnNext, btnSubmit;
    private ImageButton btnBack, btnUpload;
    private TextView txtUpload;
    private CardView crdUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vaccination_status);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        q1SecDose = findViewById(R.id.secondDoseRadioButton);
        q1FirstDose = findViewById(R.id.firstDoseRadioButton);
        q1NotYet = findViewById(R.id.notYetRadioButton);
        btnNext = findViewById(R.id.btn_next_vaccination_status);
        btnSubmit = findViewById(R.id.btn_submit_vaccination_status);
        btnBack = findViewById(R.id.btn_back_vaccination_status);
        btnUpload = findViewById(R.id.btn_upload_image);
        txtUpload = findViewById(R.id.vaccination_status_q4);
        crdUpload = findViewById(R.id.crd_upload);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(q1FirstDose.isChecked() || q1SecDose.isChecked() || q1NotYet.isChecked())){
                    new AlertDialog.Builder(UpdateVaccinationStatusActivity.this)
                            .setTitle("INCOMPLETE SUBMISSION.")
                            .setMessage("Please select your answer to proceed.")
                            .setNegativeButton("Close", null).show();
                }else{
                    if(q1SecDose.isChecked()) {
                        btnNext.setVisibility(View.INVISIBLE);
                        txtUpload.setVisibility(View.VISIBLE);
                        crdUpload.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.VISIBLE);

                        btnUpload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Open gallery
                                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(openGalleryIntent, 1000);

                                imgRef = FirebaseStorage.getInstance().getReference();
                            }
                        });
                    }
                    else{
                        updateVaccinationStatus("INCOMPLETE");
                        Intent intent = new Intent(UpdateVaccinationStatusActivity.this, MainActivity.class);
                        intent.putExtra("userObject", user);
                        startActivity(intent);
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateVaccinationStatusActivity.this, MainActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateVaccinationStatusActivity.this, MainActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });
    }

    //For uploading profile pic
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profilePic.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri uri){
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");
        StorageReference fileRef = imgRef.child("User/" + user.getUserId() + "/vaccination_cert.jpg");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(btnUpload);
                        updateVaccinationStatus("COMPLETED");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateVaccinationStatusActivity.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateVaccinationStatus(String vacStatus){
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");
        HashMap UpdateUser = new HashMap();
        UpdateUser.put("vacStatus", vacStatus);


        ref = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
        ref.child(user.getUserId()).updateChildren(UpdateUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    user.setVacStatus(vacStatus);
                }else{
                    Toast.makeText(UpdateVaccinationStatusActivity.this, "Update Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}