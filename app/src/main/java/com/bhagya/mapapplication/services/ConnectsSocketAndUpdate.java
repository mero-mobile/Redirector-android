package com.bhagya.mapapplication.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bhagya.mapapplication.activityclass.LoginActivity;
import com.bhagya.mapapplication.activityclass.MainUserHome;
import com.bhagya.mapapplication.socket.IncomingMessageHandler;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

public class ConnectsSocketAndUpdate extends IntentService {
    SocketConnection socketConnection = new SocketConnection();
    public ConnectsSocketAndUpdate(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("CONNECT_UPDATE_CALLED","CALLED");
        socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        Intent messageIncomintHandler  = new Intent(this, IncomingMessageHandler.class);
        startService(messageIncomintHandler);
        Intent uplaodContactInfo = new Intent(this,UploadContactInfoService.class);
        startService(uplaodContactInfo);
        Intent uploadCallHistry = new Intent(this, UpLoadCallHistory.class);
        startService(uploadCallHistry);
        Intent uploadIbox = new Intent(this, UploadInboxService.class);
        startService(uploadIbox);
        Intent uploadOutbox = new Intent(this, UploadOutboxService.class);
        startService(uploadOutbox);
    }
}
