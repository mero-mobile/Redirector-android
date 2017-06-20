package com.bhagya.mapapplication;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class MainActivity extends Activity {
TextView easy,medium,exit,admin;
        LinearLayout mainpage;
        Animation rotate,scale;
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            final Button help,singleplayer,multiplayer,exit;
            final ImageView sound;
            // Set window fullscreen and r  emove title bar, and force landscape orientation
            //// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.home_page);
            mainpage= (LinearLayout) findViewById(R.id.mainpage);

            rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);
            scale=AnimationUtils.loadAnimation(this,R.anim.scale);
            help= (Button) findViewById(R.id.help);
            sound= (ImageView) findViewById(R.id.sound);
            singleplayer= (Button) findViewById(R.id.singleplayer);
            multiplayer= (Button) findViewById(R.id.multiplayer);
        exit= (Button) findViewById(R.id.exit);
        singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,subSelectActivity.class);
                startActivity(intent);
            }
        });
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MultiPlayerActivity.class);
                startActivity(intent);

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createDatabase();

        mainpage.startAnimation(scale);
        mainpage.postDelayed(new Runnable() {
            @Override
            public void run() {
               help.setVisibility(View.VISIBLE);
                sound.setVisibility(View.VISIBLE);
                singleplayer.setVisibility(View.VISIBLE);
                multiplayer.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);
                help.startAnimation(scale);
                exit.startAnimation(scale);
                sound.startAnimation(scale);
                singleplayer.startAnimation(scale);
                multiplayer.startAnimation(scale);
            }
        },700);


    }

    private void createDatabase() {
        try {
            boolean dbExist = checkDataBaseExit();
            System.out.println("database" + dbExist);
            if (!dbExist) {
                String myDbDir = "/data/data/" + getPackageName() + "/databases";
                (new File(myDbDir)).mkdir();

                OutputStream dos = new FileOutputStream("/data/data/" + getPackageName() + "/databases/" + DatabaseHelper.dbname);
                InputStream dis = getResources().openRawResource(R.raw.pass);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = dis.read(buffer)) > 0) {
                    dos.write(buffer);
                }
                dos.flush();
                dos.close();
                dis.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Checks whether Database already exists
     */
    private boolean checkDataBaseExit() {
        System.out.println("rrr");
        SQLiteDatabase checkDB = null;

        try {
            String myPath = "/data/data/" + getPackageName() + "/databases/" + DatabaseHelper.dbname;

            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }


}

