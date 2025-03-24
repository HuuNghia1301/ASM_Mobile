package com.example.asm_ad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExpenseFrafment extends Fragment {
    private Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        // Gán view cho btn
        btn = view.findViewById(R.id.btnAddExpense);

        // Thêm sự kiện click cho button


        return view;
    }

    // Đổi tên method từ test() -> openBudgetActivity() để rõ ràng hơn



}
