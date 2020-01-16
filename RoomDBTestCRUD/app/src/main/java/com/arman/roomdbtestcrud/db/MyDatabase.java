package com.arman.roomdbtestcrud.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.arman.roomdbtestcrud.dao.ContactDao;
import com.arman.roomdbtestcrud.entity.Tbl_Contact;

@Database(entities = {Tbl_Contact.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
}
