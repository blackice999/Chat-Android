package com.course.chat.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vhernest on 26/11/15.
 */
public class ChatOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String NAME = "chat.db";

    public ChatOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS messages(id INT, timestamp INT, username TEXT, message TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE messages");
        onCreate(db);
    }
}
