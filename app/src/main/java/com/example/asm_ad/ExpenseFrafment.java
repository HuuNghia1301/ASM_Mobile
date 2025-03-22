package com.example.asm_ad;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm_ad.Database.DataBaseUserHelper;

public class ExpenseFrafment extends Fragment {
    private EditText etAmount, etCategory, etDate;
    private Button btnAddExpense;
    private DataBaseUserHelper dbHelper;
    private String userEmail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        etAmount = view.findViewById(R.id.editTextAmount);
        etCategory = view.findViewById(R.id.editTextCategory);
        etDate = view.findViewById(R.id.editTextDate);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);

        dbHelper = new DataBaseUserHelper(getActivity());
        userEmail = getUserEmailFromDatabase(); // Lấy email từ SQLite

        // Lưu email vào SharedPreferences
        saveUserEmailToPrefs(userEmail);

        btnAddExpense.setOnClickListener(v -> addExpense());
        return view;
    }
    private String getUserEmailFromDatabase() {
        Cursor cursor = dbHelper.getLoggedInUser();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow("email");
                    return cursor.getString(columnIndex);
                }
            } finally {
                cursor.close();
            }
        }
        return "unknown@example.com"; // Giá trị mặc định nếu không lấy được email
    }

    private void saveUserEmailToPrefs(String email) {
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_email", email);
            editor.apply();
        }
    }

    private void addExpense() {
        String amountStr = etAmount.getText().toString();
        String category = etCategory.getText().toString();
        String date = etDate.getText().toString();

        if (amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        boolean success = dbHelper.addExpense(userEmail, amount, category, date);

        if (success) {
            Toast.makeText(getActivity(), "Chi tiêu đã được thêm!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Lỗi khi thêm chi tiêu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBudget() {
        double budget = dbHelper.getUserBudget(userEmail);
        Toast.makeText(getActivity(), "Budget còn lại: " + budget, Toast.LENGTH_SHORT).show();
    }

}
