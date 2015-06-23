package ir.rasen.charsoo.view.adapter;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.fragment.FragmentSearchBusiness;
import ir.rasen.charsoo.view.fragment.FragmentSearchProduct;
import ir.rasen.charsoo.view.fragment.FragmentSearchUser;

public class AdapterSearchTabs extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    private Context context;

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
                return FragmentSearchProduct.newInstance();
            case 1:
                return FragmentSearchBusiness.newInstance();
            case 2:
                return FragmentSearchUser.newInstance();
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

}
