package com.bhagya.mapapplication.fragmentactivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.activityclass.inboxInfo;
import com.bhagya.mapapplication.activityclass.inboxlistAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bhagya on 7/3/2017.
 */

public class ReadWbSmsOutboxFragment extends Fragment {
    Button button,upload;
    ListView listView;
    String user_id;
    com.bhagya.mapapplication.activityclass.userinfo userinfo;
    AQuery aQuery;
    // String url="http://192.168.1.162/meromobile.com/selectContact.php";
    String url="http://127.0.0.1/auth/login";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.readinbox,null);
        aQuery=new AQuery(getActivity());
        button= (Button) view.findViewById(R.id.fetch);
        listView= (ListView) view.findViewById(R.id.inboxlistview);
        inboxInfo info=new inboxInfo();
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        populateData(databaseHelper.getListOfOutBox());
        return view;
    }

    public void populateData(ArrayList<inboxInfo> list) {
        inboxlistAdapter adapter = new inboxlistAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

}
