package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.example.asm_ad.Database.DataBaseUserHelper;

public class HomeFrafment extends Fragment {
    private Button btnBudget;
    private TextView user;
    private DataBaseUserHelper dbHelper;
    private TextView tien;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);
        tien = view.findViewById(R.id.tien);
        btnBudget = view.findViewById(R.id.btnBudget); // Đã sửa vị trí khai báo

        // Khởi tạo database helper
        dbHelper = new DataBaseUserHelper(requireContext());

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("loggedInUser", "");

        if (!userEmail.isEmpty()) {
            // Gọi hàm lấy số tiền của user
            double userBudget = dbHelper.getUserBudget(userEmail);
            Log.d("Budget", "Số tiền của user: " + userBudget);

            // Hiển thị số tiền lên TextView
            tien.setText("Số tiền của bạn: " + userBudget + " VND");
        } else {
            Log.d("Budget", "Không tìm thấy email user");
            tien.setText("Không có dữ liệu");
        }

        // Sự kiện click cho nút btnBudget
        btnBudget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BudgetActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
