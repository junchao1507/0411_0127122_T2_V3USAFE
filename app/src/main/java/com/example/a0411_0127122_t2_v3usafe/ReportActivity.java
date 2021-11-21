package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reginald.editspinner.EditSpinner;
import com.squareup.picasso.Picasso;

public class ReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseReference rootRef, reportRef;
    private StorageReference imgRef;
    private EditSpinner spnSelectIssue;
    private ArrayAdapter<String> listIssues;
    private String issueList[] = {"SOP Violation", "Covid-19 Infection"};
    private EditSpinner spnSelectLoc;
    private ArrayAdapter<String> listLoc;
    private String locList[] = {"Library", "Classroom"};
    private Button btnReportSubmit;
    private ImageButton btnUpload;
    private TextInputEditText reportDesc;
    private Report report;
    private int maxId = 1;

    public ReportActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Report a concern");
        actionBar.setElevation(0);

        btnReportSubmit = (Button) findViewById(R.id.btnReportSubmit);
        btnUpload = (ImageButton) findViewById(R.id.btn_upload_image);

        spnSelectIssue = (EditSpinner) findViewById(R.id.spinnerCategory);
        spnSelectLoc = (EditSpinner) findViewById(R.id.spinnerLocation);
        reportDesc = findViewById(R.id.edtReportDesc);

        ArrayAdapter issueAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, issueList);
        spnSelectIssue.setAdapter(issueAdapter);
        spnSelectIssue.setEditable(true);
        // set the dropdown drawable on the right of EditText and its size
        spnSelectIssue.setDropDownDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24), 60, 60);

        // set the spacing between Edited area and DropDown click area
        spnSelectIssue.setDropDownDrawableSpacing(50);

        ArrayAdapter locAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, locList);
        spnSelectLoc.setAdapter(locAdapter);
        spnSelectLoc.setEditable(true);
        // set the dropdown drawable on the right of EditText and its size
        spnSelectLoc.setDropDownDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24), 60, 60);

        // set the spacing between Edited area and DropDown click area
        spnSelectLoc.setDropDownDrawableSpacing(50);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

                imgRef = FirebaseStorage.getInstance().getReference();
            }
        });

        btnReportSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                reportRef = rootRef.child("Report");

                String errorMsg = "This field cannot be left empty!";
                String issue = spnSelectIssue.getText().toString();
                String location = spnSelectLoc.getText().toString();
                String description = reportDesc.getText().toString();

                if(TextUtils.isEmpty(issue)) {
                    spnSelectIssue.setError(errorMsg);
                    spnSelectIssue.requestFocus();
                } else  if (TextUtils.isEmpty(location)) {
                    spnSelectLoc.setError(errorMsg);
                    spnSelectLoc.requestFocus();
                } else if (TextUtils.isEmpty(description)) {
                    reportDesc.setError(errorMsg);
                    reportDesc.requestFocus();
                } else {
                    reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    maxId += 1;
                                }
                            }
                            report = new Report(issue, location, description, Integer.toString(maxId), user.getUserId());
                            reportRef.child(String.valueOf(maxId)).setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    new AlertDialog.Builder(ReportActivity.this)
                                            .setTitle("REPORT SUBMITTED")
                                            .setMessage("Your report has been submitted successfully!")
                                            .setPositiveButton("Back to Main", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).create().show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

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
        Intent getReportIntent = getIntent();
        Report report = (Report)getReportIntent.getSerializableExtra("reportObject");
        rootRef = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        reportRef = rootRef.child("Report");
        reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        maxId += 1;
                    }
                } else {
                    maxId = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        StorageReference fileRef = imgRef.child("Report/report_" + maxId + ".jpg");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerInside().into(btnUpload);
                        maxId = 1;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportActivity.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
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