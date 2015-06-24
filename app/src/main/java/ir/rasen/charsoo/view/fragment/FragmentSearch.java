package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.model.search.SearchUser;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.view.adapter.AdapterSearchTabs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.PagerSlidingTabStrip;

public class FragmentSearch extends Fragment implements IWebserviceResponse{
    public static final String TAG = "FragmentSearch";

    private enum SearchType {BUSINESSES, PRODUCTS, USERS}

    private SearchType searchType;

    private PagerSlidingTabStrip tabs;
    private ViewPager viewPager;
    private EditTextFont search;

    private ActivityMain activityMain;

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
        AdapterSearchTabs adapter = new AdapterSearchTabs(getFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
        search = (EditTextFont) view.findViewById(R.id.edt_search);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        search.setHint(R.string.search_products);
                        searchType = SearchType.PRODUCTS;
                        break;
                    case 1:
                        search.setHint(R.string.search_businesses);
                        searchType = SearchType.BUSINESSES;
                        break;
                    case 2:
                        search.setHint(R.string.search_users);
                        searchType = SearchType.USERS;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.requestFocus();
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

        if(searchKey.length()==0) {

        }

        switch (searchType) {
            case BUSINESSES:
            /*if (subcategoryId == 0) {
                DialogMessage.error(getActivity(), getString(R.string.choose_category_search)).show();
                return false;
            }*/
                //if (choosedLatLng == null) {
                //    DialogMessage.error(getActivity(), getString(R.string.err_choose_location_search)).show();
                //    return false;
                //}
/*
                Intent intent = new Intent(getActivity(), ActivitySearchBusinessResult.class);
                intent.putExtra(Params.SEARCH_KEY_WORD, editTextSearch.getText().toString());
                intent.putExtra(Params.LATITUDE, String.valueOf(choosedLatLng.latitude));
                intent.putExtra(Params.LONGITUDE, String.valueOf(choosedLatLng.longitude));
                intent.putExtra(Params.SUB_CATEGORY_ID, subcategoryId);
                startActivity(intent);*/
                break;
            case PRODUCTS:
                /*Intent intent = new Intent(getActivity(), ActivitySearchPostResult.class);
                intent.putExtra(Params.SEARCH_KEY_WORD, editTextSearch.getText().toString());
                startActivity(intent);*/
                break;
            case USERS:
                new SearchUser(getActivity(),searchKey,0,getResources().getInteger(R.integer.lazy_load_limitation),FragmentSearch.this).execute();
        }
    }

    @Override
    public void getResult(Object result) {
/*        progressDialog.dismiss();
        if (result instanceof ArrayList) {
            ArrayList<BaseAdapterItem> temp = (ArrayList<BaseAdapterItem>) result;
            if(temp.size()== 0) {
                (findViewById(R.id.textView_no_result)).setVisibility(View.VISIBLE);
                return;
            }

            results.addAll(temp);

            if (status == Status.FIRST_TIME) {
                adapterUserSearchResult = new AdapterUsersFromBAItems(ActivitySearchUser.this, 0, results, AdapterUsersFromBAItems.Mode.USERS);
                listView.setAdapter(adapterUserSearchResult);
            } else {
                //it is loading more
                listFooterView.setVisibility(View.GONE);
                adapterUserSearchResult.loadMore(temp);
            }
            status = Status.NONE;
        }*/
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        //progressDialog.dismiss();
        //DialogMessage.error(ActivitySearchUser.this, ServerAnswer.getError(ActivitySearchUser.this, errorCode, callerStringID + ">" + this.getLocalClassName())).show();
    }

}

