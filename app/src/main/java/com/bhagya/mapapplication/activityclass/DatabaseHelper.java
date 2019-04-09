package com.bhagya.mapapplication.activityclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bhagya.mapapplication.Wwboard;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Bhagya on 12/5/2016.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

     static String dbname = "defaultsettings";

    String createCoantactTable="CREATE TABLE if not exists \"contacts\" (\n" +
        "\t`contactId`\tINTEGER PRIMARY KEY,\n" +
        "\t`contactNo`\tSTRING,\n" +
        "\t`contactName`\tSTRING\n" +
        ")";



    String creatInboxTable="CREATE TABLE if not exists `inbox` (\n" +
            "\t`smsId`\tINTEGER PRIMARY KEY,\n" +
            "\t`contactNo`\tTEXT,\n" +
            "\t`content`\tTEXT,\n" +
            "\t`timeStamps`\tTEXT\n" +
            ")";

    String creatOutboxTable="CREATE TABLE if not exists  \"outbox\" (\n" +
            "\t`smsId`\tINTEGER PRIMARY KEY,\n" +
            "\t`contactNo`\tTEXT,\n" +
            "\t`content`\tTEXT,\n" +
            "\t`timeStamps`\tTEXT\n" +
            ")";


    String LocationTable="CREATE TABLE if not exists `location` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`Latitude`\tTEXT,\n" +
            "\t`Longitude`\tTEXT,\n" +
            "\t`dateTime`\tTEXT\n" +
            ")";

    String createCalLogs="CREATE TABLE if not exists `calLogs` (\n" +
            "\t`callLogId`\tINTEGER PRIMARY KEY,\n" +
            "\t`contactName`\tTEXT,\n" +
            "\t`callDuration`\tTEXT,\n" +
            "\t`countryIso`\tTEXT,\n" +
            "\t`contactNo`\tTEXT,\n" +
            "\t`callType`\tTEXT,\n" +
            "\t`dateTime`\tTEXT\n" +
            ")";

    String creatWwBoardTable = "CREATE TABLE if not exists `wwBoard` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY,\n" +
            "\t`message`\tTEXT,\n" +
            "\t`date`\tTEXT\n" +
            ")";


    public DatabaseHelper(Context context) {
        super(context, dbname, null, 1);
        getWritableDatabase().execSQL(creatWwBoardTable);
        getWritableDatabase().execSQL(createCoantactTable);
        getWritableDatabase().execSQL(creatInboxTable);
        getWritableDatabase().execSQL(creatOutboxTable);
        getWritableDatabase().execSQL(LocationTable);
        getWritableDatabase().execSQL(createCalLogs);
    }

    public void insertData(ContentValues cv,String table) {
       Log.d("INSERTDATA",table);
        getWritableDatabase().insertWithOnConflict(table, null, cv,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<Wwboard> getListOfWwBoardMessage() {
        String sql = "SELECT * FROM WwBoard";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        ArrayList<Wwboard> list = new ArrayList<>();
        while(c.moveToNext()) {
            Wwboard info = new Wwboard();
            info.message = c.getString(c.getColumnIndex("message"));
            info.date = c.getString(c.getColumnIndex("date"));
            list.add(info);
        }
        return  list;
    }

    public ArrayList<ContactInfo> getListOfContact() {
        String sql = "SELECT * FROM contacts";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        ArrayList<ContactInfo> list = new ArrayList<>();
        while(c.moveToNext()) {
            ContactInfo info = new ContactInfo();
            info.contactname = c.getString(c.getColumnIndex("contactName"));
            info.contactno= c.getString(c.getColumnIndex("contactNo"));
            list.add(info);
        }
        return  list;
    }


    public ArrayList<CallHistory> getListOfCalLogs() {
        String sql = "SELECT * FROM calLogs";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        ArrayList<CallHistory> list = new ArrayList<>();
        while(c.moveToNext()) {
            CallHistory info = new CallHistory();
            info.contactName= c.getString(c.getColumnIndex("contactName"));
            info.contactNo= c.getString(c.getColumnIndex("contactNo"));
            info.callType = c.getString(c.getColumnIndex("callType"));
            info.callDuration = c.getString(c.getColumnIndex("callDuration"));
            info.countryIso = c.getString(c.getColumnIndex("countryIso"));
            info.dateTime = c.getString(c.getColumnIndex("dateTime"));
            list.add(info);
        }
        return  list;
    }


    public ArrayList<inboxInfo> getListOfInbox() {
        String sql = "SELECT * FROM inbox";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        ArrayList<inboxInfo> list = new ArrayList<>();
        while(c.moveToNext()) {
            inboxInfo info = new inboxInfo();
            info.sms= c.getString(c.getColumnIndex("content"));
            info.phone= c.getString(c.getColumnIndex("contactNo"));
            info.date = c.getString(c.getColumnIndex("timeStamps"));
            list.add(info);
        }
        return  list;
    }
    public ArrayList<inboxInfo> getListOfOutBox() {
        String sql = "SELECT * FROM outbox";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        ArrayList<inboxInfo> list = new ArrayList<>();
        while(c.moveToNext()) {
            inboxInfo info = new inboxInfo();
            info.sms= c.getString(c.getColumnIndex("content"));
            info.phone= c.getString(c.getColumnIndex("contactNo"));
            info.date = c.getString(c.getColumnIndex("timeStamps"));
            list.add(info);
        }
        return  list;
    }

    public LocationInfo getLastLocation() {
        String sql = "SELECT * FROM location WHERE id=(SELECT MAX(id) FROM location)";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        LocationInfo locationInfo = new LocationInfo();
        while (c.moveToNext()) {
            locationInfo.latitudedataValue = c.getString(c.getColumnIndex("Latitude"));
            locationInfo.lognitudedataValue = c.getString(c.getColumnIndex("Longitude"));
            locationInfo.dataTime = c.getString(c.getColumnIndex("dateTime"));
        }
        return locationInfo ;
    }


    public void singOut() {
        Log.d("SIGN_OUT_FUNTION_DATA","Om");

        getWritableDatabase().execSQL("DELETE FROM wwBoard");
        getWritableDatabase().execSQL("DELETE FROM contacts");
        getWritableDatabase().execSQL("DELETE FROM calLogs");
        getWritableDatabase().execSQL("DELETE FROM inbox");
        getWritableDatabase().execSQL("DELETE FROM outbox");
        getWritableDatabase().execSQL("DELETE FROM contacts");
    }
    //.................read default settings....................................

  //  public  DefaultSettingsData getSettings(int id){
//        DefaultSettingsData defaultSettingsData=new DefaultSettingsData();
//        String sql="select * from defaultsettings where id="+id;
//        Cursor c=getWritableDatabase().rawQuery(sql,null);
//        while (c.moveToNext()){
//            defaultSettingsData.wifi_contacts=c.getInt(c.getColumnIndex("wifi_contacts"));
//            defaultSettingsData.wifi_callhistory=c.getInt(c.getColumnIndex("wifi_callhistory"));
//            defaultSettingsData.wifi_inbox=c.getInt(c.getColumnIndex("wifi_inbox"));
//            defaultSettingsData.wifi_outbox=c.getInt(c.getColumnIndex("wifi_outbox"));
//            defaultSettingsData.mdata_contacts=c.getInt(c.getColumnIndex("mdata_contacts"));
//            defaultSettingsData.mdata_callhistory=c.getInt(c.getColumnIndex("mdata_callhistory"));
//            defaultSettingsData.mdata_inbox=c.getInt(c.getColumnIndex("mdata_inbox"));
//            defaultSettingsData.mdata_outbox=c.getInt(c.getColumnIndex("mdata_outbox"));

      //  }
       // return defaultSettingsData;
  //  }
//........................update settings.....................................................

   public void updateSettings(String column,int value){
       String updatesql="Update defaultsettings set "+column+"="+value+" where id=1";
       getWritableDatabase().execSQL(updatesql);
       Log.d("UPDATESUCESS","SUCESS");

   }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
