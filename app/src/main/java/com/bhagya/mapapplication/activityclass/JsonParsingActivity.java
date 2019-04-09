package com.bhagya.mapapplication.activityclass;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class JsonParsingActivity extends Activity {
    Button button;
    ListView listView;
    DatabaseHelper databaseHelper;
    userinfo userinfo=new userinfo();
    TextView data;
    AQuery aQuery;
    String url="http://104.155.153.31/PanchviPass/select.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_parsing2);
        aQuery=new AQuery(this);
        button= (Button) findViewById(R.id.fetch);
        listView= (ListView) findViewById(R.id.listview);
       // data= (TextView) findViewById(R.id.data);
        databaseHelper=new DatabaseHelper(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchData();

            }
        });
       // parsData(jsonArray);

    }
    public  void FetchData()
    {
        aQuery.ajax(url, JSONArray.class,new AjaxCallback<JSONArray>(){

            @Override
            public void callback(String url, JSONArray array, AjaxStatus status) {
                super.callback(url, array, status);
                Log.i("data:","response:"+array);
                try {
                    parsData(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void parsData(JSONArray jsonArray) throws JSONException {
        ArrayList list=new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject object=jsonArray.getJSONObject(i);
            list.add(object.getString("questions"));
            list.add(object.getString("ansB"));
        }
        populatedata(list);
    }
    public void populatedata(ArrayList list)
    {

        if(list!=null)
        {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
            listView.setAdapter(adapter);
        }
    }
    }


