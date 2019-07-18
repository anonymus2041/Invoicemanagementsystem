package com.example.android.invoicemanagementsystem;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Invoice_sample extends AppCompatActivity {
//    private static final int CONTENT_VIEW_ID = 10101010;
    EditText invoice_no,invoice_date,due_date;
    DatabaseHelper databaseHelper;
    AutocompleteAdapter adapter1;
    ContentValues contentValues;
    AutoCompleteTextView autoCompleteTextView[]= new AutoCompleteTextView[10];
    ArrayList<LinearLayout>linList;
    LinearLayout lin1,lin2,lin3,lin4,lin5,lin6,lin7,lin8,lin9,lin10;
    public static int count=0;
    int i=1;
 Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_sample);

        final FloatingActionButton add_items = (FloatingActionButton) findViewById(R.id.add_items_invoice);
//        spinner = findViewById(R.id.item1);
        //spinner.setAdapter(new ItemsSpinnerAdapter(this,databaseHelper.getItemList()));
        linList=new ArrayList<LinearLayout>();
        lin1=(LinearLayout)findViewById(R.id.add_item1);
        lin2=(LinearLayout)findViewById(R.id.add_item2);
        lin3=(LinearLayout)findViewById(R.id.add_item3);
        lin4=(LinearLayout)findViewById(R.id.add_item4);
        lin5=(LinearLayout)findViewById(R.id.add_item5);
        lin6=(LinearLayout)findViewById(R.id.add_item6);
        lin7=(LinearLayout)findViewById(R.id.add_item7);
        lin8=(LinearLayout)findViewById(R.id.add_item8);
        lin9=(LinearLayout)findViewById(R.id.add_item9);
        lin10=(LinearLayout)findViewById(R.id.add_item10);
        linList.add(lin1);
        linList.add(lin2);
        linList.add(lin3);
        linList.add(lin4);
        linList.add(lin5);
        linList.add(lin6);
        linList.add(lin7);
        linList.add(lin8);
        linList.add(lin9);
        linList.add(lin10);
        autoCompleteTextView[0]=findViewById(R.id.autocomplete);
        autoCompleteTextView[1]=findViewById(R.id.autocomplete2);
        autoCompleteTextView[2]=findViewById(R.id.autocomplete3);
        autoCompleteTextView[3]=findViewById(R.id.autocomplete4);
        autoCompleteTextView[4]=findViewById(R.id.autocomplete5);
        autoCompleteTextView[5]=findViewById(R.id.autocomplete6);
        autoCompleteTextView[6]=findViewById(R.id.autocomplete7);
        autoCompleteTextView[7]=findViewById(R.id.autocomplete8);
        autoCompleteTextView[8]=findViewById(R.id.autocomplete9);
        autoCompleteTextView[9]=findViewById(R.id.autocomplete10);

        add_items.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(count<10) {
                    linList.get(count).setVisibility(View.VISIBLE);
                    count++;
                }
                if(count==10)
                {
                    count=0;
                }

            }
        });
        for(int j=0;j<10;j++)
        {
            databaseHelper = new DatabaseHelper(Invoice_sample.this);
            adapter1 = new AutocompleteAdapter(Invoice_sample.this,databaseHelper.getItemsNamelist());
            autoCompleteTextView[j].setAdapter(adapter1);
        }
        invoice_no = findViewById(R.id.invoice_no);
        invoice_date = findViewById(R.id.invoice_date);
        due_date = findViewById(R.id.due_date);
        contentValues = new ContentValues();
        contentValues.put("invoice_no",invoice_no.getText().toString());
        contentValues.put("invoice_date",invoice_date.getText().toString());
        contentValues.put("due_date",due_date.getText().toString());
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.insertInvoiceData(contentValues);
        save = (Button) findViewById(R.id.save);
        databaseHelper = new DatabaseHelper(Invoice_sample.this);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                contentValues.put("invoice_no",invoice_no.getText().toString());
                contentValues.put("invoice_date",invoice_date.getText().toString());
                contentValues.put("due_date",due_date.getText().toString());
                boolean result=databaseHelper.insertInvoiceData(contentValues);
                if(result)
                {
                    Toast toast = Toast.makeText(Invoice_sample.this,"data inserted successfully",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
