package com.bhagya.mapapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Bhagya on 12/5/2016.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    static String dbname = "pass";
//       String createTable = "CREATE TABLE if not exists `userTable` (\n" +
//               "\t`id`\tINTEGER UNIQUE,\n" +
//               "\t`username`\tTEXT,\n" +
//               "\t`password`\tTEXT\n" +
//               ")";
String contactinfoTable="CREATE TABLE if not exists \"contactinfo\" (\n" +
        "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
        "\t`contactNo`\tTEXT,\n" +
        "\t`contactName`\tTEXT,\n" +
        "\t`Image`\tBLOB\n" +
        ")";
    String smsTable="CREATE TABLE if not exists `smsinfo` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`mobileno`\tTEXT,\n" +
            "\t`sms`\tTEXT,\n" +
            "\t`date`\tTEXT\n" +
            ")";

    String sentsmsTable="CREATE TABLE if not exists  \"sentsmsinfo\" (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`mobileno`\tTEXT,\n" +
            "\t`sms`\tTEXT,\n" +
            "\t`date`\tTEXT\n" +
            ")";


    String LocationTable="CREATE TABLE if not exists `Location` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`Latitude`\tTEXT,\n" +
            "\t`Longitude`\tTEXT,\n" +
            "\t`dateTime`\tTEXT\n" +
            ")";

    String createTable = "CREATE TABLE if not exists `ansQsnLevel1` (\n" +
            "\t`SN`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`question`\tTEXT,\n" +
            "\t`ansA`\tTEXT,\n" +
            "\t`ansB`\tTEXT,\n" +
            "\t`ansC`\tTEXT,\n" +
            "\t`ansD`\tTEXT,\n" +
            "\t`correctAns`\tTEXT\n" +
            ")";
    String creatRandomTable = "CREATE TABLE if not exists `RandomTable` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`randomid`\tINTEGER\n" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, dbname, null, 1);
        getWritableDatabase().execSQL(createTable);
        getWritableDatabase().execSQL(creatRandomTable);

       // getWritableDatabase().execSQL(createTable);
        getWritableDatabase().execSQL(contactinfoTable);
        getWritableDatabase().execSQL(smsTable);
        getWritableDatabase().execSQL(sentsmsTable);
        getWritableDatabase().execSQL(LocationTable);

    }


    public void InsertQsnAns(ContentValues cv) {
        getWritableDatabase().insert("questionset1", "", cv);
    }

    public void InsertRandomid(ContentValues cv) {
        getWritableDatabase().insert("RandomTable", "", cv);
    }

    public qsn_ans_data getdata(int id) {

        String sql = "Select * from questionset1 where id=" + id;
        Cursor c = getWritableDatabase().rawQuery(sql, null);
        qsn_ans_data data = new qsn_ans_data();
        while (c.moveToNext()) {

            data.questions = c.getString(c.getColumnIndex("questions"));
            data.ansA = c.getString(c.getColumnIndex("ansA"));
            data.ansB = c.getString(c.getColumnIndex("ansB"));
            data.ansC = c.getString(c.getColumnIndex("ansC"));
            data.ansD = c.getString(c.getColumnIndex("ansD"));
            data.correctAns = c.getString(c.getColumnIndex("correctAns"));


        }
        c.close();
        return data;

    }

    public ArrayList<Integer> getListofRandomid() {
        String sql = "Select * from RandomTable";

        Cursor c = getWritableDatabase().rawQuery(sql, null);
        ArrayList list = new ArrayList();
        while (c.moveToNext()) {

            // info.id= c.getString(c.getColumnIndex("id"));
            list.add(c.getInt(c.getColumnIndex("randomid")));


        }
        c.close();
        return list;

    }

    public void resetdata() {

        String sqlforreset="UPDATE RandomTable\n" +
                "SET randomid = NULL";
        getWritableDatabase().execSQL(sqlforreset);
        //  Cursor c=getWritableDatabase().rawQuery(sqlforreset,null);

    }




    public void Insertsms(ContentValues cv) {
        getWritableDatabase().insert("smsinfo", "", cv);
    }
    public  void Insertsentsms(ContentValues cv){
        getWritableDatabase().insert("sentsmsinfo","",cv);
    }
    public  void Insertcaontactinfo(ContentValues cv){
        getWritableDatabase().insert("contactinfo","",cv);
    }
