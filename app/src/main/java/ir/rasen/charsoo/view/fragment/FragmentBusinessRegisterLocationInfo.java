package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.gms.maps.GoogleMap;
import java.util.Arrays;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.Location_M;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.ui.ButtonFont;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.view.activity.ActivityMapChoose;

public class FragmentBusinessRegisterLocationInfo extends Fragment  {

    private Spinner spinnerStates;
    EditTextFont editTextCity, editTextStreet;
    private GoogleMap googleMap;
    //MapView mapView;
    //LatLng choosedLatLng;
    Business business;
    String latitude,longitude;
    ButtonFont buttonMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_business_location_info,
                container, false);

        spinnerStates = (Spinner) view.findViewById(R.id.spinner_states);
        editTextCity = (EditTextFont) view.findViewById(R.id.edt_city);
        editTextStreet = (EditTextFont) view.findViewById(R.id.edt_street);

        List<String> states = Arrays.asList(getResources().getStringArray(R.array.states));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.layout_spinner_back, states);
        categoryAdapter.setDropDownViewResource(R.layout.layout_spinner_back_drop_down);
        spinnerStates.setAdapter(categoryAdapter);

        boolean isEditting = false;
        try {
            isEditting = getArguments().getBoolean(Params.IS_EDITTING);
        } catch (Exception e) {

        }


        if (isEditting) {
            business = ((MyApplication) getActivity().getApplication()).business;
            editTextCity.setText(business.city);
            editTextStreet.setText(business.address);
            for (int i = 0; i < states.size(); i++) {
                if (business.state.equals(states.get(i))) {
                    spinnerStates.setSelection(i);
                    break;
                }
            }
        }

        //map shouldn't be here. The scenario has been changed.
        /*//Map section
        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (mapView != null) {
            googleMap = mapView.getMap();
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
            if (isEditting) {
                LatLng latLng = new LatLng(Double.valueOf(business.location_m.getLatitude()), Double.valueOf(business.location_m.getLongitude()));
                choosedLatLng = latLng;
                MarkerOptions marker = new MarkerOptions().position(
                        latLng);
                googleMap.addMarker(marker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.valueOf(business.location_m.getLatitude()), Double.valueOf(business.location_m.getLongitude())), 5));
            }
            else
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.valueOf(getString(R.string.sample_latitude)), Double.valueOf(getString(R.string.sample_longitude))), 5));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000,
                    null);

        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                choosedLatLng = latLng;
                MarkerOptions marker = new MarkerOptions().position(
                        latLng);
                googleMap.addMarker(marker);
            }
        });

        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight / 3);
        mapView.setLayoutParams(params);*/
        //Map section
        buttonMap = (ButtonFont)view.findViewById(R.id.btn_map);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ActivityMapChoose.class), Params.ACTION_CHOOSE_LOCATION);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        //mapView.onResume();
        super.onResume();
    }

    public boolean isVerified() {
        if (spinnerStates.getSelectedItemPosition() == 0) {
            new DialogMessage(getActivity(), getString(R.string.err_choose_one_state)).show();
            return false;
        }
        if (!Validation.validateCity(getActivity(), editTextCity.getText().toString()).isValid()) {
            editTextCity.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateAddress(getActivity(), editTextStreet.getText().toString()).isValid()) {
            editTextStreet.setError(Validation.getErrorMessage());
            return false;
        }
        if (latitude == null || longitude == null) {
            new DialogMessage(getActivity(), getString(R.string.err_choose_location)).show();
            return false;
        }
        ((MyApplication) getActivity().getApplication()).business.state = String.valueOf(spinnerStates.getSelectedItem());
        ((MyApplication) getActivity().getApplication()).business.city = editTextCity.getText().toString();
        ((MyApplication) getActivity().getApplication()).business.address = editTextStreet.getText().toString();


        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Params.ACTION_CHOOSE_LOCATION && resultCode == Activity.RESULT_OK) {
            latitude = data.getStringExtra(Params.LATITUDE);
            String s  = data.getStringExtra(Params.LATITUDE);
            longitude = data.getStringExtra(Params.LONGITUDE);
            ((MyApplication)getActivity().getApplication()).business.location_m = new Location_M(String.valueOf(latitude), String.valueOf(longitude));
            buttonMap.setBackgroundResource(R.drawable.selector_button_register);
            buttonMap.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
        }
    }

}
