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

public class AnswerQuestion1 extends Activity {
    TextView question, ans_a, conter, ans_b, ans_c, ans_d, thou10, thou20, thou50, one_lakh, two_lakh, three_lakh, four_lakh, five_lakh, ten_lakh, twenty_lakh, thirty_lakh, fourty_lakh, fifity_lakh, one_crore, two_crore, three_crore;
    Animation scale, rotate, alpha,scalefortakjhak,animforbachao;
    DatabaseHelper dbhelper;
    MediaPlayer player, rightmusic, worngmusic;
    CountDownTime  timmer = new CountDownTime(30000, 1000);
    String questionVlaue, ansAvalue, ansBvalue, ansCvalue, ansDvalue, CorrectAns;
    Button fiftyfifty, takjhak, copy, bachao;
    fiftyfityclass fiftyfiftymethod;
    LinearLayout ansqsnpage, customdialoge;
    Random random=new Random();
    int randomid=random.nextInt(16)+1;
    ContentValues cv=new ContentValues();
    int count = 1;

  //  ArrayList list=new ArrayList();




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.question_ans_page3);
        createDatabase();
        player = MediaPlayer.create(this, R.raw.tick);
        rightmusic = MediaPlayer.create(this, R.raw.rightanswer);
        worngmusic = MediaPlayer.create(this, R.raw.wronganswer);
        timeupActivity timeupActivity;
        dbhelper = new DatabaseHelper(this);
       cv.put("randomid",randomid);
         dbhelper.InsertRandomid(cv);

      //  list=dbhelper.getListofRandomid();
       // timmer.start();
        // player.start();
        // player.setLooping(true);
//        timmer();
//finding view of all xml components.................................................................................
        customdialoge = (LinearLayout) findViewById(R.id.customdialoge);
        ansqsnpage = (LinearLayout) findViewById(R.id.ansqsnpage);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);


        scalefortakjhak = AnimationUtils.loadAnimation(this, R.anim.scalefortakjhak);