public void InsertLocation(ContentValues cv){
    getWritableDatabase().insert("Location","",cv);
}

    public ArrayList getcontactdata() {

        String sql = "Select * from contactinfo";
        ArrayList list=new ArrayList();
        Cursor c = getWritableDatabase().rawQuery(sql, null);

        while (c.moveToNext()) {

//            data.username = c.getString(c.getColumnIndex("username"));
//            data.password = c.getString(c.getColumnIndex("password"));
            list.add(c.getString(c.getColumnIndex("contactNo")));
            list.add(c.getString(c.getColumnIndex("contactName")));


        }

        c.close();
        return list;

    }
    public ArrayList getsentsms() {

        String sql = "Select * from sentsmsinfo ";
        ArrayList list=new ArrayList();
        Cursor c = getWritableDatabase().rawQuery(sql, null);
        userinfo data = new userinfo();
        while (c.moveToNext()) {

//            data.username = c.getString(c.getColumnIndex("username"));
//            data.password = c.getString(c.getColumnIndex("password"));
            list.add(c.getString(c.getColumnIndex("mobileno")));
            list.add(c.getString(c.getColumnIndex("sms")));
            list.add(c.getString(c.getColumnIndex("date")));


        }

        c.close();
        return list;

    }
    public ArrayList getsms() {
        String sql = "Select * from smsinfo ";
        ArrayList list=new ArrayList();
        Cursor c = getWritableDatabase().rawQuery(sql, null);
        userinfo data = new userinfo();
        while (c.moveToNext()) {
//            data.username = c.getString(c.getColumnIndex("username"));
//            data.password = c.getString(c.getColumnIndex("password"));
            list.add(c.getString(c.getColumnIndex("mobileno")));
            list.add(c.getString(c.getColumnIndex("sms")));
            list.add(c.getString(c.getColumnIndex("date")));


        }

        c.close();
        return list;

    }


 public ArrayList getLocation() {
        String sql = "Select * from Location";
        ArrayList list=new ArrayList();
        Cursor c = getWritableDatabase().rawQuery(sql, null);
        userinfo data = new userinfo();
        while (c.moveToNext()) {
//            data.username = c.getString(c.getColumnIndex("username"));
//            data.password = c.getString(c.getColumnIndex("password"));
            list.add(c.getString(c.getColumnIndex("id")));
            list.add(c.getString(c.getColumnIndex("Latitude")));
            list.add(c.getString(c.getColumnIndex("Longitude")));
            list.add(c.getString(c.getColumnIndex("dateTime")));



        }

        c.close();
        return list;

    }

    //**************************************************************************************************************************

//    String createTable = "CREATE TABLE if not exists `ansQsnLevel1` (\n" +
//            "\t`SN`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//            "\t`question`\tTEXT,\n" +
//            "\t`ansA`\tTEXT,\n" +
//            "\t`ansB`\tTEXT,\n" +
//            "\t`ansC`\tTEXT,\n" +
//            "\t`ansD`\tTEXT,\n" +
//            "\t`correctAns`\tTEXT\n" +
//            ")";
//    String creatRandomTable = "CREATE TABLE if not exists `RandomTable` (\n" +
//            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//            "\t`randomid`\tINTEGER\n" +
//            ")";
//
//    public DatabaseHelper(Context context) {
//        super(context, dbname, null, 1);
//        getWritableDatabase().execSQL(createTable);
//        getWritableDatabase().execSQL(creatRandomTable);
//        // getWritableDatabase().execSQL(createTalble2);
//    }

//
//    public void InsertQsnAns(ContentValues cv) {
//        getWritableDatabase().insert("questionset1", "", cv);
//    }
//
//    public void InsertRandomid(ContentValues cv) {
//        getWritableDatabase().insert("RandomTable", "", cv);
//    }
//
//    public qsn_ans_data getdata(int id) {
//
//        String sql = "Select * from questionset1 where id=" + id;
//        Cursor c = getWritableDatabase().rawQuery(sql, null);
//        qsn_ans_data data = new qsn_ans_data();
//        while (c.moveToNext()) {
//
//            data.questions = c.getString(c.getColumnIndex("questions"));
//            data.ansA = c.getString(c.getColumnIndex("ansA"));
//            data.ansB = c.getString(c.getColumnIndex("ansB"));
//            data.ansC = c.getString(c.getColumnIndex("ansC"));
//            data.ansD = c.getString(c.getColumnIndex("ansD"));
//            data.correctAns = c.getString(c.getColumnIndex("correctAns"));
//
//
//        }
//        c.close();
//        return data;
//
//    }
//
//    public ArrayList<Integer> getListofRandomid() {
//        String sql = "Select * from RandomTable";
//
//        Cursor c = getWritableDatabase().rawQuery(sql, null);
//        ArrayList list = new ArrayList();
//        while (c.moveToNext()) {
//
//            // info.id= c.getString(c.getColumnIndex("id"));
//            list.add(c.getInt(c.getColumnIndex("randomid")));
//
//
//        }
//        c.close();
//        return list;
//
//    }
//
//    public void resetdata() {
//
//        String sqlforreset="UPDATE RandomTable\n" +
//                "SET randomid = NULL";
//        getWritableDatabase().execSQL(sqlforreset);
//        //  Cursor c=getWritableDatabase().rawQuery(sqlforreset,null);
//
//    }
//
//

    //**************************************************************************************************************************











    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
