package com.dewansoft.app.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Register extends Fragment {
    private Button signupBtn,loginBtn;
    private TextInputEditText nameField,phoneField,emailField,passField,rePassField;
    private DatabaseReference DB_REF;
    private ProgressDialog progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Firebase Refernce
        DB_REF = FirebaseDatabase.getInstance().getReference().child("Users");
        //TextInputEditText
        nameField = view.findViewById(R.id.nameField_id);
        phoneField = view.findViewById(R.id.phoneField_id);
        emailField = view.findViewById(R.id.emailField_id);
        passField = view.findViewById(R.id.passwordField_id);
        rePassField = view.findViewById(R.id.re_passwordField_id);

        //Button
        signupBtn = view.findViewById(R.id.register_btn_id);
        loginBtn = view.findViewById(R.id.login_btn_id);

        //ProgressDialog
        progressBar = new ProgressDialog(getContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginProcess();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setTitle("Creating Account..");
                progressBar.setMessage("Please wait some time...");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.show();
                startRegistationProcess();
            }
        });
        return view;
    }

    private void startLoginProcess() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.register_or_login_framelayout_id,new Login()).commit();
    }
    private void startRegistationProcess() {
        final String name = nameField.getText().toString();
        final String phone = phoneField.getText().toString();
        final String email = emailField.getText().toString();
        final String pass = passField.getText().toString();
        String repass = rePassField.getText().toString();
        final String date = new DateClass(getContext()).getDate();
        if(name.isEmpty()||phone.isEmpty()||email.isEmpty()||pass.isEmpty()||repass.isEmpty()){
            Toast.makeText(getActivity(),"You can't put Any Field Empty",Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
        }else {
            if(pass.contains(repass)){


                //DATA SEND TO SERVER
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ds-call.000webhostapp.com/DS_CALL_DEWAN_SOFT/signup.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String x = response.toString();
                                if (x.equals("true")){
                                    HashMap<String ,String> hashMap = new HashMap<String ,String>();
                                    hashMap.put("name",name);
                                    hashMap.put("phone",phone);
                                    hashMap.put("email",email);
                                    hashMap.put("password",pass);
                                    hashMap.put("story","Now I Am A DS CALL User..");
                                    hashMap.put("status","Active");
                                    hashMap.put("dob","null");
                                    hashMap.put("joindate",date);
                                    hashMap.put("image","null");
                                    hashMap.put("cover_img","null");
                                    DB_REF.child(phone).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressBar.dismiss();
                                                FragmentManager fm = getFragmentManager();
                                                fm.beginTransaction().replace(R.id.register_or_login_framelayout_id,new Login()).commit();
                                            }else {
                                                progressBar.dismiss();
                                                Toast.makeText(getContext(),"Some Problem Occur..Try Again..",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else {
                                    progressBar.dismiss();
                                    Toast.makeText(getActivity(),"Some Problem Occur..",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Error...",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> values = new HashMap<String ,String>();
                        values.put("user_phone",phone);
                        values.put("user_password",pass);
                        return values;
                    }
                };
                NewUserRegister.getInstance(getActivity()).addRequstQueue(stringRequest);
            }else {
                Toast.makeText(getContext(),"Re-password Don't Match..",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