animforbachao=AnimationUtils.loadAnimation(this,R.anim.animforbachao);
        ans_a = (TextView) findViewById(R.id.ans_a);
        ans_b = (TextView) findViewById(R.id.ans_b);
        ans_c = (TextView) findViewById(R.id.ans_c);
        ans_d = (TextView) findViewById(R.id.ans_d);
        question = (TextView) findViewById(R.id.question);
        conter = (TextView) findViewById(R.id.timmer);
        //  reward1=(TextView) findViewById(R.id.score);
        thou10 = (TextView) findViewById(R.id.thousand10);
        thou20 = (TextView) findViewById(R.id.thousand20);
        thou50 = (TextView) findViewById(R.id.thousand50);
        one_lakh = (TextView) findViewById(R.id.lakh_1);
        two_lakh = (TextView) findViewById(R.id.lakh_2);
        three_lakh = (TextView) findViewById(R.id.lakh_3);
        four_lakh = (TextView) findViewById(R.id.lakh_4);
        five_lakh = (TextView) findViewById(R.id.lakh_5);
        ten_lakh = (TextView) findViewById(R.id.lakh_10);
        twenty_lakh = (TextView) findViewById(R.id.lakh_20);
        thirty_lakh = (TextView) findViewById(R.id.lakh_30);
        fourty_lakh = (TextView) findViewById(R.id.lakh_40);
        fifity_lakh = (TextView) findViewById(R.id.lakh_50);
        one_crore = (TextView) findViewById(R.id.crore_1);
        two_crore = (TextView) findViewById(R.id.crore_2);
        three_crore = (TextView) findViewById(R.id.crore_3);
        fiftyfifty = (Button) findViewById(R.id.fifty50);
        copy = (Button) findViewById(R.id.copy);
        takjhak = (Button) findViewById(R.id.takjhak);
        bachao = (Button) findViewById(R.id.bachao);


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
        fiftyfifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // Toast.makeText(AnswerQuestion1.this, (CharSequence) list,Toast.LENGTH_SHORT).show();
              //  System.out.println(list);
              //  dbhelper.resetdata();
                fiftyfifty();
                fiftyfifty.setClickable(false);
                fiftyfifty.setBackgroundResource(R.drawable.after_click);
            }
        });
 timeSart();
        takjhak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qsn_ans_data data = dbhelper.getdata(randomid);
                questionVlaue = data.questions;
                ansAvalue = data.ansA;
                ansBvalue = data.ansB;
                ansCvalue = data.ansC;
                ansDvalue = data.ansD;
               // Toast.makeText(AnswerQuestion1.this,ans_a.getText(),Toast.LENGTH_SHORT).show();
                if (ansAvalue.equals(data.correctAns)){
                    Takjahk("A");
                }
                else  if (ansBvalue.equals(data.correctAns))
                {
                    Takjahk("B");
                }
                else  if (ansCvalue.equals(data.correctAns))
                {
                    Takjahk("C");
                }
                else  if (ansDvalue.equals(data.correctAns))
                {
                    Takjahk("D");
                }
                takjhak.setClickable(false);
                takjhak.setBackgroundResource(R.drawable.after_click);

            }



        });


        bachao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qsn_ans_data data = dbhelper.getdata(randomid);
                questionVlaue = data.questions;
                ansAvalue = data.ansA;
                ansBvalue = data.ansB;
                ansCvalue = data.ansC;
                ansDvalue = data.ansD;


                if (ansAvalue.equals(data.correctAns)){
                   Bachao("A");
                }
                else  if (ansBvalue.equals(data.correctAns))
                {
                    Bachao("B");
                }
                else  if (ansCvalue.equals(data.correctAns))
                {
                    Bachao("C");
                }
                else  if (ansDvalue.equals(data.correctAns))
                {
                    Bachao("D");
                }
                bachao.setClickable(false);
                bachao.setBackgroundResource(R.drawable.after_click);

            }



        });


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qsn_ans_data data = dbhelper.getdata(randomid);
                questionVlaue = data.questions;
                ansAvalue = data.ansA;
                ansBvalue = data.ansB;
                ansCvalue = data.ansC;
                ansDvalue = data.ansD;


                if (ansAvalue.equals(data.correctAns)){
                    ans_a.setBackgroundResource(R.drawable.ans_question_onclick);

//                    for (int i=0;i<3;i++) {
//
//                    try {
//               Thread.sleep(500);
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//
//          }
//                          ans_a.setBackgroundResource(R.drawable.ans_question_background);
//                    }

                }
                else  if (ansBvalue.equals(data.correctAns))
                {
                    ans_b.setBackgroundResource(R.drawable.ans_question_onclick);
                }
                else  if (ansCvalue.equals(data.correctAns))
                {
                    ans_c.setBackgroundResource(R.drawable.ans_question_onclick);
                }
                else  if (ansDvalue.equals(data.correctAns))
                {
                    ans_d.setBackgroundResource(R.drawable.ans_question_onclick);
                }
                copy.setClickable(false);
                copy.setBackgroundResource(R.drawable.after_click);

            }



        });





    }
