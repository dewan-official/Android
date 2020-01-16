package com.arman.roomdbtestcrud.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.arman.roomdbtestcrud.entity.Tbl_Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM tbl_contact")
    public List<Tbl_Contact> getAllContacts();

    @Insert
    public void insert(Tbl_Contact contact);

}
