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

public class InvoiceItemsListAdapter extends ArrayAdapter<invoiceitemsTable> {
    Context context;
    DatabaseHelper databaseHelper;
    public InvoiceItemsListAdapter(@NonNull Context context, ArrayList<invoiceitemsTable> list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.show_invoice_items,null);
        TextView itemname = view.findViewById(R.id.itemnameInvoice_listView);
        TextView amount= view.findViewById(R.id.item_amt_invoiceListView);
        TextView quantity= view.findViewById(R.id.qty_in_InvoiceValue_list);
        TextView discount = view.findViewById(R.id.discount_amt_Invoice_listView);
        final invoiceitemsTable item = getItem(position);
        databaseHelper =new DatabaseHelper(context);
        InventoryItems product = new InventoryItems();
        product=databaseHelper.getItemNameForInvoice(Integer.toString(item.product_id));
//        itemname.setText("#00"+item.);
        discount.setText("Rs."+item.discount);
        quantity.setText(""+item.quantity);

        itemname.setText(product.product_name);

        amount.setText("Rs."+item.amount);
        return view;
    }
}
