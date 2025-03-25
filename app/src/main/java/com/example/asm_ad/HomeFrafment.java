package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm_ad.Database.DataBaseUserHelper;


public class HomeFrafment extends Fragment {

    private TextView user;
    private DataBaseUserHelper dbHelper;
    private SharedPreferences sharedPreferences;

    private Button btnaddBudget;

    private TextView txtBudget;

    private int userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);
        txtBudget = view.findViewById(R.id.txtBudget); // Cần khai báo trong XML

        btnaddBudget = view.findViewById(R.id.addBudget);

        // Khởi tạo database helper

        dbHelper = new DataBaseUserHelper(requireContext());

        btnaddBudget.setOnClickListener(v -> showAddBudget());

        getUserName();
        showBudget();
        return view;
    }
    public void getUserName() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        dbHelper.getUserFullname(userId);
        user.setText("Welcome :" + dbHelper.getUserFullname(userId));
    }
    public void showAddBudget(){
        Intent intent = new Intent(getActivity(), BudgetActivity.class);
        startActivity(intent);
    }
    public void showBudget() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        double budget = dbHelper.getUserBudget(userId); // Lấy tổng số tiền budget
        String budgetText = String.valueOf(budget); // Chuyển từ double sang String

        txtBudget.setText("Budget $ "+ budgetText); // Gán vào TextView
    }







}




