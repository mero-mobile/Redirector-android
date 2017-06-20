package com.bhagya.mapapplication;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class LoginActivity extends Activity {
Button usernamebtn,passwordbtn;
    EditText username,password;
    ListView listView;
    AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        usernamebtn= (Button) findViewById(R.id.usernamebtn);
        passwordbtn= (Button) findViewById(R.id.passwordbtn);
        listView= (ListView) findViewById(R.id.listview);
        aQuery=new AQuery(this);
        usernamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            fetchData();
            }
        });

        passwordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,CreateNewUserActivity.class);
                startActivity(intent);

            }
        });

    }

    public void fetchData() {
        //String url="http://192.168.1.16/PanchviPass/select.php";
        String url="http://192.168.2.106/meromobile.com/selectuser.php";
        aQuery.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray array, AjaxStatus status) {
                super.callback(url, array, status);
                parseData(array);
                Log.i("response", "REsponser:" + array);
            }
        });
    }
    public void parseData(JSONArray array) {

      //  ArrayList<userinfo> list = new ArrayList<userinfo>();

      // for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(array.length()-1);
                String usernameValue,passwordValue;
             //  userinfo info = new userinfo();
                usernameValue= obj.getString("username");
                passwordValue = obj.getString("password");
                // info.email = obj.getString("email");
              //  list.add(info);
              if(usernameValue.equals(username.getText().toString()) && passwordValue.equals(password.getText().toString()))  {
                  Intent intent=new Intent(LoginActivity.this,BackiupActivity.class);
                  startActivity(intent);

              }
                else {
                   Toast.makeText(LoginActivity.this,"Wrong Username and Password",Toast.LENGTH_LONG).show();
              }
            } catch (JSONException e) {
                e.printStackTrace();
            }
      // populateData(list);
    }

    public void populateData(ArrayList<userinfo> list) {
        listAdapter adapter = new listAdapter(this, list);
        listView.setAdapter(adapter);
    }
}
