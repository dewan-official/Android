package com.example.personalcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText username,userPhone,userDob,userEmail,userPass,userRePass;
    private RadioButton userMale,userFemale;
    private Button login,signUp;
    private FirebaseAuth mAuth;
    private String gender;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressBar = new ProgressDialog(SignUp.this);
        username = findViewById(R.id.r_username);
        userPhone = findViewById(R.id.r_userphone);
        userDob = findViewById(R.id.r_user_dob);
        userEmail = findViewById(R.id.r_useremail);
        userPass = findViewById(R.id.r_userpass);
        userRePass = findViewById(R.id.r_userrepass);
        userMale = findViewById(R.id.userGMale);
        userFemale = findViewById(R.id.userGFemale);
        login = findViewById(R.id.r_login_btn);
        signUp = findViewById(R.id.r_signup_btn);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            progressBar.setTitle("Creating Account");
            progressBar.setMessage("Please wait some time...");
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();
            if(username.getText().toString().isEmpty() || userPhone.getText().toString().isEmpty() || userEmail.getText().toString().isEmpty()
            || userPass.getText().toString().isEmpty() || userRePass.getText().toString().isEmpty()){
                Toast.makeText(SignUp.this, "All Feild Need to be Fillup.", Toast.LENGTH_LONG).show();
                progressBar.dismiss();
            }else {
                if (userMale.isChecked() || userFemale.isChecked()){
                    final String name = username.getText().toString();
                    final String phone = userPhone.getText().toString();
                    final String dob = userDob.getText().toString();
                    final String email = userEmail.getText().toString();
                    final String pass = userPass.getText().toString();
                    String repass = userRePass.getText().toString();
                    gender = "Male";
                    if(pass.equals(repass)){
                        if(userFemale.isChecked()){
                            gender = "Female";
                        }
                        final String finalGender = gender;
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            // Write a message to the database
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference("user_info").child(user.getUid());
                                            myRef.child("Name").setValue(name);
                                            myRef.child("Phone").setValue(phone);
                                            myRef.child("Birthday").setValue(dob);
                                            myRef.child("Email").setValue(email);
                                            myRef.child("Gender").setValue( gender);
                                            myRef.child("proImage").setValue( "none");
                                            mAuth.signOut();
                                            progressBar.dismiss();
                                            startActivity(new Intent(SignUp.this, Login.class));
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignUp.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                            progressBar.dismiss();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(SignUp.this, "Re-Pass does'nt match.", Toast.LENGTH_LONG).show();
                        progressBar.dismiss();
                    }

                }else {
                    Toast.makeText(SignUp.this, "Select your Gender.", Toast.LENGTH_LONG).show();
                    progressBar.dismiss();
                }
            }
            }
        });
    }
}
