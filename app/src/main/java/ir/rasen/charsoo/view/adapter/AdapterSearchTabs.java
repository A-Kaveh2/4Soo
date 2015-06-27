package ir.rasen.charsoo.view.adapter;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.fragment.FragmentSearch;
import ir.rasen.charsoo.view.fragment.search.FragmentSearchBusiness;
import ir.rasen.charsoo.view.fragment.search.FragmentSearchProduct;
import ir.rasen.charsoo.view.fragment.search.FragmentSearchUser;

public class AdapterSearchTabs extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    private Context context;

    private FragmentSearchProduct fragmentSearchProduct;
    private FragmentSearchUser fragmentSearchUser;
    private FragmentSearchBusiness fragmentSearchBusiness;

    public AdapterSearchTabs(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragmentSearchProduct = FragmentSearchProduct.newInstance();
                return fragmentSearchProduct;
            case 1:
                fragmentSearchBusiness = FragmentSearchBusiness.newInstance();
                return fragmentSearchBusiness;
            case 2:
                fragmentSearchUser = FragmentSearchUser.newInstance();
                return fragmentSearchUser;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.products);
            case 1:
                return context.getString(R.string.businesses);
            case 2:
                return context.getString(R.string.users);
            default:
                return null;
        }
    }

    public Fragment getFragment(FragmentSearch.SearchType searchType) {
        switch (searchType) {
            case PRODUCTS:
                return fragmentSearchProduct;
            case BUSINESSES:
                return fragmentSearchBusiness;
            case USERS:
                return fragmentSearchUser;
            default:
                return null;
        }
    }

}
