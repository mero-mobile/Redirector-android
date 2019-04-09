package com.bhagya.mapapplication.socket;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import com.bhagya.mapapplication.Wwboard;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.services.UploadContactInfoService;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SocketConnection extends Activity {

//    static SocketConnection socketConnection = new SocketConnection();
    public  Socket rsocket;
//    public  static SocketConnection getInstance() {
//        return  socketConnection;
//    }
//    DatabaseHelper db = new DatabaseHelper(this);

    public void  connectSocket(String token) {
        final String URL = "http://192.168.0.105:4000/?accessToken="+token;
        Log.d("ULR_LINK",URL);
        try {
            rsocket = IO.socket(URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        rsocket.connect();
    }

    public  void disconnectSocket() {
        rsocket.disconnect();
    }

    public boolean isSocketConnected() {
        return  rsocket.connected();
    }

    public  void storeIntialData() {
      if(isSocketConnected()) {

      }
    }
}
