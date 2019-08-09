package com.example.android.invoicemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionsAdapter extends ArrayAdapter<Transactionstable> {
    Context context;
    DatabaseHelper databaseHelper;
    TextView customerName,status,transactionTotal,transactionBalance,transactionDate;
    public TransactionsAdapter(@NonNull Context context, ArrayList<Transactionstable>list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_transactions,null);
        customerName=view.findViewById(R.id.customerName);
        status=view.findViewById(R.id.transaction_status);
        transactionTotal=view.findViewById(R.id.totalTransactionAmount);
        transactionBalance=view.findViewById(R.id.totalTransactionBalance);
        transactionDate=view.findViewById(R.id.transactionDate);
        Transactionstable transaction=getItem(position);
        databaseHelper=new DatabaseHelper(context);
        if(transaction.purchase_id!=0){
            PurchaseInfo item = new PurchaseInfo();
            PartyInfo customer=new PartyInfo();
            item=databaseHelper.getPurchaseInvoiceInfoForDisplay(String.valueOf(transaction.purchase_id));
            customer= databaseHelper.getCustomerName(String.valueOf(item.customer_id));
            status.setText("purchase");
            status.setBackground(context.getResources().getDrawable(R.drawable.shape));
            //status.setBackgroundResource(R.drawable.shape);
            status.setBackgroundColor(Color.parseColor("#ff3b5b"));
            //#de1245
            status.setPadding(30,0,30,0);
            transactionTotal.setText("Rs."+item.invoice_total);
            transactionBalance.setText("Rs."+item.balance_due);
            transactionDate.setText(item.invoice_date);
            customerName.setText(customer.name);

            final PurchaseInfo finalItem = item;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,InvoiceDetails.class);
                    intent.putExtra("invoice_id",Integer.toString(finalItem.purchase_id));
                    context.startActivity(intent);
                }
            });
        }

        if(transaction.sale_id!=0){
            invoiceInfo item = new invoiceInfo();
            PartyInfo customer=new PartyInfo();
            item=databaseHelper.getInvoiceInfoForDisplay(String.valueOf(transaction.sale_id));
            customer= databaseHelper.getCustomerName(String.valueOf(item.customer_id));
            status.setText("sale");
            status.setBackground(context.getResources().getDrawable(R.drawable.shape));
            status.setBackgroundColor(Color.parseColor("#38e309"));
            status.setPadding(30,0,30,0);
            transactionTotal.setText("Rs."+item.invoice_total);
            transactionBalance.setText("Rs."+item.balance_due);
            transactionDate.setText(item.invoice_date);
            customerName.setText(customer.name);
            final invoiceInfo finalItem = item;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,InvoiceDetails.class);
                    intent.putExtra("invoice_id",Integer.toString(finalItem.invoice_no));
                    context.startActivity(intent);
                }
            });
        }


        return  view;
    }
}
