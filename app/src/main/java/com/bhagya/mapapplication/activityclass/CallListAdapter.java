package com.bhagya.mapapplication.activityclass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bhagya.mapapplication.R;

import java.util.ArrayList;

/**
 * Created by Bhagya on 7/2/2017.
 */

public class CallListAdapter extends ArrayAdapter<CallHistory> {

    Context context;
    ImageView call1,sms1,calltype_history;
    AQuery aQuery;

    public CallListAdapter(Context context, ArrayList<CallHistory> list) {

        super(context,0,list);
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.callhistory_adapter, null);
        final TextView phoneno,calltype,duration,date,contactName;

        contactName = view.findViewById(R.id.contactname);
        phoneno= (TextView) view.findViewById(R.id.phonenumber);
        calltype=(TextView) view.findViewById(R.id.calltype1);
        duration=(TextView) view.findViewById(R.id.duration);
        date=(TextView) view.findViewById(R.id.date);
        call1= (ImageView) view.findViewById(R.id.call_histry);
        sms1= (ImageView) view.findViewById(R.id.sms_histry);
        final CallHistory info = getItem(position);

        String Contactname = info.contactName;

        if(Contactname.equals("null")) {
            contactName.setText("Unknown");
        } else {
            contactName.setText(Contactname);
        }
        phoneno.setText(info.contactNo);
        calltype.setText(info.callType);
        date.setText(info.dateTime);

        duration.setText(info.callDuration+" sec");
        //  String string = "<font color=\"red\">Red</font> <font color=\"blue\">Blue</font><font color=\"Green\">Green</font>";


        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novalue=phoneno.getText().toString();
                //String number = editText.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + novalue));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);

            }
        });
        sms1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,sms_send_recieve.class);
                String novalue=phoneno.getText().toString();
                intent.putExtra("number",novalue);
                context.startActivity(intent);
            }
        });
        return view;
    }}
