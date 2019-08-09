package com.example.android.invoicemanagementsystem;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectUnit extends AppCompatActivity {
   Spinner spinner,spinner_sec;
   UnitsSpinnerAdapter adapter,adapter_sec;
   FloatingActionButton conversion_rate;
   String primaryUnit,secUnit;
   EditText conversionUnit;
   Button saveConversionUnit,saveUnits;
   TextView showConvSummary;
    InventoryFragment inventoryFragment;
    String conversionUnitValue;
    Bundle bundle;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int[] checkPrimary = {0};
        final int[] checkSec = {0};
        setContentView(R.layout.activity_select_unit);
        saveUnits=findViewById(R.id.saveUnitsButton);
        spinner= (Spinner)findViewById(R.id.primary_units);
        spinner_sec=(Spinner)findViewById(R.id.secondary_units);
        conversion_rate=findViewById(R.id.add_conversionRate);

        showConvSummary=findViewById(R.id.show_conversionSummary);

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Primary_Units)));
        adapter=new UnitsSpinnerAdapter(this,list);
        spinner.setAdapter(adapter);
        ArrayList<String> list_sec = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Secondary_Units)));
        adapter_sec=new UnitsSpinnerAdapter(this,list_sec);
        spinner_sec.setAdapter(adapter_sec);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                primaryUnit=spinner.getSelectedItem().toString();
                if(!primaryUnit.equals("Select Primary Unit")){
                    checkPrimary[0]=1;
                }
                else
                {
                    checkPrimary[0]=0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secUnit=spinner_sec.getSelectedItem().toString();
                if(!secUnit.equals("Select Secondary Unit")){
                    checkSec[0]=1;
                }
                else
                {
                    checkSec[0]=0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        conversion_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPrimary[0]==1 && checkSec[0]==1) {
                    showDialogBox();
                }
            }
        });

        saveUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            SelectUnit.this.finish();
            }
        });
    }
    public  void  showDialogBox(){
        View view = LayoutInflater.from(this).inflate(R.layout.conversion_rate,null );

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Inventory");

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        conversionUnit=(EditText)view.findViewById(R.id.conversionUnit);
        conversionUnitValue=conversionUnit.getText().toString();
        saveConversionUnit=view.findViewById(R.id.saveConversionUnit);
        TextView primary= view.findViewById(R.id.primaryU);
        TextView sec= view.findViewById(R.id.SecondaryU);
        primary.setText(primaryUnit);
        sec.setText(secUnit);
        preferences = getSharedPreferences("conversionUnit",0);
        SharedPreferences.Editor editor=preferences.edit();
        saveConversionUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("conversionUnit",0);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("primaryUnit",primaryUnit);
                editor.putString("secUnit",secUnit);
                editor.putString("conversionUnit",conversionUnit.getText().toString());
                editor.apply();
                showConvSummary.setText("1 "+primaryUnit+" = "+conversionUnit.getText().toString()+" "+secUnit);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
