package com.arman.databaseapp;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM st_table")
    List<User> getAll();

    @Query("SELECT * FROM st_table WHERE batch_name = :batchName")
    List<User> getSpacific(String batchName);

    @Query("SELECT * FROM batch_table")
    List<Batch_table> getBatchAll();

    @Insert
    void insert(User st_table);

    @Insert
    void insert2(Batch_table batch_table);

    @Query("DELETE FROM st_table WHERE uid = :id")
    void delete(int id);

    @Query("UPDATE st_table SET first_name= :fname, last_name = :lname, batch_name = :batch WHERE uid = :id")
    void update(String fname, String lname, String batch, int id);
}