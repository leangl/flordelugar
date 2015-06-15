package ar.gob.buenosaires.camino.utils;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.concurrent.TimeUnit;

import ar.gob.buenosaires.camino.CaminoApplication;
import ar.gob.buenosaires.camino.R;
import rx.Observable;
import rx.Subscriber;

public class LocationClient implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //private static final Long REQUEST_TIMEOUT = TimeUnit.SECONDS.toMillis(10);
    private static LocationClient sInstance;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Subscriber<? super Location> mSubscriber;
    //private Handler mHandler = new Handler();

    private LocationClient(Subscriber<? super Location> subscriber) {
        mSubscriber = subscriber;
    }

    private void start() {
        mGoogleApiClient = new GoogleApiClient.Builder(CaminoApplication.getInstance())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void stop() {
        //mHandler.removeCallbacksAndMessages(null);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        LocationRequest request = LocationRequest.create();
        //request.setExpirationDuration(REQUEST_TIMEOUT);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(request);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        pendingResult.setResultCallback(result -> {
            final Status status = result.getStatus();
            final LocationSettingsStates state = result.getLocationSettingsStates();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location requests here.
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);

                    /*mHandler.postDelayed(() -> {
                        stop();
                        if (mLastLocation != null) {
                            mSubscriber.onNext(mLastLocation);
                            //mSubscriber.onCompleted();
                        } else {
                            mSubscriber.onError(new LocationException() {
                                @Override
                                public void startResolutionForResult(Activity activity, int requestCode) throws IntentSender.SendIntentException {
                                    Toast.makeText(activity, R.string.no_location, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, REQUEST_TIMEOUT);*/
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                    mSubscriber.onError(new LocationException() {
                        @Override
                        public void startResolutionForResult(Activity activity, int requestCode) throws IntentSender.SendIntentException {
                            status.startResolutionForResult(activity, requestCode);
                        }
                    });
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                    mSubscriber.onError(new LocationException() {
                        @Override
                        public void startResolutionForResult(Activity activity, int requestCode) {
                            Toast.makeText(activity, R.string.no_location, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("Location", "Location suspended");
        //mSubscriber.onCompleted();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        stop();
        mSubscriber.onError(new LocationException() {
            @Override
            public void startResolutionForResult(Activity activity, int requestCode) throws IntentSender.SendIntentException {
                connectionResult.startResolutionForResult(activity, requestCode);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        //stop();
        mSubscriber.onNext(location);
        //mSubscriber.onCompleted();
    }

    public static Observable<Location> requestLocation() {
        return Observable.create(subscriber -> {
            LocationClient client = new LocationClient(subscriber);
            client.start();
        });
    }

    public abstract static class LocationException extends Exception {
        public LocationException() {
        }

        public abstract void startResolutionForResult(Activity activity, int requestCode) throws IntentSender.SendIntentException;
    }

}