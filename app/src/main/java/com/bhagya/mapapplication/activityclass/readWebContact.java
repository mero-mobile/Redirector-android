package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class readWebContact extends Activity {
    Button button,upload;
    ListView listView;
    userinfo userinfo;
    AQuery aQuery;
    String url="http://104.155.153.31/selectContact.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_listview);
        aQuery=new AQuery(this);
        button= (Button) findViewById(R.id.fetch);
        listView= (ListView) findViewById(R.id.contactlistview);

        ContactInfo info=new ContactInfo();
        fetchData();
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

        ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                ContactInfo info = new ContactInfo();
                info.contactname = obj.getString("contactname");
                info.contactno = obj.getString("contactno");
                list.add(info);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        populateData(list);

    }

    public void populateData(ArrayList<ContactInfo> list) {
        contactListAdapter adapter = new contactListAdapter(this, list);
        listView.setAdapter(adapter);
    }

}

