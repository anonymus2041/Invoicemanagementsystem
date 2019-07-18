package com.example.android.invoicemanagementsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PartyListAdapter extends ArrayAdapter<PartyInfo> {
    Context context;
    DatabaseHelper databaseHelper;
    TextView name,date,amount;
    ImageView cashStatus;
    public PartyListAdapter(@NonNull Context context, ArrayList<PartyInfo>list) {
        super(context, 0,list);
        this.context=context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.show_party_info,null);
         name = view.findViewById(R.id.partyName);
         amount= view.findViewById(R.id.amountParty);
         date= view.findViewById(R.id.date);
         cashStatus = view.findViewById(R.id.cashStatus);
         PartyInfo party = getItem(position);
         databaseHelper =new DatabaseHelper(context);

        name.setText(party.name);
        date.setText(party.asOfdate);

        amount.setText("Rs."+party.opening_balance);
        if(party.toPay==1) {
            cashStatus.setImageResource(R.drawable.down_arrow_round);
        }
        if(party.toReceive==1)
        {
            cashStatus.setImageResource(R.drawable.up_arrow_round);
        }
//        else
//        {
//            cashStatus.setImageResource(R.drawable.plus_round);
//        }
        return view;
    }
}
