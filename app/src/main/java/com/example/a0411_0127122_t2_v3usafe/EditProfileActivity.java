package com.example.a0411_0127122_t2_v3usafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements ChangeInfoDialog.UsernameDialogueInputListener, ChangeInfoDialog.PasswordDialogueInputListener {
    private DatabaseReference ref;
    private StorageReference imgRef;
    private ImageButton btnBack;
    private CircleImageView profilePic;
    private Button btnChgProfPic, btnChgUserName, btnChgPassword, btnUpdate, btnDeleteProfile;
    private TextView tvName, tvEmail, tvPass;
    private String userName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        btnBack = (ImageButton)findViewById(R.id.btn_back_edit_profile);
        profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        btnChgProfPic = (Button)findViewById(R.id.btn_change_profilepic);
        btnChgUserName = (Button)findViewById(R.id.btn_change_username);
        btnChgPassword = (Button)findViewById(R.id.btn_change_password);
        btnUpdate = (Button)findViewById(R.id.btn_update);
        btnDeleteProfile= (Button)findViewById(R.id.btn_delete_account);
        tvName = (TextView)findViewById(R.id.tv_name);
        tvEmail = (TextView)findViewById(R.id.tv_email);
        tvPass = (TextView)findViewById(R.id.tv_password);

        // Retrieving data from firebase real-time db.
        ref = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName =  snapshot.child(user.getUserId()).child("userName").getValue().toString();
                String email = snapshot.child(user.getUserId()).child("email").getValue().toString();
                String password = snapshot.child(user.getUserId()).child("password").getValue().toString();

                user.setUserName(userName);
                user.setEmail(email);
                user.setPassword(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userName = user.getUserName();
        email = user.getEmail();
        password = user.getPassword();

        tvName.setText(userName);
        tvEmail.setText(email);
        tvPass.setText(displayMaskedPw(password));

        //Load saved image from firebase firestore database.
        imgRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = imgRef.child("User/" + user.getUserId() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        btnChgProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

                imgRef = FirebaseStorage.getInstance().getReference();
            }
        });

        btnChgUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Username");
            }
        });

        btnChgPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Password");
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(tvName.getText().toString(), password);
            }
        });

        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("PROFILE DELETE CONFIRMATION")
                        .setMessage("Are you sure you want to delete your account? You won't be able to sign in again.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProfile();
                                Intent intent = new Intent(EditProfileActivity.this, CoverActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", null).show();
            }
        });
    }

    public void applyUserName(String newUserName, String name) {
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        if(name.equals(user.getPassword())) {
            tvName.setText(newUserName);
            tvName.setTextColor(Color.parseColor("#FFA500"));
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("VERIFICATION FAILED")
                    .setMessage("Incorrect Password Entered. Please try again.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openDialog("Username");
                        }
                    }).setNegativeButton("Close", null).show();
        }
    }

    public void applyPw(String newPw, String pw) {
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        if(pw.equals(user.getPassword())) {
            password = newPw;
            tvPass.setText(displayMaskedPw(newPw));
            tvPass.setTextColor(Color.parseColor("#FFA500"));
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("VERIFICATION FAILED")
                    .setMessage("Incorrect Password Entered. Please try again.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openDialog("Username");
                        }
                    }).setNegativeButton("Close", null).show();
        }
    }

    public void openDialog(String title){
        ChangeInfoDialog inputDialogue = new ChangeInfoDialog(title);
        inputDialogue.show(getSupportFragmentManager(), "Dialog");
    }

    public String displayMaskedPw(String pw){
        String masked = "";

        for(int i = 0; i < pw.length(); i++){
            masked = masked + "*";
        }

        return masked;
    }

    public void updateInfo(String userName, String password){
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");
        HashMap UpdateUser = new HashMap();
        UpdateUser.put("userName", userName);
        UpdateUser.put("password", password);


        ref = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");
        ref.child(user.getUserId()).updateChildren(UpdateUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    tvName.setTextColor(Color.parseColor("#000000"));
                    tvPass.setTextColor(Color.parseColor("#000000"));
                    user.setUserName(userName);
                    user.setPassword(password);
                    Toast.makeText(EditProfileActivity.this, "Update Successful.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditProfileActivity.this, "Update Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteProfile(){
        Intent getUserIntent = getIntent();
        User user = (User)getUserIntent.getSerializableExtra("userObject");

        ref = FirebaseDatabase.getInstance("https://usafe---0127122-a31c2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(user.getUserId());
        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Account Deleted.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditProfileActivity.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                }
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
        StorageReference fileRef = imgRef.child("User/" + user.getUserId() + "/profile.jpg");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}