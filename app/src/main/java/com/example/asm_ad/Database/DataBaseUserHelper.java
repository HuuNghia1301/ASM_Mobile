package com.example.asm_ad.Database;

import static com.example.asm_ad.Database.ReportDbHelper.TABLE_USERS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.asm_ad.Login;
import com.example.asm_ad.Model.Budget;
import com.example.asm_ad.Model.User;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.List;

public class DataBaseUserHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    // Thông tin database
    private static final String DATABASE_NAME = "UserManager.db";
    private static final int DATABASE_VERSION = 3;

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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long addUser(User user, double budgetAmount, String budgetCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // Bắt đầu transaction

        long userId = -1;

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


            db.setTransactionSuccessful(); // Đánh dấu transaction thành công
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi thêm user : ", e);
        } finally {
            db.endTransaction(); // Kết thúc transaction
            db.close();
        }

        return (userId != -1 ) ? userId : -1; // Trả về userId nếu thành công, ngược lại -1
    }
    public long addBudget(double amount, String category, long userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BUDGET_AMOUNT, amount);
        contentValues.put(COLUMN_BUDGET_CATEGORY, category);
        contentValues.put(COLUMN_ID, userId);
        db.insert(TABLE_BUDGET, null, contentValues);
        return userId;
    }
    public int getIdUserForEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        int userId = -1; // Mặc định nếu không tìm thấy
        if (cursor.moveToFirst()) { // Kiểm tra nếu có dữ liệu
            userId = cursor.getInt(0); // Lấy ID từ cột đầu tiên
        }
        cursor.close(); // Đóng Cursor để tránh rò rỉ bộ nhớ
        db.close(); // Đóng database
        return userId;
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
        db.close();

        Log.d(TAG, "checkUser: Kiểm tra user " + username + " - Kết quả: " + (count > 0));
        return count > 0;
    }
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public String getUserFullname(int IdUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullname = "";

        try {
            String query = "SELECT " + COLUMN_LASTNAME + ", " + COLUMN_FIRSTNAME +
                    " FROM " + TABLE_USER +
                    " WHERE " + COLUMN_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(IdUser)});

            if (cursor.moveToFirst()) {
                String lastName = cursor.getString(0);
                String firstName = cursor.getString(1);
                fullname = lastName + " " + firstName;
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return fullname;
    }

    public List<Budget> getAllBudgets() {
        List<Budget> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDGET, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String category = cursor.getString(1);
                double amount = cursor.getDouble(2);
                budgetList.add(new Budget(id, amount, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return budgetList;
    }
    //  Cập nhật ngân sách
    public void updateBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET_CATEGORY, budget.getCategory());
        values.put(COLUMN_BUDGET_AMOUNT, budget.getAmount());

        db.update(TABLE_BUDGET, values, COLUMN_ID + " = ?", new String[]{String.valueOf(budget.getId())});
        db.close();
    }
    public void deleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGET, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public long addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", budget.getUserId()); // Đảm bảo tên cột đúng với database
        values.put("amount", budget.getAmount());
        values.put("category", budget.getCategory());

        long insertedId = -1;
        try {
            insertedId = db.insert("budgets", null, values);
            if (insertedId == -1) {
                Log.e("DB_ERROR", "Lỗi khi chèn ngân sách vào database!");
            }
        } catch (Exception e) {
            Log.e("DB_EXCEPTION", "Lỗi SQL khi chèn ngân sách: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return insertedId;
    }

    public double getUserBudget(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double budgetAmount = 0.0;

        try {
            String query = "SELECT SUM(" + COLUMN_BUDGET_AMOUNT + ") " +
                    "FROM " + TABLE_BUDGET +
                    " WHERE " + COLUMN_ID + " = ?"; // COLUMN_ID là khóa ngoại tham chiếu userId

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst() && !cursor.isNull(0)) { // Kiểm tra null trước khi lấy giá trị
                budgetAmount = cursor.getDouble(0);
            }

            cursor.close();
            Log.d(TAG, "getUserBudget: Tổng budget của User ID " + userId + " là " + budgetAmount);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi lấy tổng budget của user", e);
        } finally {
            db.close();
        }

        return budgetAmount;
    }

    public List<String[]> getUserBudgets(int userId) {
        List<String[]> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + COLUMN_BUDGET_AMOUNT + ", " + COLUMN_BUDGET_CATEGORY +
                    " FROM " + TABLE_BUDGET + " WHERE " + COLUMN_ID + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            while (cursor.moveToNext()) {
                String amount = cursor.getString(0);
                String category = cursor.getString(1);
                budgetList.add(new String[]{amount, category});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return budgetList;
    }



}