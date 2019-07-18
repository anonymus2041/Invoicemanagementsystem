package com.example.android.invoicemanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    FloatingActionButton fab_parties;
    EditText asOfDate;
    DatePickerDialog.OnDateSetListener setListener;
    Button saveParty;
    ContentValues contentValues;
    DatabaseHelper databaseHelper;
    EditText name,phone,address,openingBalance;
    TextView toPay,toReceive;
    ListView listView;
    PartyListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.activity_home,container,false);
        databaseHelper= new DatabaseHelper(getActivity());
        fab_parties=(FloatingActionButton) view.findViewById(R.id.fab_parties);
        fab_parties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });
        listView=view.findViewById(R.id.listOfParties);
        adapter=new PartyListAdapter(getActivity(),databaseHelper.getPartyInfos());
        listView.setAdapter(adapter);
        return view;
    }
   public void showDialogBox(){
       View view=LayoutInflater.from(getActivity()).inflate(R.layout.add_parties,null);
       databaseHelper=new DatabaseHelper(getActivity());
       final int[] pay = {0};
       final int[] receive = {0};
       final Dialog dialog = new Dialog(getActivity());
       dialog.setTitle("Parties");

       dialog.setContentView(view);
       Window window = dialog.getWindow();
       window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        asOfDate= (EditText) view.findViewById(R.id.enter_asOfDate);
       name= (EditText) view.findViewById(R.id.enter_party_name);
       phone= (EditText) view.findViewById(R.id.enter_partyPhone);
       address= (EditText) view.findViewById(R.id.enter_billingAddress);
       openingBalance= (EditText) view.findViewById(R.id.enter_openingBalance);
       toPay= (TextView) view.findViewById(R.id.toPay);
       toReceive= (TextView) view.findViewById(R.id.toReceive);

       toPay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               pay[0] =1;
           }
       });

       toReceive.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               receive[0] =1;
           }
       });

         saveParty=(Button) view.findViewById(R.id.saveParty);

       Calendar calendar= Calendar.getInstance();
       final int year =calendar.get(Calendar.YEAR);
       final int month =calendar.get(Calendar.MONTH);
       final int day =calendar.get(Calendar.DAY_OF_MONTH);
       asOfDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
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
       saveParty.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               contentValues=new ContentValues();
               contentValues.put("name",name.getText().toString());
               contentValues.put("billing_address",address.getText().toString());
               contentValues.put("phone",phone.getText().toString());
               contentValues.put("opening_balance",openingBalance.getText().toString());
               contentValues.put("asOfDate",asOfDate.getText().toString());
               contentValues.put("toPay",Integer.toString(pay[0]));
               contentValues.put("toReceive",Integer.toString(receive[0]));
               databaseHelper.insertParty(contentValues);
               dialog.dismiss();
           }
       });

       dialog.show();
   }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
