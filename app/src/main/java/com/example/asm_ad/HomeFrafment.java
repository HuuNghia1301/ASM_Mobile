package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.asm_ad.Database.DataBaseUserHelper;


public class HomeFrafment extends Fragment {

    private TextView user;
    private DataBaseUserHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private TextView tien;
    private CardView cardView;
    private Button btn;
    private Button btnExit , btnAddBudgetAmount;
    private TextView txtBudgetAmount;

    private int userId;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);
        tien = view.findViewById(R.id.tien); // Cần khai báo trong XML
        cardView = view.findViewById(R.id.cardview1);
        btn = view.findViewById(R.id.btn1);

        btnExit = view.findViewById(R.id.btnExit);
        txtBudgetAmount = view.findViewById(R.id.txtBudgetAmount);
        btnAddBudgetAmount = view.findViewById(R.id.btnaddBudgetAmount);
        // Khởi tạo database helper

        dbHelper = new DataBaseUserHelper(requireContext());

        cardView.setVisibility(View.GONE); // Ẩn hoàn toàn
        // khởi tạo lấy id để thêm sửa xóa budget
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("userId", -1);
        userId = id;

        btn.setOnClickListener(v -> showCardView());

        btnExit.setOnClickListener(v -> hideCardView());
        btnAddBudgetAmount.setOnClickListener(v -> updateBudgetAmount());
        // Lấy email từ SharedPreferences
        getUserName();
        tien();
        return view;
    }
    public void getUserName() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("loggedInUser", "");

        dbHelper.getUserFullname(userEmail);
      user.setText("Welcome :" + dbHelper.getUserFullname(userEmail));

    }
    public void tien(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("loggedInUser", "");
        dbHelper.getUserBudget(userEmail);

        if (!userEmail.isEmpty()) {
            // Gọi hàm lấy số tiền của user
            double userBudget = dbHelper.getUserBudget(userEmail);
            Log.d("Budget", "Total $ " + userBudget);
            // Hiển thị số tiền lên TextView
            tien.setText("Total $:" + userBudget + " VND");
        } else {
            Log.d("Budget", "Không tìm thấy email user");
            tien.setText("Không có dữ liệu");
        }
    }
    public void showCardView() {
        cardView.setVisibility(View.VISIBLE);
    }
    public void hideCardView() {
        cardView.setVisibility(View.GONE);
    }
    public void updateBudgetAmount() {
        String budgetText = txtBudgetAmount.getText().toString().trim();
        double newBudgetAmount = Double.parseDouble(budgetText);
        if(newBudgetAmount > 0){
            dbHelper.updateBudgetAmount(userId,newBudgetAmount) ;
            tien();
            txtBudgetAmount.setText("");
            hideCardView();
        }
        else {

        }


    }
}




