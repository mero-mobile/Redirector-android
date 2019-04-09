package com.bhagya.mapapplication.activityclass;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.jobschedular.JobschedulerForUpdateData;

//import com.bhagya.mapapplication.jobschedular.JobschedulerForUpdateData;
public class AutoBackupActivity extends AppCompatActivity {
CheckBox wificontact,mobiledatacontact,wifiinbox,mobiledatainbox,wifioutbox,mobiledataoutbox
        ,wificallhistory,mobiledatacallhistory;
        DatabaseHelper databaseHelper;
        private  static final int JOB_ID=101;
        private JobScheduler jobScheduler;
        private JobInfo jobInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wificontact= (CheckBox) findViewById(R.id.wifi_checkcontacts);
        mobiledatacontact= (CheckBox) findViewById(R.id.mobiledata_contacts);
        wificallhistory= (CheckBox) findViewById(R.id.wifi_callhistorychek);
        mobiledatacallhistory= (CheckBox) findViewById(R.id.mobiledata_callhistorychek);
        wifiinbox= (CheckBox) findViewById(R.id.wifi_inboxchek);
        mobiledatainbox= (CheckBox) findViewById(R.id.mobiledata_inboxchek);
        wifioutbox= (CheckBox) findViewById(R.id.wifi_outboxcheck);
        mobiledataoutbox= (CheckBox) findViewById(R.id.mobiledata_outboxcheck);
        databaseHelper=new DatabaseHelper(this);


//        private static final int JOB_ID = 1001;
//        private static final long REFRESH_INTERVAL  = 5 * 1000; // 5 seconds
//
//        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, serviceName)
//                .setPeriodic(REFRESH_INTERVAL)
//                .setExtras(bundle).build();
//

        ComponentName componentName=new ComponentName(this, JobschedulerForUpdateData.class);
//        JobInfo.Builder builder= new JobInfo.Builder(JOB_ID,componentName);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,componentName);
            builder.setPeriodic(10000);
            builder.setMinimumLatency(10000);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setPersisted(true);//job continue even if system is reboot;
            jobInfo=builder.build();
            jobScheduler= (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(jobInfo);
        }
//...............set the all checkbox by default settings.......................................
        int a=1;
      //  DefaultSettingsData defaultSettingsData=databaseHelper.getSettings(a);
//         Log.d("RETURNSETING", "data"+String.valueOf(defaultSettingsData.wifi_callhistory));
//        Toast.makeText(this,defaultSettingsData.wifi_contacts,Toast.LENGTH_LONG).show();

//         if (defaultSettingsData.wifi_contacts==1){
//             wificontact.setChecked(true);
//         }
//         else wificontact.setChecked(false);
//        if(defaultSettingsData.wifi_callhistory==1){
//            wificallhistory.setChecked(true);
//        }
//        else wificallhistory.setChecked(false);
//        if (defaultSettingsData.wifi_inbox==1){
//            wifiinbox.setChecked(true);
//        }
//        else wifiinbox.setChecked(false);
//        if (defaultSettingsData.wifi_outbox==1){
//            wifioutbox.setChecked(true);
//        }
//        else wifioutbox.setChecked(false);
//        if (defaultSettingsData.mdata_contacts==1){
//            mobiledatacontact.setChecked(true);
//        }
//        else mobiledatacontact.setChecked(false);
//        if (defaultSettingsData.mdata_callhistory==1)
//        {
//            mobiledatacallhistory.setChecked(true);
//        }
//        else mobiledatacallhistory.setChecked(false);
//        if (defaultSettingsData.mdata_inbox==1){
//            mobiledatainbox.setChecked(true);
//        }else mobiledatainbox.setChecked(false);
//        if (defaultSettingsData.mdata_outbox==1){
//            mobiledataoutbox.setChecked(true);
//        }else mobiledataoutbox.setChecked(false);

//.................click listner for all checkbox for update settings................................................

         wificontact.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
              boolean checked=wificontact.isChecked();
              if(checked){
                  jobScheduler.schedule(jobInfo);
                 // jobScheduler.schedule(jobInfo);
                  Toast toast=Toast.makeText(AutoBackupActivity.this,"BackGround job started",Toast.LENGTH_LONG);
                  toast.show();
               databaseHelper.updateSettings("wifi_contacts",1);

              }
              else {
                  jobScheduler.cancel(JOB_ID);
                  databaseHelper.updateSettings("wifi_contacts",0);

                  Toast toast=Toast.makeText(AutoBackupActivity.this,"Background job stoped",Toast.LENGTH_LONG);
                  toast.show();
              }
          }
      });

        wificallhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked=wificallhistory.isChecked();
                if(checked){
                    databaseHelper.updateSettings("wifi_callhistory",1);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"checked",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    databaseHelper.updateSettings("wifi_callhistory",0);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"Unchecked",Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        wifiinbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean checked=wifiinbox.isChecked();
                if(checked){
                    databaseHelper.updateSettings("wifi_inbox",1);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"checked",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    databaseHelper.updateSettings("wifi_inbox",0);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"Unchecked",Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        wifioutbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean checked=wifioutbox.isChecked();
                if(checked){
                    databaseHelper.updateSettings("wifi_outbox",1);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"checked",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    databaseHelper.updateSettings("wifi_outbox",0);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"Unchecked",Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        mobiledatacontact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean checked=mobiledatacontact.isChecked();
                if(checked){
                    databaseHelper.updateSettings("mdata_contacts",1);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"checked",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    databaseHelper.updateSettings("mdata_contacts",0);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"Unchecked",Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });
        mobiledatacallhistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean checked=mobiledatacallhistory.isChecked();
                if(checked){
                    databaseHelper.updateSettings("mdata_callhistory",1);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"checked",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    databaseHelper.updateSettings("mdata_callhistory",0);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"Unchecked",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        mobiledatainbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean checked=mobiledatainbox.isChecked();
                if(checked){
                    databaseHelper.updateSettings("mdata_inbox",1);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"checked",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    databaseHelper.updateSettings("mdata_inbox",0);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"Unchecked",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        mobiledataoutbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked=mobiledataoutbox.isChecked();
                if(checked){
                    databaseHelper.updateSettings("mdata_outbox",1);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"checked",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    databaseHelper.updateSettings("mdata_outbox",0);
                    Toast toast=Toast.makeText(AutoBackupActivity.this,"Unchecked",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

