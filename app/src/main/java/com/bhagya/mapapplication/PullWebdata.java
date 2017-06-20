package com.bhagya.mapapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PullWebdata extends Activity {
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
                Intent intent=new Intent(PullWebdata.this,readWebContact.class);
                startActivity(intent);
            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PullWebdata.this,readWebInbox.class);
                startActivity(intent);
            }
        });
        outbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PullWebdata.this,readWeboutbox.class);
                startActivity(intent);
            }
        });
    }
}
