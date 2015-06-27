package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LocationManagerTracker;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.model.search.SearchBusinessesLocation;
import ir.rasen.charsoo.model.search.SearchPost;
import ir.rasen.charsoo.model.search.SearchUser;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.view.adapter.AdapterSearchTabs;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.fragment.search.FragmentSearchBusiness;
import ir.rasen.charsoo.view.fragment.search.FragmentSearchProduct;
import ir.rasen.charsoo.view.fragment.search.FragmentSearchUser;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.PagerSlidingTabStrip;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.buttons.ButtonFontStandard;
import ir.rasen.charsoo.view.widgets.material_library.widgets.Dialog;

public class FragmentSearch extends Fragment implements IWebserviceResponse {
    public static final String TAG = "FragmentSearch";

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    public boolean back() {
        if (mapView != null && mapView.getVisibility() == View.VISIBLE) {
            mapView.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    public enum SearchType {BUSINESSES, PRODUCTS, USERS}

    private SearchType searchType;

    private PagerSlidingTabStrip tabs;
    private ViewPager viewPager;
    private AdapterSearchTabs adapter;
    private EditTextFont search;
    private ButtonFontStandard place;
    private View filters, headers;
    private TextViewFont status;
    private View waitView;

    private ActivityMain activityMain;
    private boolean searching = true;
    private LocationManager locManager;
    private Location location;
    private GoogleMap googleMap;
    private MapView mapView;
    private MarkerOptions marker;
    private View view;
    private Bundle savedInstanceState;
    private boolean headersVisibility = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,
                container, false);

        activityMain = (ActivityMain) getActivity();

        init(view);
        this.view = view;
        this.savedInstanceState = savedInstanceState;

        return view;
    }

