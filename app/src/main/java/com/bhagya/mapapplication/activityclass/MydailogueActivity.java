package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.bhagya.mapapplication.R;

/**
 * Created by Bhagya on 7/4/2017.
 */

public class MydailogueActivity extends Activity
{
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.remote_activity);
      final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
      alertDialog.setTitle("your title");
      alertDialog.setMessage("your message");
      alertDialog.setIcon(R.drawable.takjhak_optionbackgraound);
      alertDialog.show();
  }


}
