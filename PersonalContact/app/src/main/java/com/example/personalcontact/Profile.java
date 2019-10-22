package com.example.personalcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.sql.Timestamp;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private Button edit_btn, pro_img_edit, home_intent;
    private TextView name, phone, email,dob, gender;
    private ImageView imageView;
    private Uri uri;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private String pName,pPhone, pGender, pBirthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = new ProgressDialog(Profile.this);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(mAuth.getCurrentUser()!= null){
            mStorageRef = FirebaseStorage.getInstance().getReference();
            myRef = database.getReference("user_info").child(user.getUid());
        }
        edit_btn = findViewById(R.id.edit_profile_id);
        pro_img_edit = findViewById(R.id.pro_img_upload);
        home_intent = findViewById(R.id.home_intent_btn);

        imageView = findViewById(R.id.pro_img);

        name = findViewById(R.id.pro_name);
        phone = findViewById(R.id.pro_phone);
        email = findViewById(R.id.pro_email);
        dob = findViewById(R.id.pro_dob);
        gender = findViewById(R.id.pro_gender);

        home_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });

        pro_img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i,"SELECT IMAGES"),1);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataProcessor dataProcessor;
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    dataProcessor = dataSnapshot.getValue(DataProcessor.class);
                    pName = dataProcessor.getName();
                    pPhone = dataProcessor.getPhone();
                    pBirthday = dataProcessor.getBirthday();
                    pGender = dataProcessor.getGender();
                    name.setText(pName);
                    phone.setText(pPhone);
                    dob.setText(pBirthday);
                    email.setText(dataProcessor.getEmail());
                    gender.setText(pGender);
                    Picasso.get().load(dataProcessor.getProImage()).error(R.drawable.person).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Profile.this, "Some Problem Occur.", Toast.LENGTH_LONG);
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Saving");
                progressDialog.setMessage("Please wait some time...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                final AlertDialog alertDialog = new AlertDialog.Builder(Profile.this).create();
                View view = alertDialog.getLayoutInflater().inflate(R.layout.activity_sign_up, null, false);
                alertDialog.setView(view);
                final EditText xname = view.findViewById(R.id.r_username);
                final EditText xphone = view.findViewById(R.id.r_userphone);
                EditText xemail = view.findViewById(R.id.r_useremail);
                final EditText xdob = view.findViewById(R.id.r_user_dob);
                TextView xtitle = view.findViewById(R.id.r_title);
                EditText xpass = view.findViewById(R.id.r_userpass);
                EditText xrepass = view.findViewById(R.id.r_userrepass);
                final RadioButton male = view.findViewById(R.id.userGMale);
                final RadioButton female = view.findViewById(R.id.userGFemale);
                Button save = view.findViewById(R.id.r_signup_btn);
                Button cancel = view.findViewById(R.id.r_login_btn);


                xemail.setVisibility(View.GONE);
                xpass.setVisibility(View.GONE);
                xrepass.setVisibility(View.GONE);
                xtitle.setText("EDIT PROFILE");
                save.setText("SAVE");
                cancel.setText("CANCEL");

                xname.setText(pName);
                xphone.setText(pPhone);
                xdob.setText(pBirthday);
                if(pGender.equals("Male")){
                    male.setChecked(true);
                }else {
                    female.setChecked(true);
                }

                alertDialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(xname.getText().toString().equals("") || xphone.getText().toString().equals("") || xdob.getText().toString().equals("")){
                            Toast.makeText(Profile.this, "Fill all the Fields.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }else {
                            String xy = "";
                            if(male.isChecked()){
                                xy = "Male";
                            }else if(female.isChecked()){
                                xy = "Female";
                            }else {
                                Toast.makeText(Profile.this, "Select Gender", Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                                progressDialog.dismiss();
                            }
                            if(!xy.equals("")){
                                myRef.child("Name").setValue(xname.getText().toString());
                                myRef.child("Phone").setValue(xphone.getText().toString());
                                myRef.child("Birthday").setValue(xdob.getText().toString());
                                myRef.child("Gender").setValue(xy);
                                alertDialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(Profile.this, "Saved.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK){
            uri = data.getData();
            CropImage.activity(uri).setAspectRatio(1,1).start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                uri = result.getUri();
                if(uri != null) {
                    progressDialog.setTitle("Uploading");
                    progressDialog.setMessage("Please wait some time...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    StorageReference riversRef = mStorageRef.child("profile").child(mAuth.getCurrentUser().getUid().concat(".jpg"));
                    riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                            String path = uri.toString();
                            myRef.child("proImage").setValue(path).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Profile.this, "Successfully Added!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            });
                            }
                        });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(Profile.this, "Add Contact Failed!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser().getUid() == null){
            startActivity(new Intent(Profile.this, Login.class));
            finish();
        }
    }
}
