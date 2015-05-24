package ir.rasen.charsoo.view.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.CurrentLocationProvider;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;

public class ActivityMapChoose extends CharsooActivity {

    // Google Map
    private GoogleMap googleMap;
    MapView mapView;
    //LatLng latLng;
    LatLng choosedLatLng;
    Marker marker;
    MenuItem menuItemTik;
    boolean isLocationInitialized = false;
    //boolean isFirstTime = true;

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            if (googleMap != null && !isLocationInitialized) {
                marker = googleMap.addMarker(new MarkerOptions().position(loc));
                menuItemTik.setVisible(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.0f));
                choosedLatLng = loc;
                isLocationInitialized = true;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.choose_location));
        setContentView(R.layout.activity_map);
        try {
            MapsInitializer.initialize(ActivityMapChoose.this);
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);

            if (mapView != null) {
                googleMap = mapView.getMap();
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setLocationSource(new CurrentLocationProvider(this));
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                if (getIntent().getExtras().getBoolean(Params.IS_EDITTING)) {

                    LatLng loc = new LatLng(Double.valueOf(getIntent().getStringExtra(Params.LATITUDE)), Double.valueOf(getIntent().getStringExtra(Params.LONGITUDE)));
                   marker = googleMap.addMarker(new MarkerOptions().position(loc));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.0f));
                    choosedLatLng = loc;

                    isLocationInitialized = true;
                }
            }
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (marker != null)
                        marker.remove();
                    choosedLatLng = latLng;
                    marker = googleMap.addMarker(new MarkerOptions().position(latLng)
                            .title(getString(R.string.business_location)));
                    menuItemTik.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        mapView.onPause();
        finish();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_tik) {
            Intent i = getIntent();
            i.putExtra(Params.LATITUDE, String.valueOf(choosedLatLng.latitude));
            i.putExtra(Params.LONGITUDE, String.valueOf(choosedLatLng.longitude));
            setResult(RESULT_OK, i);
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tik_button, menu);
        menuItemTik = menu.findItem(R.id.action_tik);
        if (!isLocationInitialized)
            menuItemTik.setVisible(false);
        return true;
    }

}
