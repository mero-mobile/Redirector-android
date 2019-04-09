package com.bhagya.mapapplication.fragmentactivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.activityclass.homePage;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Bhagya on 7/3/2017.
 */

public class SendSmsRandomlyFragment extends Fragment {
    String  no1;
    EditText mobileno,message;
    Button internet_btn,sms_btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_sms_send_recieve,null);
        message=(EditText)view.findViewById(R.id.editText2);
        internet_btn=(Button)view.findViewById(R.id.internetBtn);
        sms_btn= (Button) view.findViewById(R.id.sms_btn);
        mobileno= (EditText) view.findViewById(R.id.mobileno_edittext);
        no1=getActivity().getIntent().getStringExtra("number");
        Toast.makeText(getActivity(),no1,Toast.LENGTH_LONG).show();
        mobileno.setText(no1);

        internet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INTERNET_CALL","called");
                SharedPreferences preferences = getActivity().getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                String userId =  preferences.getString("CurrentUser_id","Undefined");
                String no= mobileno.getText().toString();
                String msg=message.getText().toString();
                JSONObject msgObj = new JSONObject();

                try {
                    msgObj.put("message",msg);
                    msgObj.put("mobileNo",no);
                    msgObj.put("userId",userId);
                    SocketConnection socketConnection = new SocketConnection();
                    socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
                    socketConnection.rsocket.emit("sendMessage",msgObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   String no=getIntent().getStringExtra("number");

                String no="9801074961";
                String msg=message.getText().toString();
                msg=msg+":"+no1;
                //Getting intent and PendingIntent instance
                Intent intent=new Intent(getActivity().getApplicationContext(),homePage.class);
                PendingIntent pi=PendingIntent.getActivity(getActivity(), 0, intent,0);
                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(no, null, msg, pi,null);
                Toast.makeText(getActivity(), "Message Sent successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
