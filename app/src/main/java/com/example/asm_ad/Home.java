package com.example.asm_ad;

import static com.example.asm_ad.Database.UserDatabaseHelper.COLUMN_FIRST_NAME;
import static com.example.asm_ad.Database.UserDatabaseHelper.COLUMN_ID;
import static com.example.asm_ad.Database.UserDatabaseHelper.TABLE_USERS;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_ad.Database.UserDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    public String getWelcomeMessage() {
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String message = "Welcome!";
        String query = "SELECT " + COLUMN_FIRST_NAME + " FROM " + TABLE_USERS + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            message = "Welcome, " + cursor.getString(0) + "!";
        }
        cursor.close();
        db.close();
        return message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);

    }

}