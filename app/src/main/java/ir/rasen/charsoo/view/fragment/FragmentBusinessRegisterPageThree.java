package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LocationManagerTracker;
import ir.rasen.charsoo.controller.helper.Location_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.model.GetCountryStates;
import ir.rasen.charsoo.model.GetStateCities;
import ir.rasen.charsoo.view.activity.ActivityMapChoose;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.buttons.ButtonFont;

/**
 * Created by hossein-pc on 6/14/2015.
 */
public class FragmentBusinessRegisterPageThree extends Fragment implements IWebserviceResponse{
    public static final String TAG="FragmentBusinessRegisterPageThree";

    private Spinner spinnerStates,spinnerCities;

    EditTextFont editTextStreet;
    private GoogleMap googleMap;
    //MapView mapView;
    //LatLng choosedLatLng;
    Business business;
    String latitude, longitude;
    ButtonFont buttonMap;
    boolean isStatesLoaded=false;
    boolean doRefreshCitiesList=true;
    int selectedStatePosition, selectedCityPosition;

    String selectedState, selectedCity, streetAddress="";
    List<String> stateList, cityList;
    private android.app.ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_register_page_three,
                container, false);

        progressDialog = new android.app.ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        spinnerStates = (Spinner) view.findViewById(R.id.spinner_States);
        spinnerCities = (Spinner) view.findViewById(R.id.spinner_Cities);

        if (selectedState == null){
            stateList =new ArrayList<>();
            setSpinnerStatesAdapter(new ArrayList<String>(),getActivity());
            progressDialog.show();
            new GetCountryStates(getActivity(),FragmentBusinessRegisterPageThree.this).execute();
        }
        else {
            doRefreshCitiesList=false;
            final int tempStatePosition = selectedStatePosition;
            setSpinnerStatesAdapter(stateList,getActivity());
            spinnerStates.post(new Runnable() {
                @Override
                public void run() {
                    doRefreshCitiesList=false;
                    spinnerStates.setSelection(tempStatePosition);
                }
            });
        }

        if (selectedCity==null){
            cityList =new ArrayList<>();
            setSpinnerCitiesAdapter(new ArrayList<String>(),getActivity());
        }
        else{
            final int tempCityPosition= selectedCityPosition;
            setSpinnerCitiesAdapter(cityList,getActivity());
            spinnerCities.post(new Runnable() {
                @Override
                public void run() {
                    spinnerCities.setSelection(tempCityPosition);
                }
            });
        }

        editTextStreet = (EditTextFont) view.findViewById(R.id.edt_street);


//        setSpinnerStatesAdapter(stateList,getActivity());



        buttonMap = (ButtonFont) view.findViewById(R.id.btn_map);

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
                intent.putExtra(Params.IS_EDITTING, false);
                startActivityForResult(intent, Params.ACTION_CHOOSE_LOCATION);
            }
        });

        spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStatePosition=i;
                selectedState=adapterView.getItemAtPosition(i).toString();
                if (doRefreshCitiesList){
                    progressDialog.show();
                    new GetStateCities(getActivity(),selectedState,FragmentBusinessRegisterPageThree.this).execute();
                }
                else
                    doRefreshCitiesList=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCityPosition = i;
                selectedCity=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        if (spinnerCities.getSelectedItemPosition()==0){
            new DialogMessage(getActivity(), getString(R.string.err_choose_city)).show();
            return false;
        }
        if (!Validation.validateAddress(getActivity(), editTextStreet.getText().toString()).isValid()) {
            editTextStreet.setError(Validation.getErrorMessage());
            return false;
        }
        else
            streetAddress=editTextStreet.getText().toString();

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Params.ACTION_CHOOSE_LOCATION && resultCode == Activity.RESULT_OK) {
            latitude = data.getStringExtra(Params.LATITUDE);
            longitude = data.getStringExtra(Params.LONGITUDE);
            buttonMap.setBackgroundResource(R.drawable.selector_button_register);
            buttonMap.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
        }
    }


    private void setSpinnerStatesAdapter(List<String> states,Context c){
        List<String> tempList=new ArrayList<>();
        tempList.add(c.getString(R.string.txt_state));
        tempList.addAll(states);
        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_spinner_dropdown_item, tempList);
        spinnerStates.setAdapter(statesAdapter);
    }

    private void setSpinnerCitiesAdapter(List<String> cities,Context c){
        List<String> tempList=new ArrayList<>();
        tempList.add(c.getString(R.string.txt_City));
        tempList.addAll(cities);
        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_spinner_dropdown_item, tempList);
        spinnerCities.setAdapter(citiesAdapter);
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof ArrayList) {
            if (!isStatesLoaded) {
                stateList.clear();
                stateList = (ArrayList<String>) result;
                setSpinnerStatesAdapter(stateList, getActivity());
                isStatesLoaded = true;
            } else {
                cityList.clear();
                cityList = (ArrayList<String>) result;
                setSpinnerCitiesAdapter(cityList, getActivity());
            }
        }
    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode, callerStringID + ">" + TAG)).show();
    }

    public Hashtable<String,String> getInputData(){
        if (isVerified()){
            Hashtable<String,String> result=new Hashtable<>();
            result.put(Params.SELECTED_STATE,selectedState);
            result.put(Params.SELECTED_CITY, selectedCity);
            result.put(Params.STREET_ADDRESS, streetAddress);
            if ((latitude !=null)&&(longitude!=null)) {
                result.put(Params.LOC_LATITUDE, latitude);
                result.put(Params.LOC_LONGITUDE, longitude);
            }
            return result;
        }
        else
            return null;
    }
}
