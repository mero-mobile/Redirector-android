package com.bhagya.mapapplication;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class Turnon_onwifi extends Activity {
Button turnon,gpsturnon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnon_onwifi);
        turnon= (Button) findViewById(R.id.wifi);
        gpsturnon= (Button) findViewById(R.id.turnongps);

        gpsturnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
                sendBroadcast(intent);
            }
        });
//        PackageManager p = getPackageManager();
//        ComponentName componentName = new ComponentName(this,Turnon_onwifi.class);
//        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//        PackageManager p = getPackageManager();
//        ComponentName componentName = new ComponentName(this,Turnon_onwifi.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
//        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        turnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String data="9801074961@name is@bhagya sah";
//                String[] str_array = data.split("@");
//                String stringa = str_array[0];
//                String stringb = str_array[1];
//                String name=str_array[2];
               // Toast.makeText(Turnon_onwifi.this,""+stringa,Toast.LENGTH_LONG).show();
                WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
              //  boolean wifiEnabled = wifiManager.isWifiEnabled(); for checking status of wifi condition;
                // wifiManager.setWifiEnabled(false);
            }
        });
    }



    private boolean canToggleGPS() {
        PackageManager pacman = getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if(pacInfo != null){
            for(ActivityInfo actInfo : pacInfo.receivers){
                //test if recevier is exported. if so, we can toggle GPS.
                if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
                    return true;
                }
            }
        }

        return false; //default
    }
}
