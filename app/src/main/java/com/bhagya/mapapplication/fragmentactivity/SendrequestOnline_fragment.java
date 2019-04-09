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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bhagya on 7/4/2017.
 */

public class SendrequestOnline_fragment  extends Fragment {
    Button gpslocation,ringphone,sutdownphone,turnonwifi,getlocationoffline,findmyphone;
    String user_id;
    AQuery aQuery;
    SocketConnection socketConnection = new SocketConnection();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_remote_access,null);
        turnonwifi= (Button) view.findViewById(R.id.tunonwifi_online);
        ringphone= (Button) view.findViewById(R.id.ringmyphone_online);
        findmyphone= (Button) view.findViewById(R.id.findmayphone_online);
        aQuery=new AQuery(getContext());
        ringphone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SharedPreferences prfs = getContext().getSharedPreferences("CURRENT_USER_RECORD", MODE_PRIVATE);
        user_id = prfs.getString("CurrentUser_id", "");
        JSONObject param = new JSONObject();
        try {
            param.put("userId",user_id);
            socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
            socketConnection.rsocket.emit("ringMyPhone", param);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
});
        turnonwifi.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

    sendRequestForRemoteControl("turnonwifi");
         }
     });
        findmyphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prfs = getContext().getSharedPreferences("CURRENT_USER_RECORD", MODE_PRIVATE);
                user_id = prfs.getString("CurrentUser_id", "");
                JSONObject params = new JSONObject();
                try{
                    params.put("user_id",user_id);
                    params.put("accessToken",AccessToken.getCurrentAccessToken().getToken());
                    socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
                    socketConnection.rsocket.emit("findMyPhoneRequest",params);
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return view;
    }

    public  void sendRequestForRemoteControl(String command){
        String uploadurl="http://104.155.153.31/firebase/send_notification.php";
        SharedPreferences prfs = getActivity().getSharedPreferences("new_userid", MODE_PRIVATE);
        user_id = prfs.getString("user_id", "");
        // Toast.makeText(getActivity(),"user _id="+user_id, Toast.LENGTH_SHORT).show();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("user_id",user_id);
        params.put("message",command);
        params.put("title","9801074961");
        aQuery.ajax(uploadurl,params,JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray array, AjaxStatus status) {
                super.callback(url, array, status);
                Log.i("response", "REsponser:" + array);
            }
        });
    }

}
