package com.bhagya.mapapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class homePage extends Activity {
Button call,message,homeauto,mmc,pcauto,contacts,quiz,facebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
       // call= (Button) findViewById(R.id.call);
        message= (Button) findViewById(R.id.inoutsms);
        //pcauto= (Button) findViewById(R.id.PCcontrol);
        contacts= (Button) findViewById(R.id.contacts);
       // homeauto= (Button) findViewById(R.id.hauto);
        mmc= (Button) findViewById(R.id.Computer);
        quiz= (Button) findViewById(R.id.Gaming);
      //  facebook= (Button) findViewById(R.id.facebook);


        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,AnswerQuestion1.class);
                startActivity(intent);

//                    Intent i = homePage.this.getPackageManager().getLaunchIntentForPackage("package com.example.bhagya.kbc");
//                    homePage.this.startActivity(i);


            }
        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,readWebContact.class);
                startActivity(intent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,PullWebdata.class);
                startActivity(intent);
            }
        });

//      homeauto.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Intent intent=new Intent(homePage.this,HomeAutomateActivity.class);
//        startActivity(intent);
//
//    }
//});
        mmc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homePage.this,MasterActivity.class);
                startActivity(intent);

            }
        });




    }
}
