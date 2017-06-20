package com.bhagya.mapapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Bhagya on 6/7/2017.
 */

public class Mysingleton {
    private static Mysingleton minstance;
    private static Context mctx;
    private RequestQueue requestQueue;
    private Mysingleton(Context context){
        mctx=context;
        requestQueue=getRequestQueue();


    }
    private  RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());

        }
        return requestQueue;
    }
    public static synchronized Mysingleton getMinstance(Context context){
        if (minstance==null){

         minstance=new Mysingleton(context);

        }
        return minstance;
    }
public<T> void addToRequestque(Request<T> request){
    getRequestQueue().add(request);

}
}
