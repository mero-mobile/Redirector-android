package com.bhagya.mapapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class listgpslocation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listgpslocation);


        ArrayList location1 ;
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        location1=databaseHelper.getLocation();
        ListView listView= (ListView) findViewById(R.id.listview);
        ArrayAdapter arrayAdapter;
        arrayAdapter = new ArrayAdapter(
                listgpslocation.this,
                android.R.layout.simple_list_item_1
                ,location1
        );
        listView.setAdapter(arrayAdapter);

    }
}
