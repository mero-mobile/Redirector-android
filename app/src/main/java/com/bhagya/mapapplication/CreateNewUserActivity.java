package com.bhagya.mapapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class CreateNewUserActivity extends Activity {
    EditText username,password,mobileno;
    Button signup;
    String usernamevalue,passwordvalue,mobilenoValue;
    AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        mobileno= (EditText) findViewById(R.id.mobileno);
        signup= (Button) findViewById(R.id.passwordbtn);

        aQuery=new AQuery(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernamevalue=username.getText().toString();
                passwordvalue=password.getText().toString();
                mobilenoValue=mobileno.getText().toString();

                String uploadurl="http://192.168.43.4/meromobile.com/insertuser.php";
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("username",usernamevalue);
                params.put("password",passwordvalue);
                params.put("phone",mobilenoValue);
                aQuery.ajax(uploadurl,params, JSONArray.class, new AjaxCallback<JSONArray>() {
                    @Override
                    public void callback(String url, JSONArray array, AjaxStatus status) {
                        super.callback(url, array, status);
                        Log.i("response", "REsponser:" + array);
                    }
                });
                username.setText("");
                password.setText("");
                mobileno.setText("");
                Toast.makeText(CreateNewUserActivity.this,"Singn up completed",Toast.LENGTH_LONG).show();

            }
        });

    }


    public void uploadData(){

    }
}
