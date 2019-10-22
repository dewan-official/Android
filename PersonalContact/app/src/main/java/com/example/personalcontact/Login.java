package com.example.personalcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText useremail,userpass;
    private Button login,signup;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = new ProgressDialog(Login.this);
        mAuth = FirebaseAuth.getInstance();
        useremail = findViewById(R.id.userEmail);
        userpass = findViewById(R.id.userpass);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setTitle("Login");
                progressBar.setMessage("Please wait some time..");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.show();
                if (useremail.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Enter Email Address", Toast.LENGTH_LONG).show();
                    progressBar.dismiss();
                }else {
                    if(userpass.getText().toString().isEmpty()){
                        Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_LONG).show();
                    }else {
                        mAuth.signInWithEmailAndPassword(useremail.getText().toString(), userpass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.dismiss();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.dismiss();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user  != null){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }
}
