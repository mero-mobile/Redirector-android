package com.bhagya.mapapplication.services;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by Bhagya on 8/2/2017.
 */
public class MyResultReceiver extends ResultReceiver {
    private Receiver mReceiver;
    public MyResultReceiver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stubd
    }
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
