package com.example.android.invoicemanagementsystem;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class InvoiceTest extends AppCompatActivity {
    ContentValues contentValues,contentValues1,contentValues2;
    DatabaseHelper databaseHelper;
    EditText invoice_no,invoice_date,due_date,discountPercent,TotalAmount,balanceDue,Receivedamount;
    AutocompleteAdapter adapter,customerAdapter;
    AutoCompleteTextView customers;
    String selectedItem,selectedCustomer;
    InventoryItems items,updateItem;
    ListView listView;
    Button save;
    Float subTotal;
    InvoiceItemsListAdapter itemsListAdapter;
    TextView subTotalAmount;
    Bundle bundle;
    InventoryFragment inventoryFragment;
    float qtyOfItemInInvoice;
    float finalQty;
    InventoryItems checkOutOfStock;
    ArrayList<InventoryItems> listOutOfStock;
    ArrayList<String> units;
    UnitsSpinnerAdapter UnitsAdapter;
    Spinner unitsSpinner;
    String selectedUnit;
    float unitPrice;
    float currentStock;
    int finalMonth;
    PartyInfo customerInfo;
    float finalInvoiceTotal,finalDueBalance,finalDiscountPercent,finalReceivedAmount;
    //int day,month,year;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_test);
        invoice_no = findViewById(R.id.enter_invoiceNo);
        invoice_date = findViewById(R.id.enter_invoiceDate);
        discountPercent = findViewById(R.id.enter_discountPercent);
        due_date = findViewById(R.id.enter_dueDate);
        TotalAmount = findViewById(R.id.TotalamountOfInvoice);
        Receivedamount = findViewById(R.id.receivedAmount);
        balanceDue = findViewById(R.id.balanceDue);
        save = findViewById(R.id.saveInvoiceButton);
        listView=findViewById(R.id.listView_showInvoiceItems);
        customers=findViewById(R.id.customerForInvoice);
        //Date of Invoice
        Calendar calendar= Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month =calendar.get(Calendar.MONTH);
        final int day =calendar.get(Calendar.DAY_OF_MONTH);
        finalMonth=month+1;
        String date= day+"/"+finalMonth+"/"+year;
        invoice_date.setText(date);
        due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(InvoiceTest.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month =month+1;
                String date = day+"/"+month+"/"+year;
                due_date.setText(date);
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


        subTotalAmount=findViewById(R.id.totalAmount_invoice);
        subTotal=0.0f;
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_invoiceItem);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogBox(invoice_no.getText().toString(),listView);

            }
        });

        discountPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        //set the values of payment on real time use textwatcher


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
        Receivedamount.addTextChangedListener(new TextWatcher() {

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




        //


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finalDiscountPercent=Float.parseFloat(discountPercent.getText().toString());
//                finalReceivedAmount=Float.parseFloat(Receivedamount.getText().toString());
//                finalInvoiceTotal=subTotal-(subTotal*finalDiscountPercent)/100;
//                finalDueBalance=finalInvoiceTotal-finalReceivedAmount;
//                Toast.makeText(InvoiceTest.this,"subtotal="+subTotal,Toast.LENGTH_LONG).show();
                //TotalAmount.setText(String.valueOf(finalInvoiceTotal));
//                balanceDue.setText(String.valueOf(finalDueBalance));
                contentValues=new ContentValues();
                databaseHelper=new DatabaseHelper(InvoiceTest.this);
                contentValues.put("invoice_no",invoice_no.getText().toString());
                contentValues.put("invoice_date",invoice_date.getText().toString());
                contentValues.put("due_date",due_date.getText().toString());
                contentValues.put("invoice_total",String.valueOf(finalInvoiceTotal));
                contentValues.put("discount_percent",String.valueOf(finalDiscountPercent));
                contentValues.put("received_amount",Receivedamount.getText().toString());
                contentValues.put("balance_due",Math.round(finalDueBalance));
                contentValues.put("customer_id",customerInfo.party_id);
                boolean result=databaseHelper.insertInvoiceData(contentValues);

                //insert data to transactions table

                contentValues2 = new ContentValues();
                contentValues2.put("sale_id",invoice_no.getText().toString());
                contentValues2.put("customer_id",customerInfo.party_id);
                contentValues2.put("date",invoice_date.getText().toString());
                databaseHelper.insertTransaction(contentValues2);
                //

                if(result)
                {
                    Toast toast = Toast.makeText(InvoiceTest.this,"data inserted successfully",Toast.LENGTH_LONG);
                    toast.show();
                }

                checkOutOfStock= new InventoryItems();
                listOutOfStock= new ArrayList<>();
                listOutOfStock=databaseHelper.getProductOutOfStockList();

                if(!listOutOfStock.isEmpty())
                {
                    Calendar calendar=Calendar.getInstance();
                    calendar.add(Calendar.SECOND,5);
                    Intent intent = new Intent(InvoiceTest.this,Notification_receiver.class);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(InvoiceTest.this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager= (AlarmManager) InvoiceTest.this.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                }
                InvoiceTest.this.finish();
            }
        });

    }
    public void showCustomDialogBox(final String invoice_no, final ListView listView){
        View view = LayoutInflater.from(this).inflate(R.layout.add_items_to_invoice,null );
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Invoice Items");

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button saveToInvoice = (Button) view.findViewById(R.id.addItemsToInvoiceButton);
        final EditText qty,rate,discount;
        final AutoCompleteTextView itemName;
        items = new InventoryItems();
        itemName = view.findViewById(R.id.enter_item_AutoComplete);
        qty = view.findViewById(R.id.enter_itemQuantity);
        rate =view.findViewById(R.id.enter_unitPrice_Invoice);
        unitsSpinner=view.findViewById(R.id.unitOfConversionInvoice);
        discount =view.findViewById(R.id.enter_discountAmount);
        databaseHelper = new DatabaseHelper(this);
        adapter = new AutocompleteAdapter(this,databaseHelper.getItemsNamelist());
        itemName.setAdapter(adapter);
        itemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String)parent.getItemAtPosition(position);
                items = databaseHelper.getItemIdForInvoice(selectedItem);
                units = new ArrayList<>();
                units.add(items.primary_unit);
                units.add(items.secondary_unit);
                UnitsAdapter = new UnitsSpinnerAdapter(InvoiceTest.this,units);
                unitsSpinner.setAdapter(UnitsAdapter);
                unitPrice=items.sale_price;
                rate.setText("Rs:"+unitPrice);

                unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedUnit=unitsSpinner.getSelectedItem().toString();

                        if(selectedUnit.equals(items.secondary_unit)) {
                            unitPrice/=items.conversion_rate;
                            rate.setText("Rs:"+unitPrice);

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

        });

        contentValues = new ContentValues();
        saveToInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float amount = (unitPrice * Integer.parseInt(qty.getText().toString()))-Float.parseFloat(discount.getText().toString());

                contentValues.put("invoice_no",invoice_no);
                contentValues.put("product_id",items.product_id);
                contentValues.put("quantity",qty.getText().toString());
                contentValues.put("discount",discount.getText().toString());
                contentValues.put("amount",amount);
                databaseHelper.insertInvoiceItemsData(contentValues);
                itemsListAdapter = new InvoiceItemsListAdapter(InvoiceTest.this,databaseHelper.getInvoiceItemsList(invoice_no));
                listView.setAdapter(itemsListAdapter);
                listView.setVisibility(View.VISIBLE);
                subTotal+=amount;
                subTotalAmount.setText("Rs."+Math.round(subTotal));
