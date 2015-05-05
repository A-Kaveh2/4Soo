package ir.rasen.charsoo;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rasen.charsoo.adapters.AdapterPostTimeLine;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.GridViewWithHeaderAndFooter;
import ir.rasen.charsoo.webservices.post.GetTimeLinePosts;

public class FragmentHome extends Fragment implements IWebserviceResponse {

    ProgressDialog progressDialog;
    AdapterPostTimeLine adapterPostTimeLine;
    GridViewWithHeaderAndFooter gridView;
    ArrayList<Post> results;
    ArrayList<Post> sampleResults;
    private View listFooterView;
    String searchKeyWord;
    String latitude;
    String longitude;
    int subCategoryId;

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;
    SwipeRefreshLayout swipeLayout;
    BroadcastReceiver timeLineUpdateReceiver;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);

        try {
            sampleResults = TestUnit.getPostAdapterListItems();
        } catch (Exception e) {

        }



        results = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading TimeLine");

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (status == Status.LOADING_MORE) {
                    swipeLayout.setRefreshing(false);
                    return;
                }

                status = Status.REFRESHING;
                results.clear();
                new GetTimeLinePosts(getActivity(), LoginInfo.getUserId(getActivity()), 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
                //gridView.setEnabled(false);
            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gridView);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem
                    ,
                    currentVisibleItemCount
                    ,
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
                    if (status != Status.LOADING_MORE
                            && results.size() > 0 && results.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });

        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
        gridView.addFooterView(listFooterView);

        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        if (myApplication.isHomeCreated) {
            initialize(myApplication.homePosts);
        } else {
            progressDialog.show();
            new GetTimeLinePosts(getActivity(), LoginInfo.getUserId(getActivity()), 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
            myApplication.isHomeCreated = true;
        }

        timeLineUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                switch (bundle.getString(Params.UPATE_TIME_LINE_TYPE)) {
                    case Params.UPATE_TIME_LINE_TYPE_SHARE:
                        updateShare(true,bundle.getInt(Params.POST_ID));
                        break;
                    case Params.UPATE_TIME_LINE_TYPE_CANCEL_SHARE:
                        updateShare(false,bundle.getInt(Params.POST_ID));
                        break;
                }

            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(timeLineUpdateReceiver, new IntentFilter(Params.UPATE_TIME_LINE));


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(timeLineUpdateReceiver);
    }

    private void initialize(ArrayList<Post> results) {
        if (status == Status.FIRST_TIME) {
            adapterPostTimeLine = new AdapterPostTimeLine(getActivity(), results);
            gridView.setAdapter(adapterPostTimeLine);
        } else {
            //it is loading more
            listFooterView.setVisibility(View.GONE);
        }
        status = Status.NONE;
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        listFooterView.setVisibility(View.VISIBLE);

        new GetTimeLinePosts(getActivity(), LoginInfo.getUserId(getActivity()), results.get(results.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
    }

    @Override
    public void getResult(Object result) {
        //gridView.setEnabled(true);
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        if (swipeLayout.isRefreshing())
            swipeLayout.setRefreshing(false);
        if (result instanceof ArrayList) {
            ArrayList<Post> temp = (ArrayList<Post>) result;
            results.addAll(temp);
            ((MyApplication) getActivity().getApplication()).homePosts = results;
            initialize(results);
            if (status != Status.FIRST_TIME)
                adapterPostTimeLine.loadMore(temp);
        }
    }

    public void updateShare(boolean isShared,int postId) {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).id == postId) {
                results.get(i).isShared = isShared;
                adapterPostTimeLine.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode)).show();
    }


}
