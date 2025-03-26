
package com.example.asm_ad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.Model.Expense;

import java.util.List;


public class HomeFrafment extends Fragment {

    private TextView user;
    private DataBaseUserHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private Button btnaddBudget;
    private int userId;
    private TextView txtBudget;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);
        txtBudget = view.findViewById(R.id.txtBudget); // Cần khai báo trong XML

        btnaddBudget = view.findViewById(R.id.addBudget);

        // Khởi tạo database helper

        dbHelper = new DataBaseUserHelper(getContext());
        List<Expense> expenseList = dbHelper.getAllExpenses(userId);
        expenseAdapter = new ExpenseAdapter(getContext(), expenseList);
        btnaddBudget.setOnClickListener(v -> showAddBudget());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expenseAdapter);
        dbHelper.getAllExpenses(userId);
        dbHelper = new DataBaseUserHelper(getActivity());
        loadExpenses();
        getUserName();
        showBudget();
        return view;
    }
    public void loadExpenses() {
        expenseList = dbHelper.getAllExpenses(userId);
        if (expenseAdapter != null) {
            expenseAdapter.updateList(expenseList);
            expenseAdapter.notifyDataSetChanged();
        } else {
            Log.e("HomeFragment", "expenseAdapter is null");
        }
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
