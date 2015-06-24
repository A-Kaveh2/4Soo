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
import android.widget.GridView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.SearchItemPost;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.model.search.SearchPost;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.view.adapter.AdapterPostGrid;
import ir.rasen.charsoo.view.adapter.AdapterSpecialProductsGrid;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;

public class FragmentSearchProduct extends Fragment implements IWebserviceResponse {
    public static final String TAG = "FragmentSearchProduct";

    private GridView gridView;
    private AdapterPostGrid adapter;

    private ArrayList<SearchItemPost> searchResults;
    private boolean showingResults = false;
    private String currentResultIsFor = "";

    public static FragmentSearchProduct newInstance() {
        FragmentSearchProduct fragment = new FragmentSearchProduct();
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
        View view = inflater.inflate(R.layout.fragment_search_products,
                container, false);

        searchResults = new ArrayList<>();
        adapter = new AdapterPostGrid(getActivity(), searchResults, 0, Post.GetPostType.SEARCH);
        init(view);

        return view;
    }

    private void init(View view) {
        ArrayList<Post> fakePosts = new ArrayList<>();
        Post post1 = new Post();
        post1.title = "عنوان";
        post1.pictureId = 6372;
        fakePosts.add(post1);
        Post post2 = new Post();
        post2.title = "عنوان";
        post2.pictureId = 5410;
        fakePosts.add(post2);
        Post post3 = new Post();
        post3.title = "عنوان 3212313131312";
        post3.pictureId = 6372;
        fakePosts.add(post3);
        Post post4 = new Post();
        post4.title = "PRODUCT";
        post4.pictureId = 5410;
        fakePosts.add(post4);
        gridView = (GridView) view.findViewById(R.id.gridView);
        AdapterSpecialProductsGrid adapterSpecialProductsGrid = new AdapterSpecialProductsGrid(getActivity(), fakePosts);
        gridView.setAdapter(adapterSpecialProductsGrid);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        ((ActivityMain) getActivity()).getFragmentSearch().searchStarted();
        new SearchPost(getActivity(), currentResultIsFor, searchResults.get(searchResults.size() - 1).postId, getResources().getInteger(R.integer.lazy_load_limitation), FragmentSearchProduct.this).execute();
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof ArrayList) {
            if (!showingResults) {
                gridView.setAdapter(adapter);
                showingResults = true;
            }
            ((ActivityMain) getActivity()).getFragmentSearch().searchFinished();
            ArrayList<SearchItemPost> temp = (ArrayList<SearchItemPost>) result;
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
        this.currentResultIsFor = currentResultIsFor;
    }

    public String getCurrentResultIsFor() {
        return currentResultIsFor;
    }

    public boolean isSearching() {
        return ((ActivityMain) getActivity()).getFragmentSearch().isSearching();
    }

}
