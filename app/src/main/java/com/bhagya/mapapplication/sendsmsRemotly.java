package com.bhagya.mapapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sendsmsRemotly extends Activity {
    Button send;
    EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsms_remotly);
        send= (Button) findViewById(R.id.smsbuton);
        message= (EditText) findViewById(R.id.messege);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageValue=message.getText().toString();
                Intent intent1=new Intent(sendsmsRemotly.this,read_Incomingsms.class);
                PendingIntent pi=PendingIntent.getActivity(sendsmsRemotly.this, 0, intent1,0);
                String mob="9801074961";//9846913433
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(mob, null,messageValue, pi,null);
            }
        });
    }
}
