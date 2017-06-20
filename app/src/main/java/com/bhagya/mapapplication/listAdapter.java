package com.bhagya.mapapplication;

/**
 * Created by Bhagya on 1/21/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by Bhagya on 12/15/2016.
 */

public class listAdapter extends ArrayAdapter <userinfo>{
    Context context;
    VideoView videoView;
    AQuery aQuery;
    ImageView imageView;

        public listAdapter(Context context, ArrayList<userinfo>list) {
            super(context,0,list);
            this.context = context;
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_showdata, null);
            TextView username, id;
            username = (TextView) view.findViewById(R.id.username);
            id= (TextView) view.findViewById(R.id.password);

          //  address = (TextView) view.findViewById(R.id.address);
            final userinfo info = getItem(position);
            String string = "<font color=\"red\">Red</font> <font color=\"blue\">Blue</font><font color=\"Green\">Green</font>";
            username.setText(Html.fromHtml(string + info.username));
           id.setText(info.password);
        //    address.setText(info.address);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context,DetailActivity.class);
//                    intent.putExtra("id",info.id);
//                    context.startActivity(intent);
//                }
//            });

            return view;
        }}
