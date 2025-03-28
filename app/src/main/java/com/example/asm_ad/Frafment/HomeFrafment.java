package com.example.asm_ad.Frafment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm_ad.BudgetActivity;
import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.R;


public class HomeFrafment extends Fragment {

    private TextView user;
    private DataBaseUserHelper dbHelper;
    private SharedPreferences sharedPreferences;

    private Button btnaddBudget;

    private TextView txtBudget;

    private int userId;
    private ProgressBar progressBar;
    private TextView txtExpensePercentage;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);
        txtBudget = view.findViewById(R.id.txtBudget); // Cần khai báo trong XML
        progressBar = view.findViewById(R.id.thismothBudget);
        txtExpensePercentage = view.findViewById(R.id.txtExpensePercentage);
        btnaddBudget = view.findViewById(R.id.addBudget);

        // Khởi tạo database helper

        dbHelper = new DataBaseUserHelper(requireContext());

        btnaddBudget.setOnClickListener(v -> showAddBudget());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        this.userId = userId;

        getUserName();
        showBudget();
        showBudget1();
        return view;
    }
    public void getUserName() {
        dbHelper.getUserFullname(userId);
        user.setText("Welcome :" + dbHelper.getUserFullname(userId));
    }
    public void showAddBudget(){
        Intent intent = new Intent(getActivity(), BudgetActivity.class);
        startActivity(intent);
    }
    public void showBudget() {
        double expense = dbHelper.getUseExpense(userId); // Lấy tổng số tiền chi tiêu
        double budget = dbHelper.getUserBudget(userId); // Lấy tổng số tiền budget
        String budgetText = String.valueOf(budget-expense); // Chuyển từ double sang String

        txtBudget.setText("Budget $ "+ budgetText); // Gán vào TextView
    }
    public void showBudget1() {
        double expense = dbHelper.getUseExpense(userId);
        double budget = dbHelper.getUserBudget(userId);

        int percent = (budget > 0) ? (int) ((expense / budget) * 100) : 0;

        txtExpensePercentage.setText("Expense this month: " + percent + "%");
        progressBar.setProgress(percent);
    }
    @Override
    public void onResume() {
        super.onResume();
        showBudget();
        showBudget1();
    }

}