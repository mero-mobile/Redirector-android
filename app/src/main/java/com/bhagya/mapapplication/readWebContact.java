package com.bhagya.mapapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class readWebContact extends Activity {

    Button button,upload;
    ListView listView;
    userinfo userinfo;
    AQuery aQuery;
    String url="http://192.168.43.162/meromobile.com/selectContact.php";
    String uploadurl="http://192.168.43.162/meromobile.com/outbox.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_listview);
        aQuery=new AQuery(this);
        button= (Button) findViewById(R.id.fetch);
        listView= (ListView) findViewById(R.id.contactlistview);
       // upload= (Button) findViewById(R.id.upload);
        contactinfo info=new contactinfo();
        fetchData();
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadData();

//            }
//        });
        final DatabaseHelper databaseHelper=new DatabaseHelper(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();

            }
        });
    }
    public void fetchData() {
        aQuery.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray array, AjaxStatus status) {
                super.callback(url, array, status);
                parseData(array);
                Log.i("response", "REsponser:" + array);
            }
        });
    }
    public void parseData(JSONArray array) {

        ArrayList<contactinfo> list = new ArrayList<contactinfo>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                contactinfo info = new contactinfo();
               // info.id = obj.getString("id");
                info.contactname = obj.getString("contactname");
                info.contactno = obj.getString("contactno");
                // info.email = obj.getString("email");
                list.add(info);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        populateData(list);

    }

    public void populateData(ArrayList<contactinfo> list) {
        contactListAdapter adapter = new contactListAdapter(this, list);
        listView.setAdapter(adapter);
    }
    public void uploadData(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("phoneno","hkjhkk");
        params.put("sms","fhafhaldhlkdkadjaklfjkljfldkjflafjl fa ");
        params.put("date","203910390");
        aQuery.ajax(uploadurl,params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray array, AjaxStatus status) {
                super.callback(url, array, status);
                Log.i("response", "REsponser:" + array);
            }
        });
    }
}

