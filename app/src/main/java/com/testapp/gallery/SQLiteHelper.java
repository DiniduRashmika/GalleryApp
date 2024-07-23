package com.testapp.gallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "galleryDB";
    private static final int DB_VERSION = 1;

    public SQLiteHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {

        String userQuery = "CREATE TABLE users ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "picture TEXT,"
                + "name TEXT,"
                + "email TEXT,"
                + "password TEXT)";

        String captureQuery = "CREATE TABLE captures ("
                + "img_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id TEXT,"
                + "path TEXT,"
                + "timestamp TEXT,"
                + "lat TEXT,"
                + "lon TEXT)";

        db.execSQL(captureQuery);
        db.execSQL(userQuery);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS captures");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public void addCapture(String userid, String path, String timestamp, String lat, String lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userid);
        values.put("path", path);
        values.put("timestamp", timestamp);
        values.put("lat", lat);
        values.put("lon", lon);
        db.insert("captures", null, values);
        db.close();
    }

    public void addUser(String pic, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("picture", pic);
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        db.insert("users", null, values);
        db.close();
    }

    public boolean checkUserExists(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { "email" };
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = { email, password };
        Cursor cursor = db.query(
                "users", columns, selection, selectionArgs, null, null, null
        );

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name"};
        String selection = "email = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(
                "users", columns, selection, selectionArgs, null, null, null
        );

        String username = null;
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        db.close();

        return username;
    }

    public String getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"user_id"};
        String selection = "email = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(
                "users", columns, selection, selectionArgs, null, null, null
        );

        String userid = null;
        if (cursor.moveToFirst()) {
            userid = cursor.getString(cursor.getColumnIndex("user_id"));
        }
        cursor.close();
        db.close();

        return userid;
    }

    public String getUserNameById(String userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name"};
        String selection = "user_id = ?";
        String[] selectionArgs = { userid };
        Cursor cursor = db.query(
                "users", columns, selection, selectionArgs, null, null, null
        );

        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        db.close();

        return name;
    }


}



