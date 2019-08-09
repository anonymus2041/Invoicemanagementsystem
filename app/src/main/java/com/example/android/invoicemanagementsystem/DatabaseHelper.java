package com.example.android.invoicemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String name ="InvoiceManagement_SystemDb";
    static  int version=1;

    String sqlCreateTable ="CREATE TABLE if not exists \"inventory\" \n" +
            "( \"product_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            " \"product_name\" TEXT, \"primary_unit\" TEXT, \n" +
            " \"secondary_unit\" TEXT,\"conversion_rate\" REAL,\"purchase_price\" REAL ,\n"+
            "\"selling_price\" REAL, \"opening_stock\" REAL, \"AsOfDate\" TEXT," +
            "\"unit_price\" REAL, \"minStockQty\" REAL,\"current_stock_qty\" REAL,\"stock_value\" REAL)";

    String sqlCreateInvoiceTable = "CREATE TABLE if not exists \"invoice\" \n" +
            "( \"invoice_no\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            " \"invoice_date\" TEXT,\"due_date\" TEXT,\"invoice_total\" REAL," +
            "\"customer_id\" INTEGER NOT NULL REFERENCES parties(party_id)," +
            "\"discount_percent\" REAL default 0," +
            "\"received_amount\" REAL default 0,\n" +
            " \"balance_due\" REAL default 0)";


    String sqlCreatePurchaseInvoiceTable = "CREATE TABLE if not exists \"purchase_invoice\" \n" +
            "( \"purchase_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            " \"invoice_date\" TEXT,\"invoice_total\" REAL," +
            "\"customer_id\" INTEGER NOT NULL REFERENCES parties(party_id)," +
            "\"discount_percent\" REAL default 0," +
            "\"paid_amount\" REAL default 0,\n" +
            " \"balance_due\" REAL default 0)";

    String sqlCreateInvoiceItemsTable ="CREATE TABLE if not exists \"invoice_items\" \n" +
            "( \"invoice_no\" INTEGER NOT NULL REFERENCES invoice (invoice_no),\n" +
            " \"product_id\" INTEGER NOT NULL REFERENCES inventory(product_id) , \"quantity\" INT, \n" +
            " \"discount\" REAL,\"amount\" REAL)";
    String sqlCreateTransactionsTable ="CREATE TABLE if not exists \"transactions\" \n" +
            "( \"transaction_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            " \"purchase_id\" INTEGER  default 0 NOT NULL REFERENCES purchase_invoice(purchase_id) , " +
            "\"sale_id\" INTEGER default 0 NOT NULL REFERENCES invoice(invoice_no)," +
            "\"customer_id\"  INTEGER default 0 NOT NULL,\"date\"  TEXT default 0 NOT NULL)";

    String sqlCreateExpensesTable ="CREATE TABLE if not exists \"expenses\" \n" +
            "( \"expense_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "\"expense_name\"  TEXT  NOT NULL,\n" +
            " \"operating\" INTEGER  default 0 NOT NULL , " +
            "\"direct\" INTEGER default 0 NOT NULL ," +
            "\"amount\"  REAL default 0 NOT NULL,\"date\"  TEXT default 0 NOT NULL," +
            "\"party_id\"  INTEGER default 0 )";
    String sqlCreateInterestTable ="CREATE TABLE if not exists \"interest\" \n" +
            "( \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            " \"interest\" INTEGER  default 0 NOT NULL , " +
            "\"tax\" INTEGER default 0 NOT NULL ," +
            "\"amount\"  REAL default 0 NOT NULL,\"date\"  TEXT default 0 NOT NULL," +
            "\"party_id\"  INTEGER default 0 )";

    String sqlCreateReturnsTable ="CREATE TABLE if not exists \"returns\" \n" +
            "( \"return_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "\"return_name\"  TEXT  NOT NULL,\n" +
            " \"purchase\" INTEGER  default 0 NOT NULL , " +
            "\"sales\" INTEGER default 0 NOT NULL ,\"return_qty\" INTEGER default 0 NOT NULL," +
            "\"amount\"  REAL default 0 NOT NULL,\"date\"  TEXT default 0 NOT NULL," +
            "\"party_id\"  INTEGER default 0 )";


    String sqlCreateIncomeTable ="CREATE TABLE if not exists \"income\" \n" +
            "( \"income_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "\"income_name\"  TEXT  NOT NULL,\n" +
            " \"party_id\" INTEGER default 0, " +
            "\"amount\"  REAL default 0 NOT NULL,\"date\"  TEXT default 0 NOT NULL)";


    String sqlPurchaseInvoiceItemsTable ="CREATE TABLE if not exists \"purchase_invoice_items\" \n" +
            "( \"invoice_no\" INTEGER NOT NULL REFERENCES purchase_invoice (purchase_id),\n" +
            " \"product_name\" TEXT NOT NULL REFERENCES inventory(product_name))";

    String sqlCreatePartyTable ="CREATE TABLE if not exists \"parties\" \n" +
            "( \"party_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            " \"name\" TEXT , \"billing_address\" TEXT,\"phone\" TEXT, \"opening_balance\" REAL, \n" +
            " \"asOfDate\" TEXT,\"toPay\" INT DEFAULT 0,\"toReceive\" INT DEFAULT 0)";


    public DatabaseHelper(Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(sqlCreateTable);
        getWritableDatabase().execSQL(sqlCreateInvoiceTable);
        getWritableDatabase().execSQL(sqlCreatePurchaseInvoiceTable);
        getWritableDatabase().execSQL(sqlPurchaseInvoiceItemsTable);
        getWritableDatabase().execSQL(sqlCreateInvoiceItemsTable);
        getWritableDatabase().execSQL(sqlCreateTransactionsTable);
        getWritableDatabase().execSQL(sqlCreatePartyTable);
        getWritableDatabase().execSQL(sqlCreateExpensesTable);
        getWritableDatabase().execSQL(sqlCreateIncomeTable);
        getWritableDatabase().execSQL(sqlCreateReturnsTable);
        getWritableDatabase().execSQL(sqlCreateInterestTable);
    }
    public void insertItem(ContentValues contentValues){
        getWritableDatabase().insert("inventory","",contentValues);
    }
    public void insertInterest(ContentValues contentValues){
        getWritableDatabase().insert("interest","",contentValues);
    }
    public void insertReturns(ContentValues contentValues){
        getWritableDatabase().insert("returns","",contentValues);
    }

    public void insertExpenses(ContentValues contentValues){
        getWritableDatabase().insert("expenses","",contentValues);
    }
    public void insertIncome(ContentValues contentValues){
        getWritableDatabase().insert("income","",contentValues);
    }
    public void insertParty(ContentValues contentValues){
        getWritableDatabase().insert("parties","",contentValues);}

    public void insertTransaction(ContentValues contentValues){
        getWritableDatabase().insert("transactions","",contentValues);}

    public boolean insertInvoiceData(ContentValues contentValues){
        getWritableDatabase().insert("invoice","",contentValues);
        return true;
    }
    public void insertPurchaseInvoiceData(ContentValues contentValues){
        getWritableDatabase().insert("purchase_invoice","",contentValues);
    }
    public void insertPurchaseInvoiceItemsData(ContentValues contentValues){
        getWritableDatabase().insert("purchase_invoice_items","",contentValues);
    }
    public boolean insertInvoiceItemsData(ContentValues contentValues){
        getWritableDatabase().insert("invoice_items","",contentValues);
        return true;
    }
    public void updateInventoryStock(String product_id,ContentValues contentValues){
        getWritableDatabase().update("inventory",contentValues,"product_id=?",new String[]{product_id});
    }
    public boolean updateInvoiceDetails(String invoice_no,ContentValues contentValues){
        getWritableDatabase().update("invoice",contentValues,"invoice_no=?",new String[]{invoice_no});
        return true;
    }
    public InventoryItems getItemIdForInvoice(String itemName)
    {
//        String[] itemNameArr =new
        String sql = "select * from inventory where product_name like ?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{itemName});

        InventoryItems item = new InventoryItems();
        while(c.moveToNext())
        {
            item.product_id = c.getInt(c.getColumnIndex("product_id"));
            item.product_name = c.getString(c.getColumnIndex("product_name"));
            item.unit_price = c.getFloat(c.getColumnIndex("unit_price"));
            item.opening_stock = c.getFloat(c.getColumnIndex("opening_stock"));
            item.AsOfDate = c.getString(c.getColumnIndex("AsOfDate"));
            item.purchase_price = c.getFloat(c.getColumnIndex("purchase_price"));
            item.sale_price = c.getFloat(c.getColumnIndex("selling_price"));
            item.minStockQty = c.getFloat(c.getColumnIndex("minStockQty"));
            item.primary_unit = c.getString(c.getColumnIndex("primary_unit"));
            item.secondary_unit = c.getString(c.getColumnIndex("secondary_unit"));
            item.conversion_rate = c.getFloat(c.getColumnIndex("conversion_rate"));
            item.current_stock_qty = c.getFloat(c.getColumnIndex("current_stock_qty"));
            item.stock_value = c.getFloat(c.getColumnIndex("stock_value"));

        }
        c.close();
       return  item;
    }

    public InventoryItems getItemInfoForPurchaseInvoice(String itemName)
    {
//        String[] itemNameArr =new
        String sql = "select * from inventory where product_name like ?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{itemName});
        InventoryItems item = new InventoryItems();
        while(c.moveToNext())
        {

            item.product_id = c.getInt(c.getColumnIndex("product_id"));
            item.product_name = c.getString(c.getColumnIndex("product_name"));
            item.unit_price = c.getFloat(c.getColumnIndex("unit_price"));
            item.opening_stock = c.getFloat(c.getColumnIndex("opening_stock"));
            item.AsOfDate = c.getString(c.getColumnIndex("AsOfDate"));
            item.purchase_price = c.getFloat(c.getColumnIndex("purchase_price"));
            item.sale_price = c.getFloat(c.getColumnIndex("selling_price"));
            item.minStockQty = c.getFloat(c.getColumnIndex("minStockQty"));
            item.primary_unit = c.getString(c.getColumnIndex("primary_unit"));
            item.secondary_unit = c.getString(c.getColumnIndex("secondary_unit"));
            item.conversion_rate = c.getFloat(c.getColumnIndex("conversion_rate"));
            item.current_stock_qty = c.getFloat(c.getColumnIndex("current_stock_qty"));
            item.stock_value = c.getFloat(c.getColumnIndex("stock_value"));
        }
        c.close();
        return  item;
    }


    public  PartyInfo getCustomerName(String customer_id){
        String sql = "select * from parties where party_id=?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{customer_id});
        PartyInfo customer = new PartyInfo();
        while(c.moveToNext()) {
            customer.party_id=c.getInt(c.getColumnIndex("party_id"));
            customer.name=c.getString(c.getColumnIndex("name"));
            customer.billing_address=c.getString(c.getColumnIndex("billing_address"));
            customer.phone=c.getString(c.getColumnIndex("phone"));
            customer.opening_balance=c.getFloat(c.getColumnIndex("opening_balance"));
            customer.asOfdate=c.getString(c.getColumnIndex("asOfDate"));
            customer.asOfdate=c.getString(c.getColumnIndex("asOfDate"));
            customer.toReceive=c.getInt(c.getColumnIndex("toReceive"));

        }
        c.close();
        return customer;
    }


    public PartyInfo getCustomerIdForInvoice(String CustomerName)
    {
//        String[] itemNameArr =new
        String sql = "select * from parties where name like ?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{CustomerName});

        PartyInfo party = new PartyInfo();
        while(c.moveToNext())
        {
            party.party_id= c.getInt(c.getColumnIndex("party_id"));
            party.name= c.getString(c.getColumnIndex("name"));
            party.phone = c.getString(c.getColumnIndex("phone"));
            party.billing_address = c.getString(c.getColumnIndex("billing_address"));
            party.asOfdate = c.getString(c.getColumnIndex("asOfDate"));
            party.opening_balance = c.getFloat(c.getColumnIndex("opening_balance"));
            party.toPay = c.getInt(c.getColumnIndex("toPay"));
            party.toReceive = c.getInt(c.getColumnIndex("toReceive"));

        }
        c.close();
        return  party;
    }


    public invoiceInfo getInvoiceInfoForDisplay(String invoice_id)
    {
//        String[] itemNameArr =new
        String sql = "select * from invoice where invoice_no=?";
        Cursor c=getReadableDatabase().rawQuery(sql,new String[]{invoice_id});
        invoiceInfo item = new invoiceInfo();
        while(c.moveToNext())
        {
            item.invoice_no = c.getInt(c.getColumnIndex("invoice_no"));
            item.invoice_date = c.getString(c.getColumnIndex("invoice_date"));
            item.due_date = c.getString(c.getColumnIndex("due_date"));
            item.invoice_total = c.getFloat(c.getColumnIndex("invoice_total"));
            item.discount_percent = c.getFloat(c.getColumnIndex("discount_percent"));
            item.received_amount = c.getFloat(c.getColumnIndex("received_amount"));
            item.balance_due = c.getFloat(c.getColumnIndex("balance_due"));
            item.customer_id = c.getInt(c.getColumnIndex("customer_id"));

        }
        c.close();
        return  item;
    }

    public PurchaseInfo getPurchaseInvoiceInfoForDisplay(String invoice_id)
    {
        String sql = "select * from purchase_invoice where purchase_id=?";
        Cursor c=getReadableDatabase().rawQuery(sql,new String[]{invoice_id});
        PurchaseInfo item = new PurchaseInfo();
        while(c.moveToNext())
        {
            item.purchase_id = c.getInt(c.getColumnIndex("purchase_id"));
            item.invoice_date = c.getString(c.getColumnIndex("invoice_date"));
            item.invoice_total = c.getFloat(c.getColumnIndex("invoice_total"));
            item.discount_percent = c.getFloat(c.getColumnIndex("discount_percent"));
            item.paid_amount = c.getFloat(c.getColumnIndex("paid_amount"));
            item.balance_due = c.getFloat(c.getColumnIndex("balance_due"));
            item.customer_id = c.getInt(c.getColumnIndex("customer_id"));

        }
        c.close();
        return  item;
    }

    public InventoryItems getItemNameForInvoice(String id)
    {
//        String[] itemNameArr =new
        String sql = "select * from inventory where product_id=?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{id});

        InventoryItems item = new InventoryItems();
        while(c.moveToNext())
        {
            item.product_id = c.getInt(c.getColumnIndex("product_id"));
            item.product_name = c.getString(c.getColumnIndex("product_name"));
            item.unit_price = c.getFloat(c.getColumnIndex("unit_price"));
            item.opening_stock = c.getFloat(c.getColumnIndex("opening_stock"));
            item.AsOfDate = c.getString(c.getColumnIndex("AsOfDate"));
            item.purchase_price = c.getFloat(c.getColumnIndex("purchase_price"));
            item.sale_price = c.getFloat(c.getColumnIndex("selling_price"));
            item.minStockQty = c.getFloat(c.getColumnIndex("minStockQty"));
            item.primary_unit = c.getString(c.getColumnIndex("primary_unit"));
            item.secondary_unit = c.getString(c.getColumnIndex("secondary_unit"));
            item.conversion_rate = c.getFloat(c.getColumnIndex("conversion_rate"));
            item.current_stock_qty = c.getFloat(c.getColumnIndex("current_stock_qty"));
            item.stock_value = c.getFloat(c.getColumnIndex("stock_value"));

        }
        c.close();
        return  item;
    }


    public ArrayList<InventoryItems> getItemslist(){
        String sql = "select * from inventory order by product_name";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<InventoryItems>list = new ArrayList<>();
        while(c.moveToNext()) {
            InventoryItems item = new InventoryItems();
            item.product_id = c.getInt(c.getColumnIndex("product_id"));
            item.product_name = c.getString(c.getColumnIndex("product_name"));
            item.unit_price = c.getFloat(c.getColumnIndex("unit_price"));
            item.opening_stock = c.getFloat(c.getColumnIndex("opening_stock"));
            item.AsOfDate = c.getString(c.getColumnIndex("AsOfDate"));
            item.purchase_price = c.getFloat(c.getColumnIndex("purchase_price"));
            item.sale_price = c.getFloat(c.getColumnIndex("selling_price"));
            item.minStockQty = c.getFloat(c.getColumnIndex("minStockQty"));
            item.primary_unit = c.getString(c.getColumnIndex("primary_unit"));
            item.secondary_unit = c.getString(c.getColumnIndex("secondary_unit"));
            item.conversion_rate = c.getFloat(c.getColumnIndex("conversion_rate"));
            item.current_stock_qty = c.getFloat(c.getColumnIndex("current_stock_qty"));
            item.stock_value = c.getFloat(c.getColumnIndex("stock_value"));
            list.add(item);
        }
        c.close();
        return list;
    }

    public ArrayList<PartyInfo> getPartyInfos(){
        String sql = "select * from parties ";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<PartyInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            PartyInfo party = new PartyInfo();
            party.party_id= c.getInt(c.getColumnIndex("party_id"));
            party.name= c.getString(c.getColumnIndex("name"));
            party.phone = c.getString(c.getColumnIndex("phone"));
            party.billing_address = c.getString(c.getColumnIndex("billing_address"));
            party.asOfdate = c.getString(c.getColumnIndex("asOfDate"));
            party.opening_balance = c.getFloat(c.getColumnIndex("opening_balance"));
            party.toPay = c.getInt(c.getColumnIndex("toPay"));
            party.toReceive = c.getInt(c.getColumnIndex("toReceive"));
            list.add(party);
        }
        c.close();
        return list;
    }

    public ArrayList<InventoryItems> getProductOutOfStockList(){
        String sql = "select * from inventory where current_stock_qty <=minStockQty";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<InventoryItems>list = new ArrayList<>();
        while(c.moveToNext()) {
            InventoryItems item = new InventoryItems();
            item.product_id = c.getInt(c.getColumnIndex("product_id"));
            item.product_name = c.getString(c.getColumnIndex("product_name"));
            item.unit_price = c.getFloat(c.getColumnIndex("unit_price"));
            item.opening_stock = c.getFloat(c.getColumnIndex("opening_stock"));
            item.AsOfDate = c.getString(c.getColumnIndex("AsOfDate"));
            item.purchase_price = c.getFloat(c.getColumnIndex("purchase_price"));
            item.sale_price = c.getFloat(c.getColumnIndex("selling_price"));
            item.minStockQty = c.getFloat(c.getColumnIndex("minStockQty"));
            item.primary_unit = c.getString(c.getColumnIndex("primary_unit"));
            item.secondary_unit = c.getString(c.getColumnIndex("secondary_unit"));
            item.conversion_rate = c.getFloat(c.getColumnIndex("conversion_rate"));
            item.current_stock_qty = c.getFloat(c.getColumnIndex("current_stock_qty"));
            item.stock_value = c.getFloat(c.getColumnIndex("stock_value"));
            list.add(item);
        }
        c.close();
        return list;
    }

    public ArrayList<InvoiceTable> getInvoiceList(){
        String sql = "select * from invoice order by invoice_no";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<InvoiceTable>list = new ArrayList<>();

        while(c.moveToNext()) {
            InvoiceTable item = new InvoiceTable();
            item.invoice_no= c.getInt(c.getColumnIndex("invoice_no"));
            item.invoice_date = c.getString(c.getColumnIndex("invoice_date"));
            item.due_date = c.getString(c.getColumnIndex("due_date"));
            item.invoice_total=  c.getFloat(c.getColumnIndex("invoice_total"));
            item.discount_percent = c.getFloat(c.getColumnIndex("discount_percent"));
            item.received_amount = c.getFloat(c.getColumnIndex("received_amount"));
            item.balance_due = c.getFloat(c.getColumnIndex("balance_due"));
            item.customer_id = c.getInt(c.getColumnIndex("customer_id"));

            //item.qty_in_stock = c.getInt(c.getColumnIndex("qty_in_stock"));
            list.add(item);
        }
        c.close();
        return list;
    }
    public ArrayList<invoiceitemsTable> getInvoiceItemsList(String invoice_id){
        String sql = "select * from invoice_items where invoice_no=?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{invoice_id});
        ArrayList<invoiceitemsTable>list = new ArrayList<>();
        while(c.moveToNext()) {
            invoiceitemsTable item = new invoiceitemsTable();
            item.invoice_no= c.getInt(c.getColumnIndex("invoice_no"));
            item.product_id = c.getInt(c.getColumnIndex("product_id"));
            item.quantity = c.getInt(c.getColumnIndex("quantity"));
            item.discount =c.getFloat(c.getColumnIndex("discount"));
            item.amount=c.getFloat(c.getColumnIndex("amount"));
            list.add(item);
        }
        c.close();
        return list;
    }

    public ArrayList<PurchaseInvoiceTable> getPurchaseInvoiceItemsList(String invoice_id){
        String sql = "select * from purchase_invoice_items where invoice_no=?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{invoice_id});
        ArrayList<PurchaseInvoiceTable>list = new ArrayList<>();
        while(c.moveToNext()) {
            PurchaseInvoiceTable item = new PurchaseInvoiceTable();
            item.invoice_no= c.getInt(c.getColumnIndex("invoice_no"));
            item.product_name = c.getString(c.getColumnIndex("product_name"));
            list.add(item);
        }
        c.close();
        return list;
    }
