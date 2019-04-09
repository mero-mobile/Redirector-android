package com.bhagya.mapapplication.activityclass;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.bhagya.mapapplication.services.UpLoadCallHistory;
import com.bhagya.mapapplication.services.UploadContactInfoService;
import com.bhagya.mapapplication.services.UploadInboxService;
import com.bhagya.mapapplication.services.UploadOutboxService;
import com.bhagya.mapapplication.socket.IncomingMessageHandler;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener{
    Button usernamebtn,passwordbtn,facebook_button;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    LoginButton loginButton;
    AQuery aQuery;
    Context context;
    String gender,user_id,user_email,user_name;
    CallbackManager callbackManager;
    SignInButton signInButton;
    SocketConnection socketConnection = new SocketConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        context=this;
        loginButton = (LoginButton) findViewById(R.id.fb_loginid);
        signInButton= (SignInButton) findViewById(R.id.google_signin);
        aQuery=new AQuery(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        callbackManager=CallbackManager.Factory.create();
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        //checking for facebook login status
//      Log.d("LoginActivityIntent",intent.getStringExtra("Intent").toString());
            Log.d("FACEBOOK_ACCESSTOKEN", String.valueOf(AccessToken.getCurrentAccessToken()));
            if(AccessToken.getCurrentAccessToken() != null) {
                        Intent intentUserHome=new Intent(LoginActivity.this,MainUserHome.class);
                        startActivity(intentUserHome);
                    }
        //checking for Gmail login status
//        if (googleApiClient != null && googleApiClient.isConnected()) {
//            Intent intent=new Intent(LoginActivity.this,MainUserHome.class);
//            startActivity(intent);
//        }
        //facebook login....
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.singOut();
                Log.d("Login Sucess",loginResult.getAccessToken().getUserId());
                Toast.makeText(LoginActivity.this, loginResult.getAccessToken().getUserId(),Toast.LENGTH_LONG).show();
                //user verification functioncall for
                userVerification(loginResult.getAccessToken().getUserId());
                Log.d("Token",loginResult.getAccessToken().getToken());
                }
            @Override
            public void onCancel() {
                Log.d("login cancel","login cancel");
                Toast.makeText(LoginActivity.this,"login cancel",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(FacebookException error) {
                Log.d("login error","login error");
                Toast.makeText(LoginActivity.this,"Login Error",Toast.LENGTH_LONG).show();
            }

        });
        createDatabase();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Gmail login....
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,REQ_CODE );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
    private void handleResult(GoogleSignInResult result) {
        if(result.isSuccess())
        {
            GoogleSignInAccount account=result.getSignInAccount();
            String name=account.getDisplayName();
            String email=account.getEmail();
            String id=account.getId();
            Toast.makeText(this,id,Toast.LENGTH_LONG).show();
            String img_url = account.getPhotoUrl().toString();
            Intent intent=new Intent(this,MainUserHome.class);
            intent.putExtra("name",name);
            intent.putExtra("email",email);
            intent.putExtra("image_url",img_url);
            startActivity(intent);

        }
    }

    private void createDatabase() {
        try {
            boolean dbExist = checkDataBaseExit();
//            System.out.println("database" + dbExist);
            if (!dbExist) {
                String myDbDir = "/data/data/" + getPackageName() + "/databases";
                (new File(myDbDir)).mkdir();
                OutputStream dos = new FileOutputStream("/data/data/" + getPackageName() + "/databases/" + DatabaseHelper.dbname);
                InputStream dis = getResources().openRawResource(R.raw.meromobiledefaultsettings);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = dis.read(buffer)) > 0) {
                    dos.write(buffer);
                }
                dos.flush();
                dos.close();
                dis.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Checks whether Database already exists
     */
    private boolean checkDataBaseExit() {
//        System.out.println("rrr");
        SQLiteDatabase checkDB = null;

        try {
            String myPath = "/data/data/" + getPackageName() + "/databases/" + DatabaseHelper.dbname;

            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public void userVerification(final String user_id) {
    String url = "http://192.168.0.105:4000/auth/login/";
    RequestQueue queue = Volley.newRequestQueue(this);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the response string.
                    String current_userid,current_usertoken,current_username,current_useremail,current_gender,current_mobile_no;
                    Log.d("response", response);
                    try {

                        JSONObject responseObj = new JSONObject(response);
                        String status = responseObj.getString("status");
                        Log.d("ELSE_HANDLER",status);
                        if(status.equals("not_register")) {
                            String REG_TOKEN = responseObj.getString("REG_TOKEN");
                            Log.d("NOT_REGITER","CALLED");
                            SharedPreferences.Editor editor = getSharedPreferences("DEVICE_ID",MODE_PRIVATE).edit();
                            editor.putString("REG_TOKEN",REG_TOKEN);
                            editor.apply();
                            DatabaseHelper databaseHelper = new DatabaseHelper(context);
                            databaseHelper.singOut();
                            Intent registerItent = new Intent(context, Mobile_NoRegisterActivity.class);
                            startActivity(registerItent);
                        } else {

                            try {
                                Log.d("ELSE_HANDLER","CALLED");
                                JSONObject obj = new JSONObject(response);
                                current_userid=obj.getString("id");
                                current_username=obj.getString("name");
                                current_useremail=obj.getString("email");
                                current_mobile_no=obj.getString("mobileNo");
                                SharedPreferences currentUserRecord=getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                                SharedPreferences.Editor editor=currentUserRecord.edit();
                                editor.putString("CurrentUser_name",current_username);
                                editor.putString("CurrentUser_id",current_userid);
                                editor.putString("CurrentUser_email",current_useremail);
                                editor.putString("CurrentUser_mobile_no",current_mobile_no);
                                editor.commit();
                                Log.d("FILE_USERNAME",""+current_username);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intentUserHome=new Intent(LoginActivity.this,MainUserHome.class);
                            startActivity(intentUserHome);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
            params.put("accessToken",AccessToken.getCurrentAccessToken().getToken().toString());
            return params;
        }
    };
    queue.add(stringRequest);
  }

    @Override
    protected void onDestroy() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.singOut();
        super.onDestroy();
    }
}
