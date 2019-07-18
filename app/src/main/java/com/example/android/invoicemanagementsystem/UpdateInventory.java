package com.example.android.invoicemanagementsystem;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class UpdateInventory extends AppCompatActivity {
    String product_id;
    Button update;
    EditText itemName,unit_price,stock;
    ContentValues contentValues;
    DatabaseHelper databaseHelper;
    InventoryItems item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(UpdateInventory.this).inflate(R.layout.activity_update_inventory, null);
        product_id=this.getIntent().getStringExtra("product_id");
        databaseHelper=new DatabaseHelper(this);
        item=databaseHelper.getItemNameForInvoice(product_id);
        final Dialog dialog = new Dialog(UpdateInventory.this);
        dialog.setTitle("Inventory Items");
        update=view.findViewById(R.id.update_item_to_inventoryBtn);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemName=view.findViewById(R.id.update_item_nameInventory);
        unit_price=view.findViewById(R.id.update_unitPrice_Inventory);
        stock=view.findViewById(R.id.update_itemStock);
        itemName.setText(item.product_name);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues= new ContentValues();
                contentValues.put("product_id",item.product_id);
                contentValues.put("product_name",item.product_name);
                contentValues.put("unit_price",unit_price.getText().toString());
                contentValues.put("qty_in_stock",stock.getText().toString());
                databaseHelper.updateInventoryStock(String.valueOf(item.product_id),contentValues);
                dialog.dismiss();
                UpdateInventory.this.finish();
            }
        });
        dialog.show();

    }
}
