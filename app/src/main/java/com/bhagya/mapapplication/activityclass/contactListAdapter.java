package com.bhagya.mapapplication.activityclass;

/**
 * Created by Bhagya on 1/21/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
 * Created by Bhagya on 12/15/2016.
 */

public class contactListAdapter extends ArrayAdapter <ContactInfo>{
    Context context;
    AQuery aQuery;
    ImageView call,sms,location;
    public contactListAdapter(Context context, ArrayList<ContactInfo>list) {
        super(context,0,list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(com.bhagya.mapapplication.R.layout.activity_read_web_contact, null);
        final TextView username, no;
        username = (TextView) view.findViewById(R.id.conatactname);
        no= (TextView) view.findViewById(R.id.contactNo);
        call= (ImageView) view.findViewById(R.id.call);
        sms= (ImageView) view.findViewById(R.id.messege);
        location= (ImageView) view.findViewById(R.id.location);
        final ContactInfo info = getItem(position);
      //  String string = "<font color=\"red\">Red</font> <font color=\"blue\">Blue</font><font color=\"Green\">Green</font>";
        username.setText(info.contactname);
        no.setText(info.contactno);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novalue=no.getText().toString();
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

 sms.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    Intent intent=new Intent(context,sms_send_recieve.class);
        String novalue=no.getText().toString();
        intent.putExtra("number",novalue);
        context.startActivity(intent);
    }
});

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentlocation=new Intent(context,RequestsenderActivity.class);
                String novalue=no.getText().toString();
                intentlocation.putExtra("number",novalue);
                context.startActivity(intentlocation);
            }
        });
        return view;
    }
}
