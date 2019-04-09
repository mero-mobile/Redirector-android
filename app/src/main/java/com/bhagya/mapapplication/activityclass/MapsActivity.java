package com.bhagya.mapapplication.activityclass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bhagya.mapapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    PolylineOptions lines;
    Double latitude,longitude;
    String datetime,user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent=getIntent();
        SharedPreferences UserName = getSharedPreferences("CURRENT_USER_RECORD",MODE_PRIVATE);
        user_name = UserName.getString("CurrentUserName","");
        SharedPreferences prfs = getSharedPreferences("Location_Response", MODE_PRIVATE);
//        user_name = prfs.getString("userName", "");
        latitude= Double.valueOf(prfs.getString("latitude",""));
        longitude= Double.valueOf(prfs.getString("longitude",""));
        datetime=prfs.getString("dateTime","");
        Toast.makeText(MapsActivity.this,prfs.getString("latitude","")+prfs.getString("longitude",""),Toast.LENGTH_LONG).show();

          // latitude = Double.valueOf(intent.getStringExtra("latitude"));
           //longitude = Double.valueOf(intent.getStringExtra("longitude"));
//
//        latitude = 27.63736636;
//       longitude = 85.33359333;
       // Toast.makeText(this,latitude+longitude,Toast.LENGTH_LONG).show();
       // datetime=intent.getStringExtra("datetime");
//        Toast.makeText(this,"locationdata in mapactivity"+datetime+latitude+longitude,Toast.LENGTH_LONG).show();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(user_name+"("+datetime+")"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else
            mMap.setMyLocationEnabled(true);
            lines= new PolylineOptions();
//          mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
////               latLng.longitude;
//                mMap.addMarker(new MarkerOptions().position(latLng).title("New marker"));
//                lines.add(latLng);
//                mMap.addPolyline(lines);
//            }
//        });
     //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