//    public invoiceitemsTable get(String product_id){
//        String sql = "select * from invoice_items where product_id=?";
//        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{product_id});
//        invoiceitemsTable item = new invoiceitemsTable();
//        while(c.moveToNext()) {
//
//            item.invoice_no= c.getInt(c.getColumnIndex("invoice_no"));
//            item.product_id = c.getInt(c.getColumnIndex("product_id"));
//            item.quantity = c.getInt(c.getColumnIndex("quantity"));
//            item.discount =c.getFloat(c.getColumnIndex("discount"));
//            item.amount=c.getFloat(c.getColumnIndex("amount"));
//        }
//        c.close();
//        return item;
//    }
    public  ArrayList<String> getItemsNamelist(){
        String sql = "select * from inventory order by product_name";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<String>list = new ArrayList<>();
        while(c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex("product_name")));
        }
        c.close();
        return list;
    }

    public  ArrayList<Transactionstable> getTransactionsList(){
        String sql = "select * from transactions order by transaction_id desc";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<Transactionstable>list = new ArrayList<>();
        while(c.moveToNext()) {
            Transactionstable transactions= new Transactionstable();
            transactions.transaction_id =c.getInt(c.getColumnIndex("transaction_id"));
            transactions.purchase_id =c.getInt(c.getColumnIndex("purchase_id"));
            transactions.sale_id =c.getInt(c.getColumnIndex("sale_id"));
            transactions.customer_id =c.getInt(c.getColumnIndex("customer_id"));
            list.add(transactions);
        }
        c.close();
        return list;
    }

    public  ArrayList<Transactionstable> getTransactionsListByDate(String date){
        String sql = "select * from transactions where date like ?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{date});
        ArrayList<Transactionstable>list = new ArrayList<>();
        while(c.moveToNext()) {
            Transactionstable transactions= new Transactionstable();
            transactions.transaction_id =c.getInt(c.getColumnIndex("transaction_id"));
            transactions.purchase_id =c.getInt(c.getColumnIndex("purchase_id"));
            transactions.sale_id =c.getInt(c.getColumnIndex("sale_id"));
            transactions.customer_id =c.getInt(c.getColumnIndex("customer_id"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;
    }

    public  ArrayList<Transactionstable> getPartyTransactionsList(String customer_id){
        String sql = "select * from transactions where customer_id=?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{customer_id});
        ArrayList<Transactionstable>list = new ArrayList<>();
        while(c.moveToNext()) {
            Transactionstable transactions= new Transactionstable();
            transactions.transaction_id =c.getInt(c.getColumnIndex("transaction_id"));
            transactions.purchase_id =c.getInt(c.getColumnIndex("purchase_id"));
            transactions.sale_id =c.getInt(c.getColumnIndex("sale_id"));
            transactions.customer_id =c.getInt(c.getColumnIndex("customer_id"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;
    }

    public  ArrayList<String> getCustomerNamelist(){
        String sql = "select * from parties order by name";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<String>list = new ArrayList<>();
        while(c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex("name")));
        }
        c.close();
        return list;
    }

    public ArrayList<ExpenseInfo> getExpenseInfo() {
        String sql = "select * from expenses order by expense_id desc";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<ExpenseInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
           ExpenseInfo transactions= new ExpenseInfo();
            transactions.expense_id =c.getInt(c.getColumnIndex("expense_id"));
            transactions.expense_name =c.getString(c.getColumnIndex("expense_name"));
            transactions.operating =c.getInt(c.getColumnIndex("operating"));
            transactions.direct =c.getInt(c.getColumnIndex("direct"));
            transactions.amount=c.getFloat(c.getColumnIndex("amount"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;

    }
    public ArrayList<ExpenseInfo> getExpenseInfoByDate(String date) {
        String sql = "select * from expenses where date like ?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{date});
        ArrayList<ExpenseInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            ExpenseInfo transactions= new ExpenseInfo();
            transactions.expense_id =c.getInt(c.getColumnIndex("expense_id"));
            transactions.expense_name =c.getString(c.getColumnIndex("expense_name"));
            transactions.operating =c.getInt(c.getColumnIndex("operating"));
            transactions.direct =c.getInt(c.getColumnIndex("direct"));
            transactions.amount=c.getFloat(c.getColumnIndex("amount"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;

    }
    public ArrayList<InterestInfo> getInterestInfo() {
        String sql = "select * from interest order by id desc";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<InterestInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            InterestInfo transactions= new InterestInfo();
            transactions.id =c.getInt(c.getColumnIndex("id"));
            transactions.party_id =c.getInt(c.getColumnIndex("party_id"));
            transactions.interest =c.getInt(c.getColumnIndex("interest"));
            transactions.tax =c.getInt(c.getColumnIndex("tax"));
            transactions.amount=c.getFloat(c.getColumnIndex("amount"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;

    }
    public ArrayList<InterestInfo> getInterestInfoByDate(String date) {
        String sql = "select * from interest where date like ? ";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{date});
        ArrayList<InterestInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            InterestInfo transactions= new InterestInfo();
            transactions.id =c.getInt(c.getColumnIndex("id"));
            transactions.party_id =c.getInt(c.getColumnIndex("party_id"));
            transactions.interest =c.getInt(c.getColumnIndex("interest"));
            transactions.tax =c.getInt(c.getColumnIndex("tax"));
            transactions.amount=c.getFloat(c.getColumnIndex("amount"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;

    }

    public ArrayList<ReturnInfo> getReturnsInfo() {
        String sql = "select * from returns order by return_id desc";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<ReturnInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            ReturnInfo transactions= new ReturnInfo();
            transactions.return_id =c.getInt(c.getColumnIndex("return_id"));
            transactions.return_name =c.getString(c.getColumnIndex("return_name"));
            transactions.purchase =c.getInt(c.getColumnIndex("purchase"));
            transactions.sales =c.getInt(c.getColumnIndex("sales"));
            transactions.amount=c.getFloat(c.getColumnIndex("amount"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            transactions.party_id=c.getInt(c.getColumnIndex("party_id"));
            list.add(transactions);
        }
        c.close();
        return list;

    }
    public ArrayList<ReturnInfo> getReturnsInfoByDate(String date) {
        String sql = "select * from returns where date like ? ";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{date});
        ArrayList<ReturnInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            ReturnInfo transactions= new ReturnInfo();
            transactions.return_id =c.getInt(c.getColumnIndex("return_id"));
            transactions.return_name =c.getString(c.getColumnIndex("return_name"));
            transactions.purchase =c.getInt(c.getColumnIndex("purchase"));
            transactions.sales =c.getInt(c.getColumnIndex("sales"));
            transactions.amount=c.getFloat(c.getColumnIndex("amount"));
            transactions.date=c.getString(c.getColumnIndex("date"));
            transactions.party_id=c.getInt(c.getColumnIndex("party_id"));
            list.add(transactions);
        }
        c.close();
        return list;

    }

    public ArrayList<IncomeInfo> getIncomeInfo() {
        String sql = "select * from income order by income_id desc";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<IncomeInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            IncomeInfo transactions= new IncomeInfo();
            transactions.income_id =c.getInt(c.getColumnIndex("income_id"));
            transactions.party_id =c.getInt(c.getColumnIndex("party_id"));
            transactions.income_name =c.getString(c.getColumnIndex("income_name"));
            transactions.amount =c.getFloat(c.getColumnIndex("amount"));
            transactions.date =c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;

    }

    public ArrayList<IncomeInfo> getIncomeInfoByDate(String date) {
        String sql = "select * from income where date like ? ";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{date});
        ArrayList<IncomeInfo>list = new ArrayList<>();
        while(c.moveToNext()) {
            IncomeInfo transactions= new IncomeInfo();
            transactions.income_id =c.getInt(c.getColumnIndex("income_id"));
            transactions.party_id =c.getInt(c.getColumnIndex("party_id"));
            transactions.income_name =c.getString(c.getColumnIndex("income_name"));
            transactions.amount =c.getFloat(c.getColumnIndex("amount"));
            transactions.date =c.getString(c.getColumnIndex("date"));
            list.add(transactions);
        }
        c.close();
        return list;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
