package ir.rasen.charsoo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.ui.ButtonFont;

public class ActivityMapChoose extends ActionBarActivity {

    // Google Map
    private GoogleMap googleMap;
    MapView mapView;
    LatLng latLng;
    LatLng choosedLatLng;
    Marker marker;
    MenuItem menuItemTik;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.choose_location));
        setContentView(R.layout.activity_map);


        latLng = new LatLng(Double.valueOf(getString(R.string.sample_latitude)), Double.valueOf(getString(R.string.sample_longitude)));

        try {


            MapsInitializer.initialize(ActivityMapChoose.this);
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);


            if (mapView != null) {
                googleMap = mapView.getMap();
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setMyLocationEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        latLng, 4));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(4), 2000,
                        null);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

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
        }
        else if (item.getItemId() == R.id.action_tik){
            Intent i = getIntent();
            i.putExtra(Params.LATITUDE, String.valueOf(choosedLatLng.latitude));
            i.putExtra(Params.LONGITUDE, String.valueOf(choosedLatLng.longitude));
            setResult(RESULT_OK, i);
            finish();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tik_button, menu);
        menuItemTik = menu.findItem(R.id.action_tik);
        menuItemTik.setVisible(false);
        return true;
    }

}
