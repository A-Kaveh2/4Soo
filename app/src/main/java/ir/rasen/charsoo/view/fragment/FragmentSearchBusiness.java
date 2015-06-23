package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rasen.charsoo.R;

public class FragmentSearchBusiness extends Fragment {
    public static final String TAG = "FragmentSearchBusiness";

    public static FragmentSearchBusiness newInstance() {
        FragmentSearchBusiness fragment = new FragmentSearchBusiness();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_business_specials,
                container, false);

        init(view);

        return view;
    }

    private void init(View view) {
    }

}
