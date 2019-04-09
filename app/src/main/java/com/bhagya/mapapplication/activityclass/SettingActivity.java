package com.bhagya.mapapplication.activityclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.bhagya.mapapplication.R;

public class SettingActivity extends AppCompatActivity {
LinearLayout mobile_registration_layout,manual_backup_layout,auto_ackup_layout,remote_access_layout
    ,create_friend_circle_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mobile_registration_layout= (LinearLayout) findViewById(R.id.mob_registration_layout);
        manual_backup_layout= (LinearLayout) findViewById(R.id.manualbakup_layout);
        auto_ackup_layout= (LinearLayout) findViewById(R.id.auto_backup);
        remote_access_layout= (LinearLayout) findViewById(R.id.remote_access_layout);
        create_friend_circle_layout= (LinearLayout) findViewById(R.id.friend_circle_layout);
        mobile_registration_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mobregIntent=new Intent(SettingActivity.this,Mobile_NoRegisterActivity.class);
                startActivity(mobregIntent);
            }
        });
        manual_backup_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent manualbackupIntent=new Intent(SettingActivity.this,ManaulBackupActivity.class);
                startActivity(manualbackupIntent);
            }
        });
        auto_ackup_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent automaticbackup =new Intent(SettingActivity.this,AutoBackupActivity.class);
                startActivity(automaticbackup);
            }
        });
        remote_access_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        create_friend_circle_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
