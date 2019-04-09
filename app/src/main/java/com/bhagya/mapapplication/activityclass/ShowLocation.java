//package com.bhagya.mapapplication;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.androidquery.AQuery;
//import com.androidquery.callback.AjaxCallback;
//import com.androidquery.callback.AjaxStatus;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class ShowLocation extends Activity {
//TextView latitude,longitude;
//    Button showmap;
//    AQuery aQuery;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
 //      setContentView(R.layout.activity_show_location);
//        latitude= (TextView) findViewById(R.id.latitude);
//        longitude= (TextView) findViewById(R.id.longitude);
//        showmap= (Button) findViewById(R.id.showonmap);
//        aQuery=new AQuery(this);
//        fetchData();
//        showmap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ShowLocation.this,MapsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        String no=getIntent().getStringExtra("number");
//        Toast.makeText(this,no,Toast.LENGTH_LONG).show();
//    }
//    public void fetchData() {
//        //String url="http://192.168.1.16/PanchviPass/select.php";
//        String url="http://104.155.153.31/meromobile.com/selectgps.php";
//        aQuery.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
//            @Override
//            public void callback(String url, JSONArray array, AjaxStatus status) {
//                super.callback(url, array, status);
//                parseData(array);
//                Log.i("response", "REsponser:" + array);
//            }
//        });
//    }
//    public void parseData(JSONArray array) {
//
//        //  ArrayList<userinfo> list = new ArrayList<userinfo>();
//
//        // for (int i = 0; i < array.length(); i++) {
//        try {
//            JSONObject obj = array.getJSONObject(array.length()-1);
//            String latitudeValue,longitudeValue;
//            //  userinfo info = new userinfo();
//            latitudeValue= obj.getString("latitude");
//            longitudeValue = obj.getString("longitude");
//            // info.email = obj.getString("email");
//            //  list.add(info);
//            latitude.setText(latitude.getText().toString()+latitudeValue);
//            longitude.setText(longitude.getText().toString()+longitudeValue);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        // populateData(list);
//    }
//}
