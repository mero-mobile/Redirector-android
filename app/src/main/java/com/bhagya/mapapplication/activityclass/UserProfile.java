package com.bhagya.mapapplication.activityclass;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhagya.mapapplication.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bhagya on 6/26/2017.
 */

public class UserProfile extends FragmentActivity implements  View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    TextView username;
    ImageView user_pics;
    Button logout;
    Profile profile;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    SignInButton signInButton;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bhagya.mapapplication.R.layout.activity_facebook);
        context = this;
        username = (TextView) findViewById(R.id.fb_username);
        user_pics = (ImageView) findViewById(R.id.profile_pics);
        // profile = (ProfilePictureView)findViewById(R.id.picture);
        logout = (Button) findViewById(R.id.logout);
        signInButton = (SignInButton) findViewById(R.id.google_signin);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
signInButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        signIn();
    }
});
        if (AccessToken.getCurrentAccessToken() != null) {
            RequestData();
            // details.setVisibility(View.VISIBLE);
            // share.setVisibility(View.VISIBLE);
        }
    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                        username.setText(Html.fromHtml(text));
                        // profile.setProfileId(json.getString("id"));
                        profile = Profile.getCurrentProfile();
                        Toast.makeText(UserProfile.this,""+profile.getProfilePictureUri(200,200).toString(),Toast.LENGTH_SHORT).show();
                        Picasso.with(context)
                                .load(profile.getProfilePictureUri(200, 200).toString())
                                .transform(new CropCircleTransformation())
                                .resize(150, 150)
                                .into(user_pics);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,REQ_CODE );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void signOut() {

    }

    private void handleResult(GoogleSignInResult result) {
        if(result.isSuccess())
        {
            GoogleSignInAccount account=result.getSignInAccount();
            String name=account.getDisplayName();
            String email=account.getEmail();
            username.setText(name+" "+email);
            String img_url = account.getPhotoUrl().toString();
            Picasso.with(context)
                    .load(img_url)
                    .transform(new CropCircleTransformation())
                    .resize(150, 150)
                    .into(user_pics);
        }

    }
}

