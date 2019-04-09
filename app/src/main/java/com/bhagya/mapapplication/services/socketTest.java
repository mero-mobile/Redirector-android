package com.bhagya.mapapplication.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;

public class socketTest extends IntentService {

    public socketTest(String name) {
        super("socketTest");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SocketConnection socketConnection = new SocketConnection();
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {

//        Toast.makeText(this,"socket connected",Toast.LENGTH_LONG).show();

        super.onCreate();
    }
//    private Emitter.Listener handleIncomingMessage = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            socketConnection.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        JSONArray data = new JSONArray(args[0]);
//                        Log.d("DATA_SERVICE_SOCKET", String.valueOf(data));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    };

}
