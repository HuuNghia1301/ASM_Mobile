package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Model.Budget;

import java.util.ArrayList;
import java.util.List;
import com.example.asm_ad.Database.DataBaseUserHelper;

public class BudgetActivity extends AppCompatActivity {
    private EditText edtBudgetAmount, edtBudgetCategory;
    private Button btnSaveBudget, btnBack;
    private RecyclerView rvBudgets;
    private BudgetAdapter budgetAdapter;
    private List<Budget> budgetList;
    private SharedPreferences sharedPreferences;

    private DataBaseUserHelper dbHelper;

    private int userId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        // Ánh xạ View
        edtBudgetAmount = findViewById(R.id.edtBudgetAmount);
        edtBudgetCategory = findViewById(R.id.edtBudgetCategory);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        rvBudgets = findViewById(R.id.rvBudgets);
        btnBack = findViewById(R.id.btnBack);


        // Khởi tạo SQLite Database Helper
        dbHelper = new DataBaseUserHelper(this);
        // Khởi tạo danh sách ngân sách

        budgetList = new ArrayList<>();
        budgetAdapter = new BudgetAdapter(budgetList);



        // Thiết lập RecyclerView
        rvBudgets.setLayoutManager(new LinearLayoutManager(this));
        rvBudgets.setAdapter(budgetAdapter);

        // Xử lý khi bấm nút "Lưu ngân sách"
        btnBack.setOnClickListener(v -> backToHome());
        btnSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = edtBudgetCategory.getText().toString().trim();
                String amountStr = edtBudgetAmount.getText().toString().trim();

                if (category.isEmpty() || amountStr.isEmpty()) {
                    Toast.makeText(BudgetActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                budgetList.add(new Budget(category, amount));
                budgetAdapter.notifyDataSetChanged();
                 saveBudget();

                // Xóa dữ liệu nhập sau khi lưu
                edtBudgetCategory.setText("");
                edtBudgetAmount.setText("");
            }
        });
    }
    public void saveBudget() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String category = edtBudgetCategory.getText().toString().trim();
        String amountStr = edtBudgetAmount.getText().toString().trim();
        double amount;
        try {
            amount = Double.parseDouble(amountStr); // Chuyển đổi từ String -> double
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        long insertedId = dbHelper.addBudget(amount, category, userId);
        if (insertedId != -1) {
            Toast.makeText(this, "Ngân sách đã được lưu!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi lưu ngân sách!", Toast.LENGTH_SHORT).show();
        }
    }
    public void backToHome(){
        Intent intent = new Intent(BudgetActivity.this, Home.class);
        startActivity(intent);
    }







}

