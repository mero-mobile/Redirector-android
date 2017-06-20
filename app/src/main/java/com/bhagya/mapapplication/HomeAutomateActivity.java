//package com.bhagya.mapapplication;
//import android.app.Activity;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.telephony.SmsManager;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.google.android.gms.ads.internal.overlay.zzo;
//public class HomeAutomateActivity extends Activity {
//Button dooropen,doorclosed,on,off;
//    ImageView door,bulb;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_automate);
//        door= (ImageView) findViewById(R.id.doorimage);
//        bulb= (ImageView) findViewById(R.id.bulbimage);
//        doorclosed= (Button) findViewById(R.id.doorclosed);
//        dooropen= (Button) findViewById(R.id.dooropen);
//        on= (Button) findViewById(R.id.turnon);
//        off= (Button) findViewById(R.id.turnof);
//        dooropen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1=new Intent(HomeAutomateActivity.this,read_Incomingsms.class);
//                PendingIntent pi=PendingIntent.getActivity(HomeAutomateActivity.this, 0, intent1,0);
//                String mob="9808926955";
//                SmsManager sms=SmsManager.getDefault();
//                sms.sendTextMessage(mob, null,"s1", pi,null);
//                door.setImageResource(R.mipmap.dooropen);
//
//            }
//        });
//        doorclosed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1=new Intent(HomeAutomateActivity.this,read_Incomingsms.class);
//                PendingIntent pi=PendingIntent.getActivity(HomeAutomateActivity.this, 0, intent1,0);
//                String mob="9808926955";
//                SmsManager sms=SmsManager.getDefault();
//                sms.sendTextMessage(mob, null,"s0", pi,null);
//                door.setImageResource(R.mipmap.doorclose);
//            }
//        });
//        on.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1=new Intent(HomeAutomateActivity.this,read_Incomingsms.class);
//           PendingIntent pi=PendingIntent.getActivity(HomeAutomateActivity.this, 0, intent1,0);
//           String mob="9808926955";
//            SmsManager sms=SmsManager.getDefault();
//            sms.sendTextMessage(mob, null,"on", pi,null);
//                bulb.setImageResource(R.mipmap.onbulb);
//            }
//        });
//        off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1=new Intent(HomeAutomateActivity.this,read_Incomingsms.class);
//                PendingIntent pi=PendingIntent.getActivity(HomeAutomateActivity.this, 0, intent1,0);
//                String mob="9808926955";
//                SmsManager sms=SmsManager.getDefault();
//                sms.sendTextMessage(mob, null,"of", pi,null);
//               // bulb.setImageResource(R.mipmap.onbulb);
//                bulb.setImageResource(R.mipmap.ofbulb);
//            }
//        });
//    }
//}
