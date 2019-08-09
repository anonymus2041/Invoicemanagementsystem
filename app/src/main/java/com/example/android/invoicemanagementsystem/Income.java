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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class Income extends AppCompatActivity {
    IncomeAdapter incomeAdapter;
    FloatingActionButton fab_income;
    EditText asOfDate;
    DatePickerDialog.OnDateSetListener setListener;
    ContentValues contentValues;
    DatabaseHelper databaseHelper;
    EditText IncomeName,IncomePrice;
    AutocompleteAdapter adapter;
    AutoCompleteTextView party;
    ListView listView;
    String selectedCustomer;
    PartyInfo customerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        fab_income=findViewById(R.id.fab_add_income);
        listView=findViewById(R.id.income_list_view);
        databaseHelper= new DatabaseHelper(this);
        incomeAdapter=new IncomeAdapter(this,databaseHelper.getIncomeInfo());
        listView.setAdapter(incomeAdapter);

        fab_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogBox();
            }
        });
    }

    public void showCustomDialogBox(){
        View view = LayoutInflater.from(this).inflate(R.layout.add_income,null );

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Inventory");

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button saveToInventory = (Button) view.findViewById(R.id.save_income);
        asOfDate= (EditText) view.findViewById(R.id.enterAsOfIncomeDate);
        Calendar calendar= Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month =calendar.get(Calendar.MONTH);
        final int day =calendar.get(Calendar.DAY_OF_MONTH);

        //to show autocomplete party name
        databaseHelper= new DatabaseHelper(this);
        party=view.findViewById(R.id.IncomePartyName);
        adapter = new AutocompleteAdapter(this,databaseHelper.getCustomerNamelist());
        party.setAdapter(adapter);
        customerInfo=new PartyInfo();
        party.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                databaseHelper=new DatabaseHelper(Income.this);
                selectedCustomer=(String)parent.getItemAtPosition(position);
                customerInfo = databaseHelper.getCustomerIdForInvoice(selectedCustomer);
                Toast.makeText(Income.this,"id= "+customerInfo.party_id,Toast.LENGTH_LONG).show();
            }
        });
        asOfDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(Income.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
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
                asOfDate.setText(date);
            }
        };

        IncomeName = view.findViewById(R.id.IncomeName);
        IncomePrice = view.findViewById(R.id.IncomeAmount);


        contentValues = new ContentValues();
        databaseHelper = new DatabaseHelper(this);
        saveToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put("income_name",IncomeName.getText().toString());
                contentValues.put("party_id",customerInfo.party_id);
                contentValues.put("amount",IncomePrice.getText().toString());
                contentValues.put("date",asOfDate.getText().toString());
                databaseHelper.insertIncome(contentValues);
                incomeAdapter=new IncomeAdapter(Income.this,databaseHelper.getIncomeInfo());
                listView.setAdapter(incomeAdapter);

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
