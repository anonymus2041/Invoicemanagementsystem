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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;

public class Expenses extends AppCompatActivity {
    ExpenseAdapter expenseAdapter;
    FloatingActionButton fab_expenses;
    EditText asOfDate;
    DatePickerDialog.OnDateSetListener setListener;
    ContentValues contentValues;
    DatabaseHelper databaseHelper;
    EditText ExpenseName,ExpensePrice;
    CheckBox Operating,Direct;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        fab_expenses=findViewById(R.id.fab_add_expenses);
        listView=findViewById(R.id.expenses_list_view);
        databaseHelper= new DatabaseHelper(this);
        expenseAdapter=new ExpenseAdapter(this,databaseHelper.getExpenseInfo());
        listView.setAdapter(expenseAdapter);

        fab_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showCustomDialogBox();
            }
        });
    }

    public void showCustomDialogBox(){
        View view = LayoutInflater.from(this).inflate(R.layout.add_expenses,null );

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
                DatePickerDialog datePickerDialog= new DatePickerDialog(Expenses.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
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

        ExpenseName = view.findViewById(R.id.expenseName);
        ExpensePrice = view.findViewById(R.id.expenseAmount);
        Operating = (CheckBox) view.findViewById(R.id.operatingExpense);
        Direct= (CheckBox) view.findViewById(R.id.directExpense);
        final int[] operatingExp = new int[1];
        final int[] directExp = new int[1];
        //value from checkbox
        Operating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    operatingExp[0] =1;
                }
                else{
                    operatingExp[0] =0;
                }
            }
        });
        Direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                   directExp[0] =1;
                }
                else {
                    directExp[0] =0;
                }
            }
        });

        contentValues = new ContentValues();
        databaseHelper = new DatabaseHelper(this);
        saveToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put("expense_name",ExpenseName.getText().toString());
                contentValues.put("operating",Integer.toString(operatingExp[0]));
                contentValues.put("direct",Integer.toString(directExp[0]));
                contentValues.put("amount",ExpensePrice.getText().toString());
                contentValues.put("date",asOfDate.getText().toString());
                databaseHelper.insertExpenses(contentValues);
                expenseAdapter=new ExpenseAdapter(Expenses.this,databaseHelper.getExpenseInfo());
                listView.setAdapter(expenseAdapter);

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
