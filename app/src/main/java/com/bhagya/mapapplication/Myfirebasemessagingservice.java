package com.bhagya.mapapplication;
import android.*;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Bhagya on 6/7/2017.
 */

public class Myfirebasemessagingservice extends FirebaseMessagingService {
Context context;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context=this;
        Log.d("Messge", "Message Received");
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


            }else {
                Intent intent1 = new Intent(this, Myfirebasemessagingservice.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent1, 0);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(remoteMessage.getData().get("title"), null,remoteMessage.getData().get("body"), pi, null);

            }




        }

//        if (remoteMessage.getNotification() != null){
//        Intent intent = new Intent(this,LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//       notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
//        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
//        notificationBuilder.setSmallIcon(R.mipmap.amiricanflag);
//        notificationBuilder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//       notificationManager.notify(0,notificationBuilder.build());
//        Intent intent1 = new Intent(this, Myfirebasemessagingservice.class);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent1, 0);
//        //  String mob="9843415412";
//        //Get the SmsManager instance and call the sendTextMessage method to send message
//
//
//if (remoteMessage.getNotification().getBody().equals("call@123#call")) {
//    String phoneno = remoteMessage.getNotification().getTitle();
//    Intent callIntent = new Intent(Intent.ACTION_CALL);
//    callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
//    callIntent.setData(Uri.parse("tel:" + phoneno));
//    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//        return;
//    }
//    context.startActivity(callIntent);
//}else {
//    SmsManager sms = SmsManager.getDefault();
//    sms.sendTextMessage(remoteMessage.getNotification().getTitle(), null, remoteMessage.getNotification().getBody(), pi, null);
//}}

    }
}
