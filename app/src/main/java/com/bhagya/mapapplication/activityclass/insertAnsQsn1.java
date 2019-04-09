package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhagya.mapapplication.R;

public class insertAnsQsn1 extends Activity {
    EditText question,optA,optB,optC,optD,correctans;

    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      final   DatabaseHelper dbhelper=new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_ans_qsn1);
        question= (EditText) findViewById(R.id.question);
        optA= (EditText) findViewById(R.id.optA);
        optB= (EditText) findViewById(R.id.optB);
        optC= (EditText) findViewById(R.id.optC);
        optD= (EditText) findViewById(R.id.optD);
        correctans= (EditText) findViewById(R.id.correctans);
        submit= (Button) findViewById(R.id.submit);

submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String qsn,ansA,ansB,ansC,ansD,corrcetansvalue;
        qsn=question.getText().toString();
        ansA=optA.getText().toString();
        ansB=optB.getText().toString();
        ansC=optC.getText().toString();
        ansD=optD.getText().toString();
        corrcetansvalue=correctans.getText().toString();

        Toast.makeText(insertAnsQsn1.this,"datasuccesfullyinserted",Toast.LENGTH_SHORT);
        ContentValues cv=new ContentValues();
        cv.put("questions",qsn);
        cv.put("ansA",ansA);
        cv.put("ansB",ansB);
        cv.put("ansC",ansC);
        cv.put("ansD",ansD);
        cv.put("correctAns",corrcetansvalue);
//        dbhelper.InsertQsnAns(cv);
        Toast.makeText(insertAnsQsn1.this,"Data Succesfully Inserted",Toast.LENGTH_SHORT).show();
        question.setText("");
        optA.setText("");
        optB.setText("");
        optC.setText("");
        optD.setText("");
        correctans.setText("");



    }
});


    }
}
