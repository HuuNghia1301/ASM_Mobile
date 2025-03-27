package com.example.asm_ad;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.content.Intent;
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
    private Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        etAmount = view.findViewById(R.id.editTextAmount);
        etCategory = view.findViewById(R.id.editTextCategory);
        etDate = view.findViewById(R.id.editTextDate);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);

        dbHelper = new DataBaseUserHelper(getActivity());

        // Gán view cho btn
        btn = view.findViewById(R.id.btnAddExpense);

        btnAddExpense.setOnClickListener(v -> addExpense());
        return view;
    }

    private void addExpense() {
        String amountStr = etAmount.getText().toString();
        String category = etCategory.getText().toString();
        String date = etDate.getText().toString();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        long insertedId = dbHelper.addExpense(userId, amount, category, date);
        if (insertedId != -1) {
            Toast.makeText(getActivity(), "Chi tiêu đã được lưu!", Toast.LENGTH_SHORT).show();

            // Làm mới danh sách chi tiêu trong HomeFragment
            Fragment homeFragment = getParentFragmentManager().findFragmentById(R.id.main);
            if (homeFragment instanceof HomeFrafment) {
                ((HomeFrafment) homeFragment).loadExpenses();
            }
            if (insertedId != -1) {
                etAmount.setText("");
                etCategory.setText("");
                etDate.setText("");
            }

        }
    }

    public void showBudget() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        double budget = dbHelper.getUserBudget(userId); // Lấy tổng số tiền budget
        String budgetText = String.valueOf(budget); // Chuyển từ double sang String


    }

}