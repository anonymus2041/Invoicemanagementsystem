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

public class UnitsSpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    public UnitsSpinnerAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_units_textview,null);
        String item = getItem(position);
        TextView textView =view.findViewById(R.id.show_units_in_TextView);
        textView.setText(item);
//        textView.setPadding(15,15,15,15);
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.show_units_textview,null);
        String item = getItem(position);
        TextView textView =view.findViewById(R.id.show_units_in_TextView);
        textView.setText(item);
//        textView.setPadding(15,15,15,15);
        return view;
    }
}
