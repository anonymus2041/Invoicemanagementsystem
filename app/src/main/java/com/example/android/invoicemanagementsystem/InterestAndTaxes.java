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
import android.widget.Toast;

import java.util.Calendar;

public class InterestAndTaxes extends AppCompatActivity {
    InterestAdapter interestAdapter;
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
    CheckBox interest,taxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        fab_income=findViewById(R.id.fab_add_income);
        listView=findViewById(R.id.income_list_view);
        databaseHelper= new DatabaseHelper(this);
        interestAdapter=new InterestAdapter(this,databaseHelper.getInterestInfo());
        listView.setAdapter(interestAdapter);

        fab_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogBox();
            }
        });
    }

    public void showCustomDialogBox(){
        View view = LayoutInflater.from(this).inflate(R.layout.add_interest,null );

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
                databaseHelper=new DatabaseHelper(InterestAndTaxes.this);
                selectedCustomer=(String)parent.getItemAtPosition(position);
                customerInfo = databaseHelper.getCustomerIdForInvoice(selectedCustomer);
                Toast.makeText(InterestAndTaxes.this,"id= "+customerInfo.party_id,Toast.LENGTH_LONG).show();
            }
        });
        asOfDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(InterestAndTaxes.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
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

        IncomePrice = view.findViewById(R.id.IncomeAmount);

        interest = (CheckBox) view.findViewById(R.id.interest);
        taxes= (CheckBox) view.findViewById(R.id.taxes);
        final int[] interestExp= new int[1];
        final int[] tax= new int[1];
        //value from checkbox
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    interestExp[0] =1;
                }
                else{
                    interestExp[0] =0;
                }
            }
        });
        taxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    tax[0] =1;
                }
                else {
                    tax[0] =0;
                }
            }
        });


        contentValues = new ContentValues();
        databaseHelper = new DatabaseHelper(this);
        saveToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put("interest",String.valueOf(interestExp[0]));
                contentValues.put("tax",String.valueOf(tax[0]));
                contentValues.put("party_id",customerInfo.party_id);
                contentValues.put("amount",IncomePrice.getText().toString());
                contentValues.put("date",asOfDate.getText().toString());
                databaseHelper.insertInterest(contentValues);
                interestAdapter=new InterestAdapter(InterestAndTaxes.this,databaseHelper.getInterestInfo());
                listView.setAdapter(interestAdapter);

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
