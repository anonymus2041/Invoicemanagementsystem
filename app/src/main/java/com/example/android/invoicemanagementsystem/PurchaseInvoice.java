package com.example.android.invoicemanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PurchaseInvoice extends AppCompatActivity {
    FloatingActionButton fab_purchase_items;
    EditText asOfDate,purchaseDate;
    DatePickerDialog.OnDateSetListener setListener,setListener1;
    String date,primaryUnit,conversionUnit,secUnit;
    ContentValues contentValues,contentValues1,contentValues2;
    DatabaseHelper databaseHelper;
    SharedPreferences preferences;
    PurchaseInvoiceItemsListAdapter itemsListAdapter;
    ListView listView;
    EditText itemName,purchasePrice,SellingPrice,OpeningStock,MinStockValue,
            UnitPrice;
    Button savePurchaseInvoice;
    EditText invoice_date,discountPercent,balanceDue,TotalAmount,paid_amount,invoice_no;
    float finalInvoiceTotal,finalDiscountPercent,finalDueBalance;
    PartyInfo customerInfo;
    AutocompleteAdapter customerAdapter;
    AutoCompleteTextView customers;
    String selectedCustomer;
    float subTotal;
    TextView subTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_invoice);
        fab_purchase_items=findViewById(R.id.fab_add_purchase);
        purchaseDate=findViewById(R.id.enterPurchaseDate);
        invoice_no=findViewById(R.id.enterPurchaseInvoiceNo);
        listView=findViewById(R.id.listView_showPurchaseInvoiceItems);
        savePurchaseInvoice=findViewById(R.id.savePurchaseInvoiceButton);
        customers=findViewById(R.id.enter_purchaseParty);
        subTotalAmount=findViewById(R.id.totalAmount_purchase);

        invoice_date = findViewById(R.id.enterPurchaseDate);
        discountPercent = findViewById(R.id.enter_discountPurchase);
        TotalAmount = findViewById(R.id.enter_PurchaseTotal);
        paid_amount = findViewById(R.id.enterPaidamount);
        balanceDue = findViewById(R.id.dueAmount);

        subTotal=0.0f;

        fab_purchase_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogBox();
            }
        });
        Calendar calendar= Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month =calendar.get(Calendar.MONTH);
        final int day =calendar.get(Calendar.DAY_OF_MONTH);
        purchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(PurchaseInvoice.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month =month+1;
                date = day+"/"+month+"/"+year;
                purchaseDate.setText(date);
            }
        };
        /*to show customer names in invoice*/
        databaseHelper = new DatabaseHelper(this);
        customerAdapter = new AutocompleteAdapter(this,databaseHelper.getCustomerNamelist());
        customers.setAdapter(customerAdapter);
        //to find the id of customer
        customers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomer=(String)parent.getItemAtPosition(position);
                customerInfo=databaseHelper.getCustomerIdForInvoice(selectedCustomer);
            }
        });


        discountPercent.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                try {
                    finalInvoiceTotal = subTotal - (subTotal * Float.parseFloat(value)) / 100;
                    TotalAmount.setText(String.valueOf(finalInvoiceTotal));
                }
                catch (Exception e)
                {
                    value="0";
                    finalInvoiceTotal = subTotal - (subTotal * Float.parseFloat(value)) / 100;
                    TotalAmount.setText(String.valueOf(finalInvoiceTotal));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

        });
        paid_amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                try {
                    finalDueBalance=finalInvoiceTotal-Float.parseFloat(value);
                    balanceDue.setText(String.valueOf(finalDueBalance));
                }
                catch (Exception e){
                    value="0";
                    finalDueBalance=finalInvoiceTotal-Float.parseFloat(value);
                    balanceDue.setText(String.valueOf(finalDueBalance));

                }



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
            }

        });





        savePurchaseInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues=new ContentValues();
                databaseHelper=new DatabaseHelper(PurchaseInvoice.this);
                contentValues.put("purchase_id",invoice_no.getText().toString());
                contentValues.put("invoice_date",invoice_date.getText().toString());
                contentValues.put("invoice_total",String.valueOf(finalInvoiceTotal));
                contentValues.put("discount_percent",String.valueOf(finalDiscountPercent));
                contentValues.put("paid_amount",paid_amount.getText().toString());
                contentValues.put("balance_due",Math.round(finalDueBalance));
                contentValues.put("customer_id",customerInfo.party_id);
                databaseHelper.insertPurchaseInvoiceData(contentValues);

                //insert to transactions table
                contentValues1 = new ContentValues();
                contentValues1.put("purchase_id",invoice_no.getText().toString());
                contentValues1.put("customer_id",customerInfo.party_id);
                contentValues1.put("date",invoice_date.getText().toString());
                databaseHelper.insertTransaction(contentValues1);
                PurchaseInvoice.this.finish();

            }
        });


    }
    public void showCustomDialogBox(){
        View view = LayoutInflater.from(this).inflate(R.layout.add_items_to_inventory,null );

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Inventory");

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button saveToInventory = (Button) view.findViewById(R.id.save_item_to_inventoryBtn);



        asOfDate= (EditText) view.findViewById(R.id.enterAsOfStockDate);
        asOfDate.setText(date);
        //save items to inventory
        itemName = view.findViewById(R.id.enter_item_nameInventory);
        purchasePrice = view.findViewById(R.id.enterPurchasePrice);
        SellingPrice = view.findViewById(R.id.enter_SellingPrice);
        OpeningStock = view.findViewById(R.id.enterOpening_stock);
        Toast.makeText(PurchaseInvoice.this,OpeningStock.getText().toString(),Toast.LENGTH_LONG).show();
        MinStockValue = view.findViewById(R.id.enter_minStockQty);
        UnitPrice = view.findViewById(R.id.enter_price_per_unit);
        contentValues = new ContentValues();
        databaseHelper = new DatabaseHelper(this);
        saveToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences=PurchaseInvoice.this.getSharedPreferences("conversionUnit",0);

                primaryUnit=preferences.getString("primaryUnit","");
                secUnit=preferences.getString("secUnit","");
                conversionUnit=preferences.getString("conversionUnit","");
                Float stockValue;
                try {
                    stockValue=Float.parseFloat(OpeningStock.getText().toString())*
                            Float.parseFloat(UnitPrice.getText().toString());
                }
                catch (Exception e)
                {
                    stockValue=0.0f;
                }

                contentValues = new ContentValues();
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
                //store info of invoice no and items related to it
                contentValues2=new ContentValues();
                contentValues2.put("invoice_no",invoice_no.getText().toString());
                contentValues2.put("product_name",itemName.getText().toString());
                databaseHelper.insertPurchaseInvoiceItemsData(contentValues2);


                //show in listview

                itemsListAdapter = new PurchaseInvoiceItemsListAdapter(PurchaseInvoice.this,databaseHelper.getPurchaseInvoiceItemsList(invoice_no.getText().toString()));
                listView.setAdapter(itemsListAdapter);
                listView.setVisibility(View.VISIBLE);

                subTotal+=(Float.parseFloat(OpeningStock.getText().toString())*Float.parseFloat(purchasePrice.getText().toString()));
                subTotalAmount.setText("Rs."+Math.round(subTotal));

                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
