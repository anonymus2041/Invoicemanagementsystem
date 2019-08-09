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

public class InterestAdapter extends ArrayAdapter<InterestInfo> {
    Context context;
    DatabaseHelper databaseHelper;
    public InterestAdapter(@NonNull Context context, ArrayList<InterestInfo> list) {
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
        final InterestInfo item = getItem(position);
        if(item.interest==1 && item.tax==0)
        {
            ExpensenName.setText("Interest");
        }
        if(item.interest==0 && item.tax==1)
        {
            ExpensenName.setText("Tax");
        }


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
