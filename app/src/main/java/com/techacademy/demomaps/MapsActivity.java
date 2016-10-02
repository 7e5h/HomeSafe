package com.techacademy.demomaps;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static android.app.PendingIntent.getActivity;


public class MapsActivity extends FragmentActivity {
    boolean sent;
    double destLng;
    double destLat;
    LocationManager mLocationManager;
    Location location ;//= //mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    double longitude = 0;//location.getLongitude();
    double latitude = 0;//location.getLatitude();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.i("log","location changed");
            if (!sent&&Math.sqrt(Math.pow(Math.abs(latitude-destLat)*69.172,2) + Math.pow(Math.cos(Math.abs(latitude))*Math.abs(longitude-destLng)*69.172,2)) < 0.2 )
            {
                Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
                TextView number = (TextView)findViewById(R.id.phone);
                final String numberString = number.getText().toString();
                TextView body = (TextView)findViewById(R.id.words);
                final String bodyString = body.getText().toString();
                Thread one = new Thread()
                {
                    public void run()
                    {
                        sent = true;
                        MainActivity.sendMessage(numberString,bodyString, "0");
                    }

                };
                one.start();
            }
    }
        public void onProviderDisabled(String provider) {
            //THIS IS ME CARING
        }
        public void onProviderEnabled(String provider) {
            //unlikely
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //naw
        }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sent = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        //mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(32.7,-96.9),new LatLng(32.75,-96.9)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(32.7,-96.9)));//easier for testing
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long)5000,(float)5.0, mLocationListener);

        Thread two = new Thread()
        {
            public void run()
            {
                //sendMessage("8178755978", "spaces bitch spaces!", "0");
            }

        };
        //two.start();

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList.size()>0) {
                Address address = addressList.get(0);
                LatLng latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
                destLng = address.getLongitude();
                destLat = address.getLatitude();
                mMap.addMarker(new MarkerOptions().position(latLng2).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));
                //Toast.makeText(getApplicationContext(),"Destination Set",Toast.LENGTH_LONG).show();
            }
            else
                Log.e("error","no results from geocoder");
        }
    }
    public void onSet(View view){
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList.size()>0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                destLng = address.getLongitude();
                destLat = address.getLatitude();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Toast.makeText(getApplicationContext(),"Destination Set, number will be notified on arrival",Toast.LENGTH_LONG).show();
            }
            else
                Log.e("error","no results from geocoder");
        }
    }

    public void changeType(View view)
    {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }
}
