package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.adapter.AdapterSearchTabs;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.PagerSlidingTabStrip;

public class FragmentSearch extends Fragment {
    public static final String TAG = "FragmentSearch";

    private enum SearchType {BUSINESSES, POSTS, USERS}

    private SearchType searchType;

    private PagerSlidingTabStrip tabs;
    private ViewPager viewPager;
    private EditTextFont search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,
                container, false);

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
                        break;
                    case 1:
                        search.setHint(R.string.search_businesses);
                        break;
                    case 2:
                        search.setHint(R.string.search_users);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.requestFocus();
    }

}
