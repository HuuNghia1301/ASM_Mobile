package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm_ad.Database.DataBaseUserHelper;


public class HomeFrafment extends Fragment {

    private TextView user;
    private DataBaseUserHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private TextView tien;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);
        tien = view.findViewById(R.id.tien); // Cần khai báo trong XML

        // Khởi tạo database helper
        dbHelper = new DataBaseUserHelper(requireContext());

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
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

        return view;
    }
}

//    private void getUser() {
//        //  Lấy email từ SharedPreferences
//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//        String userEmail = sharedPreferences.getString("loggedInUser", "Không có email");
//        DataBaseUserHelper dbHelper = new DataBaseUserHelper(requireContext());
//        //  Lấy tên người dùng từ cơ sở dữ liệu dựa vào email
//        dbHelper.getUserFullname(userEmail);
//        // Hiển thị email lên TextView
//        String fullName = dbHelper.getUserFullname(userEmail);
//
//        user.setText("Welcome : " + fullName);
//    }



