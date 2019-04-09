package com.bhagya.mapapplication.activityclass;

import android.Manifest;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bhagya.mapapplication.R;
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

import java.util.List;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.support.v4.app.ActivityCompat;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class locationListner extends Activity implements LocationListener {
    //String url="http://192.168.0.102/PanchviPass/select.php";
    String uploadurl="http://104.155.153.31/meromobile.com/insertgps.php";
    LocationManager locationManager;
    String mprovider;
    LocationListener listener=this;
    ListView listView;
    AQuery aQuery;
    Button turnongps,turnoffgps;
    DatabaseHelper databaseHelper;
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_listner);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        aQuery=new AQuery(this);
        listView= (ListView) findViewById(R.id.listview);
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider,1000,0,listener);
            if (location != null){
                onLocationChanged(location);}
            else{
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }}




        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) locationListner.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(locationListner.this)) {
            Toast.makeText(locationListner.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
           // finish();
        }
        // Todo Location Already on  ... end

        if(!hasGPSDevice(locationListner.this)){
            Toast.makeText(locationListner.this,"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(locationListner.this)) {
            Log.e("CheckGPS","Gps already enabled");
            Toast.makeText(locationListner.this,"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        }else{
            Log.e("CheckGPS","Gps already enabled");
            Toast.makeText(locationListner.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
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
            googleApiClient = new GoogleApiClient.Builder(locationListner.this)
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
                                status.startResolutionForResult(locationListner.this, REQUEST_LOCATION);

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
        longitude.append("Current Longitude:" + location.getLongitude());
        latitude.append("Current Latitude:" + location.getLatitude());
        //Toast.makeText(locationListner.class,""+langitude1,Toast.LENGTH_LONG).show();
//        DatabaseHelper databaseHelper=new DatabaseHelper(this);
//        ContentValues cv=new ContentValues();
//        cv.put("Latitude",langitude1);
//        cv.put("Longitude",longitude1);
//        databaseHelper.InsertLocation(cv);
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("latitude",location.getLatitude());
//        params.put("longitude",location.getLongitude());
//        aQuery.ajax(uploadurl,params, JSONArray.class, new AjaxCallback<JSONArray>() {
//            @Override
//            public void callback(String url, JSONArray array, AjaxStatus status) {
//                super.callback(url, array, status);
//                Log.i("response", "REsponser:" + array);
//            }
//        });
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

}