package com.bhagya.mapapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.Wwboard;

import java.util.ArrayList;

public class WwBoardListAdapter extends ArrayAdapter<Wwboard> {
    Context context;
    TextView message;
    TextView date;

    public WwBoardListAdapter(@NonNull Context context, ArrayList<Wwboard> list) {
        super(context, 0,list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.wwboard_message,null);

        message = view.findViewById(R.id.wboardmessage);
        date = view.findViewById(R.id.wboarddate);
        final Wwboard boardMessage = getItem(position);
        message.setText(boardMessage.message);
        Log.d("PARSE_DATADAPTER_MIAN","adapter called");
        return  view;
    }
}