//*************************create database*****************************************************************************************



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




    //*****************************************************************************************************************************






    //Scoring and change question method.....................................................................................................
    public qsn_ans_data showdata(int id,int counter) {

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
        switch (counter) {
            case 2:
                dbhelper.resetdata();
                thou10.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 3:
                thou10.setBackgroundColor(Color.BLACK);
                thou20.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 4:
                thou20.setBackgroundColor(Color.BLACK);
                thou50.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 5:
                thou50.setBackgroundColor(Color.BLACK);
                one_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 6:
                one_lakh.setBackgroundColor(Color.BLACK);
                two_lakh.setBackgroundResource(R.drawable.score_backgroud);

                break;
            case 7:
                two_lakh.setBackgroundColor(Color.BLACK);
                three_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 8:

                three_lakh.setBackgroundColor(Color.BLACK);
                four_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 9:

                four_lakh.setBackgroundColor(Color.BLACK);
                five_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 10:

                five_lakh.setBackgroundColor(Color.BLACK);
                ten_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 11:

                ten_lakh.setBackgroundColor(Color.BLACK);
                twenty_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;

            case 12:

                twenty_lakh.setBackgroundColor(Color.BLACK);
                thirty_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 13:

                thirty_lakh.setBackgroundColor(Color.BLACK);
                fourty_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 14:

                fourty_lakh.setBackgroundColor(Color.BLACK);
                fifity_lakh.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 15:

                fifity_lakh.setBackgroundColor(Color.BLACK);
                one_crore.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 16:

                one_crore.setBackgroundColor(Color.BLACK);
                two_crore.setBackgroundResource(R.drawable.score_backgroud);
                break;
            case 17:
                dbhelper.resetdata();
                two_crore.setBackgroundColor(Color.BLACK);
                three_crore.setBackgroundResource(R.drawable.score_backgroud);
                question.setText("Congratulation You Get Last Mobile Digit of your partner:=68");
                ans_a.setVisibility(View.INVISIBLE);
                ans_b.setVisibility(View.INVISIBLE);
                ans_c.setVisibility(View.INVISIBLE);
                ans_d.setVisibility(View.INVISIBLE);
                break;
        }

        return data;

    }

    //fiftyfifty option method...........................................................................................................
    public void fiftyfifty() {

        qsn_ans_data data = dbhelper.getdata(randomid);
        questionVlaue = data.questions;
        if (data.correctAns.equals(data.ansA)) {

            ans_b.setText("  B. ");
            ans_c.setText("  C. ");

        } else if (data.correctAns.equals(data.ansB)) {
            ans_c.setText("  C. ");
            ans_d.setText("  D. ");
        } else if (data.correctAns.equals(data.ansC)) {
            ans_d.setText("  D. ");
            ans_a.setText("  A. ");

        } else if (data.correctAns.equals(data.ansD)) {
            ans_a.setText("  A. ");
            ans_b.setText("  B. ");
        }
    }
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


                            ArrayList<Integer> list=dbhelper.getListofRandomid();
                            Iterator itr=list.iterator();
                            randomid=random.nextInt(17)+1;
                            boolean check=list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue",list.toString());


                            while (check){
                                randomid=random.nextInt(17)+1;
                                check=list.contains(randomid);
                            }



                            count++;
                            rightmusic.start();
                         //  randomid=random.nextInt(16)+1;
                            ContentValues cv=new ContentValues();
                            cv.put("randomid",randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid,count);

                        } else {
                            // worngmusic.start();
                            // Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                            WrongAnswer();


                        }
                        break;
                    case "B":
                        qsn_ans_data data1 = dbhelper.getdata(randomid);
                        if (ansBvalue.equals(data1.correctAns)) {
                            //timmer.cancel();
                            //timmer = new CountDownTime(30000, 1000);
                            ArrayList<Integer> list=dbhelper.getListofRandomid();
                            randomid=random.nextInt(17)+1;
                            boolean check=list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Iterator itr=list.iterator();
                            Log.d("listVlaue",list.toString());


                            while (check){
                                randomid=random.nextInt(17)+1;
                                check=list.contains(randomid);
                            }
                            count++;
                            rightmusic.start();
                         // randomid=random.nextInt(16)+1;
                            ContentValues cv=new ContentValues();
                            cv.put("randomid",randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid,count);
                        } else {
                            // worngmusic.start();
                           // Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                            WrongAnswer();
                        }

                        break;
                    case "C":
                        qsn_ans_data data2 = dbhelper.getdata(randomid);
                        if (ansCvalue.equals(data2.correctAns)) {
                            ArrayList<Integer> list=dbhelper.getListofRandomid();
                            Iterator itr=list.iterator();
                            randomid=random.nextInt(17)+1;
                            boolean check=list.contains(randomid);
                            Log.i("flag", String.valueOf(check));
                            Log.d("listVlaue",list.toString());


                            while (check){
                                randomid=random.nextInt(17)+1;
                                check=list.contains(randomid);
                            }
                            count++;
                            rightmusic.start();
                           //randomid=random.nextInt(16)+1;
                            ContentValues cv=new ContentValues();
                            cv.put("randomid",randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid,count);

                        } else {

                            //worngmusic.start();
                            //Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                            WrongAnswer();
                        }


                        break;
                    case "D":
                        qsn_ans_data data3 = dbhelper.getdata(randomid);
                        if (ansDvalue.equals(data3.correctAns)) {
                            ArrayList<Integer> list=dbhelper.getListofRandomid();
                            randomid=random.nextInt(17)+1;
                            boolean check=list.contains(randomid);

                            Iterator itr=list.iterator();
                            Log.d("listVlaue",list.toString());


                            Log.i("flag", String.valueOf(check));
                            while (check){
                                randomid=random.nextInt(17)+1;
                                check=list.contains(randomid);
                            }
                            count++;
                            //rightmusic.start();
                           //randomid=random.nextInt(16)+1;
                            ContentValues cv=new ContentValues();
                            cv.put("randomid",randomid);
                            dbhelper.InsertRandomid(cv);
                            showdata(randomid,count);


                        } else {
                            //worngmusic.start();
                           // Toast.makeText(AnswerQuestion1.this, "Wrong Answer", Toast.LENGTH_SHORT).show();

                            WrongAnswer();
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
TextView timeup,Rs;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.timeupdialoge, null);
        Rs= (TextView) view.findViewById(R.id.Rs);
        dialog.setCancelable(false);
        // final EditText value= (EditText) view.findViewById(R.id.);
