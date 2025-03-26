package com.example.asm_ad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.Model.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> expenseList;
    private Context context;
    private DataBaseUserHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private int userId;
    private ExpenseAdapter expenseAdapter;

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        dbHelper = new DataBaseUserHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvAmount.setText("Số tiền: " + expense.getAmount() + " VND");
        holder.tvCategory.setText("Danh mục: " + expense.getCategory());
        holder.tvDate.setText("Ngày: " + expense.getDate());

        // So sánh với budget
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        double budget = dbHelper.getUserBudget(userId);

        if (expense.getAmount() > budget) {
            holder.tvAmount.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteExpense(expense.getExpense_id());
            expenseList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Đã xóa chi tiêu", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sửa chi tiêu
        holder.btnEdit.setOnClickListener(v -> {
            showEditDialog(expense, position);
        });

    }
    private void showEditDialog(Expense expense, int position) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_edit_expense, null);
        builder.setView(view);

        EditText edtAmount = view.findViewById(R.id.edtAmount);
        EditText edtCategory = view.findViewById(R.id.edtCategory);
        EditText edtDate = view.findViewById(R.id.edtDate);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        // Hiển thị dữ liệu cũ
        edtAmount.setText(String.valueOf(expense.getAmount()));
        edtCategory.setText(expense.getCategory());
        edtDate.setText(expense.getDate());

        AlertDialog dialog = builder.create();
        dialog.show();

        btnUpdate.setOnClickListener(v -> {
            double newAmount = Double.parseDouble(edtAmount.getText().toString());
            String newCategory = edtCategory.getText().toString();
            String newDate = edtDate.getText().toString();

            // Cập nhật dữ liệu mới
            expense.setAmount(newAmount);
            expense.setCategory(newCategory);
            expense.setDate(newDate);

            dbHelper.updateExpense(expense); // Lưu vào database

            expenseList.set(position, expense); // Cập nhật danh sách
            notifyItemChanged(position); // Cập nhật RecyclerView

            dialog.dismiss();
            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        });
    }

    public void updateList(List<Expense> newExpenses) {

        this.expenseList.clear();
        this.expenseList.addAll(newExpenses);
    }


    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvCategory, tvDate;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
    public void loadExpenses() {
        expenseList = dbHelper.getAllExpenses(userId);
        expenseAdapter.updateList(expenseList);
    }

}
