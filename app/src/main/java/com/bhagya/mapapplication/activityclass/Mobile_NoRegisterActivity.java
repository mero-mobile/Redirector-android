package com.bhagya.mapapplication.activityclass;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.services.ConnectsSocketAndUpdate;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.hbb20.CountryCodePicker;
import android.os.Handler;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
public class Mobile_NoRegisterActivity extends AppCompatActivity {
    CountryCodePicker cpp;
    boolean status;
    boolean registerstatus,alreadyregisterstaus;
    int count=0;
    Button register;
    EditText editPoneno;
    String contrycode;
    Profile profile;
    TextView skip;
    String mobile_no;
    String reg_token;
    String user_id;
    String user_name;
    String user_email;
    String gender;
    String response,profilePicUrl,pictureUrl;
    Context context;
    AQuery aQuery;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__no_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cpp = (CountryCodePicker) findViewById(R.id.ccp);
        register = (Button) findViewById(R.id.register_btn);
        editPoneno = (EditText) findViewById(R.id.editable_phoneno);
        editPoneno.setCursorVisible(true);
        editPoneno.setFocusable(true);
        skip = (TextView) findViewById(R.id.skip);
        context = this;
        alreadyregisterstaus=true;
        contrycode = cpp.getDefaultCountryCodeWithPlus();
        aQuery = new AQuery(this);

        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d("Data Error", response.getRawResponse());
                    JSONObject json = response.getJSONObject();
                    Log.d("json object", json.toString());
                    try {
//                       Log.d("gender",json.getString("gender"));
//                      gender = (String) json.get("gender");
                        user_name =  (String) json.get("name");
                        user_id = json.getString("id");
                        user_email = json.getString("email");
                        pictureUrl=json.getJSONObject("picture").getJSONObject("data").getString("url");
                        Log.d("ProfilePicture","Picture Url"+pictureUrl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields","id,name,link,email,picture,gender");
            request.setParameters(parameters);
            request.executeAsync();
        }
        Toast.makeText(Mobile_NoRegisterActivity.this,"userdata"+user_name,Toast.LENGTH_LONG).show();
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent skipIntent = new Intent(Mobile_NoRegisterActivity.this, ManaulBackupActivity.class);
                startActivity(skipIntent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
//                Toast.makeText(Mobile_NoRegisterActivity.this,"userdata"+user_name,Toast.LENGTH_LONG).show();
//                SharedPreferences prfs = getSharedPreferences("" + getString(R.string.FCM_PREP), context.MODE_PRIVATE);
//                reg_token = prfs.getString("" + getString(R.string.FCM_TOKEN), "");
                mobile_no = (contrycode + editPoneno.getText()).toString();
//             if (editPoneno.getText().length()>=4){
//                IsServerConnected  connected=new IsServerConnected();
//                 connected.execute();
//             }else {
//                 AlertDialog.Builder alert = new AlertDialog.Builder(Mobile_NoRegisterActivity.this);
//                 alert.setTitle("Registration Error");
//                 alert.setMessage("Please enter valid mobile number...");
//                 alert.setPositiveButton("OK",null);
//                 alert.setCancelable(false);
//                 alert.setIcon(R.drawable.ic_error_black_24dp);
//                 alert.show();
//             }
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void startProcessBar() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
//
//        fileSize = 0;
//
//        new Thread(new Runnable() {
//            public void run() {
//                while (progressBarStatus < 100) {
//                    progressBarStatus = downloadFile();
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    progressBarbHandler.post(new Runnable() {
//                        public void run() {
//                            progressBar.setProgress(progressBarStatus);
//                        }
//                    });
//                }
//                if (progressBarStatus >= 100) {
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    progressBar.dismiss();
//                }
//            }
//        }).start();
    }

    public int downloadFile() {
        while (fileSize <= 1000000) {
            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            } else if (fileSize == 400000) {
                return 40;
            } else if (fileSize == 500000) {
                return 50;
            } else if (fileSize == 700000) {
                return 70;
            } else if (fileSize == 800000) {
                return 80;
            }
        }
        return 100;
    }
    public void userRegistration() {
        startProcessBar();
//        userRegistration();
//        SharedPreferences preferences = getSharedPreferences(""+getString(R.string.FCM_PREP),context.MODE_PRIVATE);
//        String reg_token=preferences.getString(""+getString((R.string.FCM_TOKEN)),"");
        SharedPreferences preferences = getSharedPreferences("DEVICE_ID",MODE_PRIVATE);
        reg_token=preferences.getString("REG_TOKEN","Undefined");
        SharedPreferences new_userid = getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
        final SharedPreferences.Editor editor = new_userid.edit();
        editor.putString("user_name", user_name);
        editor.putString("user_email", user_email);
        editor.putString("user_id", user_id);
        editor.putString("mobile_no", mobile_no);
        editor.putString("user_token",reg_token);
        editor.commit();
        SharedPreferences currentUserRecord=getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
        SharedPreferences.Editor editorc=currentUserRecord.edit();
        editorc.putString("CurrentUser_name",user_name);
        editorc.putString("CurrentUser_id",user_id);
        editorc.putString("CurrentUser_email",user_email);
        editorc.putString("CurrentUser_gender",mobile_no);
        editorc.putString("CurrentUser_regtoken",reg_token);
        editorc.putString("CurrentUser_mobile_no",mobile_no);
        editorc.commit();
        String uploadurl = "http://192.168.0.105:4000/auth/useregistration/";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.d("response",response);
                        if(response.equalsIgnoreCase("Succesfully_Register")) {
                            Toast.makeText(Mobile_NoRegisterActivity.this,"Register Successfully",Toast.LENGTH_LONG).show();
                            Intent registerItent = new Intent(context, MainUserHome.class);
                            startActivity(registerItent);
                        }
                        if (response.equalsIgnoreCase("already_register")) {
                            Toast.makeText(Mobile_NoRegisterActivity.this,response,Toast.LENGTH_LONG).show();
                            Intent connectAndUpdate = new Intent(context, ConnectsSocketAndUpdate.class);
                            startService(connectAndUpdate);
                            Intent registerItent = new Intent(context, MainUserHome.class);
                            startActivity(registerItent);
//                            NotificationGenerator notificationGenerator=new NotificationGenerator();
//                            notificationGenerator.execute("already_register");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("That didn't work!","");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", user_id);
                params.put("name", user_name);
                params.put("mobileNo", mobile_no);
                params.put("email", user_email);
                params.put("image",pictureUrl);
                params.put("regToken", reg_token);
                params.put("accessToken",AccessToken.getCurrentAccessToken().getToken());
                return params;
            }
        };

        queue.add(stringRequest);
        progressBar.dismiss();

//
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("id", user_id);
//        params.put("name", user_name);
//        params.put("mobileNo", mobile_no);
//        params.put("email", user_email);
//        params.put("regToken", reg_token);
//        aQuery.ajax(uploadurl, params, JSONArray.class, new AjaxCallback<JSONArray>() {
//            @Override
//            public void callback(String url, JSONArray array, AjaxStatus status) {
//                Log.d("Ajax_Error", status.getError() + (array == null ? "null": "not nul"));
//                if (array==null){
//                    NotificationGenerator serverconnectionfailed=new NotificationGenerator();
//                    serverconnectionfailed.execute("serverconnection_error");
//                }
//                aQuery.getProgressBar();
//                Log.d("Arrayvalue",""+array);
//                String registration_status;
//                         try {
//                             JSONObject object=array.getJSONObject(0);
//                             if (object.get("status").equals("already_register")) {
//
//                                 NotificationGenerator notificationGenerator=new NotificationGenerator();
//                                 notificationGenerator.execute("already_register");
//
//                             }
//                         } catch (JSONException e) {
//                             e.printStackTrace();
//                         }
//                try {
//                    JSONObject obj = array.getJSONObject(0);
//                    registration_status=obj.getString("status");
//                    Log.d("REMOTEUSER",""+registration_status);
//                    if (registration_status.equals("successfully_register")){
//                        Intent intentreg = new Intent(Mobile_NoRegisterActivity.this, UserWelcomeActivity.class);
//                        intentreg.putExtra("user_name",user_name);
//                        intentreg.putExtra("user_email",user_email);
////                        intentreg.putExtra("user_gender",gender);
//                        intentreg.putExtra("user_mobile_no",mobile_no);
//                        startActivity(intentreg);
//                  }else {
//                     //........
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
    public  class IsServerConnected extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            startProcessBar();
        }
        @Override
    protected String doInBackground(String... params) {
            userRegistration();
            status=true;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
      //  Log.d("INTERNET_STATUS");
        if (netInfo != null && netInfo.isConnected()) {
            Log.d("INTERNET_STATUS","OK");
            try {
                URL url = new URL("http://127.0.0.1:4000");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(3000);
                urlc.connect();
                Log.d("RESPONCE_CODE",""+urlc.getResponseCode());
                if (urlc.getResponseCode() == 200) {
                    status=true;
                    userRegistration();
                }else {
                    status=false;
                }
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            status=false;
        }
        return Boolean.toString(status);
    }
    @Override
    protected void onPostExecute(String s) {
        progressBar.dismiss();
        if (s.equals("false")){
            AlertDialog.Builder alert = new AlertDialog.Builder(Mobile_NoRegisterActivity.this);
            alert.setTitle("Connection Error");
            alert.setMessage("Please check your internet connection...");
            alert.setPositiveButton("OK",null);
            alert.setCancelable(false);
            alert.setIcon(R.drawable.ic_error_black_24dp);
            alert.show();
        }
    }
}

public  class NotificationGenerator extends AsyncTask<String,String,String>{
    @Override
    protected String doInBackground(String... params) {
        String param2=params[0];
        return param2;
    }
    @Override
    protected void onPostExecute(String s) {
        if (s.equals("already_register")){
            final AlertDialog.Builder alert = new AlertDialog.Builder(Mobile_NoRegisterActivity.this);
            alert.setTitle("Registration Status");
            alert.setMessage("This phone is already register.Do you want to update mobile no...");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mobileupdateurl="http://104.155.153.31/UserMobileNoUpdate.php";
                Map<String, String> params1=new HashMap<>();
                params1.put("reg_token",reg_token);
                params1.put("mobile_no",mobile_no);
                aQuery.ajax(mobileupdateurl,params1,JSONArray.class,new AjaxCallback<JSONArray>(){
                    @Override
                    public void callback(String url, JSONArray array, AjaxStatus status) {
                        //Log.d("STAtUS_UPDATE",""+array);)
                        if (array==null){
                            NotificationGenerator servererror=new NotificationGenerator();
                            servererror.execute("serverconnection_error");
                        }
                        try {
                            JSONObject updateobj = array.getJSONObject(0);
                            if (updateobj.get("status").equals("succusessfully_updated"));
                            {
                                NotificationGenerator notificationforMoileUpdate=new NotificationGenerator();
                                notificationforMoileUpdate.execute("updatestatus");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        alert.setNegativeButton("No",null);
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_error_black_24dp);
        alert.show();
    }
    else if (s.equals("updatestatus")){
            AlertDialog.Builder alert=new AlertDialog.Builder(Mobile_NoRegisterActivity.this);
            alert.setTitle("Update Status");
            alert.setMessage("Mobile no.updated successfully..");
            alert.setIcon(R.drawable.ic_error_black_24dp);
            alert.setPositiveButton("Ok",null);
            alert.setCancelable(false);
            alert.show();
        }
        else  if (s.equals("serverconnection_error")){
            AlertDialog.Builder alert=new AlertDialog.Builder(Mobile_NoRegisterActivity.this);
            alert.setTitle("Connection Error");
            alert.setMessage("Server connection failed error.check your internet connectivity..");
            alert.setIcon(R.drawable.ic_error_black_24dp);
            alert.setPositiveButton("Ok",null);
            alert.setCancelable(false);
            alert.show();
        }

    }


}
    }










