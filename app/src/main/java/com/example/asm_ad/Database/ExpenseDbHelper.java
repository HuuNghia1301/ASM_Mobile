package com.example.asm_ad.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ExpenseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ASM_CampusExpense_Manager";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_EXPENSE = "Expense";

    // Expense table columns
    public static final String COLUMN_ID = "_id_Expense";
    public static final String COLUMN_AMOUNT_SPEND = "amount_spend";
    public static final String COLUMN_EXPENSE_CATEGORY = "expense_category";
    public static final String COLUMN_USER_ID = "user_id"; // Foreign Key to users table
    public static final String COLUMN_DATE_SPEND = "date_spend";
    public static final String COLUMN_ID_BUDGET = "_id_budget"; // Foreign Key to Budget table

    // User table information (for foreign key)
    public static final String TABLE_USERS = "users"; // Assuming this is the table name in UserDatabaseHelper
    public static final String USER_COLUMN_ID = "_id"; // Assuming this is the primary key column in UserDatabaseHelper

    // Budget table information (for foreign key)
    public static final String TABLE_BUDGET = "Budget"; // Assuming this is the table name in BudgetDbHelper
    public static final String BUDGET_COLUMN_ID = "_id"; // Assuming this is the primary key column in BudgetDbHelper

    public ExpenseDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON");

        // Create the Expense table
        String CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_EXPENSE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_AMOUNT_SPEND + " REAL,"
                + COLUMN_EXPENSE_CATEGORY + " TEXT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_DATE_SPEND + " TEXT,"
                + COLUMN_ID_BUDGET + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_COLUMN_ID + ") ON DELETE CASCADE,"
                + "FOREIGN KEY(" + COLUMN_ID_BUDGET + ") REFERENCES " + TABLE_BUDGET + "(" + BUDGET_COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
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
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Giá trị mặc định nếu không tìm thấy user

        String query = "SELECT " + USER_COLUMN_ID + " FROM " + TABLE_USERS + " WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return userId; // Trả về ID của user, hoặc -1 nếu không tìm thấy
    }
}