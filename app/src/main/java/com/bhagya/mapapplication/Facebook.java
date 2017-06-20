package com.bhagya.mapapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Facebook extends Activity {
WebView facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        facebook= (WebView) findViewById(R.id.facebook);
        facebook.loadUrl("https://www.instagram.com/");

    }
}
