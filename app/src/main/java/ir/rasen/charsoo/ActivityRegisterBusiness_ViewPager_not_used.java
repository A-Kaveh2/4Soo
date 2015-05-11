package ir.rasen.charsoo;

/**
 * Created by android on 3/28/2015.
 */


import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.List;

public class ActivityRegisterBusiness_ViewPager_not_used extends ActionBarActivity implements
        ActionBar.TabListener {


    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private android.support.v7.app.ActionBar actionBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business_view_pager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DeepSkyBlue)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Fragment> fragments = getFragments();

        // Instantiate a ViewPager and a PagerAdapter.
       /* mPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        final int[] icons = new int[]{
                R.drawable.ic_store_mall_directory_white_24dp,
                R.drawable.ic_info_white_24dp,
                R.drawable.ic_place_white_24dp
        };

        for (int i = 0; i < icons.length; i++) {
            actionBar.addTab(actionBar.newTab()
                    .setIcon(getResources().getDrawable(icons[i]))
                    .setTabListener(this));
        }

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(new FragmentBusinessRegisterBaseInfo());
        fList.add(new FragmentBusinessRegisterContactInfo());
        fList.add(new FragmentBusinessRegisterLocationInfo());
        return fList;
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

}