package com.bhagya.mapapplication.activityclass;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class MYfirebaseInstentidservice extends FirebaseInstanceIdService {
private static final String REG_TOKEN = "REG_TOKEN";
    AQuery aQuery;
    @Override
    public void onTokenRefresh() {
        Log.d("REG_TOKEN", "MEHTODCSLLRFd");
        aQuery=new AQuery(this);
        final String recent_token= FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREP), Context.MODE_PRIVATE);
       SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN),recent_token);
        editor.commit();
        SharedPreferences sharedPreferences1=getSharedPreferences("MAIN_USER_RECORD",MODE_PRIVATE);
        String user_id=sharedPreferences1.getString("user_id","");
        String urlfortokenupdate="http://104.155.153.31/UpdateRegToken.php";
        Map<String, String> param1=new HashMap<>();
        param1.put("user_id",user_id);
        param1.put("reg_token",recent_token);
        aQuery.ajax(urlfortokenupdate,param1, JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                Log.d("REGUPDATE",""+object);
            }
        });
      Log.d("REG_TOKEN",recent_token);
    }
}
