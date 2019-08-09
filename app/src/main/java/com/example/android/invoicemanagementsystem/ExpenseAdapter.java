package com.example.android.invoicemanagementsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<ExpenseInfo> {
    Context context;
    DatabaseHelper databaseHelper;
    public ExpenseAdapter(@NonNull Context context, ArrayList<ExpenseInfo> list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.show_expenses,null);
        TextView ExpensenName = view.findViewById(R.id.expenseType);
        TextView amount= view.findViewById(R.id.totalExpenseAmount);
        TextView date= view.findViewById(R.id.expenseDate);
        TextView category = view.findViewById(R.id.expense_category);
        final ExpenseInfo item = getItem(position);

        ExpensenName.setText(item.expense_name);
        amount.setText("Rs."+item.amount);
        date.setText(item.date);
        if(item.operating==1&&item.direct==0 ) {
            category.setText("Operating expenses");
        }
        if(item.operating==0 && item.direct==1)
        {
            category.setText("Direct Expenses");
        }
        return view;
    }
}
