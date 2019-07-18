package com.example.android.invoicemanagementsystem;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class InvoiceDetails extends AppCompatActivity {
    String invoice_id;
    DatabaseHelper databaseHelper;
    invoiceInfo invoiceDetails,getInvoiceinfo;
    EditText invoiceNo,dueDate,invoiceDate;
    ListView listView;
    FloatingActionButton addItems;
    InvoiceTest invoiceTestobj;
    Button updateInvoice;
    ContentValues contentValues,contentValues1,contentValues2;
    InvoiceItemsListAdapter itemsListAdapter;
    float subTotal;
    InventoryItems items,updateItem;
    AutocompleteAdapter adapter;
    String selectedItem;
    TextView subTotalAmount;
    InvoiceItemsListAdapter invoiceItemsListAdapter;
    int qtyOfItemInInvoice;
    int finalQty;
    InventoryItems checkOutOfStock;
    ArrayList<InventoryItems> listOutOfStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        getInvoiceinfo=new invoiceInfo();
        listView=findViewById(R.id.showInvoiceItemsDetails_listView);
        invoice_id = getIntent().getStringExtra("invoice_id");

        databaseHelper =new DatabaseHelper(this);
        getInvoiceinfo=databaseHelper.getInvoiceInfoForDisplay(invoice_id);
        subTotal=getInvoiceinfo.invoice_total;

        subTotalAmount = findViewById(R.id.Show_totalAmount_invoice);
        invoiceDetails=new invoiceInfo();
        invoiceDetails = databaseHelper.getInvoiceInfoForDisplay(invoice_id);

        invoiceNo= findViewById(R.id.show_invoiceNo);
        invoiceDate= findViewById(R.id.show_invoiceDate);
        dueDate= findViewById(R.id.show_dueDate);

        invoiceNo.setText("#00"+invoiceDetails.invoice_no);
        invoiceDate.setText(invoiceDetails.invoice_date);
        dueDate.setText(invoiceDetails.due_date);

        subTotalAmount.setText("Rs."+invoiceDetails.invoice_total);
        Toast toast = Toast.makeText(this,"total="+invoiceDetails.invoice_total,Toast.LENGTH_LONG);
        toast.show();
        invoiceItemsListAdapter = new InvoiceItemsListAdapter(this,databaseHelper.getInvoiceItemsList(invoice_id));
        listView.setAdapter(invoiceItemsListAdapter);
        invoiceTestobj = new InvoiceTest();
        addItems= findViewById(R.id.fab_add_invoiceItem);
        updateInvoice= findViewById(R.id.updateInvoiceDetails);
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogBox(invoice_id,listView);
            }
        });

        updateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues2 = new ContentValues();
                contentValues2.put("invoice_no",String.valueOf(invoiceDetails.invoice_no));
                contentValues2.put("invoice_date",invoiceDate.getText().toString());
                contentValues2.put("due_date",dueDate.getText().toString());
                contentValues2.put("invoice_total",String.valueOf(subTotal));
                boolean result=databaseHelper.updateInvoiceDetails(String.valueOf(invoiceDetails.invoice_no),contentValues2);
                if(result)
                {
                    Toast.makeText(InvoiceDetails.this,"Data updated successfully",Toast.LENGTH_LONG).show();
                }
                checkOutOfStock= new InventoryItems();
                listOutOfStock= new ArrayList<>();
                listOutOfStock=databaseHelper.getProductOutOfStockList();

                if(!listOutOfStock.isEmpty())
                {
                    Calendar calendar=Calendar.getInstance();
                    calendar.add(Calendar.SECOND,5);
                    Intent intent = new Intent(InvoiceDetails.this,Notification_receiver.class);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(InvoiceDetails.this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager= (AlarmManager) InvoiceDetails.this.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                }
                InvoiceDetails.this.finish();
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
        discount =view.findViewById(R.id.enter_discountAmount);
        databaseHelper = new DatabaseHelper(this);
        adapter = new AutocompleteAdapter(this,databaseHelper.getItemsNamelist());
        itemName.setAdapter(adapter);
        itemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String)parent.getItemAtPosition(position);
                items = databaseHelper.getItemIdForInvoice(selectedItem);
                rate.setText("Rs:"+items.unit_price);
            }
        });

        contentValues = new ContentValues();
        saveToInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float amount = (items.unit_price * Integer.parseInt(qty.getText().toString()))-Float.parseFloat(discount.getText().toString());
                contentValues.put("invoice_no",invoice_no);
                contentValues.put("product_id",items.product_id);
                contentValues.put("quantity",qty.getText().toString());
                contentValues.put("discount",discount.getText().toString());
                contentValues.put("amount",amount);
                databaseHelper.insertInvoiceItemsData(contentValues);
                itemsListAdapter = new InvoiceItemsListAdapter(InvoiceDetails.this,databaseHelper.getInvoiceItemsList(invoice_no));
                listView.setAdapter(itemsListAdapter);
                listView.setVisibility(View.VISIBLE);
                subTotal+=amount;
                subTotalAmount.setText("Rs."+Math.round(subTotal));

                contentValues1=new ContentValues();
                updateItem= databaseHelper.getItemNameForInvoice(String.valueOf(items.product_id));
                qtyOfItemInInvoice = Integer.parseInt(qty.getText().toString());
                finalQty=updateItem.qty_in_stock-qtyOfItemInInvoice;
                contentValues1.put("product_name",itemName.getText().toString());
                contentValues1.put("unit_price",items.unit_price);
                contentValues1.put("qty_in_stock",Integer.toString(finalQty));
                databaseHelper.updateInventoryStock(String.valueOf(items.product_id),contentValues1);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
