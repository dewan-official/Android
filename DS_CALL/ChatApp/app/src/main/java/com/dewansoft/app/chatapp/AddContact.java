package com.dewansoft.app.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddContact extends AppCompatActivity {
    private DatabaseReference DB_REF;
    private StorageReference storageReference;
    private EditText name,phone,email,image,decription;
    private Button add_contact_btn,add_contact_cancel_btn;
    private  HashMap<String ,String > hashMap;
    private ProgressDialog progressBar;
    private Uri uri;
    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        DB_REF = FirebaseDatabase.getInstance().getReference().child("Contacts");
        storageReference = FirebaseStorage.getInstance().getReference();
        hashMap = new HashMap<String ,String >();
        progressBar = new ProgressDialog(AddContact.this);
        name = findViewById(R.id.new_contact_name);
        phone = findViewById(R.id.new_contact_phone);
        email = findViewById(R.id.new_contact_email);
        image = findViewById(R.id.new_contact_img);
        decription = findViewById(R.id.new_contact_decription);
        add_contact_btn = findViewById(R.id.add_contact_btn);
        add_contact_cancel_btn = findViewById(R.id.add_contact_cancel_btn);

        //if image field is clicked
        image.setClickable(true);
        image.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(galleryIntent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGES"), GALLERY_PICK);
            }
        });
        //if cancel Button Clicked
        add_contact_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContact.this,Home.class);
                intent.putExtra("fragment","2");
                startActivity(intent);
                finish();
            }
        });

        //if add contact btn clicked
        add_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameValue = name.getText().toString();
                final String phoneValue = phone.getText().toString();
                String emailValue = email.getText().toString();
                String imageValue = image.getText().toString();
                String decriptionValue = decription.getText().toString();

                if(nameValue.isEmpty()||phoneValue.isEmpty()){
                    Toast.makeText(AddContact.this,"Name and Phone is required",Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setTitle("Uploading..");
                    progressBar.setMessage("Please wait some time...");
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.show();
                    if(emailValue.isEmpty()){
                       if(imageValue.isEmpty()){
                           if(decriptionValue.isEmpty()){
                               hashMap.put("name",nameValue);
                               hashMap.put("phone",phoneValue);
                               hashMap.put("image","null");
                               DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                           progressBar.dismiss();
                                           Intent intent = new Intent(AddContact.this,Home.class);
                                           intent.putExtra("fragment","2");
                                           startActivity(intent);
                                           finish();
                                       }
                                   }
                               });
                           }else {
                               hashMap.put("name",nameValue);
                               hashMap.put("phone",phoneValue);
                               hashMap.put("description",decriptionValue);
                               hashMap.put("image","null");
                               DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                           progressBar.dismiss();
                                           Intent intent = new Intent(AddContact.this,Home.class);
                                           intent.putExtra("fragment","2");
                                           startActivity(intent);
                                           finish();
                                       }
                                   }
                               });
                           }
                       }else {
                           if(decriptionValue.isEmpty()){
                               hashMap.put("name",nameValue);
                               hashMap.put("phone",phoneValue);
                               hashMap.put("image",imageValue);
                               DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                           progressBar.dismiss();
                                           startActivity(new Intent(AddContact.this,Home.class));
                                           finish();
                                       }
                                   }
                               });
                           }else {
                               hashMap.put("name",nameValue);
                               hashMap.put("phone",phoneValue);
                               hashMap.put("description",decriptionValue);
                               hashMap.put("image",imageValue);
                               DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                           progressBar.dismiss();
                                           startActivity(new Intent(AddContact.this,Home.class));
                                           finish();
                                       }
                                   }
                               });
                           }
                       }
                    }else {
                        if(emailValue.isEmpty()){
                            if (imageValue.isEmpty()){
                                if(decriptionValue.isEmpty()){
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("email",emailValue);
                                    hashMap.put("image","null");
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                Intent intent = new Intent(AddContact.this,Home.class);
                                                intent.putExtra("fragment","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }else {
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("description",decriptionValue);
                                    hashMap.put("email",emailValue);
                                    hashMap.put("image","null");
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                startActivity(new Intent(AddContact.this,Home.class));
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }else {
                                if(decriptionValue.isEmpty()){
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("image",imageValue);
                                    hashMap.put("email",emailValue);
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                Intent intent = new Intent(AddContact.this,Home.class);
                                                intent.putExtra("fragment","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }else {
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("description",decriptionValue);
                                    hashMap.put("image",imageValue);
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                Intent intent = new Intent(AddContact.this,Home.class);
                                                intent.putExtra("fragment","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        }else {
                            if(imageValue.isEmpty()){
                                if(decriptionValue.isEmpty()){
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("email",emailValue);
                                    hashMap.put("image","null");
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                Intent intent = new Intent(AddContact.this,Home.class);
                                                intent.putExtra("fragment","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }else {
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("description",decriptionValue);
                                    hashMap.put("email",emailValue);
                                    hashMap.put("image","null");
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                Intent intent = new Intent(AddContact.this,Home.class);
                                                intent.putExtra("fragment","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }else {
                                if(decriptionValue.isEmpty()){
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("email",emailValue);
                                    hashMap.put("image",imageValue);
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                Intent intent = new Intent(AddContact.this,Home.class);
                                                intent.putExtra("fragment","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }else {
                                    hashMap.put("name",nameValue);
                                    hashMap.put("phone",phoneValue);
                                    hashMap.put("description",decriptionValue);
                                    hashMap.put("email",emailValue);
                                    hashMap.put("image",imageValue);
                                    DB_REF.child(new SessionManager(AddContact.this).getPhone()).child(phoneValue).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddContact.this,"Successful.",Toast.LENGTH_SHORT).show();
                                                progressBar.dismiss();
                                                Intent intent = new Intent(AddContact.this,Home.class);
                                                intent.putExtra("fragment","2");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    String extention;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            uri = data.getData();
            CropImage.activity(uri).setAspectRatio(1,1).start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                uri = result.getUri();
                progressBar.setTitle("Processing...");
                progressBar.setMessage("Please wait some time...");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.show();
                extention = uri.toString();
                extention = extention.substring(extention.length() - 4);
                final StorageReference filepath = storageReference.child("Contact_pro_img").child( new SessionManager(AddContact.this).getPhone()+""+extention);
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        image.setText(downloadUrl+"");
                        image.setEnabled(false);
                        progressBar.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddContact.this,"Some problem occur.",Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }
}
