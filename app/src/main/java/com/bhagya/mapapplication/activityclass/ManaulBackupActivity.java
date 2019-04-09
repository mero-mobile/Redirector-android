package com.bhagya.mapapplication.activityclass;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.services.MyResultReceiver;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ManaulBackupActivity extends AppCompatActivity {
    Button contacts, sms, outbox, callHistory;
    ReadContactList readContactList;
    ProgressDialog progressDialog;
    TextView skip;
    String userId;
    Context context;
    AQuery aQuery;
    public MyResultReceiver mreceiver;
    SocketConnection socketConnection = new SocketConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manaul_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sms = (Button) findViewById(R.id.smsbtn);
        outbox = (Button) findViewById(R.id.smsoutbox_btn);
        contacts = (Button) findViewById(R.id.contactbackup_btn);
        callHistory = (Button) findViewById(R.id.callhistory_btn);
        readContactList = new ReadContactList();
        skip = (TextView) findViewById(R.id.backup_skip);
        aQuery = new AQuery(this);
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent skipIntent = new Intent(ManaulBackupActivity.this, MainUserHome.class);
                startActivity(skipIntent);
            }
        });

        SharedPreferences preferences = getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
        userId = preferences.getString("user_id", "");
        // Toast.makeText(ManaulBackupActivity.this, user_id, Toast.LENGTH_LONG).show();
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UploadUserData uploadUserData = new UploadUserData();
//                uploadUserData.execute("contacts");
                uploadContacts();
            }
        });

        callHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//             UploadUserData uploadUserData=new UploadUserData();
//                uploadUserData.execute("callhistry");
                uploadCallhistory();
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uplaodInbox();
                uploadOutbox();
            }
        });

        outbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class UploadUserData extends AsyncTask<String, String, String> {
        String status;

        @Override
        protected void onPreExecute() {
            startProgessbar();
        }

        @Override
        protected String doInBackground(String... params) {
            String condition=params[0];
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    URL url = new URL("http://104.155.153.31");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        status = "true";
                        if (condition.equals("contacts")){
                        try {
                            Log.d("CALLHISTORY_CALLED","contacts methos caled");
                            uploadContacts();
                        } catch (Throwable e) {
                            // Toast.makeText(ManaulBackupActivity.this,"Exception Occurs",Toast.LENGTH_LONG).show();
                            Log.d("EXCEPTION", "permision error");
                        }
                    }

                    if (condition.equals("callhistry")) {
                     //   status = "true";

                        try {
                            Log.d("CALLHISTORY_CALLED","callhistry methos caled");
                            uploadCallhistory();

                        } catch (Throwable e) {
                            // Toast.makeText(ManaulBackupActivity.this,"Exception Occurs",Toast.LENGTH_LONG).show();
                            Log.d("EXCEPTION", "permision error");
                        }
                    }

                    }
                    else {
                        status = "false";
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                status = "false";

            }

            return status;
        }

        @Override
        protected void onPostExecute(String s) {

            AlertDialog.Builder alert = new AlertDialog.Builder(ManaulBackupActivity.this);
            alert.setTitle("Update Status");
            alert.setMessage("Updated succussfully...");
            alert.setCancelable(false);
            alert.setNegativeButton("Ok", null);
            alert.show();
            if (s.equals("false")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManaulBackupActivity.this);
                alertDialog.setTitle("Connection Error");
                alertDialog.setMessage("Please check your internet connection...");
                alertDialog.setCancelable(false);
                alertDialog.setNegativeButton("Ok", null);
                alertDialog.show();
            }
            progressDialog.dismiss();
        }
    }

    public void startProgessbar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    public void uploadContacts() {

        String contactName, contactNo,contactId;
        Cursor cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            Log.d("CONTACTID",contactId);
            SharedPreferences preferences = getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
            userId = preferences.getString("user_id", "");
            JSONObject params = new JSONObject();
            try {
                params.put("userId", userId);
                params.put("contactName", contactName);
                params.put("contactNo", contactNo);
                params.put("contactId",contactId);
                socketConnection.rsocket.emit("uploadContact",params);
                Log.d("CONTACT_DATA_UPLOADED", String.valueOf(params));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadCallhistory() {
        String strOrder1 = android.provider.CallLog.Calls.DATE + " DESC";
        String callType,callDuration,mobileNumber,dateTime,name,countryIso,callHistoryId;

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
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


    public  void uplaodInbox() {
        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date DESC","body"},null,null,null);
        cursor.moveToFirst();

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

    public void uploadOutbox() {
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
}




//communication between service and reciever.....
//    @Override
//    public void onReceiveResult(int resultCode, Bundle resultData) {
//        Log.d("BHAGYANK", "received result from Service=" + resultData.getString("ServiceTag"));
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle("Status");
//        builder.setMessage("Succesfully Uploaded Contact Data");
//        builder.show();
//
//        Toast.makeText(this, "Contact uploaded complested" + resultData.get("ServiceTag"), Toast.LENGTH_LONG).show();
//
//    }

