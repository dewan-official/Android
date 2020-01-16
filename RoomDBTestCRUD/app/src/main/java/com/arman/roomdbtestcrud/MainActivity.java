package com.arman.roomdbtestcrud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arman.roomdbtestcrud.dao.ContactDao;
import com.arman.roomdbtestcrud.db.MyDatabase;
import com.arman.roomdbtestcrud.entity.Tbl_Contact;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyDatabase myDatabase;

    EditText etName, etEmail, etPhone;
    Button btnAdd, btnCountRow;

    ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = Room.databaseBuilder(MainActivity.this,
                MyDatabase.class, "my_database")
                .allowMainThreadQueries()
                .build();

        contactDao = myDatabase.contactDao();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnAdd = findViewById(R.id.btnAdd);
        btnCountRow = findViewById(R.id.btnCountRow);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();

                Tbl_Contact contact = new Tbl_Contact();
                contact.name = name;
                contact.email = email;
                contact.phone = phone;

                contactDao.insert(contact);
            }
        });

        btnCountRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Tbl_Contact> allContacts = contactDao.getAllContacts();
                Toast.makeText(MainActivity.this, "Row Count: " + allContacts.size(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
