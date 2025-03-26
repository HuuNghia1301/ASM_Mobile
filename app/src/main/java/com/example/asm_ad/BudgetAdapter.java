package com.example.asm_ad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Model.Budget;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    private List<Budget> budgetList;
    private OnItemClickListener listener;
    private EditText edtBudgetAmount, edtBudgetCategory;
    private int selectedBudgetId = -1;  // Lưu idBudget của mục được chọn
    // Interface để gửi dữ liệu khi click
    public interface OnItemClickListener {
        void onItemClick(Budget budget);
    }
    public BudgetAdapter(List<Budget> budgetList, OnItemClickListener listener) {
        this.budgetList = budgetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.txtCategory.setText(budget.getCategory());
        holder.txtAmount.setText(String.valueOf(budget.getAmount()));

        // Bắt sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(budget); // Gửi dữ liệu về Activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtAmount;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtAmount = itemView.findViewById(R.id.txtAmount);
        }
    }
}
