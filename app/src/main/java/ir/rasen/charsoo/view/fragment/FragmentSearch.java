package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
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

import java.util.ArrayList;

import ir.rasen.charsoo.R;
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

public class FragmentSearch extends Fragment implements IWebserviceResponse {
    public static final String TAG = "FragmentSearch";

    public enum SearchType {BUSINESSES, PRODUCTS, USERS}

    private SearchType searchType;

    private PagerSlidingTabStrip tabs;
    private ViewPager viewPager;
    private AdapterSearchTabs adapter;
    private EditTextFont search;
    private TextViewFont status;
    private View waitView;

    private ActivityMain activityMain;
    private boolean searching = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,
                container, false);

        activityMain = (ActivityMain) getActivity();
        init(view);

        return view;
    }

    private void init(View view) {
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        adapter = new AdapterSearchTabs(getFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
        search = (EditTextFont) view.findViewById(R.id.edt_search);
        waitView = view.findViewById(R.id.waitView);
        status = (TextViewFont) waitView.findViewById(R.id.waitView_status);
        waitView.setVisibility(View.GONE);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        search.setHint(getHint(position));
                        searchType = SearchType.PRODUCTS;
                        FragmentSearchProduct fragmentSearchProduct =
                                ((FragmentSearchProduct) adapter.getFragment(searchType));
                        if (fragmentSearchProduct != null)
                            if (!fragmentSearchProduct.getCurrentResultIsFor().equals(search.getText().toString()))
                                searchNow();
                        break;
                    case 1:
                        search.setHint(getHint(position));
                        searchType = SearchType.BUSINESSES;
                        FragmentSearchBusiness fragmentSearchBusiness =
                                ((FragmentSearchBusiness) adapter.getFragment(searchType));
                        if (fragmentSearchBusiness != null)
                            if (!fragmentSearchBusiness.getCurrentResultIsFor().equals(search.getText().toString()))
                                searchNow();
                        break;
                    case 2:
                        search.setHint(getHint(position));
                        searchType = SearchType.USERS;
                        FragmentSearchUser fragmentSearchUser =
                                ((FragmentSearchUser) adapter.getFragment(searchType));
                        if (fragmentSearchUser != null)
                            if (!fragmentSearchUser.getCurrentResultIsFor().equals(search.getText().toString()))
                                searchNow();
                        break;
                }
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

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        searchNow();
                        return true;
                    }
                }
                return false;
            }
        });
        viewPager.setCurrentItem(1);
        setUpGeneralFeatures(view);
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

        if (searchKey.length() == 0) {
            new DialogMessage(getActivity(), R.string.search, R.string.err_fill_search_box).show();
            return;
        }

        searchStarted();

        switch (searchType) {
            case BUSINESSES:
                new SearchBusinessesLocation(getActivity(),searchKey,0,"35.463900","48.873900",0,getResources().getInteger(R.integer.lazy_load_limitation),FragmentSearch.this).execute();
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
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode, callerStringID + ">" )).show();
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
}

