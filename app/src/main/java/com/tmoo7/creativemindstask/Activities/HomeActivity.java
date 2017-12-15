package com.tmoo7.creativemindstask.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.tmoo7.creativemindstask.Adapters.Location_recycler_adapter;
import com.tmoo7.creativemindstask.Helpers.MyProgressDialog;
import com.tmoo7.creativemindstask.Layers.DefaultValue;
import com.tmoo7.creativemindstask.Layers.SingleShotLocationProvider;
import com.tmoo7.creativemindstask.Layers.VolleyRequests;
import com.tmoo7.creativemindstask.Models.LocationModel;
import com.tmoo7.creativemindstask.MyService.LocationUpdates;
import com.tmoo7.creativemindstask.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements
        VolleyRequests.OnRequestFinished,
        SingleShotLocationProvider.LocationCallback{

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    RecyclerView product_recycler;
    Location_recycler_adapter mAdapter;
    VolleyRequests volleyRequests;
    List<LocationModel> mList;
    ProgressDialog progressDialog;
    int settingvalue = 0;
    private BroadcastReceiverGO_service myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        checkLocationPermission();

    }
    private void updateui()
    {
        if (settingvalue == 0) {
            if (isMyServiceRunning(LocationUpdates.class))
            {
                Intent intent = new Intent(HomeActivity.this,LocationUpdates.class);
                stopService(intent);
            }
            progressDialog.show();
            SingleShotLocationProvider.requestSingleUpdate(HomeActivity.this, this);
        }
        else
        {
            Intent intent = new Intent(HomeActivity.this, LocationUpdates.class);
            startService(intent);
         }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent =new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
             return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void init()
    {
        volleyRequests = new VolleyRequests(getApplicationContext(),this);
        mList = new ArrayList<>();
        product_recycler = (RecyclerView) findViewById(R.id.product_recycler);
        mAdapter = new Location_recycler_adapter(HomeActivity.this,mList);
        product_recycler.setAdapter(mAdapter);
        product_recycler.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = MyProgressDialog.CustomProgressDialog(this);
        //////////////////////////////////////////////////////////////////
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (HomeActivity.this);
        settingvalue = Integer.parseInt(prefs.getString("realtime_sett", "0"));
        //////////////////////////////////////////////////////////////////////////
        myReceiver = new BroadcastReceiverGO_service();
        final IntentFilter intentFilter = new IntentFilter("reciverlocation");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        myReceiver = null;
    }

    private String buid_url(double lat, double lng)
    {
        String location  = String.valueOf(lat)+","+String.valueOf(lng);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.foursquare.com")
                .appendPath("v2")
                .appendPath("venues")
                .appendPath("explore")
                .appendQueryParameter("client_id", DefaultValue.QUERY_CLIENTID)
                .appendQueryParameter("client_secret",DefaultValue.QUERY_CLIENTSERCRET)
                .appendQueryParameter("v", "20170801")
                .appendQueryParameter("ll",location)
                .appendQueryParameter("query", "")
                .appendQueryParameter("limit", "10");
        return builder.build().toString();
    }

    @Override
    public void onrequestCompeleted(int Code, List<LocationModel> productModels) {
        progressDialog.dismiss();
        if (Code == 200)
        {
            mList.clear();
            mList.addAll(productModels);
            mAdapter.notifyDataSetChanged();
            for (LocationModel locationModel:productModels)
            {
                Log.e("sd",locationModel.toString());
            }

        }
        else
        {
            // Handel other error
            Toast.makeText(this, "can`t get informations", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        Log.e("Location",location.toString());
        volleyRequests.volleyJsonObjectRequest(Request.Method.GET,buid_url(location.getLatitude(),location.getLongitude()));

    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

             if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
        else
        {
            updateui();
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        updateui();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e("HomeFragment","On permission denied");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private class BroadcastReceiverGO_service extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("reciverlocation")) {

                double getLatitude = intent.getDoubleExtra("Latitude", -1);
                double getLongitude = intent.getDoubleExtra("Longitude", -1);
                Log.e("onReceive",getLatitude+"//"+getLongitude);
                progressDialog.show();
                volleyRequests.volleyJsonObjectRequest(Request.Method.GET,buid_url(getLatitude,getLongitude));



            }
        }
    }


}
