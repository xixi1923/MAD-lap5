package com.cscorner.expenensetracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        TextView tvAmount = findViewById(R.id.detailAmount);
        TextView tvCategory = findViewById(R.id.detailCategory);
        TextView tvRemark = findViewById(R.id.detailRemark);


        double amount = getIntent().getDoubleExtra("amount", 0);
        String currency = getIntent().getStringExtra("currency");
        String category = getIntent().getStringExtra("category");
        String remark = getIntent().getStringExtra("remark");
        String date = getIntent().getStringExtra("date");

        tvAmount.setText(amount + " " + currency);
        tvCategory.setText(category);
        tvRemark.setText(remark);

    }
}
