package com.bhagya.mapapplication.fragmentactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.Wwboard;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.activityclass.MainUserHome;
import com.bhagya.mapapplication.activityclass.Mobile_NoRegisterActivity;
import com.bhagya.mapapplication.activityclass.contactListAdapter;
import com.bhagya.mapapplication.activityclass.ContactInfo;
import com.bhagya.mapapplication.activityclass.userinfo;
import com.bhagya.mapapplication.adapter.WwBoardListAdapter;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bhagya on 7/1/2017.
 */

public class ReadWebcontactFragment extends Fragment {
    Button button,upload;
    ListView listView;
    userinfo userinfo;
    Context context;
    DatabaseHelper db;
    AQuery aQuery;
    String user_id;
    SocketConnection socketConnection;
//    String url="http://104.155.153.31/selectContact.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.contact_listview,null);
        aQuery=new AQuery(getContext());
        listView= (ListView) view.findViewById(R.id.contactlistview);
         context= this.context;
        ConnectivityManager dataManager;
//        dataManager  = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
//        try {
//            Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
//            dataMtd.setAccessible(true);
//            dataMtd.invoke(dataManager, true);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();

        db = new DatabaseHelper(getActivity());
        populateData(db.getListOfContact());


        socketConnection  = new SocketConnection();
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        socketConnection.rsocket.on("wwboardmessage", messageHandler);

        ContactInfo info=new ContactInfo();
//        fetchData();
//        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

      //  Toast.makeText(getActivity(),user_id, Toast.LENGTH_SHORT).show();
        return view;
    }

    private   Emitter.Listener messageHandler = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];
                    Log.d("INCOMING_MSG", String.valueOf(data));
                    parseData(data);
                }
            });
        }
    };

    public void fetchData() {
        String url = "http://10.0.2.2:4000/getContact/";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        String current_userid,current_usertoken,current_username,current_useremail,current_gender,current_mobile_no;
                        Log.d("response", response);
                        try {
                            JSONArray array = new JSONArray(response);
                            parseData(array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(new String(response.toString()).equals("not_register")) {
                            Log.d("NOT_REGITER","CALLED");
                            Intent registerItent = new Intent(getActivity(), Mobile_NoRegisterActivity.class);
                            startActivity(registerItent);
                        } else {

//                            Intent newIntent=new Intent(getActivity(),MainUserHome.class);
//                            startActivity(newIntent);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("That didn't work!","");
            }
        });
        //{
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", user_id);
//                params.put("accessToken", AccessToken.getCurrentAccessToken().getToken().toString());
//                return params;
//            }
//        };
        queue.add(stringRequest);
    }

    public void parseData(JSONArray array) {
        Log.d("TESTCONTACT", String.valueOf(array));
        ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                ContactInfo info = new ContactInfo();
                info.contactname = obj.getString("message");
                info.contactno = obj.getString("date");
                list.add(info);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("TESTCONTACT", String.valueOf(list));
//        populateData(list);
    }

    public void populateData(ArrayList<ContactInfo> list) {
        Log.d("TESTCONTACT", String.valueOf(list));
        contactListAdapter adapter = new contactListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

}
