package com.bhagya.mapapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidquery.AQuery;
import java.util.ArrayList;
/**
 * Created by Bhagya on 2/4/2017.
 */
public class inboxlistAdapter extends ArrayAdapter <inboxInfo> {

    Context context;
    AQuery aQuery;
    ImageView imageView;
Button reply;
    public inboxlistAdapter(Context context, ArrayList<inboxInfo>list) {
        super(context,0,list);
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.inbox_listadapter, null);
        TextView sms, no;
        sms = (TextView) view.findViewById(R.id.sms);
        no= (TextView) view.findViewById(R.id.phoneno);
        reply= (Button) view.findViewById(R.id.reply);
        final inboxInfo info = getItem(position);
        //  String string = "<font color=\"red\">Red</font> <font color=\"blue\">Blue</font><font color=\"Green\">Green</font>";
        sms.setText(info.sms);
        no.setText(info.phone);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,sms_send_recieve.class);
                intent.putExtra("number",info.phone);
                context.startActivity(intent);
            }
        });
        return view;
}}