//        if(count<4){
//            Rs.setText("You Won Rs-0");
//        }
        if(count<5){
            Rs.setText("You Won Rs-0");
        }
        else  if (count>=5 && count<9)
        {
            Rs.setText("You Won Rs-1 lakh");
        }
        else  if(count>=9 && count<12)
        {
            Rs.setText("You Won Rs-5lakh");
        }
        else if(count>=12 && count<15)
        {
            Rs.setText("You Won Rs-30lakh");
        }
        else if (count>=15 && count<17)
        {
            Rs.setText("You Won Rs-1 crore");
        }
        else {
            Rs.setText("You Won Rs-5 crore");
        }








        Button yes, no;
        // TextView message= (TextView) view.findViewById(R.id.massage);
        yes = (Button) view.findViewById(R.id.yes);
        no = (Button) view.findViewById(R.id.no);
       timeup= (TextView) view.findViewById(R.id.timeup);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnswerQuestion1.this, AnswerQuestion1.class);
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
        TextView timeup,Rs;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.wrong_answer, null);
        Rs= (TextView) view.findViewById(R.id.Rs);
        dialog.setCancelable(false);
        // final EditText value= (EditText) view.findViewById(R.id.);
        if(count<5){
            Rs.setText("You Won Rs-0");
        }
        else  if (count>=5 && count<9)
        {
            Rs.setText("You Won Rs-1 lakh");
        }
        else  if(count>=9 && count<12)
        {
            Rs.setText("You Won Rs-5lakh");
        }
        else if(count>=12 && count<15)
        {
            Rs.setText("You Won Rs-30lakh");
        }
        else if (count>=15 && count<17)
        {
            Rs.setText("You Won Rs-1 crore");
        }
        else {
            Rs.setText("You Won Rs-5 crore");
        }




        Button yes, no;
        // TextView message= (TextView) view.findViewById(R.id.massage);
        yes = (Button) view.findViewById(R.id.yes);
        no = (Button) view.findViewById(R.id.no);
        timeup= (TextView) view.findViewById(R.id.timeup);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnswerQuestion1.this, AnswerQuestion1.class);
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

        dialog.show();
    }





    public void timeSart() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.startimer, null);
        // final EditText value= (EditText) view.findViewById(R.id.);
        Button Start, no;
        // TextView message= (TextView) view.findViewById(R.id.massage);
       Start = (Button) view.findViewById(R.id.yes);
       // no = (Button) view.findViewById(R.id.no);
dialog.setCancelable(false);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ansqsnpage.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        question.startAnimation(scale);
                        ans_a.startAnimation(scale);
                        ans_b.startAnimation(scale);
                        ans_c.startAnimation(scale);
                        ans_d.startAnimation(scale);
                    }
                },0);

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


            timeUpdialoge();


        }
    }







    public void Takjahk(String rightAns) {
        LinearLayout takjhaklayout;
        final TextView timeup,Rs,takjhakans;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.takjhak_layout, null);
        takjhaklayout= (LinearLayout) findViewById(R.id.customdialoge1);
        Rs= (TextView) view.findViewById(R.id.Rs);
        takjhakans= (TextView)view.findViewById(R.id.takjhakoption);
        dialog.setCancelable(false);
