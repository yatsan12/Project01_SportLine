package com.luseen.spacenavigationview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
import sportfieldsearch.PermissionUtils;
import ttu.mis.lr0816.ChatActivity;
import ttu.mis.lr0816.LoginActivity;
import ttu.mis.lr0816.MainActivity;
import ttu.mis.lr0816.ProfileActivity;

public class ActivityWithBadge extends AppCompatActivity implements OnMapReadyCallback { // , GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener
    CircleImageView profile_image;
    private static final String TAG = "ActivityWithBadge";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationRequest locationRequest;
    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };
    private Geocoder geocoder;
    private CameraPosition cameraPosition;
    private PlacesClient placesClient;
    private String uname;
    private String[] sname, addr, lat, lng;
    private String[] sname1, addr1, lat1, lng1;
    private String[] sname2, addr2, lat2, lng2;
    private String[] sname3, addr3, lat3, lng3;
    private List<String> listSport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extra = getIntent().getExtras();
        addr = extra.getStringArray("addr");
        sname = extra.getStringArray("sname");
        Log.d("badge", sname[0]);
        Log.d("badge", addr[0]);
        lat = extra.getStringArray("lat");
        lng = extra.getStringArray("lng");
        Log.d("badge", lat[0] + ", " + lng[0]);

        addr1 = extra.getStringArray("addr1");
        sname1 = extra.getStringArray("sname1");
        Log.d("badge", sname1[0]);
        Log.d("badge", addr1[0]);
        lat1 = extra.getStringArray("lat1");
        lng1 = extra.getStringArray("lng1");
        Log.d("badge", lat1[0] + ", " + lng1[0]);

        addr2 = extra.getStringArray("addr2");
        sname2 = extra.getStringArray("sname2");
        Log.d("badge", sname2[0]);
        Log.d("badge", addr2[0]);
        lat2 = extra.getStringArray("lat2");
        lng2 = extra.getStringArray("lng2");
        Log.d("badge", lat2[0] + ", " + lng2[0]);

        addr3 = extra.getStringArray("addr3");
        sname3 = extra.getStringArray("sname3");
        Log.d("badge", sname3[0]);
        Log.d("badge", addr3[0]);
        lat3 = extra.getStringArray("lat3");
        lng3 = extra.getStringArray("lng3");
        Log.d("badge", lat3[0] + ", " + lng3[0]);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_main);
        mapFragment.getMapAsync(this);
        findViewById(R.id.map_main).setPadding(0, 0, 0, 130);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        uname = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(uname);
        profile_image = new CircleImageView(this);

        if (uname.equals("jjLin")){
            profile_image.setImageResource(R.drawable.man1211);
        }else if (uname.equals("琳琳")){
            //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
            profile_image.setImageResource(R.drawable.meow1211);
        }else if (uname.equals("翁祥恩")){
            profile_image.setImageResource(R.drawable.joe0611);
        }else if (uname.equals("yatsan")){
            profile_image.setImageResource(R.drawable.yatsan1211);
        }else if (uname.equals("雅琪")){
            profile_image.setImageResource(R.drawable.yaya090111);
        }else if (uname.equals("HDwang")){
            profile_image.setImageResource(R.drawable.hdwang1011);
        }else {
            profile_image.setImageResource(R.drawable.ic_action_face2);
        }
        toolbar.setLogo(profile_image.getDrawable());

        geocoder = new Geocoder(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //uname = getIntent().getExtras().getString("name");
        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        //spaceNavigationView.changeSpaceBackgroundColor(ContextCompat.getColor(this,R.color.quantum_pink));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_first, "日記", R.drawable.book));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_second, "聊天", R.drawable.chatroom));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_third, "場所", R.drawable.searchfield));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_forth, "計步", R.drawable.clock));
        spaceNavigationView.shouldShowFullBadgeText(false);
        spaceNavigationView.setCentreButtonIcon(R.drawable.logo99);

        spaceNavigationView.setCentreButtonId(R.id.navigation_centre);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {

            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                Intent intent = new Intent(ActivityWithBadge.this, competition.CompetitionActivity.class);
                intent.putExtra("name", uname);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Log.d("onItemClick", "" + itemIndex);
                itemAction(itemIndex, itemName);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
                itemAction(itemIndex, itemName);
            }
        });

        spaceNavigationView.setSpaceOnLongClickListener(new SpaceOnLongClickListener() {
            @Override
            public void onCentreButtonLongClick() {
                Toast.makeText(ActivityWithBadge.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(ActivityWithBadge.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final String typeArr[] = {"籃球", "游泳", "排球", "健身", "足球", "羽球", "排球", "網球", "棒球", "桌球", "跑步", "高爾夫球", "其他"};
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                //finish();
                startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case R.id.profile:
                Intent it = new Intent(this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("name", uname);
                startActivity(it);
                return true;
            case R.id.search:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("選擇運動類型")
                        .setSingleChoiceItems(typeArr, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listSport.add(typeArr[i]);
                            }
                        }).show();
        }
        showSportItems();
        return false;
    }

    private void showSportItems() {
        // Markers
        for (String sportName : listSport) {
            switch (sportName) {
                case "籃球":
                    for (int i = 0; i < sname.length; i++) {
                        LatLng myPos = new LatLng(Double.parseDouble(lat[i]), Double.parseDouble(lng[i]));
                        mMap.addMarker(
                                new MarkerOptions()
                                        .title(sname[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic11280201))
                                        .snippet(addr[i])
                                        .position(myPos)
                                        .draggable(true)
                                        .alpha(0.9f));
                    }
                    break;
                case "游泳":
                    for (int i = 0; i < sname1.length; i++) {
                        LatLng myPos = new LatLng(Double.parseDouble(lat[i]), Double.parseDouble(lng[i]));
                        mMap.addMarker(
                                new MarkerOptions()
                                        .title(sname1[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.swimmingpool2))
                                        .snippet(addr[i] + "\n")
                                        .position(myPos)
                                        .draggable(true)
                                        .alpha(0.9f));
                    }
                    break;
                case "排球":
                    for (int i = 0; i < sname3.length; i++) {
                        LatLng myPos = new LatLng(Double.parseDouble(lat[i]), Double.parseDouble(lng[i]));
                        mMap.addMarker(
                                new MarkerOptions()
                                        .title(sname3[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer2))
                                        .snippet(addr[i] + "\n")
                                        .position(myPos)
                                        .draggable(true)
                                        .alpha(0.9f));
                    }
                    break;
                case "健身":
                    for (int i = 0; i < sname2.length; i++) {
                        LatLng myPos = new LatLng(Double.parseDouble(lat[i]), Double.parseDouble(lng[i]));
                        mMap.addMarker(
                                new MarkerOptions()
                                        .title(sname2[i])
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic11280301))
                                        .snippet(addr[i] + "\n")
                                        .position(myPos)
                                        .draggable(true)
                                        .alpha(0.9f));
                    }
            }
        }
    }

    public void itemAction(int itemIndex, String itemName) {
        Intent intent;
        switch (itemIndex) {
            case 0:
                intent = new Intent(ActivityWithBadge.this, calendar_1.MainCalendarActivity.class);
                intent.putExtra("name", uname);
                break;
            case 1:
                intent = new Intent(ActivityWithBadge.this, ChatActivity.class);
                intent.putExtra("name", uname);
                break;
            default: // case 2:
                intent = new Intent(ActivityWithBadge.this, sportfieldsearch.MainActivity.class);
                intent.putExtra("name", uname);
                break;
            case 3:
                intent = new Intent(ActivityWithBadge.this, step.activity.MainActivitytest.class);
                intent.putExtra("name", uname);
                break;
        }
        startActivity(intent);
    }

    // map fragment
    public void onMapReady(GoogleMap googlemap) {
        mMap = googlemap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission((this), LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        }

        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
        }
    }

    private void setUserLocationMarker(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.showInfoWindow();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            // you need to request permissions...
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
//                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }
}