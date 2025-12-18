package com.cscorner.expenensetracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private Context context;

    public ExpenseAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvAmount.setText(expense.getAmount() + " " + expense.getCurrency());
        holder.tvCategory.setText(expense.getCategory());
        holder.tvRemark.setText(expense.getRemark());

        // Click to open detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExpenseDetailActivity.class);
            intent.putExtra("amount", expense.getAmount());
            intent.putExtra("currency", expense.getCurrency());
            intent.putExtra("category", expense.getCategory());
            intent.putExtra("remark", expense.getRemark());
            intent.putExtra("date", expense.getDate());
            context.startActivity(intent);
        });
    }
    // Inside ExpenseAdapter.java
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView remark, category, amount, date;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Change these to match the NEW IDs we added in the XML
            remark = itemView.findViewById(R.id.tvRemarkDisplay);
            category = itemView.findViewById(R.id.tvCategoryDisplay);
            amount = itemView.findViewById(R.id.tvAmountDisplay);
            date = itemView.findViewById(R.id.tvDateDisplay);
            icon = itemView.findViewById(R.id.ivCategoryIcon);
        }
    }
    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvCategory, tvRemark;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.itemAmount);
            tvCategory = itemView.findViewById(R.id.itemCategory);
            tvRemark = itemView.findViewById(R.id.itemRemark);
        }
    }
}
