package com.example.android.invoicemanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Returns extends AppCompatActivity {
    ReturnAdapter returnAdapter;
    FloatingActionButton fab_expenses;
    EditText asOfDate;
    DatePickerDialog.OnDateSetListener setListener;
    ContentValues contentValues,contentValues1;
    DatabaseHelper databaseHelper;
    EditText ExpenseName,ExpensePrice;
    CheckBox purchaseReturn,saleReturn;
    AutoCompleteTextView customer,ReturnName;
    AutocompleteAdapter adapter,adapterItems;
    ListView listView;
    TextView ReturnPrice,ReturnQty;
    String selectedCustomer,selectedItem;
    PartyInfo customerInfo;
    InventoryItems items,updateItem;
    Float currentStock,finalQty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        fab_expenses=findViewById(R.id.fab_add_expenses);
        listView=findViewById(R.id.expenses_list_view);
        databaseHelper= new DatabaseHelper(this);
        databaseHelper.insertReturns(contentValues);
        returnAdapter=new ReturnAdapter(Returns.this,databaseHelper.getReturnsInfo());
        listView.setAdapter(returnAdapter);


        fab_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showCustomDialogBox();
            }
        });
    }

    public void showCustomDialogBox(){
        View view = LayoutInflater.from(this).inflate(R.layout.add_returns,null );

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Inventory");

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button saveToInventory = (Button) view.findViewById(R.id.save_expenses);
        asOfDate= (EditText) view.findViewById(R.id.enterAsOfExpenseDate);
        Calendar calendar= Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month =calendar.get(Calendar.MONTH);
        final int day =calendar.get(Calendar.DAY_OF_MONTH);


        asOfDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(Returns.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
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

        ReturnName = view.findViewById(R.id.returnedName);
        ReturnPrice = view.findViewById(R.id.expenseAmount);
        ReturnQty = view.findViewById(R.id.ReturnQty);
        databaseHelper= new DatabaseHelper(this);
        customer =  view.findViewById(R.id.customerReturn);
        //autocompleete product name
        databaseHelper = new DatabaseHelper(this);
        adapterItems = new AutocompleteAdapter(this,databaseHelper.getItemsNamelist());
        ReturnName.setAdapter(adapterItems);
        ReturnName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                databaseHelper=new DatabaseHelper(Returns.this);
                selectedItem = (String)parent.getItemAtPosition(position);
                items = databaseHelper.getItemIdForInvoice(selectedItem);
                Toast.makeText(Returns.this,"id= "+items.product_id,Toast.LENGTH_LONG).show();
            }
        });


        //autocomplete customer name
        adapter = new AutocompleteAdapter(this,databaseHelper.getCustomerNamelist());
        customer.setAdapter(adapter);
        customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                databaseHelper=new DatabaseHelper(Returns.this);
                selectedCustomer=(String)parent.getItemAtPosition(position);
                customerInfo = databaseHelper.getCustomerIdForInvoice(selectedCustomer);
                Toast.makeText(Returns.this,"id= "+customerInfo.party_id,Toast.LENGTH_LONG).show();
            }
        });

        purchaseReturn= (CheckBox) view.findViewById(R.id.purchaseReturn);
        saleReturn= (CheckBox) view.findViewById(R.id.salesReturn);
        final int[] pReturn = new int[1];
        final int[] sReturn = new int[1];
        //value from checkbox
        purchaseReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    pReturn[0] =1;
                }
                else{
                    pReturn[0] =0;
                }
            }
        });
        saleReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                   sReturn[0] =1;
                }
                else {
                    sReturn[0] =0;
                }
            }
        });

        contentValues = new ContentValues();
        databaseHelper = new DatabaseHelper(this);
        saveToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put("return_name",ReturnName.getText().toString());
                contentValues.put("purchase",Integer.toString(pReturn[0]));
                contentValues.put("sales",Integer.toString(sReturn[0]));
                contentValues.put("amount",ReturnPrice.getText().toString());
                contentValues.put("return_qty",ReturnQty.getText().toString());
                contentValues.put("date",asOfDate.getText().toString());
                contentValues.put("party_id",customerInfo.party_id);

                databaseHelper.insertReturns(contentValues);
               returnAdapter=new ReturnAdapter(Returns.this,databaseHelper.getReturnsInfo());
                listView.setAdapter(returnAdapter);

                if(pReturn[0]==1 && sReturn[0]==0){
                    updateItem= databaseHelper.getItemNameForInvoice(String.valueOf(items.product_id));
                    currentStock=updateItem.current_stock_qty;
                    finalQty=currentStock-Float.parseFloat(ReturnQty.getText().toString());
                    contentValues1=new ContentValues();
                    contentValues1.put("product_name",ReturnName.getText().toString());
                    contentValues1.put("current_stock_qty",Float.toString(finalQty));
                    contentValues1.put("stock_value",Float.toString(finalQty*items.unit_price));
                    databaseHelper.updateInventoryStock(String.valueOf(items.product_id),contentValues1);

                }
                if(pReturn[0]==0 && sReturn[0]==1){
                    updateItem= databaseHelper.getItemNameForInvoice(String.valueOf(items.product_id));
                    currentStock=updateItem.current_stock_qty;
                    finalQty=currentStock+Float.parseFloat(ReturnQty.getText().toString());
                    contentValues1=new ContentValues();
                    contentValues1.put("product_name",ReturnName.getText().toString());
                    contentValues1.put("current_stock_qty",Float.toString(finalQty));
                    contentValues1.put("stock_value",Float.toString(finalQty*items.unit_price));
                    databaseHelper.updateInventoryStock(String.valueOf(items.product_id),contentValues1);

                }


                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
