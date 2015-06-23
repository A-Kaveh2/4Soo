package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.view.adapter.AdapterSpecialBusinesses;

public class FragmentSearchBusiness extends Fragment {
    public static final String TAG = "FragmentSearchBusiness";

    private ListView listView;
    private AdapterSpecialBusinesses adapter;

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
        ArrayList<Business> businesses = new ArrayList<>();
        Business Business1 = new Business();
        Business1.name="عنوان";
        Business1.businessIdentifier = "busin1";
        Business1.coverPictureId = 6372;
        businesses.add(Business1);
        Business Business2 = new Business();
        Business2.name="عنوان";
        Business2.businessIdentifier = "busin2";
        Business2.coverPictureId = 5410;
        businesses.add(Business2);
        Business Business3 = new Business();
        Business3.name="عنوان 3212313131312";
        Business3.businessIdentifier = "busin3";
        Business3.coverPictureId = 6372;
        businesses.add(Business3);
        Business Business4 = new Business();
        Business4.name="BUSINESS";
        Business4.businessIdentifier = "avsf";
        Business4.coverPictureId = 5410;
        businesses.add(Business4);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new AdapterSpecialBusinesses(getActivity(), businesses);
        listView.setAdapter(adapter);
    }

}
