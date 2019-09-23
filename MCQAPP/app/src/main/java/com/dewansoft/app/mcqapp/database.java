package com.dewansoft.app.mcqapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class database extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private static final int version = 1;
    private static final String db_name = "Mcq_App";

    private static final String tablenames = "tablenames";
    private static final String tablename = "table_name";
    private static final String tablenamesCreate = "CREATE TABLE "+tablenames+"( id INTEGER PRIMARY KEY AUTOINCREMENT,"+ tablename+" VARCHAR(255) NOT NULL);";


    private static String tb_name;
    private static final String ques = "question";
    private static final String op1 = "option_1";
    private static final String op2 = "option_2";
    private static final String op3 = "option_3";
    private static final String op4 = "option_4";
    private static final String ans = "answer";
    private static final String createTable = "CREATE TABLE "+ tb_name+"( id INTEGER PRIMARY KEY AUTOINCREMENT, "+ ques +" VARCHAR(100) NOT NULL, "+ op1 +" VARCHAR(100) NOT NULL, "+ op2 +" VARCHAR(100) NOT NULL, "+ op3 +" VARCHAR(100) NOT NULL, "+ op4 +" VARCHAR(100) NOT NULL, "+ ans +" VARCHAR(100) NOT NULL );";

    public database(Context context) {
        super(context, db_name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        try {
            sqLiteDatabase.execSQL(tablenamesCreate);
            Toast.makeText(context,"Table Created",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context,"Table Not Created "+e,Toast.LENGTH_SHORT).show();
        }
    }

    public boolean customTableCreate(String tb_name){
        sqLiteDatabase = this.getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("CREATE TABLE "+ tb_name+"( id INTEGER PRIMARY KEY AUTOINCREMENT, "+ ques +" VARCHAR(100) NOT NULL, "+ op1 +" VARCHAR(100) NOT NULL, "+ op2 +" VARCHAR(100) NOT NULL, "+ op3 +" VARCHAR(100) NOT NULL, "+ op4 +" VARCHAR(100) NOT NULL, "+ ans +" VARCHAR(100) NOT NULL );");
            Toast.makeText(context,"Table Created",Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            Toast.makeText(context,"Table Not Created : "+e,Toast.LENGTH_LONG).show();
            return false;
        }
    }
    Cursor cursor;
    public Cursor getData(String table_name){
        String hello = "SELECT * FROM "+ table_name +";";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            cursor = sqLiteDatabase.rawQuery(hello,null);
        } catch (Exception e){

        }
        return cursor;
    }

    long rowid;
    public long insertTableName(String table_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(tablename,table_name);
        try{
            rowid = sqLiteDatabase.insert(tablenames,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,""+e,Toast.LENGTH_SHORT).show();
        }
        return rowid;
    }

    public long insertData(String[] data, String table_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ques,data[0]);
        contentValues.put(op1,data[1]);
        contentValues.put(op2,data[2]);
        contentValues.put(op3,data[3]);
        contentValues.put(op4,data[4]);
        contentValues.put(ans,data[5]);
        try{
            rowid = sqLiteDatabase.insert(table_name,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,""+e,Toast.LENGTH_SHORT).show();
        }
        return rowid;
    }
    public Integer deleteRow(String tableName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(tablenames,tablename+" = ?",new String[] {tableName});
    }
    public boolean deleteTable(String tableName){
        Boolean xxx = false;
        String deleteQuery = "Drop Table "+tableName+";";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            sqLiteDatabase.execSQL(deleteQuery);
            xxx = true;
        }catch (Exception e){
        }
        return xxx;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
