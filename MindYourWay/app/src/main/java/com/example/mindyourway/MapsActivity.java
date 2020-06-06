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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final String TAG = "MapsActivity";

    //map
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager locationManager;

    //vars
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds latlngBounds = new LatLngBounds(new LatLng(-40,-168), new LatLng(71,136));
    private String destinationString;
    private Location checkPoint;
    private Location currentLocation;
    private HashMap<String, Pair<Double, Double>> checkpointList;

    //widgets
    private EditText mSearchText;
    private ImageView mGPS;
    private ImageView mDest;
    private ImageView mReached;
    private ImageView mBack;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        getDeviceLocation(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                Log.d(TAG, "onMapClick: "+point.toString());
                mMap.addMarker(new MarkerOptions().position(point));
            }
        });

        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initCoordinates();

        Intent intent = getIntent();
        Log.d(TAG, "onCreate: hey");
        destinationString = intent.getStringExtra(CenterMapActivity.centerExtra);
        checkPoint = getCheckPoint(destinationString);

        mSearchText = (EditText) findViewById(R.id.input_search);
        mGPS = (ImageView) findViewById(R.id.ic_gps);
        mDest = (ImageView) findViewById(R.id.ic_dest);
        mReached = (ImageView) findViewById(R.id.ic_reached);
        mBack = (ImageView) findViewById(R.id.ic_back);

        initMap();

    }

    private void initCoordinates(){
        checkpointList = new HashMap<>();
        checkpointList.put("Center_1", new Pair<Double, Double>(44.435597, 26.099499));
        checkpointList.put("Center_2", new Pair<Double, Double>(44.421419, 26.076623));
        checkpointList.put("Center_3", new Pair<Double, Double>(44.443625, 26.125010));
        checkpointList.put("Sector1_1", new Pair<Double, Double>(44.435597, 26.099499));
        checkpointList.put("Sector1_2", new Pair<Double, Double>(44.421419, 26.076623));
        checkpointList.put("Sector1_3", new Pair<Double, Double>(44.443625, 26.125010));
        checkpointList.put("Sector3_1", new Pair<Double, Double>(44.435597, 26.099499));
        checkpointList.put("Sector3_2", new Pair<Double, Double>(44.421419, 26.076623));
        checkpointList.put("Sector3_3", new Pair<Double, Double>(44.443625, 26.125010));
    }

    private Location getCheckPoint(String x){
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
        getDestinationLocation();

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
        
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked back");
                if(destinationString.contains("Center")){
                    Intent intent = new Intent(MapsActivity.this, CenterMapActivity.class);
                    startActivity(intent);
                }
                if(destinationString.contains("Sector1")){
                    Intent intent = new Intent(MapsActivity.this, Sector1MapActivity.class);
                    startActivity(intent);
                }
                if(destinationString.contains("Sector3")){
                    Intent intent = new Intent(MapsActivity.this, Sector3MapActivity.class);
                    startActivity(intent);
                }
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
                    if(MainActivity.user.getStatus(destinationString)==2)
                        MainActivity.user.incrementStatus(destinationString);
                    if(destinationString.contains("Center")){
                        Intent intent = new Intent(MapsActivity.this, CenterMapActivity.class);
                        startActivity(intent);
                    }
                    if(destinationString.contains("Sector1")){
                        Intent intent = new Intent(MapsActivity.this, Sector1MapActivity.class);
                        startActivity(intent);
                    }
                    if(destinationString.contains("Sector3")){
                        Intent intent = new Intent(MapsActivity.this, Sector3MapActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean checkIfReachedDestination() {
        if(MainActivity.user.isAdmin()) {
            return true;
        } else {
            getDeviceLocation(false);
            double distance = currentLocation.distanceTo(checkPoint);
            Log.d(TAG, "checkIfReachedDestination: distance is: " + distance);
            if (distance < 10){
                return true;
            }
            return false;
        }
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
