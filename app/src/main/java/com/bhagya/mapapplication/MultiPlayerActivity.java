package com.bhagya.mapapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MultiPlayerActivity extends Activity {
    TextView question, ans_a, conter, ans_b, ans_c, ans_d,player1,player2,p1score,p2score;
    Button pass;
    boolean p1flag=false,p2flag=false;

    Animation scale, rotate, alpha, scalefortakjhak, animforbachao;
    DatabaseHelper dbhelper;
    MediaPlayer player, rightmusic, worngmusic;
    CountDownTime timmer = new CountDownTime(31000, 1000);
    String questionVlaue, ansAvalue, ansBvalue, ansCvalue, ansDvalue, CorrectAns;

    fiftyfityclass fiftyfiftymethod;
    LinearLayout ansqsnpage, customdialoge;
    Random random = new Random();
    int randomid = random.nextInt(16) + 1;
    ContentValues cv = new ContentValues();
    int p1counter=0,p2counter=0;
    int passcheck=0;

    int count = 1;

    //  ArrayList list=new ArrayList();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_multi_player);
        player = MediaPlayer.create(this, R.raw.tick);
        rightmusic = MediaPlayer.create(this, R.raw.rightanswer);
        worngmusic = MediaPlayer.create(this, R.raw.wronganswer);
        timeupActivity timeupActivity;
        dbhelper = new DatabaseHelper(this);
        cv.put("randomid", randomid);
        dbhelper.InsertRandomid(cv);

        //  list=dbhelper.getListofRandomid();
        // timmer.start();
        // player.start();
        // player.setLooping(true);
//        timmer();
//finding view of all xml components.................................................................................
        pass= (Button) findViewById(R.id.pass);
        player1= (TextView) findViewById(R.id.p1);
        player2= (TextView) findViewById(R.id.p2);
        p1score= (TextView) findViewById(R.id.p1score);
        p2score= (TextView) findViewById(R.id.p2score);
        customdialoge = (LinearLayout) findViewById(R.id.customdialoge);
        ansqsnpage = (LinearLayout) findViewById(R.id.ansqsnpage);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);


        scalefortakjhak = AnimationUtils.loadAnimation(this, R.anim.scalefortakjhak);
        animforbachao = AnimationUtils.loadAnimation(this, R.anim.animforbachao);
        ans_a = (TextView) findViewById(R.id.ans_a);
        ans_b = (TextView) findViewById(R.id.ans_b);
        ans_c = (TextView) findViewById(R.id.ans_c);
        ans_d = (TextView) findViewById(R.id.ans_d);
        question = (TextView) findViewById(R.id.question);
        conter = (TextView) findViewById(R.id.timmer);
        //  reward1=(TextView) findViewById(R.id.score);


        ansqsnpage.startAnimation(scale);


        //first question set on the option bar..............................
        question.setVisibility(View.VISIBLE);
        ans_a.setVisibility(View.VISIBLE);
        ans_b.setVisibility(View.VISIBLE);
        ans_c.setVisibility(View.VISIBLE);
        ans_d.setVisibility(View.VISIBLE);

        question.setText(" Q. ");

        ans_a.setText("  A. ");

        ans_b.setText("  B. ");

        ans_c.setText("  C. ");

        ans_d.setText("  D. ");


//click listener of all option bar..........................................................................................
        ans_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog("A");


            }
        });

        ans_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog("B");


            }
        });
        ans_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog("C");

            }
        });
        ans_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog("D");

            }
        });
