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

public class InvoiceTest extends AppCompatActivity {
    ContentValues contentValues,contentValues1;
    DatabaseHelper databaseHelper;
    EditText invoice_no,invoice_date,due_date;
    AutocompleteAdapter adapter;
    String selectedItem;
    InventoryItems items,updateItem;
    ListView listView;
    Button save;
    Float subTotal;
    InvoiceItemsListAdapter itemsListAdapter;
    TextView subTotalAmount;
    Bundle bundle;
    InventoryFragment inventoryFragment;
    int qtyOfItemInInvoice;
    int finalQty;
    InventoryItems checkOutOfStock;
    ArrayList<InventoryItems> listOutOfStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_test);
        invoice_no = findViewById(R.id.enter_invoiceNo);
        invoice_date = findViewById(R.id.enter_invoiceDate);
        due_date = findViewById(R.id.enter_dueDate);
        save = findViewById(R.id.saveInvoiceButton);
        listView=findViewById(R.id.listView_showInvoiceItems);
        subTotalAmount=findViewById(R.id.totalAmount_invoice);
        subTotal=0.0f;
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_invoiceItem);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogBox(invoice_no.getText().toString(),listView);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues=new ContentValues();
                databaseHelper=new DatabaseHelper(InvoiceTest.this);
                contentValues.put("invoice_no",invoice_no.getText().toString());
                contentValues.put("invoice_date",invoice_date.getText().toString());
                contentValues.put("due_date",due_date.getText().toString());
                contentValues.put("invoice_total",Math.round(subTotal));

                boolean result=databaseHelper.insertInvoiceData(contentValues);
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
                    contentValues1=new ContentValues();
                    updateItem= databaseHelper.getItemNameForInvoice(String.valueOf(items.product_id));
                    qtyOfItemInInvoice = Integer.parseInt(qty.getText().toString());
                    finalQty=updateItem.qty_in_stock-qtyOfItemInInvoice;
                    contentValues1.put("product_name",itemName.getText().toString());
                    contentValues1.put("unit_price",items.unit_price);
                    contentValues1.put("qty_in_stock",Integer.toString(finalQty));
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
