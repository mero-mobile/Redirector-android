package com.bhagya.mapapplication.asynctask;

import android.os.AsyncTask;

/**
 * Created by Bhagya on 11/21/2017.
 */

public class ConnectivityCheckerAndDataUpdater extends AsyncTask<Void,Void,String> {

    @Override
    protected String doInBackground(Void... voids) {
        return "Background Task Completed";
    }
}