//...................................................................................................................................
pass.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dbhelper.resetdata();
        timmer.cancel();
        timmer.start();
        ++passcheck;
        if(passcheck==1) {

            if (p1flag) {

                // p1score.setText("" + ++p1counter);
                p1score.setBackgroundResource(R.drawable.main_option_background);
                player1.setBackgroundResource(R.drawable.ans_question_background);
                p2score.setBackgroundResource(R.drawable.p_activebackground);
                player2.setBackgroundResource(R.drawable.multiscore_background1);
                p2flag = true;
                p1flag = false;

            } else {
                // p2score.setText("" + ++p2counter);
                p2score.setBackgroundResource(R.drawable.main_option_background);
                player2.setBackgroundResource(R.drawable.ans_question_background);
                p1score.setBackgroundResource(R.drawable.p_activebackground);
                player1.setBackgroundResource(R.drawable.multiscore_background1);
                p1flag = true;
                p2flag = false;

            }

        }
        if(passcheck==2) {
            WrongAnswer();
            if (p1flag) {

                // p1score.setText("" + ++p1counter);
                p1score.setBackgroundResource(R.drawable.main_option_background);
                player1.setBackgroundResource(R.drawable.ans_question_background);
                p2score.setBackgroundResource(R.drawable.p_activebackground);
                player2.setBackgroundResource(R.drawable.multiscore_background1);
                p2flag = true;
                p1flag = false;

            } else {
                // p2score.setText("" + ++p2counter);
                p2score.setBackgroundResource(R.drawable.main_option_background);
                player2.setBackgroundResource(R.drawable.ans_question_background);
                p1score.setBackgroundResource(R.drawable.p_activebackground);
                player1.setBackgroundResource(R.drawable.multiscore_background1);
                p1flag = true;
                p2flag = false;

            }
            ArrayList<Integer> list = dbhelper.getListofRandomid();
            Iterator itr = list.iterator();
            randomid = random.nextInt(17) + 1;
            boolean check = list.contains(randomid);
            Log.i("flag", String.valueOf(check));
            Log.d("listVlaue", list.toString());


            while (check) {
                randomid = random.nextInt(17) + 1;
                check = list.contains(randomid);
            }
            ContentValues cv = new ContentValues();
            cv.put("randomid", randomid);
            dbhelper.InsertRandomid(cv);
           // count++;
            showdata(randomid, count);
        }

    }
});
        timeSart();

    }

    //Scoring and change question method.....................................................................................................
    public qsn_ans_data showdata(int id, int counter) {
        passcheck=0;
if(counter==17)
{
    timeUpdialoge();
}
        ans_a.setBackgroundResource(R.drawable.ans_question_background);
        ans_b.setBackgroundResource(R.drawable.ans_question_background);
        ans_c.setBackgroundResource(R.drawable.ans_question_background);
        ans_d.setBackgroundResource(R.drawable.ans_question_background);
        ansqsnpage.postDelayed(new Runnable() {
            @Override
            public void run() {
                question.startAnimation(scale);
                ans_a.startAnimation(scale);
                ans_b.startAnimation(scale);
                ans_c.startAnimation(scale);
                ans_d.startAnimation(scale);
            }
        }, 0);
        qsn_ans_data data = dbhelper.getdata(id);
        questionVlaue = data.questions;
        question.setText("  Q. " + data.questions);
        ansAvalue = data.ansA;
        ans_a.setText("  A. " + ansAvalue);
        ansBvalue = data.ansB;
        ans_b.setText("  B. " + data.ansB);
        ansCvalue = data.ansC;
        ans_c.setText("  C. " + data.ansC);
        ansDvalue = data.ansD;
        ans_d.setText("  D. " + data.ansD);


        return data;

    }

    //fiftyfifty option method...........................................................................................................

//CUSTOM DIALOG METHOD...................................................................................................................

    public void customDialog(final String opt) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.customdailogue, null);
