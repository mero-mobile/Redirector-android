package com.bhagya.mapapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class subSelectActivity extends Activity {
Button gk,science,math;
    Animation rotate,scale;
    LinearLayout sublect;
    ImageView pencilup,pencildown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);
        scale=AnimationUtils.loadAnimation(this,R.anim.scale);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sub_select);

sublect= (LinearLayout) findViewById(R.id.activity_sub_select);
        rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);
        scale=AnimationUtils.loadAnimation(this,R.anim.scale);

        science= (Button) findViewById(R.id.science);
        math= (Button) findViewById(R.id.math);
        gk= (Button) findViewById(R.id.gk);
        pencilup= (ImageView) findViewById(R.id.pencil1);
        pencildown= (ImageView) findViewById(R.id.pencil3);
        gk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(subSelectActivity.this,AnswerQuestion1.class);
                startActivity(intent);
            }
        });
        sublect.startAnimation(scale);

        sublect.postDelayed(new Runnable() {
            @Override
            public void run() {
                pencildown.setVisibility(View.VISIBLE);
               gk.setVisibility(View.VISIBLE);
                science.setVisibility(View.VISIBLE);
                math.setVisibility(View.VISIBLE);
                math.startAnimation(scale);
                science.startAnimation(scale);
                gk.startAnimation(scale);
                pencilup.startAnimation(rotate);
                pencildown.startAnimation(rotate);



            }
        },600);

    }
}
