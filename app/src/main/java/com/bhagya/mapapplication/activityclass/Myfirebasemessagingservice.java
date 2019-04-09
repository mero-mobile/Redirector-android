package com.bhagya.mapapplication.activityclass;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Bhagya on 6/7/2017.
 */

public class Myfirebasemessagingservice extends FirebaseMessagingService {
    Context context;
   // MediaPlayer mPlayer;
    DatabaseHelper databaseHelper;
    AQuery aQuery;
    Intent intentResult;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context=this;
        databaseHelper=new DatabaseHelper(context);
        intentResult=new Intent();
        aQuery=new AQuery(context);
        Log.d("Messge",remoteMessage.getData().get("body"));
       // Log.d("From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
             Log.d("Message data payload: ", String.valueOf(remoteMessage.getData().get("body")));
            if (remoteMessage.getData().get("body").equals("call@123#call")){
                String phoneno = remoteMessage.getData().get("title");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + phoneno));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(callIntent);
            }

                else  if (remoteMessage.getData().get("body").equals("playmp3")){
//                AudioManager am =
//                        (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//                am.setStreamVolume(
//                        AudioManager.STREAM_MUSIC,
//                        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
//                        0);
//                Log.d("MP3",remoteMessage.getData().get("body"));
//                    mPlayer = MediaPlayer.create(context, com.bhagya.mapapplication.R.raw.sanam);
//                    mPlayer.start();


                }
                else if (remoteMessage.getData().get("body").equals("registration_ok")){
                String name=remoteMessage.getData().get("name");
                String email=remoteMessage.getData().get("email");
                String mobile_no=remoteMessage.getData().get("mobile_no");
                Intent i = new Intent();
                i.setClass(this,UserWelcomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("name",name);
                i.putExtra("email",email);
                i.putExtra("mobile_no",mobile_no);
                startActivity(i);

            }
                else  if (remoteMessage.getData().get("body").equals("turnonwifi")){
                WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }

            else if(remoteMessage.getData().get("body").equals("findmyphone")){
                String REG_TOKEN=remoteMessage.getData().get("REG_TOKEN");
                Log.d("REG_TOEKN_REMOTE",remoteMessage.getData().get("REG_TOKEN"));
                SharedPreferences findmyphoneinfo = getSharedPreferences("Location_Request", MODE_PRIVATE);
                SharedPreferences.Editor editor = findmyphoneinfo.edit();
                editor.putString("REG_TOKEN",REG_TOKEN);
                editor.commit();
                responseInternet();
            }
            else if(remoteMessage.getData().get("body").equals("getlocation")){
                String remote_username=remoteMessage.getData().get("user_name");
                String remote_userid=remoteMessage.getData().get("user_id");
                String remote_mobileno=remoteMessage.getData().get("mobile_no");

                SharedPreferences location_request = getSharedPreferences("Location_Request", MODE_PRIVATE);

                SharedPreferences.Editor editor = location_request.edit();
                editor.putString("user_name",remote_username);
                editor.putString("user_id",remote_userid);
                editor.putString("mobile_no",remote_mobileno);
                editor.commit();
                Notification notification = new Notification();
                Notification.Builder builder=new Notification.Builder(context);
                Intent intentgps1=new Intent(context,LocationListnerActivity_Requestside.class);
                intentgps1.putExtra("mobile_no",remote_mobileno);
                intentgps1.putExtra("user_id",remote_userid);
                intentgps1.putExtra("user_name",remote_username);
              //  Toast.makeText(this,remote_mobileno,Toast.LENGTH_LONG).show();
                PendingIntent pendingIntent=PendingIntent.getActivity(context,102,intentgps1,0);
                notification=builder.setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("ticker")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle(remote_username)
                        .setContentText("Want to track you...").build();

                notification.sound = Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.tick);
                NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1102,notification);

            }
            else if(remoteMessage.getData().get("condition").equals("sms"))
            {
                Intent intent1 = new Intent(this, Myfirebasemessagingservice.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent1, 0);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(remoteMessage.getData().get("title"), null,remoteMessage.getData().get("body"), pi, null);

            }
            else if (remoteMessage.getData().get("condition").equals("responselocation")){
                String realdata=remoteMessage.getData().get("body");
                String user_name=remoteMessage.getData().get("user_name");
                String[] reallocation=realdata.split(":");
                String location=reallocation[1]+":"+reallocation[2]+":"+reallocation[3];
                String[] locationdata=location.split("@");

                String latitudedata=locationdata[0];
                String logitudedata=locationdata[1];
                String datetime=locationdata[2];
                SharedPreferences location_request = getSharedPreferences("FriendLocation_data", MODE_PRIVATE);

                SharedPreferences.Editor editor = location_request.edit();
                editor.putString("user_name",user_name);
                editor.putString("latitude",latitudedata);
                editor.putString("longitude",logitudedata);
                editor.putString("datetime",datetime);
                editor.commit();

                Notification notification  = new Notification();
                Notification.Builder builder=new Notification.Builder(context);
                Intent intentgps=new Intent(context,MapsActivity.class);
                intentgps.putExtra("latitude",latitudedata);
                intentgps.putExtra("longitude",logitudedata);
                intentgps.putExtra("datetime",datetime);
                intentgps.putExtra("user_name",user_name);
                PendingIntent pendingIntent=PendingIntent.getActivity(context,101,intentgps,0);
                notification=builder.setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("ticker")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle("Ready to Track")
                        .setContentText("Start tracking").build();
                notification.sound = Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.tick);
                NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1101,notification);
            }

        }

    }

    public void responseInternet(){
        String internet_username,internet_mobileno,internet_userid;
        SharedPreferences prfs = getSharedPreferences("Location_Request", MODE_PRIVATE);
        String  REG_TOKEN = prfs.getString("REG_TOKEN","");
      //  Toast.makeText(this,"Response garnalako number"+internet_mobileno+internet_username,Toast.LENGTH_LONG).show();
//        LocationInfo locationInfo=databaseHelper.getLocation();
        String uploadurl="http://104.155.153.31/firebase/FindMyphoneResponse.php";
        SharedPreferences locationprfs = getSharedPreferences("new_userid", MODE_PRIVATE);
       String user_id = locationprfs.getString("user_id", "");
        String local_username=locationprfs.getString("user_name","");
        // Toast.makeText(getActivity(),"user _id="+user_id, Toast.LENGTH_SHORT).show();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("REG_TOKEN",REG_TOKEN);
        params.put("user_name",local_username);
        params.put("condition","responselocation");
//        params.put("message","locationresponse:"
//                +locationInfo.latitudedataValue
//                +"@"+locationInfo.lognitudedataValue
//                +"@"+locationInfo.dataTime);
        params.put("title","9801074961");
        params.put("user_name",local_username);
        aQuery.ajax(uploadurl,params,JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray array, AjaxStatus status) {
                super.callback(url, array, status);
                Log.i("response", "REsponser:" + array);
            }
        });
    }

}
