package ir.rasen.charsoo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by android on 3/28/2015.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;

    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);

    }
    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
