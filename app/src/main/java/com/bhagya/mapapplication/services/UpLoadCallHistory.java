package com.bhagya.mapapplication.services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpLoadCallHistory extends IntentService {
   Context context;
    AQuery aQuery = new AQuery(this);
    TextView textView = null;
    int callcode;
    static boolean ring = false;
    static boolean callReceived = false;

    StringBuffer sb = new StringBuffer();

    public UpLoadCallHistory() {
        super("UpLoadCallHistory");
    }

  SocketConnection socketConnection = new SocketConnection();
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        Toast.makeText(this,"Conatact Data Uploading",Toast.LENGTH_LONG).show();
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
    context = this;
    uploadCallhistory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ResultReceiver rec = intentresult.getParcelableExtra("receiverTag");
//        Bundle b= new Bundle();
//        b.putString("ServiceTag","Rajiv");
//       // rec.send(0,b);
//        Log.d("ServicesDetroyed","ok");
//        Toast.makeText(this,"Conatact Data Uploaded Successfully",Toast.LENGTH_LONG).show();

    }

    public void uploadCallhistory() {
        String strOrder1 = android.provider.CallLog.Calls.DATE + " DESC";
        String callType,callDuration,mobileNumber,dateTime,name,countryIso,callHistoryId,userId;
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
        userId = preferences.getString("user_id", "");
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                .          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null, null,strOrder1);
        Log.d("CALLHISTORY_CALLED","inside callhisty method+mobileNumber+callDuration+callType+userId+realDateTime");

        while (cursor.moveToNext()) {

            callType=cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            callHistoryId = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
            callDuration=cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            mobileNumber=cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            name=cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            countryIso=cursor.getString(cursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO));
            dateTime=cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            Date realDateTime=new Date(Long.valueOf(dateTime));
            String stringCallType=null;
            int typecode=Integer.parseInt(callType);
            switch (typecode) {

                case  CallLog.Calls.INCOMING_TYPE:
                    stringCallType="Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    stringCallType="Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    stringCallType="Missed";
            }

            JSONObject paramsUpload = new JSONObject();
            try {

                paramsUpload.put("contactName",name);
                paramsUpload.put("countryIso",countryIso);
                paramsUpload.put("contactNo",mobileNumber);
                paramsUpload.put("callType",stringCallType);
                paramsUpload.put("dateTime",String.valueOf(realDateTime));
                paramsUpload.put("callDuration",callDuration);
                paramsUpload.put("callLogId",callHistoryId);
                paramsUpload.put("userId",userId);
                socketConnection.rsocket.emit("uploadCallLogs",paramsUpload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}



