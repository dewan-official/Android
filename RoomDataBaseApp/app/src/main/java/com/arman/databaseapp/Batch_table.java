package com.arman.databaseapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "batch_table")
public class Batch_table {

    @PrimaryKey(autoGenerate = true)
    public int bid;

    @ColumnInfo(name = "first_name")
    public String bacthName;
}
