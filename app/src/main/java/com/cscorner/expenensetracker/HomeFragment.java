package com.cscorner.expenensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private TextView summary;
    private FirebaseFirestore db;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        summary = view.findViewById(R.id.homeSummary);

        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            loadTotalExpenses();
        } else {
            summary.setText("Please log in to see expenses.");
        }

        return view;
    }

    private void loadTotalExpenses() {
        summary.setText("Loading...");

        db.collection("users")
                .document(userId)
                .collection("expenses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    double total = 0;

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Double amount = document.getDouble("amount");
                        if (amount != null) {
                            total += amount;
                        }
                    }

                    // Display the total
                    summary.setText("Total Expenses: $" + String.format("%.2f", total));
                })
                .addOnFailureListener(e -> {
                    summary.setText("Error loading data.");
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (userId != null) {
            loadTotalExpenses();
        }
    }
}
