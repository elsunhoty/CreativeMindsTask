package com.tmoo7.creativemindstask.Layers;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by othello on 23/07/2017.
 */
public class SingleShotLocationProvider {

    public static interface LocationCallback {
   //     public void onNewLocationAvailable(GPSCoordinates location);
        public void onNewLocationAvailable(Location location);
    }

    @SuppressWarnings("MissingPermission")
    public static void requestSingleUpdate(final Context context, final LocationCallback callback) {

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
         boolean isGPSEnableds = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnableds)  {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onNewLocationAvailable(location);
                    }

                    @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                    @Override public void onProviderEnabled(String provider) { }
                    @Override public void onProviderDisabled(String provider) { }
                }, null);
            }
        }
        else{

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    callback.onNewLocationAvailable(location);
                }

                @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override public void onProviderEnabled(String provider) { }
                @Override public void onProviderDisabled(String provider) { }
            }, null);
        }
    }


}