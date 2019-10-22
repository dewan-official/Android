package com.example.personalcontact;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CircleImageView add_btn;
    private AlertDialog alert;
    private LayoutInflater layoutInflater;
    private Uri uri;
    private RecyclerView recyclerView;
    private ViewControler viewControler;
    private HashMap<String, String> hashMap;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(mAuth.getCurrentUser()!= null){
            mStorageRef = FirebaseStorage.getInstance().getReference();
            myRef = database.getReference("Contacts").child(user.getUid());
        }
        add_btn = findViewById(R.id.add_contact);
        recyclerView = findViewById(R.id.recycleViewId);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        hashMap  = new HashMap<String, String>();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alert = new AlertDialog.Builder(MainActivity.this).create();
            View view = alert.getLayoutInflater().inflate(R.layout.new_contact_entry, null, false);
            alert.setView(view);
            alert.show();
            Button cancel = view.findViewById(R.id.con_entry_cancel_btn);
            Button imgSeclect = view.findViewById(R.id.con_image_select_btn);
            Button submit = view.findViewById(R.id.con_entry_submit_btn);
            final EditText conName = view.findViewById(R.id.con_entry_name);
            final EditText conPhone = view.findViewById(R.id.con_entry_phone);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            imgSeclect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i,"SELECT IMAGES"),1);
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                alert.dismiss();
                progressDialog.setTitle("Creating New Contact");
                progressDialog.setMessage("Please wait some time...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                if(conName.getText().toString().isEmpty() || conPhone.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Fillup Contact Name And Phone Number", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }else {
                    if(uri != null){
                        int time = (int) (System.currentTimeMillis());
                        StorageReference riversRef = mStorageRef.child("Contacts").child(user.getUid()).child(new Timestamp(time).toString().concat(".jpg"));
                        riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                alert.dismiss();
                                // Get a URL to the uploaded content
                                Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String path = uri.toString();
                                        hashMap.put("Name", conName.getText().toString());
                                        hashMap.put("Phone", conPhone.getText().toString());
                                        hashMap.put("image", path);
                                        myRef.push().setValue(hashMap);
                                        Toast.makeText(MainActivity.this, "Successfully Added!", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(MainActivity.this, "Add Contact Failed!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });

                    }else {
                        hashMap.put("Name", conName.getText().toString());
                        hashMap.put("Phone", conPhone.getText().toString());
                        hashMap.put("image", "none");
                        myRef.push().setValue(hashMap);
                        Toast.makeText(MainActivity.this, "Successfully Added!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
                }
            });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }else {
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<DataProcessor> list = new ArrayList<DataProcessor>();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        DataProcessor data = dataSnapshot1.getValue(DataProcessor.class);
                        data.setKey(dataSnapshot1.getKey());
                        list.add(data);
                    }
                    viewControler = new ViewControler(MainActivity.this, list);
                    recyclerView.setAdapter(viewControler);
                    viewControler.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(MainActivity.this, "Some Error Occur.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, Profile.class));
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String extension = "";
        if(requestCode==1 && resultCode == RESULT_OK){
            uri = data.getData();
            CropImage.activity(uri).setAspectRatio(1,1).start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                uri = result.getUri();
            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }
}
