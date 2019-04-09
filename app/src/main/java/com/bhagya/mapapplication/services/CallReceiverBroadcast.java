package com.bhagya.mapapplication.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.activityclass.DatabaseHelper;
import com.bhagya.mapapplication.activityclass.LocationInfo;
import com.bhagya.mapapplication.activityclass.LocationListnerActivity_Requestside;
import com.bhagya.mapapplication.activityclass.LoginActivity;
import com.bhagya.mapapplication.activityclass.MainUserHome;
import com.bhagya.mapapplication.activityclass.ManaulBackupActivity;
import com.bhagya.mapapplication.activityclass.MapsActivity;
import com.bhagya.mapapplication.socket.IncomingMessageHandler;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.games.internal.player.StockProfileImageRef;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CallReceiverBroadcast extends BroadcastReceiver {
    SocketConnection socketConnection = new SocketConnection();
    Context mainContex;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast toast = Toast.makeText(context, "phone state change", Toast.LENGTH_LONG);
        toast.show();
        mainContex = context;
        String statesStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
//        if(statesStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//            Intent SocketIntent = new Intent(context,IncomingMessageHandler.class);
//            SocketIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
//            context.startService(SocketIntent);
//            socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
            socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
            socketConnection.rsocket.on("call", callHandler);
            socketConnection.rsocket.on("sendMessage", sendMessageHandler);
            socketConnection.rsocket.on("locationRequest",locationRequestHandler);
            socketConnection.rsocket.on("getLocationResponse",locationResponseHandler);
            socketConnection.rsocket.on("ringMyPhone",ringMyPhoneHandler);
            socketConnection.rsocket.on("findMyPhoneRequest", findMyPhoneRequestHandler);
            socketConnection.rsocket.on("findMyPhoneResponse",findMyPhoneResponseHandler);
            uploadCallhistory(context, socketConnection);
            uploadContacts(context, socketConnection);
            uplaodInbox(context, socketConnection);

//        }
    }

    public Emitter.Listener callHandler = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("CALL_HANDLER_CALLRECIEV", String.valueOf(args[0]));
            JSONObject jsonObject = (JSONObject) args[0];
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("DEVICE_ID", MODE_PRIVATE);
            String mobileRegToken = preferences.getString("REG_TOKEN", "Undefined");
            try {
                String REG_TOKEN = jsonObject.getString("regToken");

                if (REG_TOKEN.equals(mobileRegToken)) {
                    String mobileNo = jsonObject.getString("contactNo");
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse(String.format("tel:%s", Uri.encode(mobileNo))));
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

    public void uploadContacts(Context context, SocketConnection socketConnection) {
        String contactName, contactNo, contactId, userId;
        Cursor cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            Log.d("CONTACTID", contactId);
            SharedPreferences preferences = context.getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
            userId = preferences.getString("user_id", "");
            JSONObject params = new JSONObject();
            try {
                params.put("userId", userId);
                params.put("contactName", contactName);
                params.put("contactNo", contactNo);
                params.put("contactId", contactId);
                socketConnection.rsocket.emit("uploadContact", params);
                Log.d("CONTACT_DATA_UPLOADED", String.valueOf(params));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadCallhistory(Context context, SocketConnection socketConnection) {
        String strOrder1 = android.provider.CallLog.Calls.DATE + " DESC";
        String callType, callDuration, mobileNumber, dateTime, name, countryIso, callHistoryId, userId;
        SharedPreferences preferences = context.getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
        userId = preferences.getString("user_id", "");
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, strOrder1);
//        Log.d("CALLHISTORY_CALLED","inside callhisty method+mobileNumber+callDuration+callType+userId+realDateTime");

        while (cursor.moveToNext()) {
            callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            callHistoryId = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
            callDuration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            mobileNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            countryIso = cursor.getString(cursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO));
            dateTime = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));

            Date realDateTime = new Date(Long.valueOf(dateTime));
            String stringCallType = null;
            int typecode = Integer.parseInt(callType);
            switch (typecode) {

                case CallLog.Calls.INCOMING_TYPE:
                    stringCallType = "Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    stringCallType = "Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    stringCallType = "Missed";
            }

            JSONObject paramsUpload = new JSONObject();
            try {

                paramsUpload.put("contactName", name);
                paramsUpload.put("countryIso", countryIso);
                paramsUpload.put("contactNo", mobileNumber);
                paramsUpload.put("callType", stringCallType);
                paramsUpload.put("dateTime", dateTime);
                paramsUpload.put("callDuration", callDuration);
                paramsUpload.put("callLogId", callHistoryId);
                paramsUpload.put("userId", userId);
                socketConnection.rsocket.emit("uploadCallLogs", paramsUpload);
                Log.d("CALLHISTORY_CALLED", name + mobileNumber + callDuration + callType + userId + realDateTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    public void uploadOutbox(Context context, SocketConnection socketConnection) {
        SharedPreferences preferences = context.getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
        String userId = preferences.getString("user_id", "");
        Uri sentURI = Uri.parse("content://sms/sent");
        Cursor cursor = context.getContentResolver().query(sentURI, new String[]{"_id", "address", "date", "body", "thread_id"}, null, null, null);
        cursor.moveToFirst();

        while (cursor.moveToNext()); {
            String smsId, address, body, date, thread_id;
            JSONObject params = new JSONObject();
            smsId = cursor.getString(0);
            address = cursor.getString(1);
            body = cursor.getString(3);
            date = cursor.getString(2);
            Log.d("OUTBOX", body);
            try {
                params.put("smsId", smsId);
                params.put("contactNo", address);
                params.put("content", body);
                params.put("timeStamps", date);
                params.put("userId", userId);
                socketConnection.rsocket.emit("uploadOutbox", params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }




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
                SharedPreferences location_request = getApplicationContext().getSharedPreferences("Location_Request", MODE_PRIVATE);
                SharedPreferences.Editor editor = location_request.edit();
                editor.putString("user_name",remote_username);
                editor.putString("user_id",remote_userid);
                editor.putString("mobile_no",remote_mobileno);
                editor.commit();
                Intent intent = new Intent(mainContex,LocationListnerActivity_Requestside.class);
                showNotification(mainContex,remote_username,remote_mobileno,intent);

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
                SharedPreferences.Editor editor = mainContex.getSharedPreferences("Location_Response",MODE_PRIVATE).edit();
                editor.putString("latitude",latitude);
                editor.putString("longitude",longitude);
                editor.putString("dateTime",dateTime);
                editor.putString("userName",userName);
                editor.apply();
                Intent intent = new Intent(mainContex, MapsActivity.class);
                showNotification(mainContex,userName,"response",intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public  Emitter.Listener ringMyPhoneHandler = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MediaPlayer mPlayer;
            Log.d("RING_MY_PHONE", String.valueOf(args[0]));
            JSONObject jsonObject = (JSONObject)args[0];
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("DEVICE_ID", MODE_PRIVATE);
            String mobileRegToken = preferences.getString("REG_TOKEN", "Undefined");
            try {
                String REG_TOKEN = jsonObject.getString("regToken");
                if (REG_TOKEN.equals(mobileRegToken)) {
                    AudioManager am = (AudioManager)mainContex.getSystemService(Context.AUDIO_SERVICE);
                    am.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                            0);
                    mPlayer = MediaPlayer.create(mainContex, com.bhagya.mapapplication.R.raw.sanam);
                    mPlayer.start();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };



    public void sendSMS(String phoneNo, String msg,String regToken) {
        SharedPreferences preferences = mainContex.getSharedPreferences("DEVICE_ID",MODE_PRIVATE);
        String mobileRegToken =  preferences.getString("REG_TOKEN","Undefined");
        Log.d("MESSAGETOBE_SEND", mobileRegToken + "remoteToken="+regToken);

        if(mobileRegToken.equals(regToken)) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }




    public Emitter.Listener findMyPhoneRequestHandler = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.d("findMyPhoneRequest", String.valueOf(args[0]));
            SharedPreferences preferences = mainContex.getSharedPreferences("DEVICE_ID",MODE_PRIVATE);
            String REG_TOKEN = preferences.getString("REG_TOKEN","Undefind");
            SharedPreferences preferencesuser = mainContex.getSharedPreferences("MAIN_USER_RECORD", MODE_PRIVATE);
            String userId = preferencesuser.getString("user_id", "");
            JSONObject jsonObject = (JSONObject)args[0];

            try {
                String regToken = jsonObject.getString("regToken");
//                Intent intent = new Intent(mainContex,LocationListnerActivity_Requestside.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NEW_TASK);
//                mainContex.startActivity(intent);
                DatabaseHelper databaseHelper = new DatabaseHelper(mainContex);
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
                SharedPreferences.Editor location = mainContex.getSharedPreferences("Location_Response",MODE_PRIVATE).edit();
                location.putString("latitude",latitude);
                location.putString("longitude",longitude);
                location.putString("dateTime",dateTime);
                location.apply();
                Intent mapIntent = new Intent(mainContex,MapsActivity.class);
                showNotification(mainContex,"Redirector","Your phone is ready to track",mapIntent);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };



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