//                Intent intent=new Intent(InvoiceTest.this,InventoryFragment.class);
//                intent.putExtra("product_id",items.product_id);
//                intent.putExtra("qty",qty.getText().toString());
//                startActivity(intent);
//                inventoryFragment= new InventoryFragment();
//                bundle = new Bundle();
//                bundle.putInt("product_id",items.product_id);
//                bundle.putString("qty",qty.getText().toString());
//                inventoryFragment.setArguments(bundle);

                ///////////
                    qtyOfItemInInvoice = Float.parseFloat(qty.getText().toString());
                updateItem= databaseHelper.getItemNameForInvoice(String.valueOf(items.product_id));
                currentStock=updateItem.current_stock_qty;
                finalQty=currentStock-qtyOfItemInInvoice;
                    if(selectedUnit.equals(items.secondary_unit)){
                        currentStock*=items.conversion_rate;
                        finalQty=(currentStock-qtyOfItemInInvoice)/items.conversion_rate;
                        Toast toast=Toast.makeText(InvoiceTest.this,String.valueOf(finalQty),Toast.LENGTH_LONG);
                        toast.show();
                    }
                    contentValues1=new ContentValues();
                    contentValues1.put("product_name",itemName.getText().toString());
                    contentValues1.put("current_stock_qty",Float.toString(finalQty));
                    contentValues1.put("stock_value",Float.toString(finalQty*items.unit_price));
                    databaseHelper.updateInventoryStock(String.valueOf(items.product_id),contentValues1);

                ////////////
                dialog.dismiss();
            }
        });
        dialog.show();
    }
//    public void showListView(){
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_invoice_test,null);
//        Lis
//    }
}
