package app.src.main.java.com.course.chat.db;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.course.chat.Message;
import com.course.chat.app.ChatApplication;
import com.course.chat.db.ChatOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vhernest on 03/12/15.
 */
public class MessageDAO {

    public static void add(Message message) {
        ChatOpenHelper coh = new ChatOpenHelper(ChatApplication.getInstance());
        SQLiteDatabase db = coh.getWritableDatabase();

        Object[] values = new Object[4];
        values[0] = message.getId();
        values[1] = message.getWhen();
        values[2] = message.getUsername();
        values[3] = message.getText();
        db.execSQL("INSERT INTO messages(id, timestamp, username, message) VALUES(?, ?, ?, ?)", values);

        db.close();

    }

    public static List<Message> list() {

        List<Message> messages = new ArrayList<>();

        ChatOpenHelper coh = new ChatOpenHelper(ChatApplication.getInstance());
        SQLiteDatabase db = coh.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT id, timestamp, username, message FROM messages", null);
        while (cursor.moveToNext()) {
            Message message = new Message(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3));

            messages.add(message);
        }

        cursor.close();
        db.close();

        return messages;
    }

    public static void clear() {
        ChatOpenHelper coh = new ChatOpenHelper(ChatApplication.getInstance());
        SQLiteDatabase db = coh.getWritableDatabase();

        db.execSQL("DELETE FROM messages");

        db.close();

    }
}
