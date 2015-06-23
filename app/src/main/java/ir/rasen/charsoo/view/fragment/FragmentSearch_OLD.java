package ir.rasen.charsoo.view.fragment;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LocationManagerTracker;
import ir.rasen.charsoo.controller.object.Category;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.SubCategory;
import ir.rasen.charsoo.model.NetworkConnectivityReciever;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.dialog.PopupCategories;
import ir.rasen.charsoo.view.dialog.PopupSubCategories;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;
import ir.rasen.charsoo.view.interface_m.ISelectCategory;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.interface_m.NetworkStateChangeListener;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.activity.ActivitySearchBusinessResult;
import ir.rasen.charsoo.view.activity.ActivitySearchPostResult;
import ir.rasen.charsoo.view.activity.ActivitySearchUser;
import ir.rasen.charsoo.model.business.GetBusinessGategories;
import ir.rasen.charsoo.model.business.GetBusinessSubcategories;

public class FragmentSearch_OLD extends Fragment implements IWebserviceResponse, ISelectCategory, NetworkStateChangeListener {
    public static final String TAG="FragmentSearch";

    EditTextFont editTextSearch;
    LinearLayout llIndicatorBusinesses, llIndicatorPosts, llIndicatorUsers;
    private ProgressDialog progressDialog;
    private GoogleMap googleMap;
    MapView mapView;
    LatLng choosedLatLng;
    ArrayList<String> subcategoriesStr;
    int subcategoryId = 0;

    @Override
    public void doOnNetworkConnected() {
        recursivelyCallHandler();
    }

    private enum SearchType {BUSINESSES, POSTS, USERS}

    ;
    private SearchType searchType;
    RelativeLayout rlFilters;
    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;
    ISelectCategory iSelectCategory;
    TextViewFont textViewCategories, textViewSubCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_old,
                container, false);

        NetworkConnectivityReciever.setNetworkStateListener(TAG,FragmentSearch_OLD.this);
        iSelectCategory = this;
        searchType = SearchType.BUSINESSES;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading SearchFragment");

        textViewCategories = (TextViewFont) view.findViewById(R.id.textView_category);
        textViewSubCategories = (TextViewFont) view.findViewById(R.id.textView_sub_category);
        textViewCategories.setEnabled(false);
        textViewSubCategories.setEnabled(false);

        llIndicatorBusinesses = (LinearLayout) view.findViewById(R.id.ll_indicator_businesses);
        llIndicatorPosts = (LinearLayout) view.findViewById(R.id.ll_indicator_posts);
        llIndicatorUsers = (LinearLayout) view.findViewById(R.id.ll_indicator_users);
        rlFilters = (RelativeLayout) view.findViewById(R.id.rl_filters);

        textViewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupCategories(getActivity(), categories, -1, iSelectCategory).show();
            }
        });

        textViewSubCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupSubCategories(getActivity(), subCategories, -1, iSelectCategory).show();
            }
        });

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


        //Map section
        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (!LocationManagerTracker.isGooglePlayServicesAvailable(getActivity())) {
            view.findViewById(R.id.textView_map_error).setVisibility(View.VISIBLE);
            mapView.setVisibility(View.GONE);
        } else if (mapView != null) {
            googleMap = mapView.getMap();
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

                try { // agar pish az Ejad shodane search az app kharej shavim force midahad
                    if (((MyApplication) getActivity().getApplication()).isHomeCreated) {
                        ((MyApplication) getActivity().getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_CATEGORY);
                        new GetBusinessGategories(getActivity(), FragmentSearch_OLD.this).execute();
                    } else {
                        ConnectivityManager cm =
                                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting())
                            recursivelyCallHandler();
                    }
                }catch(Exception e){

                }
            }
        }, 500);
    }

    @Override
    public void getResult(Object result) {

        if (result instanceof ArrayList) {
            //get business categories
            if (((MyApplication) getActivity().getApplication()).getCurrentWebservice() == WebservicesHandler.Webservices.GET_BUSINESS_CATEGORY) {
                categories = (ArrayList<Category>) result;
                textViewCategories.setEnabled(true);
            } else if (((MyApplication) getActivity().getApplication()).getCurrentWebservice() == WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY) {
                subCategories = (ArrayList<SubCategory>) result;
                progressDialog.dismiss();
                subcategoriesStr = SubCategory.getSubCategoryListString(subCategories);
                textViewSubCategories.setEnabled(true);
            }
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode,callerStringID+">"+TAG)).show();
    }

    private boolean search() {
        if (editTextSearch.getText().toString().equals("")) {
            editTextSearch.setError(getString(R.string.err_fill_search_box));
            return false;
        }
        if (searchType == SearchType.BUSINESSES) {
            /*if (subcategoryId == 0) {
                new DialogMessage(getActivity(), getString(R.string.choose_category_search)).show();
                return false;
            }*/
            if (choosedLatLng == null) {
                new DialogMessage(getActivity(), getString(R.string.err_choose_location_search)).show();
                return false;
            }

            Intent intent = new Intent(getActivity(), ActivitySearchBusinessResult.class);
            intent.putExtra(Params.SEARCH_KEY_WORD, editTextSearch.getText().toString());
            intent.putExtra(Params.LATITUDE, String.valueOf(choosedLatLng.latitude));
            intent.putExtra(Params.LONGITUDE, String.valueOf(choosedLatLng.longitude));
            intent.putExtra(Params.SUB_CATEGORY_ID, subcategoryId);
            startActivity(intent);

        } else if (searchType == SearchType.POSTS) {
            Intent intent = new Intent(getActivity(), ActivitySearchPostResult.class);
            intent.putExtra(Params.SEARCH_KEY_WORD, editTextSearch.getText().toString());
            startActivity(intent);
        } else if (searchType == SearchType.USERS) {
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


    @Override
    public void notifySelectCategory(int categoryListPosition) {
        int i = categoryListPosition;
        textViewCategories.setText(categories.get(categoryListPosition).name);
        progressDialog.show();
        ((MyApplication) getActivity().getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);
        new GetBusinessSubcategories(getActivity(), categories.get(categoryListPosition).id, FragmentSearch_OLD.this).execute();
    }

    @Override
    public void notifySelectSubcategory(int subcategoryListPosition) {
        this.subcategoryId = subCategories.get(subcategoryListPosition).id;
        textViewSubCategories.setText(subCategories.get(subcategoryListPosition).name);
    }


}
