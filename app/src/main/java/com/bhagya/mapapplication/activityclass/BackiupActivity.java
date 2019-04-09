package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bhagya.mapapplication.R;

public class BackiupActivity extends Activity {
Button backup,mainmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backiup);
        backup= (Button) findViewById(R.id.backup);
        mainmenu= (Button) findViewById(R.id.mainmenu);
        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BackiupActivity.this,homePage.class);
                startActivity(intent);
            }
        });

backup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(BackiupActivity.this,Postdata.class);
        startActivity(intent);
    }
});
    }
}
