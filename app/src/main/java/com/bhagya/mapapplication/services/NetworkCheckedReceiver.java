package com.bhagya.mapapplication.services;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.activityclass.Mobile_NoRegisterActivity;
import com.bhagya.mapapplication.socket.IncomingMessageHandler;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NetworkCheckedReceiver extends BroadcastReceiver implements MyResultReceiver.Receiver {
    MyResultReceiver mreceiver;
    Context context1;
    AQuery aQuery;
    String Status=" ";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"NetworkChange state change",Toast.LENGTH_LONG).show();
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d("NetworkCheckReceiver", "NetworkCheckReceiver invoked...");
            context1=context;
            SocketConnection socketConnection = new SocketConnection();
            socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
            isInternetAvailable isInternetAvailable=new isInternetAvailable();
            isInternetAvailable.execute();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    public class isInternetAvailable<String> extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] params) {
            ConnectivityManager cm = (ConnectivityManager) context1.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                try {

                    URL    url = new URL("http://192.168.0.103");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200){
//                        checkForUpdateContact();
                        Intent webMessageIntent = new Intent(context1.getApplicationContext(), IncomingMessageHandler.class);
                        webMessageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context1.startService(webMessageIntent);
                        Log.d("UPDATE_STATUS","checkedUPdatedcontact method called");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    }
            }

            return Status;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (Status.equals("permission")){
                Toast.makeText(context1,"permission denied",Toast.LENGTH_LONG).show();
            }
            else if(Status.equals("UserRecord")){
                Toast.makeText(context1,"Rgister User Record First",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context1, Mobile_NoRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context1.startActivity(intent);

            }
            Toast.makeText(context1,"asyctast called",Toast.LENGTH_LONG).show();

        }
    }

    public void checkForUpdateContact(){
            try{
            if(AccessToken.getCurrentAccessToken() != null){
            DatabaseHelper databaseHelper=new DatabaseHelper(context1);
            Cursor cursor = context1.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
            while (cursor.moveToNext()){
                String  name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContentValues cv=new ContentValues();
                cv.put("contactName",name);
                cv.put("contactNo",phonenumber);
//                Boolean check= databaseHelper.insertContactInfo(cv,"");
//                if (!check) {
//                   Log.d("UPDATE_STATUS","NEED TO UPDATE");
//                    upDateContactInRemoteDatabase(name,phonenumber);
//                }else {
//                    Log.d("UPDATE_STATUS","DOES NOT NEED TO UPDATE");
//                }

            }

        }}catch (Exception e){
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + context1.getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context1.startActivity(i);
                Status="permission";
               Log.d("UPDATE_STATUS","Permission Problem");

            }
    }


    public void upDateContactInRemoteDatabase(final String name, final String phoneno){
        Log.d("UPDATE_STATUS","UPDATE METHOD CALLED");
        final DatabaseHelper databaseHelper=new DatabaseHelper(context1);
        String contactUrl="http://104.155.153.31/inserContact.php";
        try {
            SharedPreferences preferences = context1.getSharedPreferences("MAIN_USER_RECORD", context1.MODE_PRIVATE);
            String user_id = preferences.getString("user_id", "");
            if (user_id.equals("")){
                Status="UserRecord";
            }else {
            Map<String, String> paramsContact=new HashMap<>();
            paramsContact.put("contactname",name);
            paramsContact.put("contactno",phoneno);
            paramsContact.put("user_id",user_id);
            aQuery.ajax(contactUrl,paramsContact, JSONArray.class,new AjaxCallback<JSONArray>(){
                @Override
                public void callback(String url, JSONArray object, AjaxStatus status) {
                    Log.d("UPDATE_STATUS","CALLBACKE METHOD CALLED"+object);
                    String statusUpdate;
                    try {
                        JSONObject obj=object.getJSONObject(0);
                        statusUpdate= (String) obj.get("status");
                        Log.d("REMOTE_RESPONE",statusUpdate);
                        if (statusUpdate.equals("contactUpdated")){
                            ContentValues cv=new ContentValues();
                            cv.put("contactName",name);
                            cv.put("contactNo",phoneno);
//                            databaseHelper.insertContactInfo(cv,"insert");
                            Log.d("UPDATE_STATUS","LOCAL DATABASE UPDATED");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("UpdateStatus","Successfully updated");
                }
            });
        }}catch (Exception e){
            Log.d("UPDATE_STATUS","Register your phone");
        }




    }

}






