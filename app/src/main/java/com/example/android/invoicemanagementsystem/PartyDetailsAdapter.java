package com.example.android.invoicemanagementsystem;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PartyDetailsAdapter extends ArrayAdapter<Transactionstable> {
    Context context;
    DatabaseHelper databaseHelper;

    float sumPurchase,sumSale,sumTotal;
    TextView status,transactionTotal,transactionBalance,transactionDate;
    public PartyDetailsAdapter(@NonNull Context context, ArrayList<Transactionstable>list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_party_details,null);
        sumPurchase =0.0f;
        sumSale=0.0f;
        status=view.findViewById(R.id.transaction_statusListView);
        transactionTotal=view.findViewById(R.id.totalTransactionAmountListView);
        transactionBalance=view.findViewById(R.id.totalTransactionBalanceListview);
        transactionDate=view.findViewById(R.id.transactionDateListview);
        Transactionstable transaction=getItem(position);
        databaseHelper=new DatabaseHelper(context);
        if(transaction.purchase_id!=0){
            PurchaseInfo item = new PurchaseInfo();
           // PartyInfo customer=new PartyInfo();
            item=databaseHelper.getPurchaseInvoiceInfoForDisplay(String.valueOf(transaction.purchase_id));
            //customer= databaseHelper.getCustomerName(String.valueOf(item.customer_id));
            status.setText("purchase");
            status.setBackground(context.getResources().getDrawable(R.drawable.shape));
            //status.setBackgroundResource(R.drawable.shape);
            status.setBackgroundColor(Color.parseColor("#c90a0e"));
            //#de1245
            status.setPadding(30,0,30,0);
            transactionTotal.setText("Rs."+item.invoice_total);
            transactionBalance.setText("Rs."+item.balance_due);
            transactionDate.setText(item.invoice_date);
            sumPurchase+=item.invoice_total;

        }

        if(transaction.sale_id!=0){
            invoiceInfo item = new invoiceInfo();
            //PartyInfo customer=new PartyInfo();
            item=databaseHelper.getInvoiceInfoForDisplay(String.valueOf(transaction.sale_id));
            //customer= databaseHelper.getCustomerName(String.valueOf(item.customer_id));
            status.setText("sale");
            status.setBackground(context.getResources().getDrawable(R.drawable.shape));
            status.setBackgroundColor(Color.parseColor("#08801a"));
            status.setPadding(30,0,30,0);
            transactionTotal.setText("Rs."+item.invoice_total);
            transactionBalance.setText("Rs."+item.balance_due);
            transactionDate.setText(item.invoice_date);
            sumSale+=item.invoice_total;
        }
           sumTotal=sumPurchase-sumSale;


        return  view;
    }
}