dialog.setCancelable(false);
        // final EditText value= (EditText) view.findViewById(R.id.);
        Button yes, no;
        // TextView message= (TextView) view.findViewById(R.id.massage);
        yes = (Button) view.findViewById(R.id.yes);
        no = (Button) view.findViewById(R.id.no);
        // customdialoge.setAnimation(rotate);
        no.setAnimation(rotate);
        yes.setAnimation(rotate);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //   String finalValue=value.getText().toString();
                //   toastMessage(finalValue);

                switch (opt) {

                    case "A":

                        qsn_ans_data data = dbhelper.getdata(randomid);
                        if (ansAvalue.equals(data.correctAns)) {


                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            Iterator itr = list.iterator();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue", list.toString());


                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }


                            count++;
                            if(p1flag) {

                                p1score.setText("" + ++p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else{
                                p2score.setText("" + ++p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }


                            rightmusic.start();
                            //  randomid=random.nextInt(16)+1;
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid, count);

                        } else {
                            // worngmusic.start();
                            // Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                            WrongAnswer();

                            if(p1flag) {

                                p1score.setText("" + --p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else{
                                p2score.setText("" + --p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }
                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            Iterator itr = list.iterator();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue", list.toString());


                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            count++;
                            showdata(randomid, count);



                        }
                        break;
                    case "B":
                        qsn_ans_data data1 = dbhelper.getdata(randomid);
                        if (ansBvalue.equals(data1.correctAns)) {
                            //timmer.cancel();
                            //timmer = new CountDownTime(30000, 1000);

                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Iterator itr = list.iterator();
                            Log.d("listVlaue", list.toString());


                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }
                            count++;


                            if(p1flag) {

                                p1score.setText("" + ++p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else {
                                p2score.setText("" + ++p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }

                            rightmusic.start();
                            // randomid=random.nextInt(16)+1;
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid, count);
                        } else {
                            WrongAnswer();
                            // worngmusic.start();
                            // Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();

                            if(p1flag) {

                                p1score.setText("" + --p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else{
                                p2score.setText("" + --p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }

                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            Iterator itr = list.iterator();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue", list.toString());


                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            count++;
                            showdata(randomid, count);

                        }

                        break;
                    case "C":
                        qsn_ans_data data2 = dbhelper.getdata(randomid);
                        if (ansCvalue.equals(data2.correctAns)) {
                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            Iterator itr = list.iterator();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue", list.toString());


                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }
                            count++;

                            if(p1flag) {

                                p1score.setText("" + ++p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else {
                                p2score.setText("" + ++p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }

                            rightmusic.start();
                            //randomid=random.nextInt(16)+1;
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid, count);

                        } else {
                            WrongAnswer();

                            //worngmusic.start();
                            //Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();

                            if(p1flag) {

                                p1score.setText("" + --p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else{
                                p2score.setText("" + --p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }

                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            Iterator itr = list.iterator();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue", list.toString());


                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            count++;
                            showdata(randomid, count);


                        }


                        break;
                    case "D":
                        qsn_ans_data data3 = dbhelper.getdata(randomid);
                        if (ansDvalue.equals(data3.correctAns)) {
                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);

                            Iterator itr = list.iterator();
                            Log.d("listVlaue", list.toString());


                            Log.i("flag", String.valueOf(check));
                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }
                            count++;

                            if(p1flag) {

                                p1score.setText("" + ++p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else {
                                p2score.setText("" + ++p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }

                            //rightmusic.start();
                            //randomid=random.nextInt(16)+1;
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid, count);


                        } else {
                            //worngmusic.start();
                            // Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                              WrongAnswer();

                            if(p1flag) {

                                p1score.setText("" + --p1counter);
                                p1score.setBackgroundResource(R.drawable.main_option_background);
                                player1.setBackgroundResource(R.drawable.ans_question_background);
                                p2score.setBackgroundResource(R.drawable.p_activebackground);
                                player2.setBackgroundResource(R.drawable.multiscore_background1);
                                p2flag=true;
                                p1flag=false;
                            }
                            else{
                                p2score.setText("" + --p2counter);
                                p2score.setBackgroundResource(R.drawable.main_option_background);
                                player2.setBackgroundResource(R.drawable.ans_question_background);
                                p1score.setBackgroundResource(R.drawable.p_activebackground);
                                player1.setBackgroundResource(R.drawable.multiscore_background1);
                                p1flag=true;
                                p2flag=false;

                            }


                            ArrayList<Integer> list = dbhelper.getListofRandomid();
                            Iterator itr = list.iterator();
                            randomid = random.nextInt(17) + 1;
                            boolean check = list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue", list.toString());


                            while (check) {
                                randomid = random.nextInt(17) + 1;
                                check = list.contains(randomid);
                            }
                            ContentValues cv = new ContentValues();
                            cv.put("randomid", randomid);
                            dbhelper.InsertRandomid(cv);
                            count++;
                            showdata(randomid, count);

                        }


                        break;


                }
                timmer.cancel();

                timmer = new CountDownTime(30000, 1000);
                timmer.start();

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
//        dialog.setTitle("Are you Sure ?");
        dialog.show();

    }

    public void timeUpdialoge() {
        TextView timeup, Rs;
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.timeupdialoge, null);
        Rs = (TextView) view.findViewById(R.id.Rs);
        // final EditText value= (EditText) view.findViewById(R.id.);
//        if(count<4){
//            Rs.setText("You Won Rs-0");
//        }
        if (p1counter>p2counter) {
            Rs.setText("P1 is Win");
        }
        else if (p1counter==p2counter){
            Rs.setText("Both are well Played");
        }
        else
        {
            Rs.setText("P2 is Win");
        }


        Button yes, no;
        // TextView message= (TextView) view.findViewById(R.id.massage);
        yes = (Button) view.findViewById(R.id.yes);
        no = (Button) view.findViewById(R.id.no);
        timeup = (TextView) view.findViewById(R.id.timeup);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiPlayerActivity.this, MultiPlayerActivity.class);
                startActivity(intent);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        // customdialoge.setAnimation(rotate);
        no.setAnimation(rotate);
        yes.setAnimation(rotate);
        dialog.setContentView(view);
        // dialog.setTitle(" ");
        dialog.show();
    }

    public void WrongAnswer() {
        TextView timeup, Rs;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.multiplayer_wronganswer, null);
        Rs = (TextView) view.findViewById(R.id.Rs);
        // final EditText value= (EditText) view.findViewById(R.id.);
        qsn_ans_data data = dbhelper.getdata(randomid);

        Rs.setText(""+data.correctAns);

        Button yes;
        // TextView message= (TextView) view.findViewById(R.id.massage);
        yes = (Button) view.findViewById(R.id.yes);
       // no = (Button) view.findViewById(R.id.no);
        timeup = (TextView) view.findViewById(R.id.timeup);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MultiPlayerActivity.this, MultiPlayerActivity.class);
//                startActivity(intent);
                dialog.dismiss();
            }
        });

        yes.setAnimation(rotate);
        dialog.setContentView(view);

        dialog.show();
    }


    public void timeSart() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.startimer, null);
        dialog.setCancelable(false);
        // final EditText value= (EditText) view.findViewById(R.id.);
        Button Start, no;
        // TextView message= (TextView) view.findViewById(R.id.massage);
        Start = (Button) view.findViewById(R.id.yes);
        // no = (Button) view.findViewById(R.id.no);


        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1score.setBackgroundResource(R.drawable.p_activebackground);
                player1.setBackgroundResource(R.drawable.multiscore_background1);
                p1flag=true;
                ansqsnpage.postDelayed(new Runnable() {
                    @Override

                    public void run() {

                        question.startAnimation(scale);
                        ans_a.startAnimation(scale);
                        ans_b.startAnimation(scale);
                        ans_c.startAnimation(scale);
                        ans_d.startAnimation(scale);
                    }
                }, 0);

                qsn_ans_data data = dbhelper.getdata(randomid);
                questionVlaue = data.questions;
                question.setText(" Q. " + questionVlaue);
                ansAvalue = data.ansA;
                ans_a.setText("  A. " + ansAvalue);
                ansBvalue = data.ansB;
                ans_b.setText("  B. " + ansBvalue);
                ansCvalue = data.ansC;
                ans_c.setText("  C. " + ansCvalue);
                ansDvalue = data.ansD;
                ans_d.setText("  D. " + ansDvalue);
                timmer.cancel();
                timmer.start();
                dialog.dismiss();
               // dialog.setCancelable(false);
            }

        });
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                finish();
//            }
//        });
        // customdialoge.setAnimation(rotate);
        // no.setAnimation(rotate);
        // yes.setAnimation(rotate);
        dialog.setContentView(view);

        dialog.show();
    }

    public class CountDownTime extends CountDownTimer {
        public CountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            //     player.start();
            conter.setText((l / 1000) + "");
//            String conterText = (String) conter.getText();
//            if (conterText.equals("1")) {
//
//            }

        }

        @Override
        public void onFinish() {
            conter.setText("0");
            if(p1flag) {

                // p1score.setText("" + ++p1counter);
                p1score.setBackgroundResource(R.drawable.main_option_background);
                player1.setBackgroundResource(R.drawable.ans_question_background);
                p2score.setBackgroundResource(R.drawable.p_activebackground);
                player2.setBackgroundResource(R.drawable.multiscore_background1);
                p2flag=true;
                p1flag=false;
                timmer.start();
            }
            else {
                // p2score.setText("" + ++p2counter);
                p2score.setBackgroundResource(R.drawable.main_option_background);
                player2.setBackgroundResource(R.drawable.ans_question_background);
                p1score.setBackgroundResource(R.drawable.p_activebackground);
                player1.setBackgroundResource(R.drawable.multiscore_background1);
                p1flag=true;
                p2flag=false;
                timmer.start();

            }


           // timeUpdialoge();


        }
    }
}









