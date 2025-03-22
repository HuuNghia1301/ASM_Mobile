package com.example.asm_ad.Database;

import android.content.ContentValues;
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
    //so luong chi tieu
    public static final String COLUMN_AMOUNT_SPEND = "amount_spend";
    // chi tieu ve cai gi
    public static final String COLUMN_EXPENSE_CATEGORY = "expense_category";
    public static final String COLUMN_USER_ID = "user_id"; // Foreign Key to users table
    //ngay chi tieu
    public static final String COLUMN_DATE_SPEND = "date_spend";
    //lay ngan sach
    public static final String COLUMN_ID_BUDGET = "_id_budget"; // Foreign Key to Budget table

    // User table information (for foreign key)
    public static final String TABLE_USERS = "users"; // Assuming this is the table name in UserDatabaseHelper
    public static final String USER_COLUMN_ID = "_id"; // Assuming this is the primary key column in UserDatabaseHelper

    // Budget table information (for foreign key)
    public static final String TABLE_BUDGET = "Budget"; // Assuming this is the table name in BudgetDbHelper
    public static final String COLUMN_BUDGET_QUANTITY = "budget_quantity";
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
        int userId = -1;

        String query = "SELECT " + USER_COLUMN_ID + " FROM " + TABLE_USERS + " WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    public double getBudgetQuantity(int budgetId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double budgetQuantity = 0;

        String query = "SELECT " + COLUMN_BUDGET_QUANTITY + " FROM " + TABLE_BUDGET + " WHERE " + BUDGET_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(budgetId)});

        if (cursor.moveToFirst()) {
            budgetQuantity = cursor.getDouble(0); // Lấy giá trị cột đầu tiên
        }
        cursor.close();
        db.close();

        return budgetQuantity;
    }

    public void addExpense(double amount, String category, String date, int budgetId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_SPEND, amount);
        values.put(COLUMN_EXPENSE_CATEGORY, category);
        values.put(COLUMN_DATE_SPEND, date);
        values.put(COLUMN_ID_BUDGET, budgetId);
        values.put(COLUMN_USER_ID, userId);

        db.insert(TABLE_EXPENSE, null, values);
        db.close();
    }

    public Cursor getExpensesByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
    public String getUserFullName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = "";

        String query = "SELECT lastname, firstname FROM " + TABLE_USERS + " WHERE " + USER_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            String lastName = cursor.getString(0);
            String firstName = cursor.getString(1);
            fullName = firstName + " " + lastName; // Ghép họ và tên
        }
        cursor.close();
        db.close();

        return fullName;
    }

    public void updateExpense(int expenseId, double amount, String category, String date, int budgetId, String COLUMN_BUDGET_QUANTITY) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_SPEND, amount);
        values.put(COLUMN_EXPENSE_CATEGORY, category);
        values.put(COLUMN_DATE_SPEND, date);
        values.put(COLUMN_ID_BUDGET, budgetId);

        db.update(TABLE_EXPENSE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(expenseId)});
        db.close();
    }

    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, COLUMN_ID + " = ?", new String[]{String.valueOf(expenseId)});
        db.close();
    }
    public double getTotalExpensesForBudget(int budgetId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpenses = 0;
        String query = "SELECT SUM(" + COLUMN_AMOUNT_SPEND + ") FROM " + TABLE_EXPENSE +
                " WHERE " + COLUMN_ID_BUDGET + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(budgetId)});
        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalExpenses;
    }

    public boolean isBudgetExceeded(int budgetId) {
        double totalExpenses = getTotalExpensesForBudget(budgetId);
        double budgetLimit = getBudgetQuantity(budgetId);
        return totalExpenses > budgetLimit;
    }
}