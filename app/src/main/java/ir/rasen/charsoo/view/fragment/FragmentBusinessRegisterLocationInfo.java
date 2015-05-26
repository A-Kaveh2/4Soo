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
import ir.rasen.charsoo.controller.helper.LocationManagerTracker;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.Location_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.view.widget_customized.buttons.ButtonFont;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.activity.ActivityMapChoose;

public class FragmentBusinessRegisterLocationInfo extends Fragment {

    private Spinner spinnerStates;
    EditTextFont editTextCity, editTextStreet;
    private GoogleMap googleMap;
    //MapView mapView;
    //LatLng choosedLatLng;
    Business business;
    String latitude, longitude;
    ButtonFont buttonMap;
    boolean isEditting = false;

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


        try {
            isEditting = getArguments().getBoolean(Params.IS_EDITTING);
        } catch (Exception e) {

        }

        buttonMap = (ButtonFont) view.findViewById(R.id.btn_map);

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

            buttonMap.setBackgroundResource(R.drawable.selector_button_register);
            buttonMap.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
        }

        if (!LocationManagerTracker.isGooglePlayServicesAvailable(getActivity())) {
            buttonMap.setEnabled(false);
            view.findViewById(R.id.textView_play_service).setVisibility(View.VISIBLE);

            LocationManagerTracker lt = new LocationManagerTracker(getActivity());
            // check if GPS enabled
            if(lt.canGetLocation()){

                 latitude = String.valueOf(lt.getLatitude());
                 longitude = String.valueOf(lt.getLongitude());
                ((MyApplication) getActivity().getApplication()).business.location_m = new Location_M(latitude, longitude);
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                lt.showSettingsAlert();
            }
        }
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityMapChoose.class);
                if (isEditting) {
                    intent.putExtra(Params.LATITUDE, business.location_m.getLatitude());
                    intent.putExtra(Params.LONGITUDE, business.location_m.getLongitude());
                    intent.putExtra(Params.IS_EDITTING, true);
                } else
                    intent.putExtra(Params.IS_EDITTING, false);
                startActivityForResult(intent, Params.ACTION_CHOOSE_LOCATION);
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
        if (isEditting)
            ((MyApplication) getActivity().getApplication()).business.location_m = business.location_m;
        else {
            if (latitude == null || longitude == null) {
                new DialogMessage(getActivity(), getString(R.string.err_choose_location)).show();
                return false;
            }
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
            String s = data.getStringExtra(Params.LATITUDE);
            longitude = data.getStringExtra(Params.LONGITUDE);
            ((MyApplication) getActivity().getApplication()).business.location_m = new Location_M(String.valueOf(latitude), String.valueOf(longitude));
            buttonMap.setBackgroundResource(R.drawable.selector_button_register);
            buttonMap.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
        }
    }

}
