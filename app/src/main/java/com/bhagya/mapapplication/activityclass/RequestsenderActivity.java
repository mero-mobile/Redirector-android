package com.bhagya.mapapplication.activityclass;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.socket.IncomingMessageHandler;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestsenderActivity extends AppCompatActivity {
    Button sms,internet;
    String user_id,user_name,mobile_no;
    Context context;
    AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestsender);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String no=getIntent().getStringExtra("number");
        Toast.makeText(this,no,Toast.LENGTH_LONG).show();
        sms= (Button) findViewById(R.id.sms_rqst);
        context = this;
        Intent getnumberintent=getIntent();
        mobile_no=getnumberintent.getStringExtra("number");
        Toast.makeText(RequestsenderActivity.this,mobile_no,Toast.LENGTH_LONG).show();
        internet= (Button) findViewById(R.id.internet_forqst);
        aQuery=new AQuery(this);

        internet.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)

            @Override
            public void onClick(View v) {
                Toast.makeText(RequestsenderActivity.this,"Remote mobile no"+mobile_no,Toast.LENGTH_LONG).show();
                SharedPreferences prfs = getSharedPreferences("CURRENT_USER_RECORD", MODE_PRIVATE);
                user_id = prfs.getString("CurrentUser_id", "Undefind");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userId",user_id);
                    jsonObject.put("mobileNo",mobile_no);
                    SocketConnection connection = new SocketConnection();
                    connection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
                    connection.rsocket.emit("sendLocationRequest",jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prfs = getSharedPreferences("new_userid", MODE_PRIVATE);
                user_id = prfs.getString("user_id", "");
                user_name=prfs.getString("user_name","");
                Toast.makeText(RequestsenderActivity.this,user_name,Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(RequestsenderActivity.this,read_Incomingsms.class);
                PendingIntent pi=PendingIntent.getActivity(RequestsenderActivity.this, 0, intent1,0);
                String mob="9801074961";//9846913433
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(mobile_no, null,"getlocation:"+user_name, pi,null);
            }
        });
    }
}
