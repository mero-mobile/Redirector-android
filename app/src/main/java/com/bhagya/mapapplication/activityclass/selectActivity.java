package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bhagya.mapapplication.R;

public class selectActivity extends Activity {
Button call,sms,incallread,insmsread,readinbox,shutdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        call= (Button) findViewById(R.id.call);
        sms= (Button) findViewById(R.id.sms);
        incallread= (Button) findViewById(R.id.readcallno);
        insmsread= (Button) findViewById(R.id.readsms);
        readinbox= (Button) findViewById(R.id.readInbox);
        shutdown= (Button) findViewById(R.id.shutdown);

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectActivity.this,sms_send_recieve.class);
                startActivity(intent);
            }
        });
        incallread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectActivity.this,Newincoming_callrecieve.class);
                startActivity(intent);
            }
        });
        insmsread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(selectActivity.this,read_Incomingsms.class);
              //  startActivity(intent);
            }
        });

        readinbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectActivity.this,Readmobile_Inbox.class);
                startActivity(intent);
            }
        });
      shutdown.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try {
                  Process proc = Runtime.getRuntime()
                          .exec(new String[]{ "su", "-c", "reboot -p" });
                  proc.waitFor();
              } catch (Exception ex) {
                  ex.printStackTrace();
              }
          }
      });

        findViewById(R.id.readphonecontact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectActivity.this,ReadContactList.class);
                startActivity(intent);
            }
        });

    }
}
