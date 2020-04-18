package com.example.mindyourway;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    //map
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager locationManager;
    private boolean mLocationPermissionGranted = false;

    //vars
    private static final float DEFAULT_ZOOM = 15f;
    public static final int locationPermissionRequestCode = 1234;
    private String destinationString;
    private Location checkPoint;
    private Location currentLocation;
    private List<Pair<Double, Double>> checkpointList = Arrays.asList(new Pair<Double, Double>(44.435597, 26.099499));

    //widgets
    private EditText mSearchText;
    private ImageView mGPS;
    private ImageView mDest;
    private ImageView mReached;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        // Add a marker in Sydney and move the camera
        if(mLocationPermissionGranted){
            getDeviceLocation(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        destinationString = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        checkPoint = getCheckPoint(Integer.valueOf(destinationString));

        mSearchText = (EditText) findViewById(R.id.input_search);
        mGPS = (ImageView) findViewById(R.id.ic_gps);
        mDest = (ImageView) findViewById(R.id.ic_dest);
        mReached = (ImageView) findViewById(R.id.ic_reached);

        getLocationPermission();
    }

    /*private Location getCheckPoint(String[] destinationString){
        double destinationLatitude;
        double destinationLongitude;
        Location destination = new Location("");
        destinationLatitude = Double.parseDouble(destinationString[0]);
        destinationLongitude = Double.parseDouble(destinationString[1]);
        Log.d(TAG, "onCreate: Coordinates are: lat: " + destinationLatitude + " lng: " + destinationLongitude);
        destination.setLatitude(destinationLatitude);
        destination.setLongitude(destinationLongitude);
        return destination;
    }*/
    private Location getCheckPoint(int x){
        Location destination = new Location("");
        double destinationLatitude = checkpointList.get(x).first;
        double destinationLongitude = checkpointList.get(x).second;
        Log.d(TAG, "onCreate: Coordinates are: lat: " + destinationLatitude + " lng: " + destinationLongitude);
        destination.setLatitude(destinationLatitude);
        destination.setLongitude(destinationLongitude);
        return destination;
    }


    private void initMap(){
        Log.d(TAG, "initMap: Initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void init(){
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });

        mGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked gps icon");
                getDeviceLocation(true);
            }
        });
        hideSoftKeyboard();

        mDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked destination icon");
                getDestinationLocation();
            }
        });
        hideSoftKeyboard();

        mReached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked reached icon");
                if(checkIfReachedDestination()){
                    Toast.makeText(MapsActivity.this, "Reached Location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkIfReachedDestination() {
        getDeviceLocation(false);
        double distance = currentLocation.distanceTo(checkPoint);
        Log.d(TAG, "checkIfReachedDestination: distance is: " + distance);
        if (distance < 10){
            return true;
        }
        return false;
    }

    private void getDestinationLocation() {
        Log.d(TAG, "getDestinationLocation: getting the destination's location");
        moveCamera(new LatLng(checkPoint.getLatitude(),checkPoint.getLongitude()),
                DEFAULT_ZOOM,
                "Destination Location");
    }


    private void getDeviceLocation(final boolean withCamera ){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            if(withCamera){
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM,
                                        "My location");
                            }
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void geoLocate() {
        Log.d(TAG, "geoLOcate: Geolocating");
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        } catch(IOException e) {
            Log.d(TAG, "geoLOcate: IOEXCEPTION" + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLOcate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),
                    DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
        else {
            Log.d(TAG, "geoLocate: no adress");
        }
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,permissions,locationPermissionRequestCode);
            }
        } else {
            ActivityCompat.requestPermissions(this,permissions,locationPermissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case locationPermissionRequestCode: {
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: Moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
