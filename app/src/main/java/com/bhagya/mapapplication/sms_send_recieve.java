package com.bhagya.mapapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class sms_send_recieve extends Activity {
EditText mobileno,message;
    Button sendsms,sendsmsformmy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_send_recieve);
       // mobileno=(EditText)findViewById(R.id.editText1);
        message=(EditText)findViewById(R.id.editText2);
        sendsms=(Button)findViewById(R.id.button1);
        sendsmsformmy= (Button) findViewById(R.id.sentfrommy);

        //Performing action on button click
        sendsms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String no=getIntent().getStringExtra("number");
                String msg=message.getText().toString();

                //Getting intent and PendingIntent instance
               Intent intent=new Intent(getApplicationContext(),homePage.class);
                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(no, null, msg, pi,null);
                Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });
        sendsmsformmy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             //   String no=getIntent().getStringExtra("number");
                String no1=getIntent().getStringExtra("number");
                String no="9801074961";
                String msg=message.getText().toString();
                msg=msg+":"+no1;
                //Getting intent and PendingIntent instance
                Intent intent=new Intent(getApplicationContext(),homePage.class);
                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(no, null, msg, pi,null);
                Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });

}}
