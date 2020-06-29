package com.pedromihael.desmascarandoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext = null;
    private static final String DATABASE_NAME = "desmascarando.db";
    private static final Integer DATABASE_VERSION = 2;
    SQLiteDatabase db = null;

    public DatabaseHelper() { super(null, DATABASE_NAME, null, DATABASE_VERSION); }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    private final String dropUserTable = "DROP TABLE IF EXISTS user;";
    private final String dropPostTable = "DROP TABLE IF EXISTS post;";

    private final String createTableUser =
            "CREATE TABLE user ( " +
                    "name VARCHAR(128) NOT NULL, " +
                    "email VARCHAR(128) NOT NULL, " +
                    "password VARCHAR(128) NOT NULL, " +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " );";
    private final String createTablePost =
            "CREATE TABLE post ( " +
                    "time VARCHAR(128) NOT NULL, " +
                    "longitude DOUBLE NOT NULL, " +
                    "latitude DOUBLE NOT NULL, " +
                    "post_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id_fk INTEGER, FOREIGN KEY (user_id_fk) REFERENCES user (user_id)" +
                    " );";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTableUser);
        sqLiteDatabase.execSQL(createTablePost);
        this.db = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(dropUserTable);
        sqLiteDatabase.execSQL(dropPostTable);
        this.onCreate(sqLiteDatabase);
    }

    public boolean addUser(User user) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", user.getName());
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());

        try {
            db.insert("user", null, cv);
            return true;
        } catch (SQLiteException e) {
            Log.d("SQLiteException-Insert", e.toString());
        }

        return false;
    }

    public boolean getUser(User user) {
       String query = "SELECT COUNT(*) AS n FROM user WHERE email = \""
            + user.getEmail() + "\" AND password = \""
            + user.getPassword() + "\";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex("n")) == 0) {
                return false;
            }
        }

        cursor.close();
        return true;
    }

    public Integer getUserID(User user) {
        String query = "SELECT user_id FROM user WHERE email = \""
                + user.getEmail() + "\";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("user_id"));
        }
        cursor.close();
        return -1;
    }

    public String getUserName(User user) {
        String query = "SELECT name FROM user WHERE email = \""
                + user.getEmail() + "\";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        return null;
    }

    /* NAO TA FUNCIONANDO AINDA */
    public ArrayList<Post> getPosts() {
        String query = "SELECT * FROM post;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Post> results = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String author, time, post_id;
                double latitude, longitude;

                author = cursor.getString(cursor.getColumnIndex("author"));
                latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                time = cursor.getString(cursor.getColumnIndex("time"));
                post_id = cursor.getString(cursor.getColumnIndex("post_id"));

                Post post = new Post(author, latitude, longitude, time, post_id);

                if (!author.equals("")) {
                    Toast.makeText(mContext, "class - " + post_id, Toast.LENGTH_SHORT).show();
                    results.add(post);
                }
            }
        }

        cursor.close();

        return results;
    }

    public void addPost(int user_id, String post_id, double latitude, double longitude, String time) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("user_id_fk", user_id);
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        cv.put("time", time);
        cv.put("post_id", post_id);

        db.insert("post", null, cv);
    }

}
