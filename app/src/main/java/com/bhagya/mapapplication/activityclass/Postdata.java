package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bhagya.mapapplication.R;

public class Postdata extends Activity {
Button contacts,inbox,outbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdata);
        contacts= (Button) findViewById(R.id.phonecontact);
        inbox= (Button) findViewById(R.id.inbox);
        outbox= (Button) findViewById(R.id.outbox);

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Postdata.this,ReadContactList.class);
                startActivity(intent);
            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Postdata.this,Readmobile_Inbox.class);
                startActivity(intent);
            }
        });
        outbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Postdata.this,Readoutbox.class);
                startActivity(intent);
            }
        });
    }
}
