package com.example.android.invoicemanagementsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AutocompleteAdapter extends ArrayAdapter<String> {
    Context context;
    public AutocompleteAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String item = getItem(position);
        TextView textView =new TextView(context);
        textView.setText(item);
        textView.setPadding(15,15,15,15);
        textView.setTextSize(25);
        return textView;
    }

}
