package com.dewansoft.app.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URL;
import java.util.Random;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fm;
    private ImageView userImage,rlayout;
    private ImageButton selectImage;
    private TextView userName,userEmail,userPhone,userTitle,userFriends,joinDate;
    private DatabaseReference DB_REF;
    private StorageReference mStorageRef;
    private Uri uri;
    private static final int GALLERY_PICK = 1;
    private ProgressDialog progressBar;
    private int cheak = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        try {
            if(getIntent().getStringExtra("fragment").equals("1")){
                fm.beginTransaction().replace(R.id.home_fragment_container_id,new PhoneCallFragment()).commit();
            }else if(getIntent().getStringExtra("fragment").equals("2")){
                fm.beginTransaction().replace(R.id.home_fragment_container_id,new Contact()).commit();
            }else {
                fm.beginTransaction().replace(R.id.home_fragment_container_id,new messanger()).commit();
            }
        }catch (Exception e){
            fm.beginTransaction().replace(R.id.home_fragment_container_id,new messanger()).commit();
        }

        DB_REF = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        DB_REF.keepSynced(true);
        progressBar = new ProgressDialog(Home.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        userEmail = header.findViewById(R.id.current_user_email);
        userName = header.findViewById(R.id.current_user_name);
        userImage = header.findViewById(R.id.current_user_image);
        userPhone = header.findViewById(R.id.phone_id);
        userTitle = header.findViewById(R.id.title_id);
        userFriends = header.findViewById(R.id.total_friends);
        joinDate = header.findViewById(R.id.joid_date);
        selectImage = header.findViewById(R.id.select_img);
        rlayout = header.findViewById(R.id.user_cover_image);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cheak = 2;
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(galleryIntent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGES"), GALLERY_PICK);
            }
        });
        if(!(new SessionManager(Home.this).getPhone().isEmpty())){
            DB_REF.child(new SessionManager(Home.this).getPhone()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName.setText(dataSnapshot.child("name").getValue().toString());
                    userEmail.setText(dataSnapshot.child("email").getValue().toString());
                    if(!dataSnapshot.child("image").getValue().toString().equals("null")){
                        Picasso.get().load(dataSnapshot.child("image").getValue().toString()).placeholder(R.drawable.person).fit().into(userImage);
                    }
                    if(!dataSnapshot.child("cover_img").getValue().toString().equals("null")){
                        Picasso.get().load(dataSnapshot.child("cover_img").getValue().toString()).placeholder(R.drawable.back).fit().into(rlayout);
                    }
                    userPhone.setText("  "+dataSnapshot.child("phone").getValue().toString());
                    userTitle.setText("  "+dataSnapshot.child("story").getValue().toString());
                    joinDate.setText("  "+dataSnapshot.child("joindate").getValue().toString());
                    userFriends.setText("  Total Friends : 20");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(galleryIntent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGES"), GALLERY_PICK);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SessionManager sm = new SessionManager(Home.this);
        if(sm.getPhone().isEmpty()){
            startActivity(new Intent(Home.this,RegisterOrLogin.class));
            finish();
        }
        DB_REF.child(sm.getPhone()).child("status").setValue("online").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                   // Toast.makeText(Home.this,"Online",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SessionManager sm = new SessionManager(Home.this);
        DB_REF.child(sm.getPhone()).child("status").setValue("offline").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(Home.this,"offline",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String extension = "";
        if(cheak == 1){
            if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
                uri = data.getData();
                extension = data.getType();
                CropImage.activity(uri).setAspectRatio(1,1).start(this);
            }
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    uri = result.getUri();
                    progressBar.setTitle("Uploading..");
                    progressBar.setMessage("Please wait some time...");
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.show();
                    final StorageReference filepath = mStorageRef.child("profile_images").child(new SessionManager(Home.this).getPhone()+""+extension);
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(Home.this,"Uploaded."+filepath,Toast.LENGTH_SHORT).show();
                            DB_REF.child(new SessionManager(Home.this).getPhone()).child("image").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressBar.dismiss();
                                        Toast.makeText(Home.this,"Uploaded.",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(Home.this,"Not Uploaded.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Home.this,"Error:"+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                }
            }
        }else{
            if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
                uri = data.getData();
                extension = data.getType();
                CropImage.activity(uri).setAspectRatio(10,7).start(this);
            }
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    uri = result.getUri();
                    progressBar.setTitle("Uploading..");
                    progressBar.setMessage("Please wait some time...");
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.show();
                    final StorageReference filepath = mStorageRef.child("Cover_image").child(new SessionManager(Home.this).getPhone()+""+extension);
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(Home.this,"Uploaded."+filepath,Toast.LENGTH_SHORT).show();
                            DB_REF.child(new SessionManager(Home.this).getPhone()).child("cover_img").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressBar.dismiss();
                                        Toast.makeText(Home.this,"Uploaded.",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(Home.this,"Not Uploaded.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Home.this,"Error:"+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.phoneCall_menu_btn_id){
            fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.home_fragment_container_id,new PhoneCallFragment()).commit();
        }
        if(id == R.id.contact_menu_btn_id){
            fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.home_fragment_container_id,new Contact()).commit();
        }
        if(id == R.id.chat_menu_btn_id){
            fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.home_fragment_container_id,new messanger()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.acc_settings) {
            // Handle the camera action
        } else if (id == R.id.logout_btn) {
            SessionManager sm = new SessionManager(Home.this);
            DB_REF.child(sm.getPhone()).child("status").setValue("offline").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //Toast.makeText(Home.this,"offline",Toast.LENGTH_LONG).show();
                    }
                }
            });
            new SessionManager(Home.this).logout();
            startActivity(new Intent(Home.this,RegisterOrLogin.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}