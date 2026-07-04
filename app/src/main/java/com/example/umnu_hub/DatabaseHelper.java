package com.example.umnu_hub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "UserDB.db";
    public static final String TABLE_NAME = "messages";

    // Perbaiki constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 2); // Update version to 2
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tambahkan kolom username dan timestamp
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "message TEXT NOT NULL," +
                        "timestamp LONG)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method untuk insert message dengan username
    public boolean insertMessage(String username, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("message", message);
        cv.put("timestamp", System.currentTimeMillis());

        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }

    // Method untuk get all messages dengan username
    public ArrayList<Message> getAllMessagesWithUser() {
        ArrayList<Message> messageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY timestamp ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("message"));
                messageList.add(new Message(username, content));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return messageList;
    }

    // Method untuk hapus semua messages
    public void deleteAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}