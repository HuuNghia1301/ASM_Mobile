package com.example.asm_ad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm_ad.Database.UserDatabaseHelper;

public class HomeFrafment extends Fragment {

    private TextView user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);

        // Gọi hàm lấy dữ liệu người dùng
        getUser();


        return view;
    }

    private void getUser() {
        //  Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("loggedInUser", "Không có email");
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
        //  Lấy tên người dùng từ cơ sở dữ liệu dựa vào email
        dbHelper.getUserFullname(userEmail);
        // Hiển thị email lên TextView
        String fullName = dbHelper.getUserFullname(userEmail);

        user.setText("Xin chào : " + fullName);
    }

}