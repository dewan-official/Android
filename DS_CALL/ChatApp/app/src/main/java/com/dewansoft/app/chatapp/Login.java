package com.dewansoft.app.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment {
    private Button signupBtn,loginBtn;
    private TextInputEditText phoneField,passField;
    private DatabaseReference DB_REF;
    private String cheakpass=null;
    private ProgressDialog progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //FirebaseReference
        DB_REF = FirebaseDatabase.getInstance().getReference().child("Users");
        //TextInputEditText
        phoneField = view.findViewById(R.id.login_phoneField_id);
        passField = view.findViewById(R.id.login_passwordField_id);

        //Button
        signupBtn = view.findViewById(R.id.register_btn_id);
        loginBtn = view.findViewById(R.id.login_btn_id);

        //ProgressDialog
        progressBar = new ProgressDialog(getContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setTitle("Login Process...");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.show();
                startLoginProcess();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistationProcess();
            }
        });

        return view;
    }
    private void startLoginProcess() {
        final String phone = phoneField.getText().toString();
        final String pass = passField.getText().toString();
        if (phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getActivity(), "You can't put Any Field Empty", Toast.LENGTH_SHORT).show();
        } else {
            //DATA SEND TO SERVER
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ds-call.000webhostapp.com/DS_CALL_DEWAN_SOFT/cheak_user_data.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String x = response.toString();
                            if (x.equals("true")){
                                Toast.makeText(getActivity(),"Successful..",Toast.LENGTH_SHORT).show();
                                SessionManager sm = new SessionManager(getActivity());
                                sm.setPhone(phone);
                                progressBar.dismiss();
                                Intent intent = new Intent(getActivity(),Home.class);
                                intent.putExtra("fragment","1");
                                startActivity(intent);
                                getActivity().finish();
                            }else {
                                progressBar.dismiss();
                                Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.dismiss();
                    Toast.makeText(getActivity(),"Error..."+error,Toast.LENGTH_SHORT).show();
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
        }
    }
    private void startRegistationProcess() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.register_or_login_framelayout_id,new Register()).commit();
    }
}
