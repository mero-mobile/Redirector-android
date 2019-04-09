package com.bhagya.mapapplication.activityclass;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bhagya.mapapplication.R;
import com.bhagya.mapapplication.socket.SocketConnection;
import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationListnerActivity_Requestside extends AppCompatActivity implements LocationListener {
    String uploadurl="http://104.155.153.31/meromobile.com/insertgps.php";
    LocationManager locationManager;
    String mprovider;
    LocationListener listener=this;
    ListView listView;
    LocationInfo locationInfo;
    AQuery aQuery;
    Button turnongps,turnoffgps;
    String mobilenumber,user_id,internet_username,internet_mobileno,internet_userid;
    DatabaseHelper databaseHelper;
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    Button response_sms,resposnse_internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bhagya.mapapplication.R.layout.activity_location_listner__requestside);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        response_sms= (Button) findViewById(R.id.response_sms);
        locationInfo=new LocationInfo();
        //Intent intetgps4=getIntent();
        SharedPreferences remotemobile_no =getSharedPreferences("Remotemobile_no", MODE_PRIVATE);
        mobilenumber=remotemobile_no.getString("mobile_no","");
        databaseHelper=new DatabaseHelper(this);
        SharedPreferences prfs = getSharedPreferences("Location_Request", MODE_PRIVATE);
        internet_username = prfs.getString("user_name","");
        internet_mobileno=prfs.getString("mobile_no","");
        internet_userid=prfs.getString("user_id","");
        Toast.makeText(this,"this is mobile no that we respond"+mobilenumber,Toast.LENGTH_LONG).show();
        resposnse_internet= (Button) findViewById(R.id.response_intenet);
        response_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseSms();
            }
        });
        resposnse_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseInternet();
            }
        });

        Criteria criteria = new Criteria();
        aQuery=new AQuery(this);
        listView= (ListView) findViewById(R.id.listview);
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider,1000,0,listener);
            if (location != null){
                onLocationChanged(location);}
            else{
                Toast.makeText(getBaseContext(), "No Location Provider Found", Toast.LENGTH_SHORT).show();
            }}




        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) LocationListnerActivity_Requestside.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(LocationListnerActivity_Requestside.this)) {
            Toast.makeText(LocationListnerActivity_Requestside.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
            // finish();
        }
        // Todo Location Already on  ... end

        if(!hasGPSDevice(LocationListnerActivity_Requestside.this)){
            Toast.makeText(LocationListnerActivity_Requestside.this,"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(LocationListnerActivity_Requestside.this)) {
            Log.e("CheckGPS","Gps already enabled");
            Toast.makeText(LocationListnerActivity_Requestside.this,"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        }else{
            Log.e("CheckGPS","Gps already enabled");
            Toast.makeText(LocationListnerActivity_Requestside.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(LocationListnerActivity_Requestside.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(LocationListnerActivity_Requestside.this, REQUEST_LOCATION);

                                // finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView longitude = (TextView) findViewById(R.id.latitude);
        TextView latitude = (TextView) findViewById(R.id.longitude);
        double langitude1=location.getLatitude();
        double longitude1=location.getLongitude();
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        longitude.append("Longitude:" + location.getLongitude()+"\n"+"Latitude:"+location.getLatitude()+"\nDate "+currentDateTime+"\n"+"--------------------------"+"\n");
        //latitude.append("Current Latitude:" + location.getLatitude());
        //Toast.makeText(locationListner.class,""+langitude1,Toast.LENGTH_LONG).show();
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        ContentValues cv=new ContentValues();
        cv.put("Latitude",langitude1);
        cv.put("Longitude",longitude1);
        cv.put("dateTime",currentDateTime);
        databaseHelper.insertData(cv,"location");

    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {

    }
    @Override
    public void onProviderDisabled(String s) {
    }

    public void responseSms(){
//    LocationInfo locationInfo=databaseHelper.getLocation();

        SharedPreferences prfs = getSharedPreferences("new_userid", MODE_PRIVATE);
        user_id = prfs.getString("user_id", "");
        String local_username=prfs.getString("user_name","");
        Intent intent1=new Intent(LocationListnerActivity_Requestside.this,read_Incomingsms.class);
        PendingIntent pi=PendingIntent.getActivity(LocationListnerActivity_Requestside.this, 0, intent1,0);
        SmsManager sms=SmsManager.getDefault();
        Toast.makeText(this,mobilenumber,Toast.LENGTH_LONG).show();
        sms.sendTextMessage(mobilenumber, null,"locationresponse:"
                +locationInfo.latitudedataValue
                +"@"+locationInfo.lognitudedataValue
                +"@"+locationInfo.dataTime
                +"@"+local_username, pi,null);
       // Log.d("latitude",locationInfo.latitudedataValue);
        Toast.makeText(this,locationInfo.latitudedataValue+"\n"+local_username,Toast.LENGTH_LONG).show();

    }

    public void responseInternet() {

        Toast.makeText(this,"Response garnalako number"+internet_mobileno+internet_username,Toast.LENGTH_LONG).show();
        LocationInfo locationInfo=databaseHelper.getLastLocation();
        SharedPreferences prfs = getSharedPreferences("Location_Request", MODE_PRIVATE);
        user_id = prfs.getString("user_id", "");
        SharedPreferences userpref = getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
        String respondedUserId = userpref.getString("CurrentUser_id","");
        Toast.makeText(getApplicationContext(),"user _id="+user_id, Toast.LENGTH_SHORT).show();
       JSONObject params = new JSONObject();

        try {
            params.put("userId",user_id);
            params.put("respondedUserId",respondedUserId);
            params.put("latitude",locationInfo.latitudedataValue);
            params.put("longitude",locationInfo.lognitudedataValue);
            params.put("dateTime",locationInfo.dataTime);
            SocketConnection socketConnection = new SocketConnection();
            socketConnection.connectSocket(AccessToken.getCurrentAccessToken().getToken());
            socketConnection.rsocket.emit("sendLocationResponse",params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}