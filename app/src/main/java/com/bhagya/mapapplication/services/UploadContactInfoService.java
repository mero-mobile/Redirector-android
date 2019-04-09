package com.bhagya.mapapplication.services;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.activityclass.ContactInfo;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadContactInfoService extends IntentService {
    DatabaseHelper databaseHelper;
    ListView listView;
    ArrayList<String> StoreContacts;
    ArrayAdapter<String> arrayAdapter;
    AQuery aQuery;
    Cursor cursor;
    Profile profile;
    Context context;
    Intent intentresult;
    String name, phonenumber, user_id;
    public static final int RequestPermissionCode = 1;
    public UploadContactInfoService() {
        super("");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        intentresult=new Intent(intent);
        context=this;
        databaseHelper=new DatabaseHelper(this);
        aQuery=new AQuery(this);
        uploadContacts();
    }

    SocketConnection socketConnection = new SocketConnection();
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        Toast.makeText(this,"Conatact Data Uploading",Toast.LENGTH_LONG).show();
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        return super.onStartCommand(intent, flags, startId);

    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(),
               Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions((Activity) getBaseContext(),new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
    public void uploadContacts() {
        String contactName, contactNo,contactId,userId;
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
