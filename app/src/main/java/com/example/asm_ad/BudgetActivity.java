package com.example.asm_ad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {
    private EditText edtBudgetAmount, edtBudgetCategory;
    private Button btnSaveBudget;
    private RecyclerView rvBudgets;
    private BudgetAdapter budgetAdapter;
    private List<Budget> budgetList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        // Ánh xạ View
        edtBudgetAmount = findViewById(R.id.edtBudgetAmount);
        edtBudgetCategory = findViewById(R.id.edtBudgetCategory);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        rvBudgets = findViewById(R.id.rvBudgets);

        // Khởi tạo danh sách ngân sách
        budgetList = new ArrayList<>();
        budgetAdapter = new BudgetAdapter(budgetList);


        // Thiết lập RecyclerView
        rvBudgets.setLayoutManager(new LinearLayoutManager(this));
        rvBudgets.setAdapter(budgetAdapter);

        // Xử lý khi bấm nút "Lưu ngân sách"
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


                // Xóa dữ liệu nhập sau khi lưu
                edtBudgetCategory.setText("");
                edtBudgetAmount.setText("");
            }
        });
    }
}

