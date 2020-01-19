package com.arman.databaseapp;

public interface DeleteItem {
    void delete_item(int id, int position);
    void update_item(String fname, String lname, String batch, int position, int id);
    void insert_item(String fname, String lname, String batch, int position);
}
