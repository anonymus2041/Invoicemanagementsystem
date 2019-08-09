package com.example.android.invoicemanagementsystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ShowTransacByDate extends Fragment {
    DatabaseHelper databaseHelper;
    TransactionsAdapter transactionsAdapter;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home,container,false);
        Bundle bundle=getArguments();
        String date = bundle.getString("date");
        listView= view.findViewById(R.id.listOfParties);
        databaseHelper= new DatabaseHelper(getActivity());
       // Toast.makeText(getActivity(),"No transactions in \n"+date, Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(),"No transactions in \n"+date, Toast.LENGTH_LONG).show();
        databaseHelper=new DatabaseHelper(getActivity());
       try {
           transactionsAdapter = new TransactionsAdapter(getActivity(),databaseHelper.getTransactionsListByDate(date));
            listView.setAdapter(transactionsAdapter);
       }
        catch (Exception e){
            Toast.makeText(getActivity(),"No transactions in \n"+date, Toast.LENGTH_LONG).show();

        }

       // Toast.makeText(getActivity(),"in fragment"+date,Toast.LENGTH_LONG).show();
        return view;
    }

}
