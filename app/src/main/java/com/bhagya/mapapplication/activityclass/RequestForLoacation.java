package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bhagya.mapapplication.R;

public class RequestForLoacation extends Activity {
   String user_id;
    Button sms,internet_rqst;
    AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        String no=getIntent().getStringExtra("number");
        Toast.makeText(this,no,Toast.LENGTH_LONG).show();
        internet_rqst= (Button) findViewById(R.id.internet_rqst);
        aQuery=new AQuery(this);
      internet_rqst.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(RequestForLoacation.this,"btn clicked",Toast.LENGTH_LONG).show();
          }
      });


        sms= (Button) findViewById(R.id.sms_rqst);
        sms.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent1=new Intent(RequestForLoacation.this,read_Incomingsms.class);
              PendingIntent pi=PendingIntent.getActivity(RequestForLoacation.this, 0, intent1,0);
              String mob="9801074961";//9846913433
              SmsManager sms=SmsManager.getDefault();
              sms.sendTextMessage(mob, null,"getlocation:test", pi,null);
          }
      });



    }
}
