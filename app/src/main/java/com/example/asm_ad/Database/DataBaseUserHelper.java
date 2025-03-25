package com.example.asm_ad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.asm_ad.Model.User;

public class DataBaseUserHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    // Thông tin database
    private static final String DATABASE_NAME = "UserManager.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USER = "users";

    // Tên các cột
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_LASTNAME = "fullname";
    private static final String COLUMN_FIRSTNAME = "firstname";


    // Bảng Expenses

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_EXPENSE_ID = "expense_id";
    private static final String COLUMN_EXPENSE_AMOUNT = "amount";
    private static final String COLUMN_EXPENSE_CATEGORY = "category";
    private static final String COLUMN_EXPENSE_DATE = "date";


    // Bảng Budget
    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_BUDGET_ID = "budget_id";
    private static final String COLUMN_BUDGET_AMOUNT = "amount";
    private static final String COLUMN_BUDGET_CATEGORY = "category";

    public DataBaseUserHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper: Khởi tạo database");
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database tables...");

        // Tạo bảng Users
        String createTableUsers = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_LASTNAME + " TEXT NOT NULL," +
                COLUMN_FIRSTNAME + " TEXT NOT NULL);";
        db.execSQL(createTableUsers);

        // Tạo bảng Expenses
        String createTableExpenses = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_AMOUNT + " REAL NOT NULL, " +
                COLUMN_EXPENSE_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_EXPENSE_DATE + " TEXT NOT NULL, " +
                COLUMN_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
        db.execSQL(createTableExpenses);

        // Tạo bảng Budget
        String createTableBudget = "CREATE TABLE " + TABLE_BUDGET + " (" +
                COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BUDGET_AMOUNT + " REAL NOT NULL, " +
                COLUMN_BUDGET_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
        db.execSQL(createTableBudget);

        Log.d(TAG, "Database tables created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Downgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        // Xóa database cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);

        // Tạo lại database
        onCreate(db);
    }


    public long addUser(User user, double budgetAmount, String budgetCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // Bắt đầu transaction

        long userId = -1;
        long budgetId = -1;

        try {
            // Thêm user vào bảng users
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_EMAIL, user.getEmail());
            userValues.put(COLUMN_PASSWORD, user.getPassword());
            userValues.put(COLUMN_LASTNAME, user.getLastname());
            userValues.put(COLUMN_FIRSTNAME, user.getFirstname());

            userId = db.insert(TABLE_USER, null, userValues);

            if (userId == -1) {
                throw new Exception("Lỗi khi thêm user");
            }

            Log.d(TAG, "User đã được thêm thành công với ID: " + userId);

            // Thêm dữ liệu vào bảng budget
            ContentValues budgetValues = new ContentValues();
            budgetValues.put(COLUMN_BUDGET_AMOUNT, budgetAmount);
            budgetValues.put(COLUMN_BUDGET_CATEGORY,budgetCategory);
            budgetValues.put(COLUMN_ID, userId); // Gán userId vào budget

            budgetId = db.insert(TABLE_BUDGET, null, budgetValues);

            if (budgetId == -1) {
                throw new Exception("Lỗi khi thêm budget");
            }

            Log.d(TAG, "Budget đã được thêm thành công với ID: " + budgetId);
            db.setTransactionSuccessful(); // Đánh dấu transaction thành công
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi thêm user và budget: ", e);
        } finally {
            db.endTransaction(); // Kết thúc transaction
//            db.close();
        }

        return (userId != -1 && budgetId != -1) ? userId : -1; // Trả về userId nếu thành công, ngược lại -1
    }


    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();
//        db.close();

        Log.d(TAG, "checkUser: Kiểm tra user " + username + " - Kết quả: " + (count > 0));
        return count > 0;
    }
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
//        db.close();
        return exists;
    }
    public double getUserBudget(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        double budgetAmount = 0.0;

        Cursor cursor = null;
        try {
            String query = "SELECT " + COLUMN_BUDGET_AMOUNT +
                    " FROM " + TABLE_BUDGET +
                    " WHERE " + COLUMN_ID +
                    " = (SELECT " + COLUMN_ID +
                    " FROM " + TABLE_USER +
                    " WHERE " + COLUMN_EMAIL + " = ?)";

            cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                budgetAmount = cursor.getDouble(0);
            }

            cursor.close();
            Log.d(TAG, "getUserBudget: Số tiền budget của " + email + " là " + budgetAmount);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi lấy budget của user", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        return budgetAmount;
    }

    public boolean addExpense(String email, double amount, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean success = false;

        try {
            // Lấy user ID từ bảng users
            String queryUserId = "SELECT " + COLUMN_ID + " FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?";
            Cursor cursorUser = db.rawQuery(queryUserId, new String[]{email});

            if (cursorUser.moveToFirst()) {
                int userId = cursorUser.getInt(0);
                cursorUser.close();

                // Thêm dữ liệu vào bảng expenses
                ContentValues values = new ContentValues();
                values.put(COLUMN_EXPENSE_AMOUNT, amount);
                values.put(COLUMN_EXPENSE_CATEGORY, category);
                values.put(COLUMN_EXPENSE_DATE, date);
                values.put(COLUMN_ID, userId);

                long result = db.insert(TABLE_EXPENSES, null, values);
                if (result != -1) {
                    success = true;
                    db.setTransactionSuccessful();
                }
            } else {
                Log.e(TAG, "Không tìm thấy user ID với email: " + email);
            }

        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi thêm chi tiêu", e);
        } finally {
            db.endTransaction();
        }

        return success;
    }




    public Cursor getLoggedInUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT email FROM " + TABLE_USER + " LIMIT 1", null);
    }
}