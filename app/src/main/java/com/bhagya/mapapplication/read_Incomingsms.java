package com.bhagya.mapapplication;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Bhagya on 1/16/2017.
 */
public class read_Incomingsms extends BroadcastReceiver {
    private String TAG = read_Incomingsms.class.getSimpleName();
    String messege,mobileno;
    MediaPlayer mPlayer ;
    private Context ctx;
    AQuery aQuery;
    public read_Incomingsms() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        mPlayer   = MediaPlayer.create(context, R.raw.sanam);
        SmsMessage[] msgs = null;
        ctx=context;
        aQuery=new AQuery(context);
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



            String uploadurl="http://192.168.43.162/meromobile.com/inboxsms.php";
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("phoneno",mobileno);
            params.put("sms",messege);
            params.put("date","203910390");
            aQuery.ajax(uploadurl,params, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray array, AjaxStatus status) {
                    super.callback(url, array, status);
                    Log.i("response", "REsponser:" + array);
                }
            });
            String[]  sms_array = messege.split(":");
            String realsms = sms_array[0];
            String no = sms_array[1];
            if (realsms.equals("playmp3"))
            {
                mPlayer.start();
                Intent intentone = new Intent(context.getApplicationContext(), Newincoming_callrecieve.class);
                intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentone);
            }
            if(messege.equals("stopmp3"))
            {
                mPlayer.pause();
                mPlayer.stop();
            }



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
            if(realsms.equals("turnonwifi")){
                WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }
           if (realsms.equals("shutdown"))
           {
               try {
                   Process proc = Runtime.getRuntime()
                           .exec(new String[]{ "su", "-c", "reboot -p" });
                   proc.waitFor();
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
            if(realsms.equals("call")) {
                String phoneno="9801074961";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" +phoneno));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
              context. startActivity(callIntent);
            }
            if (realsms.equals("getlocation")) {
           Intent intent1=new Intent(context,read_Incomingsms.class);
           PendingIntent pi=PendingIntent.getActivity(context, 0, intent1,0);
         //  String mob="9843415412";
            //Get the SmsManager instance and call the sendTextMessage method to send message
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(no, null,"Latitude:27.63779788"+"  longitude:85.32978869", pi,null);
                // String data="9801074961@name is@bhagya sah";
            }
            if(no.length()>=10) {
                Intent intent1 = new Intent(context, read_Incomingsms.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, 0);
                //  String mob="9843415412";
                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(no, null, realsms, pi, null);
            }
            Log.d(TAG, str);
            Toast.makeText(context,str,Toast.LENGTH_LONG).show();
        }
    }
}