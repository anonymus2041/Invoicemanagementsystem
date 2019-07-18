package com.example.android.invoicemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InvoiceFragment extends Fragment {
    InvoiceListAdapter adapter;
    ListView listView;
    DatabaseHelper databaseHelper;
    TextView purchase,sale;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_invoice,container,false);
        listView = view.findViewById(R.id.my_list);
        databaseHelper = new DatabaseHelper(getActivity());
        adapter = new InvoiceListAdapter(getActivity(),databaseHelper.getInvoiceList());
        listView.setAdapter(adapter);

        purchase=(TextView) view.findViewById(R.id.purchase);
        sale=(TextView)view.findViewById(R.id.sale);

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invoiceIntent = new Intent(getContext(), PurchaseInvoice.class);
                startActivity(invoiceIntent);
            }
        });
        registerForContextMenu(listView);
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent invoiceIntent = new Intent(getContext(), InvoiceTest.class);
                startActivity(invoiceIntent);
            }
        });
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.payment_status_menu, menu);
        menu.setHeaderTitle("Payment status");
    }
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId()==R.id.paid){
            Toast.makeText(getContext(),"PAID",Toast.LENGTH_LONG).show();
        } else{
            return false;
        }
        return true;
    }
}
