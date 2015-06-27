package ir.rasen.charsoo.view.fragment.search;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.search.SearchUser;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.view.adapter.AdapterUsers;
import ir.rasen.charsoo.view.adapter.AdapterUsersFromBAItems;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;

public class FragmentSearchUser extends Fragment implements IWebserviceResponse {
    public static final String TAG = "FragmentSearchUser";

    private ListView listView;
    private AdapterUsersFromBAItems adapter;

    private ArrayList<BaseAdapterItem> searchResults;
    private boolean showingResults = false;
    private String currentResultIsFor = "";

    public static FragmentSearchUser newInstance() {
        FragmentSearchUser fragment = new FragmentSearchUser();
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
        View view = inflater.inflate(R.layout.fragment_search_user_specials,
                container, false);

        searchResults = new ArrayList<>();
        adapter = new AdapterUsersFromBAItems(getActivity(), 0, searchResults, AdapterUsersFromBAItems.Mode.USERS);
        init(view);

        return view;
    }

    private void init(View view) {
        ArrayList<User> users = new ArrayList<>();
        User User1 = new User();
        User1.name = "عنوان";
        User1.userIdentifier = "user1";
        User1.coverPictureId = 6372;
        users.add(User1);
        User User2 = new User();
        User2.name = "عنوان";
        User2.userIdentifier = "user2";
        User2.coverPictureId = 5410;
        users.add(User2);
        User User3 = new User();
        User3.name = "عنوان 3212313131312";
        User3.userIdentifier = "user3";
        User3.coverPictureId = 6372;
        users.add(User3);
        User User4 = new User();
        User4.name = "BUSINESS";
        User4.userIdentifier = "avsf";
        User4.coverPictureId = 5410;
        users.add(User4);
        listView = (ListView) view.findViewById(R.id.listView);
        AdapterUsers specialsAdapter = new AdapterUsers(getActivity(), users);
        listView.setAdapter(specialsAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem,
                    currentVisibleItemCount,
                    currentScrollState;

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
                    if (!isSearching() && searchResults.size() > 0 && searchResults.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ((ActivityMain) getActivity()).getFragmentSearch().onScroll(firstVisibleItem);
            }
        });
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        ((ActivityMain) getActivity()).getFragmentSearch().searchStarted();
        new SearchUser(getActivity(),currentResultIsFor,searchResults.get(searchResults.size()-1).getId(),getResources().getInteger(R.integer.lazy_load_limitation),FragmentSearchUser.this).execute();
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
