package com.cscorner.expenensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private FirebaseFirestore db;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExpenseAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            loadExpensesFromFirestore();
        }

        return view;
    }

    private void loadExpensesFromFirestore() {
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(uid)
                .collection("expenses")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    List<Expense> expenses = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Expense expense = doc.toObject(Expense.class);
                        if (expense != null) {
                            expense.setId(doc.getId());
                            expenses.add(expense);
                        }
                    }
                    adapter.setExpenses(expenses);
                });
    }
}
