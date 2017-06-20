package com.bhagya.mapapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class readWeboutbox extends Activity {
    Button button,upload;
    ListView listView;
    userinfo userinfo;
    AQuery aQuery;
    // String url="http://192.168.1.162/meromobile.com/selectContact.php";
    String url="http://192.168.43.162/meromobile.com/selectoutbox.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readinbox);
        aQuery=new AQuery(this);
        button= (Button) findViewById(R.id.fetch);
        listView= (ListView) findViewById(R.id.inboxlistview);

        inboxInfo info=new inboxInfo();
        fetchData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

        ArrayList<inboxInfo> list = new ArrayList<inboxInfo>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                inboxInfo info = new inboxInfo();
                // info.id = obj.getString("id");
                info.sms = obj.getString("sms");
                info.phone = obj.getString("phoneno");
                // info.email = obj.getString("email");
                list.add(info);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        populateData(list);

    }

    public void populateData(ArrayList<inboxInfo> list) {
        inboxlistAdapter adapter = new inboxlistAdapter(this, list);
        listView.setAdapter(adapter);
    }

}

