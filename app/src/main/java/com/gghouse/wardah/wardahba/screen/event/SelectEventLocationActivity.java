package com.gghouse.wardah.wardahba.screen.event;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.viewmodel.SelectEventLocationViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectEventLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, View.OnClickListener {

    private final String TAG = SelectEventLocationActivity.class.getSimpleName();
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private SelectEventLocationViewModel mModel;
    private TextView mTvLocAddress;
    private TextView mTvSelectLocation;
    private ImageView mIvMarker;

    private GoogleMap mMap;

    private Geocoder mGeocoder;
    private List<Address> mAddresses;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Cihampelas Walk) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-6.893400380132707, 107.60556701570749);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        setContentView(R.layout.activity_select_event_location);

        // Geocoder
        mGeocoder = new Geocoder(this, Locale.getDefault());

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mTvLocAddress = (TextView) findViewById(R.id.tvLocAddress);
        mTvSelectLocation = (TextView) findViewById(R.id.tvSelectLocation);
        mTvSelectLocation.setOnClickListener(this);
        mIvMarker = (ImageView) findViewById(R.id.ivPin);
        mIvMarker.setColorFilter(getResources().getColor(R.color.colorWardahGreen), PorterDuff.Mode.SRC_ATOP);

        mModel = ViewModelProviders.of(this).get(SelectEventLocationViewModel.class);
        mModel.getAddress().observe(this, address -> {
            String addr = address.getAddressLine(0);
            mTvLocAddress.setText(addr);
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        if (mMap != null) {
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_select_event_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.action_search:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        getLocationPermission();
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    mLastKnownLocation = null;
                }
                getDeviceLocation();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                try {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
                    mModel.getAddress().setValue(getAddressFromLatLng(place.getLatLng()));
                } catch (IOException ioe) {
                    Log.d(TAG, ioe.getMessage());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onCameraIdle() {
        LatLng midLatLng = mMap.getCameraPosition().target;

        try {
            mModel.getAddress().setValue(getAddressFromLatLng(midLatLng));
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        switch (v.getId()) {
            case R.id.tvSelectLocation:
                Log.d(TAG, "You selected " + mModel.getAddress().getValue().getAddressLine(0));
                break;
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission");
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation");
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d(TAG, "onComplete");
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.
                                    getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                            try {
                                Address address = getAddressFromLatLng(latLng);
                                // Jika lokasi tidak ditemukan, gunakan lokasi default
                                if (address == null) {
                                    address = getAddressFromLatLng(mDefaultLocation);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                }

                                mModel.getAddress().setValue(address);
                            } catch (IOException ioe) {
                                Log.e(TAG, ioe.getMessage());
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            try {
                                mModel.getAddress().setValue(getAddressFromLatLng(mDefaultLocation));
                            } catch (IOException ioe) {
                                Log.e(TAG, ioe.getMessage());
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private Address getAddressFromLatLng(LatLng latLng) throws IOException {
        Log.d(TAG, "getAddressFromLatLng [" + latLng.latitude + "], [" + latLng.longitude + "]");
        mAddresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

        for (Address addr : mAddresses) {
            return addr;
        }

        return null;
    }
}
