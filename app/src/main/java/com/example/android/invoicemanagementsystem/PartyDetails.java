package com.example.android.invoicemanagementsystem;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PartyDetails extends AppCompatActivity {
    PartyDetailsAdapter adapter;
    DatabaseHelper databaseHelper;
    Transactionstable transaction;
    ListView listView;
    String customer_id,customer_name;
    TextView partyName,amountStatus,amount;
    ImageView partyStatus;
    float sumPurchase,sumSale,Total,openingSum;
    String openingBalance,to_receive,to_pay;
    LinearLayout OpeningBalanceLayout;
    TextView openingAmount;
    ImageView openingStatus;
    ArrayList<Transactionstable> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_details);
        partyName=findViewById(R.id.partyNameListView);
        amountStatus=findViewById(R.id.statusPartyListview);
        amount=findViewById(R.id.amountPartyListView);
        partyStatus=findViewById(R.id.cashStatusListView);
        openingAmount=findViewById(R.id.amountOpeningBalanceListView);
        openingStatus=findViewById(R.id.OpeningBalancecashStatusListView);
        OpeningBalanceLayout=(LinearLayout) findViewById(R.id.showOpeningBalanceListView);
        databaseHelper=new DatabaseHelper(this);

        Total=0.0f;

        //get intent messages
        customer_id=getIntent().getStringExtra("party_id");
        customer_name=getIntent().getStringExtra("party_name");
        openingBalance=getIntent().getStringExtra("opening_balance");
        to_receive=getIntent().getStringExtra("to_receive");
        to_pay=getIntent().getStringExtra("to_pay");
//        Toast.makeText(this,"opening= "+openingBalance,Toast.LENGTH_LONG).show();

        if(openingBalance!=null)
        {
            openingSum=Float.parseFloat(openingBalance);
            OpeningBalanceLayout.setVisibility(View.VISIBLE);
            openingAmount.setText("Rs."+openingBalance);
            openingStatus.setImageResource(R.drawable.down_arrow_round);
            if(Integer.parseInt(to_receive)==1)
            {
                Total=-openingSum;
                openingStatus.setImageResource(R.drawable.down_arrow_round);
            }
            else if(Integer.parseInt(to_pay)==1)
            {
                Total=openingSum;
                openingStatus.setImageResource(R.drawable.up_arrow_round);
            }

        }

        partyName.setText(customer_name);
//        Toast.makeText(this,"received customer_id:= "+customer_id,Toast.LENGTH_LONG).show();


        listView=findViewById(R.id.show_partyDetailsListView);
        adapter=new PartyDetailsAdapter(this,databaseHelper.getPartyTransactionsList(customer_id));
        listView.setAdapter(adapter);

        //to show the summary of receivable or payable amount
        list = new ArrayList<>();
        list=databaseHelper.getPartyTransactionsList(customer_id);
        if(!list.isEmpty())
        {
            sumPurchase=0.0f;
            sumSale=0.0f;
            for(int i=0;i<list.size();i++){

             transaction=new Transactionstable();
             transaction=list.get(i);
                if(transaction.purchase_id!=0){
                    PurchaseInfo item = new PurchaseInfo();
                    item=databaseHelper.getPurchaseInvoiceInfoForDisplay(String.valueOf(transaction.purchase_id));
                    sumPurchase+=item.balance_due;

                }

                if(transaction.sale_id!=0){
                    invoiceInfo item = new invoiceInfo();
                    item=databaseHelper.getInvoiceInfoForDisplay(String.valueOf(transaction.sale_id));
                    sumSale+=item.balance_due;
                }

            }
            Total+=sumPurchase-sumSale;

        }
        if(Total<0.0f){
            amountStatus.setText("Receivable");
            amount.setText("Rs."+(Total*-1.0));
            partyStatus.setImageResource(R.drawable.down_arrow_round);
        }
        else {
            amountStatus.setText("Payable");
            amount.setText("Rs."+Total);
            amount.setTextColor(Color.parseColor("#e01010"));
            partyStatus.setImageResource(R.drawable.up_arrow_round);
        }
  }
}
