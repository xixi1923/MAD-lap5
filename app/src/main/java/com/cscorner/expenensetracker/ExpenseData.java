package com.cscorner.expenensetracker;

import java.util.ArrayList;
import java.util.List;

public class ExpenseData {
    private static final List<Expense> expenseList = new ArrayList<>();

    public static List<Expense> getExpenses(){
        return expenseList;
    }

    public static void addExpense(Expense e){
        expenseList.add(e);
    }

    public static String getTotalExpenses(){
        double total = 0;
        for(Expense e : expenseList){
            // e.getAmount() already returns a double, so just add it directly.
            total += e.getAmount();
        }
        return String.valueOf(total);
    }
}
