package com.bhagya.mapapplication;

import android.view.View;

/**
 * Created by Bhagya on 1/1/2017.
 */

public class fiftyfityclass extends AnswerQuestion1{

    public void fiftyfifty(int id)
    { DatabaseHelper dbhelper=new DatabaseHelper(this);
        qsn_ans_data data = dbhelper.getdata(id);
        questionVlaue=data.questions;
     if (data.correctAns.equals(data.ansA)){

         ans_b.setText(" ");
         ans_c.setText(" ");

     }
        else if (data.correctAns.equals(data.ansB)){
         ans_c.setText(" ");
         ans_d.setText(" ");
     }
        else if (data.correctAns.equals(data.ansC))
     {
       ans_d.setText(" ");
         ans_a.setText(" ");

     }
        else if(data.correctAns.equals(data.ansD))
     {
         ans_a.setText(" ");
         ans_b.setText(" ");
     }
    }
}
