package com.example.asm_ad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BudgetDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ASM_CampusExpense_Manager";
    private static final int DATABASE_VERSION = 3;

    // Table name
    private static final String TABLE_BUDGET = "Budget";

    // Budget table columns
    private static final String COLUMN_ID_BUDGET = "_id_budget";
    private static final String COLUMN_BUDGET_QUANTITY = "budget_quantity";
    private static final String COLUMN_REMAINING_AMOUNT = "remaining_amount";
    private static final String COLUMN_USER_ID = "user_id"; // Foreign Key

    // User table information (for foreign key)
    private static final String TABLE_USERS = "users"; // Assuming this is the table name in UserDatabaseHelper
    private static final String USER_COLUMN_ID = "_id"; // Assuming this is the primary key column in UserDatabaseHelper

    public BudgetDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON");

        // Create the Budget table
        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + "("
                + COLUMN_ID_BUDGET + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BUDGET_QUANTITY + " REAL,"
                + COLUMN_REMAINING_AMOUNT + " REAL,"
                + COLUMN_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_BUDGET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        // Create tables again
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return db;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = super.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return db;
    }
}