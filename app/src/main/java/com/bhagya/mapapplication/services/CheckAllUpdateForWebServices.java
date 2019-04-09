package com.bhagya.mapapplication.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
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
import com.facebook.Profile;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckAllUpdateForWebServices extends IntentService {
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
    public CheckAllUpdateForWebServices() {
        super("");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        intentresult=new Intent(intent);
        context=this;
        databaseHelper=new DatabaseHelper(this);
        aQuery=new AQuery(this);
//        StoreContacts =databaseHelper.getcontactdata();
        GetContactsIntoArrayList();
    }
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this,"Conatact Data Uploading",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ResultReceiver rec = intentresult.getParcelableExtra("receiverTag");
        Bundle b= new Bundle();
       // b.putString("ServiceTag","Rajiv");
       // rec.send(0,b);
        Log.d("ServicesDetroyed","ok");
        Toast.makeText(this,"Conatact Data Uploaded Successfully",Toast.LENGTH_LONG).show();
    }
    public void GetContactsIntoArrayList(){
//        int count=databaseHelper.allTableFieldCountForUpdate();
//        ContactInfo[] contactInfo=new ContactInfo[count];
        SharedPreferences prfs = getSharedPreferences("MAIN_USER_RECORD",MODE_PRIVATE);
        user_id = prfs.getString("user_id","");
        String uploadurl="http://104.155.153.31/inserContact.php";
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        int i=0;
//       contactInfo=databaseHelper.readContactInfo();
        //name=contactInfo[i].contactname;
       //phonenumber=contactInfo[i].contactno;
      //  Log.d("OBJECTDATA",""+name+""+phonenumber);
        DatabaseHelper databaseHelper=new DatabaseHelper(this);

//        while (i<=count-1) {
//            name=contactInfo[i].contactname;
//            phonenumber=contactInfo[i].contactno;
//            ContentValues cvremote=new ContentValues();
//            cvremote.put("contactName",name);
//            cvremote.put("contactNo",phonenumber);
//            Boolean check= databaseHelper.InsertcaontactinfoRemote(cvremote);
//            if (!check) {
//                Map<String, Object> params = new HashMap<String,Object>();
//                params.put("contactname", name);
//                params.put("contactno", phonenumber);
//                params.put("user_id", user_id);
//                Toast.makeText(this, "this is the user id" + user_id, Toast.LENGTH_LONG).show();
//                aQuery.ajax(uploadurl, params, JSONArray.class, new AjaxCallback<JSONArray>() {
//                    @Override
//                    public void callback(String url, JSONArray array, AjaxStatus status) {
//                        super.callback(url, array, status);
//                        Log.i("response", "REsponser:" + array);
//                    }
//                });
//            }
//           databaseHelper.InsertcaontactinfoRemote(cvremote);
//            i++;
//        }

        //cursor.close();



    }
}
