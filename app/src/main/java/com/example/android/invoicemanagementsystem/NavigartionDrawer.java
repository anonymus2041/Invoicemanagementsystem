package com.example.android.invoicemanagementsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class NavigartionDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatePickerDialog.OnDateSetListener setListener;
    DatabaseHelper databaseHelper;
    TextView monthReceive,monthPay,monthPurchase,monthSale,monthExpenses,receivable,payable,saleTotal,
            purchaseTotal,expensesTotal;
    String[] months;
    String currentMonth;
    Float toPay,toReceive,purchase,sale,expenses;
    ArrayList<Transactionstable> transactionstables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InventoryItems checkOutOfStock;
        ArrayList<InventoryItems> listOutOfStock;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigartion_drawer);
//        checkOutOfStock= new InventoryItems();


//        databaseHelper=new DatabaseHelper(this);
//        listOutOfStock=databaseHelper.getProductOutOfStockList();
//        if(!listOutOfStock.isEmpty())
//        {
//            Calendar calendar=Calendar.getInstance();
//            calendar.add(Calendar.SECOND,5);
//            Intent intent = new Intent(this,Notification_receiver.class);
//            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager= (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
//        }
        monthExpenses= findViewById(R.id.monthExpenses);
        monthSale= findViewById(R.id.monthSale);
        monthPay= findViewById(R.id.monthPay);
        monthPurchase= findViewById(R.id.monthPurchase);
        monthReceive= findViewById(R.id.monthReceive);
        payable= findViewById(R.id.payable);
        receivable= findViewById(R.id.receivable);
        purchaseTotal= findViewById(R.id.purchase);
        saleTotal= findViewById(R.id.sale);
        expensesTotal= findViewById(R.id.expenses);

        months = new DateFormatSymbols().getMonths();
        //set current month
        Calendar calendar= Calendar.getInstance();
        currentMonth= months[calendar.get(Calendar.MONTH)];
        monthExpenses.setText(currentMonth);
        monthSale.setText(currentMonth);
        monthPurchase.setText(currentMonth);
        monthPay.setText(currentMonth);
        monthReceive.setText(currentMonth);

        showOverview(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationView navView = findViewById(R.id.navigation);
        //mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            Calendar calendar= Calendar.getInstance();
            currentMonth= months[calendar.get(Calendar.MONTH)];
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    monthExpenses.setText(currentMonth);
                    monthSale.setText(currentMonth);
                    monthPurchase.setText(currentMonth);
                    monthPay.setText(currentMonth);
                    monthReceive.setText(currentMonth);
                    showOverview(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_inventory:
                    monthExpenses.setText(currentMonth);
                    monthSale.setText(currentMonth);
                    monthPurchase.setText(currentMonth);
                    monthPay.setText(currentMonth);
                    monthReceive.setText(currentMonth);
                    showOverview(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
                    selectedFragment = new InventoryFragment();
                    break;
                case R.id.navigation_invoice:
                    monthExpenses.setText(currentMonth);
                    monthSale.setText(currentMonth);
                    monthPurchase.setText(currentMonth);
                    monthPay.setText(currentMonth);
                    monthReceive.setText(currentMonth);
                    showOverview(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
                    selectedFragment = new InvoiceFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigartion_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calender) {
            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, setListener,
                            year, month, day);
                    //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
            setListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month + 1;
                    String date = day + "/" + month + "/" + year;
                    showOverview(year,month,day);
                 // Toast.makeText(getApplicationContext(),"last day is "+res, Toast.LENGTH_LONG).show();

                    monthExpenses.setText(months[month-1]);
                    monthSale.setText(months[month-1]);
                    monthPurchase.setText(months[month-1]);
                    monthPay.setText(months[month-1]);
                    monthReceive.setText(months[month-1]);
                     //Toast.makeText(getApplicationContext(),date, Toast.LENGTH_LONG).show();
                     Bundle bundle=new Bundle();
                     bundle.putString("date",date);
                     ShowTransacByDate details = new ShowTransacByDate();
                     FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                     details.setArguments(bundle);
                     ft.replace(R.id.fragment_container, details);
                     ft.commit();


                }
            };

        } else if (id == R.id.nav_reports) {
            Intent intent= new Intent(NavigartionDrawer.this,ProfitAndLoss.class);
            intent.putExtra("sale", Float.toString(sale));
            intent.putExtra("purchase",Float.toString(purchase));
            //intent.putExtra("expenses",purchaseTotal.getText().toString());
            startActivity(intent);

        } else if (id == R.id.nav_Indirect_income) {
            Intent intent= new Intent(NavigartionDrawer.this, Income.class);
            startActivity(intent);

        } else if (id == R.id.nav_expenses) {
            Intent intent= new Intent(NavigartionDrawer.this,Expenses.class);
            startActivity(intent);

        } else if (id == R.id.nav_returns) {
            Intent intent= new Intent(NavigartionDrawer.this,Returns.class);
            startActivity(intent);

        } else if (id == R.id.nav_interest) {
            Intent intent= new Intent(NavigartionDrawer.this,InterestAndTaxes.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showOverview(int year,int month,int day){
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,month-1,day);
        toReceive=0.0f;
        toPay=0.0f;
        expenses=purchase=sale=0.0f;
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DATE);

        day=1;
        databaseHelper= new DatabaseHelper(getApplicationContext());
        while(day<lastDayOfMonth) {
            String date = day + "/" + month + "/" + year;
            transactionstables = new ArrayList<Transactionstable>();
            try {
                transactionstables = databaseHelper.getTransactionsListByDate(date);
                if(!transactionstables.isEmpty())
                {
                    for(int i=0;i<transactionstables.size();i++){
                        Transactionstable transactions;
                        transactions=transactionstables.get(i);
                        if(transactions.purchase_id!=0)
                        {
                            PurchaseInfo item = new PurchaseInfo();
                            item=databaseHelper.getPurchaseInvoiceInfoForDisplay(String.valueOf(transactions.purchase_id));
                            toPay+=item.balance_due;
                            purchase+=item.invoice_total;
                        }
                        if(transactions.sale_id!=0)
                        {
                            invoiceInfo item = new invoiceInfo();
                            item=databaseHelper.getInvoiceInfoForDisplay(String.valueOf(transactions.sale_id));
                            toReceive+=item.balance_due;
                            sale+=item.invoice_total;
                        }


                    }
                }
            }
            catch (Exception e){
                toPay+=0;
                toReceive+=0;
                purchase+=0;
                sale+=0;
            }
            day++;
        }
        payable.setText("Rs."+toPay);
        receivable.setText("Rs."+toReceive);
        purchaseTotal.setText("Rs."+purchase);
        saleTotal.setText("Rs."+sale);

    }
}
