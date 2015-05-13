package ir.rasen.charsoo.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ActionBar_M;
import ir.rasen.charsoo.controller.helper.Params;

public class ActivityMapDisplay extends ActionBarActivity {

    // Google Map
    private GoogleMap googleMap;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.display_location));

        double lat = getIntent().getExtras().getDouble(Params.LATITUDE);
        double lng = getIntent().getExtras().getDouble(Params.LONGITUDE);


        try {
            setContentView(R.layout.activity_map);

            MapsInitializer.initialize(ActivityMapDisplay.this);
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);

            if (mapView != null) {
                googleMap = mapView.getMap();
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setMyLocationEnabled(true);

                // create marker
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(lat, lng)).title(
                        "Title");

                googleMap.addMarker(marker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lat, lng), 15));

                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000,
                        null);

            }

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
        } else
            return super.onOptionsItemSelected(item);
    }

}
