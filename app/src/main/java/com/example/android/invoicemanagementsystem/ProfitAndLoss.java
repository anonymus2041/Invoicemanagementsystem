package com.example.android.invoicemanagementsystem;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfitAndLoss extends AppCompatActivity {
    String purchaseTotal,saleTotal;
    TextView purchase,sale,purchaseReturn,saleReturn,Directexpenses,income,openingStock,closingStock,
    taxes,interests,COGS,operatingExpenses;
    EditText fromDate,toDate;
    DatePickerDialog.OnDateSetListener setListener,setListener1;
    DatabaseHelper databaseHelper;
    int fromDay,toDay,fromMonth,toMonth,fromYear,toYear;
    ArrayList<Transactionstable> transactions;
    ArrayList<ExpenseInfo> expenseInfos;
    ArrayList<IncomeInfo> incomeInfos;
    ArrayList<InterestInfo> interestInfos;
    ArrayList<ReturnInfo> returnInfos;
    String date;
    Boolean isDateChanged,isDateFromChanged;
    Float sumSales,sumPurchase,sumExpenses,sumIncome,sumPurchaseReturn,sumSalesReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setListener=null;
        setListener1=null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_and_loss);
        purchaseTotal = getIntent().getStringExtra("purchase");
        saleTotal = getIntent().getStringExtra("sale");
        databaseHelper = new DatabaseHelper(this);

        purchase = findViewById(R.id.plPurchase);
        sale = findViewById(R.id.plSale);
        purchaseReturn = findViewById(R.id.plPurchaseReturn);
        saleReturn = findViewById(R.id.plSalesReturn);
        Directexpenses = findViewById(R.id.plDirectExpenses);
        income = findViewById(R.id.plIncome);
        openingStock = findViewById(R.id.plOpeningStock);
        closingStock = findViewById(R.id.plClosingStock);
        taxes = findViewById(R.id.plTaxes);
        interests = findViewById(R.id.plInterest);
        COGS = findViewById(R.id.plCOGS);
        operatingExpenses = findViewById(R.id.plOperatingExpenses);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);

        sumPurchase = 0.0f;
        sumSales = 0.0f;
        sumExpenses = 0.0f;

        isDateChanged = false;
        isDateFromChanged=false;


        //get dates of accounting period
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        fromDay=toDay=day;
        fromMonth=toMonth=month;
        fromYear=toYear=year;
           fromDate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   DatePickerDialog datePickerDialog = new DatePickerDialog(ProfitAndLoss.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener,
                           year, month, day);
                   datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                   datePickerDialog.show();
               }
           });
           setListener = new DatePickerDialog.OnDateSetListener() {
               @Override
               public void onDateSet(DatePicker view, int year, int month, int day) {
                   month = month + 1;
                   String date = day + "/" + month + "/" + year;

                   fromDate.setText(date);
                   SharedPreferences sharedpreferences = getSharedPreferences("fromDates", Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedpreferences.edit();
                   editor.putInt("fromDay",day);
                   editor.putInt("fromMonth",month);
                   editor.putInt("fromYear",year);
                   editor.commit();
                   // Toast.makeText(ProfitAndLoss.this,"from date"+fromDay+"-"+fromMonth+"-"+fromYear,Toast.LENGTH_LONG).show();
               }
           };
           toDate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   DatePickerDialog datePickerDialog = new DatePickerDialog(ProfitAndLoss.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener1,
                           year, month, day);
                   datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                   datePickerDialog.show();
               }
           });
           setListener1 = new DatePickerDialog.OnDateSetListener() {
               @Override
               public void onDateSet(DatePicker view, int year, int month, int day) {
                   month = month + 1;
                   String date = day + "/" + month + "/" + year;
                   toDate.setText(date);
                   SharedPreferences shared = getSharedPreferences("fromDates", MODE_PRIVATE);
                   int fromDay =  shared.getInt("fromDay",0);
                   int fromMonth =  shared.getInt("fromMonth",0);
                   int fromYear =  shared.getInt("fromYear",0);
                   isDateChanged(fromDay,fromMonth,fromYear,day,month,year);
               }

           };



            Toast.makeText(ProfitAndLoss.this,"from date"+fromDay+"-"+fromMonth+"-"+fromYear,Toast.LENGTH_LONG).show();

            //Toast.makeText(this,purchase,Toast.LENGTH_LONG).show();


        }
    public void isDateChanged(int dayF,int monthF,int yearF,int dayT,int monthT,int yearT){

        toDay = dayT;
        toMonth = monthT;
        toYear = yearT;
        fromDay = dayF;
        fromMonth = monthF;
        fromYear = yearF;

        if (fromYear == toYear) {
            if (fromMonth == toMonth) {
                if (fromDay == toDay) {

                } else {
                    transactions = new ArrayList<Transactionstable>();
                    expenseInfos = new ArrayList<ExpenseInfo>();
                    incomeInfos = new ArrayList<IncomeInfo>();
                    interestInfos = new ArrayList<InterestInfo>();
                    returnInfos = new ArrayList<ReturnInfo>();
                    for (int i = fromDay; i <= toDay; i++) {
                        date = i + "/" + fromMonth + "/" + fromYear;
                        Toast.makeText(ProfitAndLoss.this, "" + date, Toast.LENGTH_LONG).show();
                        transactions = databaseHelper.getTransactionsListByDate(date);
                        if (!transactions.isEmpty()) {
                            for (int j = 0; j < transactions.size(); j++) {
                                if (transactions.get(j).purchase_id != 0) {
                                    PurchaseInfo item = new PurchaseInfo();
                                    item = databaseHelper.getPurchaseInvoiceInfoForDisplay(String.valueOf(transactions.get(j).purchase_id));
                                    sumPurchase += item.invoice_total;


                                }

                                if (transactions.get(j).sale_id != 0) {
                                    invoiceInfo item = new invoiceInfo();
                                    item = databaseHelper.getInvoiceInfoForDisplay(String.valueOf(transactions.get(j).sale_id));
                                    sumSales += item.invoice_total;

                                }

                            }
                        }

                        //get the expenses
                        expenseInfos = databaseHelper.getExpenseInfoByDate(date);
                        if (!expenseInfos.isEmpty()) {
                            for (int j = 0; j < expenseInfos.size(); j++) {
                                sumExpenses += expenseInfos.get(j).amount;
                            }
                        }
                        //get the purchase returns
//                        returnInfos = databaseHelper.getReturnsInfoByDate(date);
//                        {
//                            if(!returnInfos.isEmpty())
//                            {
//                                for (int j = 0; j < returnInfos.size(); j++) {
//                                    if(returnInfos.get(j).sales==1 &&returnInfos.get(j).purchase==0)
//                                    {
//                                        sumSalesReturn+=returnInfos.get(j).amount;
//                                    }
//                                    if(returnInfos.get(j).sales==0 &&returnInfos.get(j).purchase==1)
//                                    {
//                                        sumPurchaseReturn+=returnInfos.get(j).amount;
//                                    }
//
//                                }
//                            }

//                        incomeInfos = databaseHelper.getIncomeInfoByDate(date);
//                        {
//                            if(!incomeInfos.isEmpty())
//                            {
//                                for (int j = 0; j < incomeInfos.size(); j++) {
//                                    sumExpenses += expenseInfos.get(j).amount;
//                                }
//                            }
//                        }


                    }
                    operatingExpenses.setText(String.valueOf(sumExpenses));
                    sale.setText(String.valueOf(sumSales));
                    purchase.setText(String.valueOf(sumPurchase));
                    sumExpenses=sumSales=sumPurchase=0.0f;
                }
            }
        }



    }


}

