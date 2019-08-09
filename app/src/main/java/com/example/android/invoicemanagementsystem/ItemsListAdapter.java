package com.example.android.invoicemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemsListAdapter extends ArrayAdapter<InventoryItems> {
    Context context;
    TextView itemName,stock,stockValue,purchasePrice,salePrice,asOfDate;
    ContentValues contentValues;
    Button save;
    private NotificationManagerCompat notificationManager;
    public ItemsListAdapter(@NonNull Context context, ArrayList<InventoryItems> list) {
        super(context, 0,list);
        this.context=context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_show_inventory,null);
        itemName = view.findViewById(R.id.itemname_listView);
        stock = view.findViewById(R.id.qty_value_list);
        stockValue=view.findViewById(R.id.stockValue_listView);
        purchasePrice=view.findViewById(R.id.Purchaseprice_listView);
        salePrice=view.findViewById(R.id.salePrice_value_list);
        asOfDate=view.findViewById(R.id.asOfdATEdisplay);
        final InventoryItems item = getItem(position);
        itemName.setText(item.product_name);
        purchasePrice.setText("Rs."+item.purchase_price);
        salePrice.setText("Rs."+item.sale_price);
        stock.setText(""+item.current_stock_qty);
        stockValue.setText("Rs."+item.current_stock_qty*item.unit_price);
        asOfDate.setText(item.AsOfDate);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateInventory.class);
                intent.putExtra("product_id",String.valueOf(item.product_id));
                context.startActivity(intent);
               }
        });
        return view;
    }

}