//takjhaklayout.postDelayed(new Runnable() {
//    @Override
//    public void run() {
//        takjhakans.setAnimation(scale);
//    }
//},500);
        takjhakans.setText(rightAns);

        Button yes, no;
        // TextView message= (TextView) view.findViewById(R.id.massage);
        yes = (Button) view.findViewById(R.id.yes);
      //  no = (Button) view.findViewById(R.id.no);
        timeup= (TextView) view.findViewById(R.id.timeup);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//        // customdialoge.setAnimation(rotate);
       // no.setAnimation(rotate);


       takjhakans.setAnimation(scalefortakjhak);
        dialog.setContentView(view);

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        timmer.cancel();
        super.onDestroy();
    }

    public void Bachao(String right) {
        ImageView bachao;

        LinearLayout takjhaklayout;
        final TextView timeup,Rs,takjhakans;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.bachao_layout, null);
        takjhaklayout= (LinearLayout) findViewById(R.id.customdialoge3);
        bachao= (ImageView)view.findViewById(R.id.bachao2);
        bachao.setAnimation(animforbachao);
        dialog.setCancelable(false);
        if(right.equals("A")) {
            bachao.setImageResource(R.mipmap.bachao1a1);
        }
        if(right.equals("B")) {
            bachao.setImageResource(R.mipmap.bachao1b1);
        }

        if(right.equals("C")) {
            bachao.setImageResource(R.mipmap.bachao1c1);
        }
        if(right.equals("D")) {
            bachao.setImageResource(R.mipmap.bachao1d1);
        }


        Button yes, no;

        yes = (Button) view.findViewById(R.id.yes);
        timeup= (TextView) view.findViewById(R.id.timeup);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


      //  takjhakans.setAnimation(scalefortakjhak);
        dialog.setContentView(view);

        dialog.show();
    }
//
//Thread forA=new Thread(new Runnable() {
//    @Override
//    public void run() {
//        ArrayList list=dbhelper.getListofRandomid();
//        Log.d("listVlaue",list.toString());
//        boolean check=list.contains(randomid);
//        while(check);
//        {
//
//
//            randomid = random.nextInt(16) + 1;
//            check=list.contains(randomid);
//        }
//        count++;
//        ContentValues cv = new ContentValues();
//        cv.put("randomid", randomid);
//        dbhelper.InsertRandomid(cv);
//        showdata(randomid, count);
//    }
//});
//Thread forB=new Thread(new Runnable() {
//    @Override
//    public void run() {
//        rightmusic.start();
//        //timmer.start();
//        ArrayList list=dbhelper.getListofRandomid();
//        Log.d("listVlaue",list.toString());
//        boolean check=list.contains(randomid);
//        while(check);
//        {
//
//
//            randomid = random.nextInt(16) + 1;
//            check=list.contains(randomid);
//        }
//        count++;
//        // randomid=random.nextInt(16)+1;
//        ContentValues cv=new ContentValues();
//        cv.put("randomid",randomid);
//        dbhelper.InsertRandomid(cv);
//        showdata(randomid,count);
//
//    }
//});
//
//    Thread forC=new Thread(new Runnable() {
//        @Override
//        public void run() {
//            ArrayList list=dbhelper.getListofRandomid();
//            Log.d("listVlaue",list.toString());
//            boolean check=list.contains(randomid);
//
//            while(check);
//            {
//
//
//                randomid = random.nextInt(16) + 1;
//                check=list.contains(randomid);
//            }
//            count++;
//            rightmusic.start();
//            //randomid=random.nextInt(16)+1;
//            ContentValues cv=new ContentValues();
//            cv.put("randomid",randomid);
//            dbhelper.InsertRandomid(cv);
//            showdata(randomid,count);
//
//
//
//        }
//    });
//    Thread forD=new Thread(new Runnable() {
//        @Override
//        public void run() {
//            ArrayList list=dbhelper.getListofRandomid();
//            Log.d("listVlaue",list.toString());
//            boolean check=list.contains(randomid);
//
//            while(check);
//            {
//
//
//                randomid = random.nextInt(16) + 1;
//                check=list.contains(randomid);
//            }
//            count++;
//            rightmusic.start();
//            //randomid=random.nextInt(16)+1;
//            ContentValues cv=new ContentValues();
//            cv.put("randomid",randomid);
//            dbhelper.InsertRandomid(cv);
//            showdata(randomid,count);
//
//
//
//        }
//    });
}


//    Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//
//
//            conter.setText("" + msg.what);
//            return false;
//        }
//    });


//    Thread timmer = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            timmer();
//        }
//    });
//    Thread timmer1 = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            timmer();
//        }
//    });

//    public void timmer() {
//        for (int i = 30; i >= 0; i--) {
//            //   conter.setText(""+i);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//
//            }
//
//            handler.sendEmptyMessage(i);
//        }
//    }
//    method for play music in Run time on ans_question Page....................................................................




