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
            " \"secondary_unit\" TEXT,\"conversion_rate\" INTEGER,\"purchase_price\" REAL ,\n"+
            "\"selling_price\" REAL, \"opening_stock\" REAL, \"AsOfDate\" TEXT," +
            "\"unit_price\" REAL, \"minStockQty\" REAL)";
    String sqlCreateInvoiceTable = "CREATE TABLE if not exists \"invoice\" \n" +
            "( \"invoice_no\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            " \"invoice_date\" TEXT, \"due_date\" TEXT, \n" +
            " \"invoice_total\" INT )";
    String sqlCreateInvoiceItemsTable ="CREATE TABLE if not exists \"invoice_items\" \n" +
            "( \"invoice_no\" INTEGER NOT NULL REFERENCES invoice (invoice_no),\n" +
            " \"product_id\" INTEGER NOT NULL REFERENCES inventory(product_id) , \"quantity\" INT, \n" +
            " \"discount\" REAL,\"amount\" REAL)";

    String sqlCreatePartyTable ="CREATE TABLE if not exists \"parties\" \n" +
            "( \"party_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            " \"name\" TEXT , \"billing_address\" TEXT,\"phone\" TEXT, \"opening_balance\" REAL, \n" +
            " \"asOfDate\" TEXT,\"toPay\" INT DEFAULT 0,\"toReceive\" INT DEFAULT 0)";

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(sqlCreateTable);
        getWritableDatabase().execSQL(sqlCreateInvoiceTable);
        getWritableDatabase().execSQL(sqlCreateInvoiceItemsTable);
        getWritableDatabase().execSQL(sqlCreatePartyTable);
    }
    public void insertItem(ContentValues contentValues){
        getWritableDatabase().insert("inventory","",contentValues);
    }
    public void insertParty(ContentValues contentValues){
        getWritableDatabase().insert("parties","",contentValues);}

    public boolean insertInvoiceData(ContentValues contentValues){
        getWritableDatabase().insert("invoice","",contentValues);
        return true;
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
            item.qty_in_stock = c.getInt(c.getColumnIndex("opening_stock"));

        }
        c.close();
       return  item;
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
            item.invoice_total = c.getInt(c.getColumnIndex("invoice_total"));
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
            item.qty_in_stock = c.getInt(c.getColumnIndex("opening_stock"));

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
            item.qty_in_stock = c.getInt(c.getColumnIndex("opening_stock"));
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
    public ArrayList<InventoryItems> getOutOfStockList(){
        String sql = "select * from inventory where qty_in_stock <=?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{String.valueOf(2)});
        ArrayList<InventoryItems>list = new ArrayList<>();
//        while(c.moveToNext()) {
//            InventoryItems item = new InventoryItems();
//            item.product_id = c.getInt(c.getColumnIndex("product_id"));
//            item.product_name = c.getString(c.getColumnIndex("product_name"));
//            item.unit_price = c.getFloat(c.getColumnIndex("unit_price"));
//            item.qty_in_stock = c.getInt(c.getColumnIndex("opening_stock"));
//            list.add(item);
//        }
//        c.close();
        return list;
    }

    public ArrayList<InventoryItems> getProductOutOfStockList(){
        String sql = "select * from inventory where qty_in_stock <=?";
        Cursor c = getReadableDatabase().rawQuery(sql,new String[]{String.valueOf(2)});
        ArrayList<InventoryItems>list = new ArrayList<>();
        while(c.moveToNext()) {
            InventoryItems item = new InventoryItems();
            item.product_id = c.getInt(c.getColumnIndex("product_id"));
            item.product_name = c.getString(c.getColumnIndex("product_name"));
            item.unit_price = c.getFloat(c.getColumnIndex("unit_price"));
            item.qty_in_stock = c.getInt(c.getColumnIndex("opening_stock"));
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
            item.invoice_total=  c.getInt(c.getColumnIndex("invoice_total"));

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
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
