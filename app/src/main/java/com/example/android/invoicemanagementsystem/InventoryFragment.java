package com.example.android.invoicemanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;

public class InventoryFragment extends Fragment {
    DatabaseHelper databaseHelper;
    ContentValues contentValues;
    ListView listView;
    ItemsListAdapter adapter;
    int product_id;
    String qtyInInvoice;
    InventoryItems item,checkOutOfStock;
    int qtyOfItemInInvoice;
    int finalQty;
    EditText itemName,purchasePrice,SellingPrice,OpeningStock,MinStockValue,UnitPrice;
    int status;
    EditText asOfDate;
    DatePickerDialog.OnDateSetListener setListener;
    Button selectUnit,saveToInventory;
    String primaryUnit,secUnit,conversionUnit;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inventory,container,false);
        databaseHelper = new DatabaseHelper(getActivity());
        listView = (ListView) view.findViewById(R.id.inventory_list_view);
        adapter = new ItemsListAdapter(getActivity(),databaseHelper.getItemslist());
        listView.setAdapter(adapter);

//
//        Toast toast=Toast.makeText(getActivity(),"primaryUnit= "+primaryUnit,Toast.LENGTH_LONG);
//        toast.show();

        FloatingActionButton addInventoryItems = (FloatingActionButton) view.findViewById(R.id.fab_add_inventory);
        addInventoryItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogBox();
            }
        });

        return view;
    }
    public void showCustomDialogBox(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_items_to_inventory,null );

        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Inventory");

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button saveToInventory = (Button) view.findViewById(R.id.save_item_to_inventoryBtn);
        asOfDate= (EditText) view.findViewById(R.id.enterAsOfStockDate);
        selectUnit=view.findViewById(R.id.select_unit);

        selectUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent= new Intent(getActivity(),SelectUnit.class);
            startActivity(intent);
            }
        });

        Calendar calendar= Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month =calendar.get(Calendar.MONTH);
        final int day =calendar.get(Calendar.DAY_OF_MONTH);
        asOfDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                           year,month,day);
                   datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                   datePickerDialog.show();
            }
        });
        setListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String date = day+"/"+month+"/"+year;
                asOfDate.setText(date);
            }
        };

        itemName = view.findViewById(R.id.enter_item_nameInventory);
        purchasePrice = view.findViewById(R.id.enterPurchasePrice);
        SellingPrice = view.findViewById(R.id.enter_SellingPrice);
        OpeningStock = view.findViewById(R.id.enterOpening_stock);
        MinStockValue = view.findViewById(R.id.enter_minStockQty);
        UnitPrice = view.findViewById(R.id.enter_price_per_unit);
        contentValues = new ContentValues();
        databaseHelper = new DatabaseHelper(getActivity());
        saveToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences=getActivity().getSharedPreferences("conversionUnit",0);

                primaryUnit=preferences.getString("primaryUnit","");
                secUnit=preferences.getString("secUnit","");
                conversionUnit=preferences.getString("conversionUnit","");
                Float stockValue=Float.parseFloat(OpeningStock.getText().toString())*
                        Float.parseFloat(UnitPrice.getText().toString());
                contentValues.put("product_name",itemName.getText().toString());
                contentValues.put("primary_unit",primaryUnit);
                contentValues.put("secondary_unit",secUnit);
                contentValues.put("conversion_rate",conversionUnit);
                contentValues.put("purchase_price",purchasePrice.getText().toString());
                contentValues.put("selling_price",SellingPrice.getText().toString());
                contentValues.put("opening_stock",OpeningStock.getText().toString());
                contentValues.put("AsOfDate",asOfDate.getText().toString());
                contentValues.put("unit_price",UnitPrice.getText().toString());
                contentValues.put("minStockQty",MinStockValue.getText().toString());
                contentValues.put("current_stock_qty",OpeningStock.getText().toString());
                contentValues.put("stock_value",String.valueOf(stockValue));

                databaseHelper.insertItem(contentValues);

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
