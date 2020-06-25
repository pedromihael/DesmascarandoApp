package com.pedromihael.desmascarandoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext = null;
    private static final String DATABASE_NAME = "desmascarando.db";
    private static final Integer DATABASE_VERSION = 1;
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
                    "time DATE NOT NULL, " +
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
}
