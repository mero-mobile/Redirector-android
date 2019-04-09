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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.activityclass.CallHistory;
import com.bhagya.mapapplication.activityclass.CallListAdapter;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bhagya on 7/2/2017.
 */

public class ReadWebCallHistoryFragment extends Fragment {
String user_id;
    Button button,upload;
    ListView listView;
    AQuery aQuery;
    String url="http://104.155.153.31/selectCall_Logs.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.call_history,null);
        aQuery=new AQuery(getContext());
        listView= (ListView) view.findViewById(R.id.callhistrylist);
        CallHistory callHistory=new CallHistory();
        final DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
        populateData(databaseHelper.getListOfCalLogs());
        return view;
    }

    public void populateData(ArrayList<CallHistory> list) {
        CallListAdapter adapter = new CallListAdapter(getActivity(),list);
        listView.setAdapter(adapter);
    }

}
