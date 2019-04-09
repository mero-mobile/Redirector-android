package com.bhagya.mapapplication.socket;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.activityclass.LocationInfo;
import com.bhagya.mapapplication.activityclass.LocationListnerActivity_Requestside;
import com.bhagya.mapapplication.activityclass.MainUserHome;
import com.bhagya.mapapplication.activityclass.MapsActivity;
import com.bhagya.mapapplication.activityclass.homePage;
import com.facebook.AccessToken;
import com.facebook.share.Share;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class IncomingMessageHandler extends IntentService {

    public IncomingMessageHandler() {
        super("IncomingMessageHandler");
    }

    SocketConnection socketConnection = new SocketConnection();
    DatabaseHelper databaseHelper;
    Context context;
    MediaPlayer mPlayer;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
       socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = this;
        databaseHelper = new DatabaseHelper(this);
           Log.d("INCOMING_MESSAHE_HANDER","CALLED");
            socketConnection.rsocket.on("initialData",handleIncomingData);
            socketConnection.rsocket.on("wwboardmessage", wwBoardMesssageHandler);
            socketConnection.rsocket.on("call",callHandler);
            socketConnection.rsocket.on("sendMessage", sendMessageHandler);
            socketConnection.rsocket.on("locationRequest",locationRequestHandler);
            socketConnection.rsocket.on("getLocationResponse",locationResponseHandler);
        socketConnection.rsocket.on("findMyPhoneRequest", findMyPhoneRequestHandler);
        socketConnection.rsocket.on("findMyPhoneResponse",findMyPhoneResponseHandler);
//            socketConnection.rsocket.on("ringMyPhone",ringMyPhoneHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public Emitter.Listener handleIncomingData = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.d("INCOMING_DATA", String.valueOf(args[0]));
            JSONObject obj = (JSONObject)args[0];
            try {
                JSONArray wwboardJSONArray = new JSONArray(obj.getString("wwBoardMessage"));
                for(int i=0; i < wwboardJSONArray.length(); i++) {
                    JSONObject obj1 = new JSONObject(wwboardJSONArray.get(i).toString());
                    ContentValues cv = new ContentValues();
                    cv.put("id",obj1.getString("id"));
                    cv.put("message",obj1.getString("message"));
                    cv.put("date",obj1.getString("date"));
                    databaseHelper.insertData(cv,"wwBoard");
                        }
                JSONArray contactJSONArray = new JSONArray(obj.getString("contacts"));
                for(int i=0; i < contactJSONArray.length(); i++) {
                    JSONObject obj1 = new JSONObject(contactJSONArray.get(i).toString());
                    ContentValues cv = new ContentValues();
                    cv.put("contactId",obj1.getString("contactId"));
                    cv.put("contactNo",obj1.getString("contactNo"));
                    cv.put("contactName",obj1.getString("contactName"));
                    databaseHelper.insertData(cv,"contacts");
                }

                JSONArray calLogsJSONArray = new JSONArray(obj.getString("calLogs"));
                for(int i=0; i < calLogsJSONArray.length(); i++) {
                    JSONObject obj1 = new JSONObject(calLogsJSONArray.get(i).toString());
                    ContentValues cv = new ContentValues();
                    cv.put("callLogId",obj1.getString("callLogId"));
                    cv.put("contactName",obj1.getString("contactName"));
                    cv.put("contactNo",obj1.getString("contactNo"));
                    cv.put("dateTime",obj1.getString("dateTime"));
                    cv.put("callDuration",obj1.getString("callDuration"));
                    cv.put("callType",obj1.getString("callType"));
                    databaseHelper.insertData(cv,"calLogs");
                }

                JSONArray inboxJSONArray = new JSONArray(obj.getString("inbox"));
                for(int i=0; i < inboxJSONArray.length(); i++) {
                    JSONObject obj1 = new JSONObject(inboxJSONArray.get(i).toString());
                    ContentValues cv = new ContentValues();
                    cv.put("smsId",obj1.getString("smsId"));
                    cv.put("timeStamps",obj1.getString("timeStamps"));
                    cv.put("contactNo",obj1.getString("contactNo"));
                    cv.put("content",obj1.getString("content"));
                    databaseHelper.insertData(cv,"inbox");
                }

                JSONArray outboxJSONArray = new JSONArray(obj.getString("outbox"));
                for(int i=0; i < outboxJSONArray.length(); i++) {
                    JSONObject obj1 = new JSONObject(outboxJSONArray.get(i).toString());
                    ContentValues cv = new ContentValues();
                    cv.put("smsId",obj1.getString("smsId"));
                    cv.put("timeStamps",obj1.getString("timeStamps"));
                    cv.put("contactNo",obj1.getString("contactNo"));
                    cv.put("content",obj1.getString("content"));
                    databaseHelper.insertData(cv,"outbox");
                }

                Log.d("INCOMING_DATA", String.valueOf(wwboardJSONArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

 public  Emitter.Listener wwBoardMesssageHandler = new Emitter.Listener() {
     @Override
     public void call(Object... args) {
         Log.d("wwBordMessage", String.valueOf(args[0]));
         JSONObject obj1 = (JSONObject)args[0];
         ContentValues cv = new ContentValues();
         try {
             cv.put("id",obj1.getString("id"));
             cv.put("message",obj1.getString("message"));
             cv.put("date",obj1.getString("date"));
             databaseHelper.insertData(cv,"wwBoard");
         } catch (JSONException e) {
             e.printStackTrace();
         }

         Intent intent = new Intent(IncomingMessageHandler.this, MainUserHome.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);
     }
 };

 public Emitter.Listener callHandler = new Emitter.Listener() {
     @Override
     public void call(Object... args) {
         Log.d("CALL_HANDLER_INCOMING", String.valueOf(args[0]));
         JSONObject jsonObject = (JSONObject) args[0];
         SharedPreferences preferences = getSharedPreferences("DEVICE_ID",MODE_PRIVATE);
         String mobileRegToken =  preferences.getString("REG_TOKEN","Undefined");
         try {
             String REG_TOKEN =  jsonObject.getString("regToken");
             String mobileNo = jsonObject.getString("contactNo");

             if (REG_TOKEN.equals(mobileRegToken)) {
                 Intent callIntent = new Intent(Intent.ACTION_CALL);
                 callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
                 callIntent.setData(Uri.parse(String.format("tel:%s",Uri.encode(mobileNo))));
                 if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                     return;
                 }
                 getApplicationContext().startActivity(callIntent);
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
 };

 public Emitter.Listener sendMessageHandler = new Emitter.Listener() {
     @Override
     public void call(Object... args) {
         String message,mobileNo,regToken;
         JSONObject obj = (JSONObject)args[0];
         Log.d("MESSAGETOBE_SEND", String.valueOf(obj));
         try {
             message = obj.getString("message");
             mobileNo = obj.getString("mobileNo");
             regToken = obj.getString("regToken");
             sendSMS(mobileNo, message,regToken);

         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
 };

 public Emitter.Listener locationRequestHandler = new Emitter.Listener() {
     @Override
     public void call(Object... args) {
         JSONObject jsonObject = (JSONObject)args[0];
         Log.d("locationRequestListner", String.valueOf(jsonObject));

         try {

             String  remote_username = jsonObject.getString("userName");
             String remote_userid= jsonObject.getString("userId");
             String remote_mobileno= jsonObject.getString("mobileNo");
             SharedPreferences location_request = getSharedPreferences("Location_Request", MODE_PRIVATE);
             SharedPreferences.Editor editor = location_request.edit();
             editor.putString("user_name",remote_username);
             editor.putString("user_id",remote_userid);
             editor.putString("mobile_no",remote_mobileno);
             editor.commit();
             Intent intent = new Intent(IncomingMessageHandler.this,LocationListnerActivity_Requestside.class);
             showNotification(context,remote_username,remote_mobileno,intent);

         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
 };

 public Emitter.Listener locationResponseHandler = new Emitter.Listener() {
     @Override
     public void call(Object... args) {
         String latitude,longitude,dateTime,userName;
         Log.d("getLocationResponse", String.valueOf(args[0]));
         JSONObject jsonObject = (JSONObject)args[0];

         try {
            latitude= jsonObject.getString("latitude");
            longitude= jsonObject.getString("longitude");
            dateTime = jsonObject.getString("dateTime");
            userName = jsonObject.getString("userName");
            SharedPreferences.Editor editor = getSharedPreferences("Location_Response",MODE_PRIVATE).edit();
            editor.putString("latitude",latitude);
            editor.putString("longitude",longitude);
            editor.putString("dateTime",dateTime);
            editor.putString("userName",userName);
            editor.apply();
            Intent intent = new Intent(context, MapsActivity.class);
            showNotification(context,userName,"response",intent);

         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
 };

  public  Emitter.Listener ringMyPhoneHandler = new Emitter.Listener() {
      @Override
      public void call(Object... args) {
          Log.d("RING_MY_PHONE", String.valueOf(args[0]));
          AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        0);
                    mPlayer = MediaPlayer.create(context, com.bhagya.mapapplication.R.raw.sanam);
                    mPlayer.start();
      }
  };



    public Emitter.Listener findMyPhoneRequestHandler = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.d("findMyPhoneRequest", String.valueOf(args[0]));
            SharedPreferences preferences = getSharedPreferences("DEVICE_ID",MODE_PRIVATE);
            String REG_TOKEN = preferences.getString("REG_TOKEN","Undefind");
            SharedPreferences preferencesuser = getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
            String userId = preferencesuser.getString("user_id", "");
            JSONObject jsonObject = (JSONObject)args[0];

            try {
                String regToken = jsonObject.getString("regToken");
//                Intent intent = new Intent(context,LocationListnerActivity_Requestside.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
//               context.startActivity(intent);
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                LocationInfo locationInfo = databaseHelper.getLastLocation();
                jsonObject.put("latitude",locationInfo.latitudedataValue);
                jsonObject.put("longitude",locationInfo.lognitudedataValue);
                jsonObject.put("dateTime",locationInfo.dataTime);
                jsonObject.put("userId",userId);
                if(regToken.equals(REG_TOKEN)){
                    socketConnection.rsocket.emit("findMyPhoneResponse",jsonObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public  Emitter.Listener findMyPhoneResponseHandler = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String latitude,longitude,dateTime;
            JSONObject obj = (JSONObject) args[0];
            try {
                latitude = obj.getString("latitude");
                longitude = obj.getString("longitude");
                dateTime = obj.getString("dateTime");
                SharedPreferences.Editor location = getSharedPreferences("Location_Response",MODE_PRIVATE).edit();
                location.putString("latitude",latitude);
                location.putString("longitude",longitude);
                location.putString("dateTime",dateTime);
                location.apply();
                Intent mapIntent = new Intent(context,MapsActivity.class);
                showNotification(context,"Redirector","Your phone is ready to track",mapIntent);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };



    public void sendSMS(String phoneNo, String msg,String regToken) {
        SharedPreferences preferences = getSharedPreferences("DEVICE_ID",MODE_PRIVATE);
        String mobileRegToken =  preferences.getString("REG_TOKEN","Undefined");

        if(mobileRegToken.equals(regToken)) {
            try {
                Log.d("MESSAGETOBE_SEND", mobileRegToken + "remoteToken="+regToken);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void showNotification(Context context, String title, String body, Intent intent) {

       if(body.equals("response")){
           NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           int notificationId = 2;
           String channelId = "channel-02";
           String channelName = "Channel response";
           int importance = NotificationManager.IMPORTANCE_HIGH;

           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
               NotificationChannel mChannel = new NotificationChannel(
                       channelId, channelName, importance);
               notificationManager.createNotificationChannel(mChannel);
           }
           NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                   .setSmallIcon(R.mipmap.ic_launcher)
                   .setContentTitle("Redirector Location Response")
                   .setAutoCancel(true)
                   .setTicker("Ticker")
                   .setContentText("Location response from"+title);
           TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
           stackBuilder.addNextIntent(intent);
           PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                   0,
                   PendingIntent.FLAG_UPDATE_CURRENT
           );
           mBuilder.setContentIntent(resultPendingIntent);

           notificationManager.notify(notificationId, mBuilder.build());
       } else {
           NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           int notificationId = 1;
           String channelId = "channel-01";
           String channelName = "Channel Name";
           int importance = NotificationManager.IMPORTANCE_HIGH;

           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
               NotificationChannel mChannel = new NotificationChannel(
                       channelId, channelName, importance);
               notificationManager.createNotificationChannel(mChannel);
           }
           NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                   .setSmallIcon(R.mipmap.ic_launcher)
                   .setContentTitle("Redirector Location Request")
                   .setAutoCancel(true)
                   .setTicker("Ticker")
                   .setContentText(title+" want to track you "+"("+body+")");
           TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
           stackBuilder.addNextIntent(intent);
           PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                   0,
                   PendingIntent.FLAG_UPDATE_CURRENT
           );
           mBuilder.setContentIntent(resultPendingIntent);
           notificationManager.notify(notificationId, mBuilder.build());
       }
    }

}
