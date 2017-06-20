package com.bhagya.mapapplication;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Timmer extends AppCompatActivity {
TextView textView;
    CountDownT timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_ans_page3);
        textView= (TextView) findViewById(R.id.timmer);
        timer=new CountDownT(10000,1000);
        textView.setText("10");

    }
    public void ans_A(View view){
     timer.start();

    }
    public void stop(View view){
        timer.cancel();

    }
    public  class CountDownT extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountDownT(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            textView.setText((l/1000)+"");

        }

        @Override
        public void onFinish() {
            textView.setText("timer finished");

        }
    }

}