    private void init(View view) {
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        search = (EditTextFont) view.findViewById(R.id.edt_search);
        place = (ButtonFontStandard) view.findViewById(R.id.edt_place);
        filters = view.findViewById(R.id.view_filter);
        waitView = view.findViewById(R.id.waitView);
        status = (TextViewFont) waitView.findViewById(R.id.waitView_status);
        headers = view.findViewById(R.id.view_header);
        waitView.setVisibility(View.GONE);
        adapter = new AdapterSearchTabs(getFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        hideFilters();
                        searchType = SearchType.PRODUCTS;
                        FragmentSearchProduct fragmentSearchProduct =
                                ((FragmentSearchProduct) adapter.getFragment(searchType));
                        if (fragmentSearchProduct != null)
                            if (!fragmentSearchProduct.getCurrentResultIsFor().equals(search.getText().toString()))
                                searchNow();
                        break;
                    case 1:
                        searchType = SearchType.BUSINESSES;
                        FragmentSearchBusiness fragmentSearchBusiness =
                                ((FragmentSearchBusiness) adapter.getFragment(searchType));
                        if (fragmentSearchBusiness != null)
                            if (!fragmentSearchBusiness.getCurrentResultIsFor().equals(search.getText().toString()))
                                searchNow();
                        break;
                    case 2:
                        hideFilters();
                        searchType = SearchType.USERS;
                        FragmentSearchUser fragmentSearchUser =
                                ((FragmentSearchUser) adapter.getFragment(searchType));
                        if (fragmentSearchUser != null)
                            if (!fragmentSearchUser.getCurrentResultIsFor().equals(search.getText().toString()))
                                searchNow();
                        break;
                }
                showFloatingViews();
                search.setHint(getHint(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchNow();
                }
                return false;
            }
        });
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (mapView != null && mapView.getVisibility() == View.VISIBLE) {
                    mapView.setVisibility(View.GONE);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        searchNow();
                        return true;
                    }
                }
                return false;
            }
        });
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapView != null && mapView.getVisibility() == View.VISIBLE)
                    mapView.setVisibility(View.GONE);
                else
                    initMapView();
            }
        });/*new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (place.getRight() - place.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        place.setText("");
                        return true;
                    }
                    if (event.getRawX() < (place.getRight() - place.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        initMapView();
                        return true;
                    }
                }
                return false;
            }
        });*/

        viewPager.setCurrentItem(1);
        setUpGeneralFeatures(view);
        setUpLocationServices();
    }

    private void setUpGeneralFeatures(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    activityMain.hideSoftKeyboard();
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUpGeneralFeatures(innerView);
            }
        }
    }

    private void searchNow() {
        activityMain.hideSoftKeyboard();

        String searchKey = search.getText().toString().trim();
        search.setText(searchKey);
        String placeStr = place.getText().toString().trim();
        place.setText(placeStr);

        if (searchKey.length() == 0) {
            search.setError(getString(R.string.err_fill_search_box));
//            new DialogMessage(getActivity(), R.string.search, R.string.err_fill_search_box).show();
            return;
        }

        switch (searchType) {
            case BUSINESSES:
                if (placeStr.length() == 0) {
                    if (location == null) {
                        locationIsNull();
                        return;
                    }
                    new SearchBusinessesLocation(getActivity(), searchKey, 0, location.getLatitude() + "", location.getLongitude() + "", 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentSearch.this).execute();
                } else {
                    if (marker != null)
                        new SearchBusinessesLocation(getActivity(), searchKey, 0, marker.getPosition().latitude + "", marker.getPosition().longitude + "", 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentSearch.this).execute();
                    else {
                        initMapView();
                    }
                }
                ((FragmentSearchBusiness) adapter.getFragment(searchType)).setCurrentResultIsFor(searchKey);
                ((FragmentSearchBusiness) adapter.getFragment(searchType)).reset();
                break;
            case PRODUCTS:
                new SearchPost(getActivity(), searchKey, 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentSearch.this).execute();
                ((FragmentSearchProduct) adapter.getFragment(searchType)).setCurrentResultIsFor(searchKey);
                ((FragmentSearchProduct) adapter.getFragment(searchType)).reset();
                break;
            case USERS:
                new SearchUser(getActivity(), searchKey, 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentSearch.this).execute();
                ((FragmentSearchUser) adapter.getFragment(searchType)).setCurrentResultIsFor(searchKey);
                ((FragmentSearchUser) adapter.getFragment(searchType)).reset();
                break;
        }

        searchStarted();

    }

    @Override
    public void getResult(Object result) {
        if (result instanceof ArrayList) {
            switch (searchType) {
                case USERS:
                    ((FragmentSearchUser) adapter.getFragment(searchType)).getResult(result);
                    break;
                case BUSINESSES:
                    ((FragmentSearchBusiness) adapter.getFragment(searchType)).getResult(result);
                    break;
                case PRODUCTS:
                    ((FragmentSearchProduct) adapter.getFragment(searchType)).getResult(result);
                    break;
            }
        }
    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {
        searchFinished();
        //progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode, callerStringID + ">")).show();
    }

    private String getHint(int position) {
        switch (position) {
            case 0:
                return getString(R.string.search_products);
            case 1:
                return getString(R.string.search_businesses);
            case 2:
                return getString(R.string.search_users);
            default:
                return null;
        }
    }

    public void searchStarted() {
        searching = true;
        status.setText(getString(R.string.doing) + " " + getHint(viewPager.getCurrentItem()));
        waitView.setVisibility(View.VISIBLE);
    }

    public void searchFinished() {
        searching = false;
        waitView.setVisibility(View.GONE);
    }

    public boolean isSearching() {
        return searching;
    }

    // set place edit text view visibility
    private void hideFilters() {
        YoYo.with(Techniques.FadeOutDown).duration(200).playOn(filters);
    }

    private void setUpLocationServices() {
        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location newLocation) {
            if (isBetterLocation(newLocation, location)) {
                location = newLocation;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        locManager.removeUpdates(locationListener);
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void locationIsNull() {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            Dialog dialog = new Dialog(getActivity(), getString(R.string.search_businesses), getString(R.string.activate_location_services));
            dialog.addCancelButton(getString(R.string.cancel));
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    chooseLocationManualy();
                }
            });
            dialog.show();
        } else if (location == null) {
            chooseLocationManualy();
        }
    }

    private void chooseLocationManualy() {
        Dialog dialog = new Dialog(getActivity(), getString(R.string.search_businesses), getString(R.string.no_location_found));
        dialog.addCancelButton(getString(R.string.cancel));
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMapView();
            }
        });
        dialog.show();
    }

    private void initMapView() {
        //Map section
        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.setVisibility(View.VISIBLE);

        if (!LocationManagerTracker.isGooglePlayServicesAvailable(getActivity())) {
            Dialog dialog = new Dialog(getActivity(), getString(R.string.err), getString(R.string.err_map_loading_description));
            dialog.addCancelButton(getString(R.string.ok));
            dialog.setAcceptText(getString(R.string.install_now));
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = "com.google.android.gms";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            dialog.show();
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
                    if (marker == null) {
                        marker = new MarkerOptions().position(
                                latLng);
                        googleMap.addMarker(marker);
                        marker.draggable(true);
                    } else {
                        marker.position(latLng);
                    }
                    mapView.setVisibility(View.GONE);
                    place.setText(marker.getPosition().latitude + "," + marker.getPosition().longitude);
                }
            });
        }
    }

    private int mLastFirstVisibleItem;

    public void onScroll(int firstVisibleItem) {
        if (mLastFirstVisibleItem != -1) {
            if (mLastFirstVisibleItem < firstVisibleItem) {
                YoYo.with(Techniques.FadeOutUp).duration(200).playOn(headers);
                if (searchType == SearchType.BUSINESSES)
                    YoYo.with(Techniques.FadeOutDown).duration(200).playOn(filters);
                headersVisibility=false;
            }
            if (mLastFirstVisibleItem > firstVisibleItem) {
                showFloatingViews();
            }
        }
        mLastFirstVisibleItem = firstVisibleItem;
    }

    private void showFloatingViews() {
        if(!headersVisibility) {
            YoYo.with(Techniques.FadeInDown).duration(200).playOn(headers);
            headersVisibility=true;
        }
        if (searchType == SearchType.BUSINESSES)
            YoYo.with(Techniques.FadeInUp).duration(200).playOn(filters);
    }
}