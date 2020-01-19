package com.arman.databaseapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Batch_table.class}, version = 1)
public abstract class AppData extends RoomDatabase {
    public abstract UserDao userDao();
}
