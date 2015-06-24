package ir.rasen.charsoo.view.fragment.search;

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
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.model.search.SearchBusinessesLocation;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.view.adapter.AdapterBusinessSearchResult;
import ir.rasen.charsoo.view.adapter.AdapterSpecialBusinesses;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;

public class FragmentSearchBusiness extends Fragment implements IWebserviceResponse {
    public static final String TAG = "FragmentSearchBusiness";


    private ListView listView;
    private AdapterBusinessSearchResult adapter;

    private ArrayList<BaseAdapterItem> searchResults;
    private boolean showingResults = false;
    private String currentResultIsFor = "";

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

        searchResults = new ArrayList<>();
        adapter = new AdapterBusinessSearchResult(getActivity(), searchResults);
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
        AdapterSpecialBusinesses adapterSpecialBusinesses = new AdapterSpecialBusinesses(getActivity(), businesses);
        listView.setAdapter(adapterSpecialBusinesses);
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        ((ActivityMain) getActivity()).getFragmentSearch().searchStarted();
        // TODO: ATTENTION NEEDED
        new SearchBusinessesLocation(getActivity(),currentResultIsFor,0,"35.463900","48.873900",searchResults.get(searchResults.size()-1).getId(),getResources().getInteger(R.integer.lazy_load_limitation),FragmentSearchBusiness.this).execute();
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof ArrayList) {
            if(!showingResults) {
                listView.setAdapter(adapter);
                showingResults=true;
            }
            ((ActivityMain) getActivity()).getFragmentSearch().searchFinished();
            ArrayList<BaseAdapterItem> temp = (ArrayList<BaseAdapterItem>) result;
            searchResults.addAll(temp);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {
        ((ActivityMain) getActivity()).getError(errorCode, callerStringID);
    }

    public void reset() {
        searchResults.clear();
    }

    public void setCurrentResultIsFor(String currentResultIsFor) {
        this.currentResultIsFor=currentResultIsFor;
    }
    public String getCurrentResultIsFor() {
        return currentResultIsFor;
    }

    public boolean isSearching() {
        return ((ActivityMain) getActivity()).getFragmentSearch().isSearching();
    }

}
