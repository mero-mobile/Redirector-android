package com.bhagya.mapapplication.test;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bhagya.mapapplication.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TestServer extends Activity {
    Socket mSocket;
    final String URL = "http://10.0.2.2:4000/?accessToken=12345667";
    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_server);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Button b = findViewById(R.id.test);
        e = findViewById(R.id.editText1);

        try{
            JSONObject object = new JSONObject();
            object.put("id","bhagya");
            mSocket = IO.socket(URL);
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        mSocket.connect();
        if(mSocket.connected()) {
            Toast.makeText(TestServer.this, "Connected !!",Toast.LENGTH_SHORT).show();
        }

        mSocket.on("chat", handleInComingMessages);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "bhagya";
                String message = "hlw form android";
                JSONObject params = new JSONObject();
                try {
                    params.put("handle",name);
                    params.put("message",message);
                    mSocket.emit("chat",params);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


public  void jsonObjectRequest() {
    String url = "http://10.0.2.2:4000/auth/login";
    RequestQueue queue = Volley.newRequestQueue(this);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the response string.
                   Log.d("response",response);
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
            params.put("name", "bhagya");
            params.put("email", "bhagya.sah@yahoo.com");
            return params;
        }
    };
    queue.add(stringRequest);
}
 private   Emitter.Listener handleInComingMessages = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
//            Log.d("INCOMING_MSG", String.valueOf(data));
            try {
                Log.d("INCOMING_MESSAGE",data.getString("message").toString());
//                Toast.makeText(TestServer.this,data.getString("message"),Toast.LENGTH_LONG).show();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        mSocket.disconnect();
        super.onDestroy();
    }
}



