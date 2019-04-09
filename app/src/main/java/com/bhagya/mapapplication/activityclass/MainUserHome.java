package com.bhagya.mapapplication.activityclass;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.Wwboard;
import com.bhagya.mapapplication.adapter.WwBoardListAdapter;
import com.bhagya.mapapplication.services.UpLoadCallHistory;
import com.bhagya.mapapplication.services.UploadContactInfoService;
import com.bhagya.mapapplication.services.UploadInboxService;
import com.bhagya.mapapplication.services.UploadOutboxService;
import com.bhagya.mapapplication.services.socketTest;
import com.bhagya.mapapplication.socket.IncomingMessageHandler;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.bhagya.mapapplication.test.TestServer;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.github.nkzawa.emitter.Emitter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainUserHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {
    ImageView user_picture;
    TextView username,user_email;
    ListView wwboardmessageList;
    AppCompatImageView senderBtn;
    EditText boardmessage;
    DatabaseHelper db;
    Context context;
    public static  String currentUser_name;
    public static   String currentUser_gender,currentUser_email,currentUser_id;
    Profile profile;
    private GoogleApiClient googleApiClient;
    SocketConnection socketConnection = new SocketConnection();
    private static final int REQ_CODE = 9001;
    GoogleSignInResult result;
    Wwboard wwboardInfo = new Wwboard();

    @Override
    protected void onStart() {
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        Intent intent  = new Intent(this, IncomingMessageHandler.class);
        startService(intent);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        wwboardmessageList = findViewById(R.id.wwboardList);
        senderBtn = findViewById(R.id.senderbtn);
        boardmessage = findViewById(R.id.boardmessage);
        db = new DatabaseHelper(context);
        Log.d("WWBOARD_MESSAGE", String.valueOf(db.getListOfWwBoardMessage()));
        populateData(db.getListOfWwBoardMessage());
//       populateData(db.getListOfWwBoardMessage());
       // navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        username = (TextView) header.findViewById(R.id.uername_main);
        user_picture = (ImageView) header.findViewById(R.id.user_picture);

        if(AccessToken.getCurrentAccessToken() != null) {
            RequestData();
        }

        senderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prfs = getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
                final String user_id = prfs.getString("CurrentUser_id","");
                JSONObject object = new JSONObject();
                try {
                    object.put("message",boardmessage.getText().toString());
                    object.put("date", "238429842");
                    object.put("userId",user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socketConnection.rsocket.emit("wwboardmessage",object);
                boardmessage.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_user_home, menu);
       // username.setText("Bhagya sah");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Mboard) {
            // Handle the camera action
            Intent contactintent= new Intent(MainUserHome.this,MainUserHome.class);
            startActivity(contactintent);
        }
        else if (id == R.id.nav_contacts) {
            Intent contactintent= new Intent(MainUserHome.this,MainContactActivity.class);
            startActivity(contactintent);
        }
        else if (id == R.id.nav_sms) {
            Intent smsintent=new Intent(MainUserHome.this,MainSmsActivity.class);
            startActivity(smsintent);
        }
        else if (id == R.id.nav_remoteacss) {
           Intent remoteaccess =new Intent(MainUserHome.this,MainRemoteController_Activity.class);
            startActivity(remoteaccess);
        }

        else  if (id==R.id.nav_Settings){
            Intent settingsIntent=new Intent(MainUserHome.this,SettingActivity.class);
            startActivity(settingsIntent);
        }
        else if (id==R.id.nav_singout){
            db.singOut();
           LogoutFromFacebook();
           if(socketConnection.rsocket.connected()) {
               socketConnection.disconnectSocket();
           }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               e.printStackTrace();
           }
            Intent intent1 = new Intent(this,LoginActivity.class);
           intent1.putExtra("Intent","MainUserHome");
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            finish();
            super.onBackPressed();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void RequestData(){

        SharedPreferences prfs = getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
        final String user_name = prfs.getString("CurrentUser_name","");
        final String user_email=prfs.getString("CurrentUser_email","");

        Toast.makeText(MainUserHome.this,user_name+user_email,Toast.LENGTH_LONG).show();
                    String text1 = "<b>Name :</b> "+user_name+"<br><b>Email:</b>"+user_email;
                    username.setText(Html.fromHtml(text1));
                    profile= Profile.getCurrentProfile();
                   try {
                       Picasso.with(context)
                               .load(profile.getProfilePictureUri(200, 200).toString())
                               .transform(new CropCircleTransformation())
                               .resize(200, 200)
                               .into(user_picture);
                   }catch (Exception e){
                       Log.d("PROFILE_ERROR",e.getMessage());

                   }
                }

    public void LogoutFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);

            }
        }).executeAsync();

    }
    public void logoutFromGmail(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent intent=new Intent(MainUserHome.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                       onBackPressed();
                        // ...
                    }
                });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public  void currentUserRecords(){
        if(AccessToken.getCurrentAccessToken() != null){
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    // Log.d("Data Error", response.getRawResponse());
                    JSONObject json = response.getJSONObject();
                    try {
                        if(json != null){
                           currentUser_gender=json.getString("gender");
                            currentUser_name=json.getString("name");
                            currentUser_id=json.getString("id");
                            currentUser_email=json.getString("email");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Toast.makeText(this,""+currentUser_email+""+currentUser_name+""+currentUser_id+""+currentUser_gender,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
//       if(socketConnection.rsocket.connected()) {
//            socketConnection.disconnectSocket();
//        }
        super.onDestroy();
    }

    public void populateData(ArrayList<Wwboard> list) {
        Log.d("PARSE_DATADAPTER", String.valueOf(list));
        WwBoardListAdapter wwBoardListAdapter = new WwBoardListAdapter(context, list);
        wwboardmessageList.setAdapter(wwBoardListAdapter);
    }

}



