package com.bhagya.mapapplication.activityclass;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.app.PendingIntent;
import android.telephony.SmsManager;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class sms_send_recieve extends Activity {
    EditText mobileno,message;
    Button internet_btn,sms_btn;
    String no,user_id;
    AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_send_recieve);
       // mobileno=(EditText)findViewById(R.id.editText1);
        message=(EditText)findViewById(R.id.editText2);
        internet_btn=(Button)findViewById(R.id.internetBtn);
        sms_btn= (Button) findViewById(R.id.sms_btn);
        mobileno= (EditText) findViewById(R.id.mobileno_edittext);
        no=getIntent().getStringExtra("number");
        mobileno.setText(no);
        aQuery=new AQuery(this);
        //Performing action on button click

        internet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INTERNET_CALL","called");
                SharedPreferences preferences = getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                String userId =  preferences.getString("CurrentUser_id","Undefined");
                String msg = message.getText().toString();
                JSONObject msgObj = new JSONObject();
                try {
                    msgObj.put("message",msg);
                    msgObj.put("mobileNo",mobileno.getText().toString());
                    msgObj.put("userId",userId);
                    SocketConnection socketConnection = new SocketConnection();
                    socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
                    socketConnection.rsocket.emit("sendMessage",msgObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sms_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                String mobileNo =  sharedPreferences.getString("CurrentUser_mobile_no","undededfind");
                String msg=message.getText().toString();
                msg="sendsms:"+msg+"@"+mobileno.getText().toString();
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(mobileNo, null, msg, null,null);

            }
        });
    }
}
