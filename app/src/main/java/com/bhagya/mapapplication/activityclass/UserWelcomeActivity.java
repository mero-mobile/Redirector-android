package com.bhagya.mapapplication.activityclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhagya.mapapplication.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class UserWelcomeActivity extends AppCompatActivity {
    ImageView remote_userimage;
    TextView remote_uername,remote_email,remote_contactno;
    Profile profile;
    String name,email,mobile_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        remote_userimage= (ImageView) findViewById(R.id.remotelogin_ImageView);
        remote_uername= (TextView) findViewById(R.id.remotelogin_username);
        remote_email= (TextView) findViewById(R.id.remotelogin_emailid);
        remote_contactno= (TextView) findViewById(R.id.remotelogin_phoneno);
        Intent remotedata=getIntent();
        name=remotedata.getStringExtra("user_name");
        email=remotedata.getStringExtra("user_email");
        mobile_no=remotedata.getStringExtra("user_mobile_no");
       RequestData();
    }
    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                if(json != null){
                   // String text = "<b>Name :</b> "+json.getString("name")+"<br><b>Email:<b>"+json.getString("email");
                    String text1 = "<b>Name :</b> "+name+"<br><b>Email :</b>"+email+"<br><b>Mobile No:<b>"+mobile_no;
                    remote_uername.setText(Html.fromHtml(text1));
                    // profile.setProfileId(json.getString("id"));
                    //Log.d("UserInfo",text1);
                    profile= Profile.getCurrentProfile();
                    Picasso.with(getApplicationContext())
                            .load("http://images.indianexpress.com/2018/01/kumkum-bhagya.jpg")
                            .transform(new CropCircleTransformation())
                            .resize(400,400)
                            .into(remote_userimage);
                }
            }
        });
        Toast.makeText(this,"user sucessfully login",Toast.LENGTH_LONG).show();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
