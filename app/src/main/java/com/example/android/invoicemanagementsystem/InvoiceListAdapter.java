package com.example.android.invoicemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class InvoiceListAdapter extends ArrayAdapter<InvoiceTable> {
    Context context;
    DatabaseHelper databaseHelper;
    public InvoiceListAdapter(@NonNull Context context, ArrayList<InvoiceTable> list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_show_invoice_list,null);

        TextView invoiceNo = view.findViewById(R.id.InvoiceNo_listView);
        TextView invoiceDate= view.findViewById(R.id.InvoiceDate_listView);
        TextView dueDate = view.findViewById(R.id.DueDateInvoice_listView);
        TextView amount= view.findViewById(R.id.Total_amountListView);
        TextView paymentStatus =view.findViewById(R.id.payment_statusListView);
        databaseHelper=new DatabaseHelper(context);
        final InvoiceTable item = getItem(position);

        invoiceNo.setText("#00"+item.invoice_no);
        invoiceDate.setText(item.invoice_date);
        dueDate.setText(""+item.due_date);
        amount.setText("Rs."+item.invoice_total);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,InvoiceDetails.class);
                intent.putExtra("invoice_id",Integer.toString(item.invoice_no));
                context.startActivity(intent);
            }
        });
        return view;
    }

}
