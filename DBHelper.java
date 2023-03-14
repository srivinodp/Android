package com.example.store;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Table1";

    private static final String ID_COL = "id";
    private static final String TYPE_COL = "Type";
    private static final String[] colNames = new String[]{"D1","D2","D3","D4","D5"};
    private static HashSet<String> tablesCreated = new HashSet<>();

    private SQLiteDatabase mReadDB, mWriteDB;

    public void tableCreate(String tableName, List<String> details){
        if(tablesCreated.contains(tableName)) return;

        createTable(tableName,details);
        tablesCreated.add(tableName);
    }
    public void tableCreate(String tableName){
        if(tablesCreated.contains(tableName)) return;
        createTable(tableName, Arrays.asList("Type","D1","D2","D3","D4","D5"));
        tablesCreated.add(tableName);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mReadDB = null;
        mWriteDB = null;
        getTablesCreated();
    }
    public SQLiteDatabase getRdDB(){
        if(mReadDB==null) mReadDB = getReadableDatabase();
        return mReadDB;
    }
    public SQLiteDatabase getWrDB(){
        if(mWriteDB==null) mWriteDB = getWritableDatabase();
        return mWriteDB;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public synchronized HashSet<String> getTablesCreated(){
        if(tablesCreated.isEmpty()==false) return tablesCreated;
        SQLiteDatabase db = getRdDB();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if(c.moveToFirst()){
            do{
                tablesCreated.add(c.getString(0));
                Log.d(TAG, "getTablesCreated: "+c.getString(0));
            }while(c.moveToNext());
        }
        return tablesCreated;
    }
    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    private synchronized void createTable(String name, List<String> fields){
        String cols = "";
        for(String field:fields){
            cols+=","+field+" TEXT";
        }
        String query = "CREATE TABLE " + name + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT"+cols+ ")";
        getWrDB().execSQL(query);
        updateDetails(MainActivity.tgle,new String[]{"0"});
        Log.d(TAG, "onCreate: Table with name"+TABLE_NAME+"created");
    }
    public synchronized boolean delDetails(String type){
        SQLiteDatabase db = getRdDB();
        return db.delete(TABLE_NAME, TYPE_COL + " = ?" ,new String[]{type} ) > 0;
    }
    @SuppressLint("Range")
    public synchronized List<String> getDetailsListWise(String type){
        SQLiteDatabase db = getRdDB();
        Cursor cursor = db.rawQuery("select * from " +TABLE_NAME+ " where " + TYPE_COL+ " =?",new String[]{type});
        List<String> list= new ArrayList<>();
        if(cursor!=null && cursor.moveToFirst()){
            do{
                for(String s:colNames){
                    list.add(cursor.getString(cursor.getColumnIndex(s)));
                }
            }while(cursor.moveToNext());
        }
        return list;
    }
    @SuppressLint("Range")
    public synchronized String getDetails(String type){
        SQLiteDatabase db = getRdDB();
        Cursor cursor = db.rawQuery("select * from " +TABLE_NAME+ " where " + TYPE_COL+ " =?",new String[]{type});
        StringBuffer sb = new StringBuffer();
        sb.append(type+": \n");
        if(cursor!=null && cursor.moveToFirst()){
            do{
                for(String s:colNames){
                    if(s.length()!=0) sb.append(cursor.getString(cursor.getColumnIndex(s))+"\n");
                }
            }while(cursor.moveToNext());
        }
        return sb.toString();
    }
    public boolean CheckIsDataAlreadyInDBorNot(SQLiteDatabase db,String TableName,
                                                      String dbfield, String fieldValue) throws Exception{

        Log.d("Columns: ", Arrays.toString(db.query(TABLE_NAME,null,null,null,null,null,null).getColumnNames()));
        String Query = "Select * from " + TableName + " where " + dbfield + " = ?";
        Cursor cursor = db.rawQuery(Query, new String[]{fieldValue});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public boolean CheckIsDataAlreadyInDBorNot(SQLiteDatabase db,String TableName,
                                               String dbfield1, String fieldValue1,String dbfield2, String fieldValue2) throws Exception{
        Log.d("Columns: ", Arrays.toString(db.query(TABLE_NAME,null,null,null,null,null,null).getColumnNames()));

        String Query = "Select * from " + TableName + " where " + dbfield1 + " = ?" + " AND "+ dbfield2+" = ?";
        Cursor cursor = db.rawQuery(Query, new String[]{fieldValue1,fieldValue2});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    @SuppressLint("Range")
    public synchronized TreeSet<String> getTags(){
        TreeSet<String> a = new TreeSet<>();
        SQLiteDatabase db = getRdDB();
        tableCreate(TABLE_NAME);
        Cursor cursor = db.rawQuery("Select * from "+ TABLE_NAME, new String[]{});
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    a.add(cursor.getString(cursor.getColumnIndex(TYPE_COL)));
                }while(cursor.moveToNext());
            }
        }
        return a;
    }

    public synchronized void updateDetails(String type,String details[]){
        SQLiteDatabase db = getWrDB();
        tableCreate(TABLE_NAME);
        ContentValues values = new ContentValues();
        values.put(TYPE_COL,type);
        for(int i = 1;i<details.length;i++){
            values.put("D"+i,details[i]);
        }
        try {
            db.update(TABLE_NAME,values,TYPE_COL +" = ?",new String[]{type});
        } catch (Exception e) {
            db.close();
            e.printStackTrace();
        }
    }
    public synchronized void addDetails(String type,String details[]){
        SQLiteDatabase db = getWrDB();
        tableCreate(TABLE_NAME);
        ContentValues values = new ContentValues();
        values.put(TYPE_COL,type);
        for(int i = 1;i<6;i++){
            values.put("D"+i,details[i]);
        }
        db.insert(TABLE_NAME, null, values);
    }
}
