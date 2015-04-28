package ir.rasen.charsoo;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.Category;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.SubCategory;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.WebservicesHandler;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.webservices.business.GetBusinessGategories;
import ir.rasen.charsoo.webservices.business.GetBusinessSubcategories;

public class FragmentSearch extends Fragment implements IWebserviceResponse {

    EditTextFont editTextSearch;
    LinearLayout llIndicatorBusinesses, llIndicatorPosts,llIndicatorUsers;
    private ProgressDialog progressDialog;
    private GoogleMap googleMap;
    MapView mapView;
    LatLng choosedLatLng;
    ArrayList<String> subcategoriesStr;

    private enum SearchType {BUSINESSES, POSTS,USERS}

    ;
    private SearchType searchType;
    RelativeLayout rlFilters;
    private Spinner spinnerCategory, spinnerSubCategory;
    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,
                container, false);

        searchType = SearchType.BUSINESSES;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading SearchFragment");


        llIndicatorBusinesses = (LinearLayout) view.findViewById(R.id.ll_indicator_businesses);
        llIndicatorPosts = (LinearLayout) view.findViewById(R.id.ll_indicator_posts);
        llIndicatorUsers = (LinearLayout) view.findViewById(R.id.ll_indicator_users);
        rlFilters = (RelativeLayout) view.findViewById(R.id.rl_filters);

        (view.findViewById(R.id.rl_businesses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchType != SearchType.BUSINESSES) {
                    searchType = SearchType.BUSINESSES;
                    llIndicatorBusinesses.setVisibility(View.VISIBLE);
                    llIndicatorPosts.setVisibility(View.GONE);
                    llIndicatorUsers.setVisibility(View.GONE);
                    rlFilters.setVisibility(View.VISIBLE);
                }
            }
        });
        (view.findViewById(R.id.rl_posts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchType != SearchType.POSTS) {
                    searchType = SearchType.POSTS;
                    llIndicatorBusinesses.setVisibility(View.GONE);
                    llIndicatorUsers.setVisibility(View.GONE);
                    llIndicatorPosts.setVisibility(View.VISIBLE);
                    rlFilters.setVisibility(View.GONE);
                }
            }
        });
        (view.findViewById(R.id.rl_users)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchType != SearchType.USERS) {
                    searchType = SearchType.USERS;
                    llIndicatorBusinesses.setVisibility(View.GONE);
                    llIndicatorUsers.setVisibility(View.VISIBLE);
                    llIndicatorPosts.setVisibility(View.GONE);
                    rlFilters.setVisibility(View.GONE);
                }
            }
        });

        editTextSearch = (EditTextFont) view.findViewById(R.id.edt_search);
        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextSearch.getRight() - editTextSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        return search();
                    }
                }
                return false;
            }
        });
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return search();
                }
                return false;
            }
        });

        spinnerCategory = (Spinner) view.findViewById(R.id.spinner_category);
        spinnerSubCategory = (Spinner) view.findViewById(R.id.spinner_sub_category);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0 && position != 1) {
                    progressDialog.show();
                    ((MyApplication) getActivity().getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);
                    new GetBusinessSubcategories(getActivity(), categories.get(position).id, FragmentSearch.this).execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //Map section
        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (mapView != null) {
            googleMap = mapView.getMap();
            try {
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setMyLocationEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.valueOf(getString(R.string.sample_latitude)), Double.valueOf(getString(R.string.sample_longitude))), 5));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000,
                        null);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        choosedLatLng = latLng;
                        MarkerOptions marker = new MarkerOptions().position(
                                latLng);
                        googleMap.addMarker(marker);
                    }
                });
            } catch (Exception e) {
                new DialogMessage(getActivity(),getString(R.string.err_map_loading)+getString(R.string.err_map_loading_description)).show();
            }
        }

        //Map section

        recursivelyCallHandler();

        return view;
    }

    Handler handler = new Handler();

    public void recursivelyCallHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                if (((MyApplication) getActivity().getApplication()).isHomeCreated) {
                    if (((MyApplication) getActivity().getApplication()).isSearchCreated) {

                        initializeCategories();
                    } else {
                        ((MyApplication) getActivity().getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_CATEGORY);
                        new GetBusinessGategories(getActivity(), FragmentSearch.this).execute();
                    }
                } else
                    recursivelyCallHandler();
            }
        }, 500);
    }


    private void initializeCategories() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.layout_spinner_back, Category.getCategoryListString(categories));
        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_back_drop_down);
        spinnerCategory.setAdapter(dataAdapter);

        //get subcategory for first category
        if (((MyApplication) getActivity().getApplication()).isSearchCreated) {
            initializeSubcategories();

        } else {
            ((MyApplication) getActivity().getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);
            new GetBusinessSubcategories(getActivity(), categories.get(1).id, FragmentSearch.this).execute();
            spinnerCategory.setEnabled(true);
            ((MyApplication) getActivity().getApplication()).isSearchCreated = true;

        }
    }

    private void initializeSubcategories() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.layout_spinner_back, subcategoriesStr);
        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_back_drop_down);
        spinnerSubCategory.setAdapter(dataAdapter);
        spinnerSubCategory.setEnabled(true);
    }

    @Override
    public void getResult(Object result) {

        if (result instanceof ArrayList) {
            //get business categories
            if (((MyApplication) getActivity().getApplication()).getCurrentWebservice() == WebservicesHandler.Webservices.GET_BUSINESS_CATEGORY) {
                categories = (ArrayList<Category>) result;
                categories.add(0, new Category(0, getString(R.string.category)));
                initializeCategories();
            } else if (((MyApplication) getActivity().getApplication()).getCurrentWebservice() == WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY) {
                subCategories = (ArrayList<SubCategory>) result;
                subCategories.add(0, new SubCategory(0, getString(R.string.subcategory)));
                progressDialog.dismiss();
                subcategoriesStr = SubCategory.getSubCategoryListString(subCategories);
                initializeSubcategories();
            }
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode)).show();
    }

    private boolean search() {
        if (searchType == SearchType.BUSINESSES) {
           /* if(!Validation.validateIdentifier(getActivity(),editTextSearch.getText().toString()).isValid()){
                editTextSearch.setError(Validation.getErrorMessage());
                return false;
            }*/
            if (spinnerSubCategory.getSelectedItemPosition() == 0) {
                new DialogMessage(getActivity(), getString(R.string.choose_category_search)).show();
                return false;
            }
            if (choosedLatLng == null) {
                new DialogMessage(getActivity(), getString(R.string.err_choose_location_search)).show();
                return false;
            }

            Intent intent = new Intent(getActivity(), ActivitySearchBusinessResult.class);
            intent.putExtra(Params.SEARCH_KEY_WORD, editTextSearch.getText().toString());
            intent.putExtra(Params.LATITUDE, String.valueOf(choosedLatLng.latitude));
            intent.putExtra(Params.LONGITUDE, String.valueOf(choosedLatLng.longitude));
            intent.putExtra(Params.SUB_CATEGORY_ID, subCategories.get(spinnerSubCategory.getSelectedItemPosition()).id);
            startActivity(intent);

        } else if (searchType == SearchType.POSTS) {
            Intent intent = new Intent(getActivity(), ActivitySearchPostResult.class);
            intent.putExtra(Params.SEARCH_KEY_WORD, editTextSearch.getText().toString());
            startActivity(intent);
        }
        else if (searchType == SearchType.USERS){
            Intent intent = new Intent(getActivity(), ActivitySearchUser.class);
            intent.putExtra(Params.SEARCH_KEY_WORD, editTextSearch.getText().toString());
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
}
