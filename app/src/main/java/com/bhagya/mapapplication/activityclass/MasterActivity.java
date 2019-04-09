package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

import com.bhagya.mapapplication.R;

public class MasterActivity extends Activity {
Button gpslocation,ringphone,sutdownphone,turnonwifi,getlocationoffline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
       // gpslocation= (Button) findViewById(R.id.gpslocation);
        ringphone= (Button) findViewById(R.id.Ringphone);
        sutdownphone= (Button) findViewById(R.id.Shutdown);
        turnonwifi= (Button) findViewById(R.id.turnonwifi);

        ringphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent1=new Intent(MasterActivity.this,read_Incomingsms.class);
                PendingIntent pi=PendingIntent.getActivity(MasterActivity.this, 0, intent1,0);
                String mob="9801074961";//9846913433
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(mob, null,"playmp3:test", pi,null);
            }
        });
        sutdownphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MasterActivity.this,read_Incomingsms.class);
                PendingIntent pi=PendingIntent.getActivity(MasterActivity.this, 0, intent1,0);
                String mob="9801074961";//9846913433
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(mob, null,"shutdown:test", pi,null);
            }
        });

        turnonwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MasterActivity.this,read_Incomingsms.class);
                PendingIntent pi=PendingIntent.getActivity(MasterActivity.this, 0, intent1,0);
                String mob="9801074961";//9846913433
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(mob, null,"turnonwifi:test", pi,null);
            }
        });

    }
}




















//getlocationoffline= (Button) findViewById(R.id.getlocation);
//        getlocationoffline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent1=new Intent(MasterActivity.this,read_Incomingsms.class);
//                PendingIntent pi=PendingIntent.getActivity(MasterActivity.this, 0, intent1,0);
//                String mob="9801074961";//9846913433
//                SmsManager sms=SmsManager.getDefault();
//                sms.sendTextMessage(mob, null,"getlocation:test", pi,null);
//
//            }
//        });


//        gpslocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MasterActivity.this,ShowLocation.class);
//                startActivity(intent);
//            }
//        });

