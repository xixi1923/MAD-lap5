package com.cscorner.expenensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddExpenseFragment extends Fragment {

    private EditText etAmount, etDate, etRemark;
    private Spinner spinnerCategory, spinnerCurrency;
    private Button btnSave;

    private FirebaseFirestore db;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        etAmount = view.findViewById(R.id.etAmount);
        etDate = view.findViewById(R.id.etDate);
        etRemark = view.findViewById(R.id.etRemark);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerCurrency = view.findViewById(R.id.spinnerCurrency);
        btnSave = view.findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        String[] currencies = {"USD", "Riel"};
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, currencies);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(currencyAdapter);

        String[] categories = {
                "Food", "Transport", "Shopping", "Entertainment", "Rent",
                "Health & Medical", "Education", "Personal Care", "Gifts & Donations", "Others"
        };
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        etDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveExpenseToFirestore());

        return view;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDate.setText(dateString);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveExpenseToFirestore() {
        String amountStr = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String currency = spinnerCurrency.getSelectedItem().toString();
        String remark = etRemark.getText().toString().trim();

        if (amountStr.isEmpty()) {
            etAmount.setError("Amount required");
            return;
        }
        if (date.isEmpty()) {
            Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            etAmount.setError("Invalid number");
            return;
        }

        Map<String, Object> expense = new HashMap<>();
        expense.put("amount", amount);
        expense.put("category", category);
        expense.put("currency", currency);
        expense.put("remark", remark.isEmpty() ? "No remark" : remark);
        expense.put("date", date);
        // Add timestamp for sorting later
        expense.put("timestamp", System.currentTimeMillis());

        btnSave.setEnabled(false);
        btnSave.setText("Saving...");

        db.collection("users")
                .document(uid)
                .collection("expenses")
                .add(expense)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(getContext(), "Expense Saved Successfully!", Toast.LENGTH_SHORT).show();
                    // Clear inputs after success
                    etAmount.setText("");
                    etRemark.setText("");
                    etDate.setText("");
                    btnSave.setEnabled(true);
                    btnSave.setText("Save Expense");
                })
                .addOnFailureListener(e -> {
                    btnSave.setEnabled(true);
                    btnSave.setText("Save Expense");
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
