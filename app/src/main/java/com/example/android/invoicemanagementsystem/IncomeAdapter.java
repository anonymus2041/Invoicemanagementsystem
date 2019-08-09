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

public class IncomeAdapter extends ArrayAdapter<IncomeInfo> {
    Context context;
    DatabaseHelper databaseHelper;
    public IncomeAdapter(@NonNull Context context, ArrayList<IncomeInfo> list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.show_income,null);
        TextView ExpensenName = view.findViewById(R.id.incomeType);
        TextView amount= view.findViewById(R.id.totalIncomeAmount);
        TextView date= view.findViewById(R.id.incomeDate);
        TextView party = view.findViewById(R.id.IncomeParty);
        final IncomeInfo item = getItem(position);

        ExpensenName.setText(item.income_name);
        amount.setText("Rs."+item.amount);
        date.setText(item.date);
        if(item.party_id>0) {

            databaseHelper=new DatabaseHelper(context);
            PartyInfo customer = new PartyInfo();
            customer = databaseHelper.getCustomerName(String.valueOf(item.party_id));
            party.setText(customer.name);
        }
        else{
            party.setText("");
        }

        return view;
    }
}
