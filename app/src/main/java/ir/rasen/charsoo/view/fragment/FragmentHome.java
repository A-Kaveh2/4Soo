package ir.rasen.charsoo.view.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshList;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.model.NetworkConnectivityReciever;
import ir.rasen.charsoo.model.post.GetTimeLinePost;
import ir.rasen.charsoo.model.post.GetTimeLinePosts_new;
import ir.rasen.charsoo.view.adapter.AdapterPostTimeLine;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IGetNewTimeLinePost;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.interface_m.NetworkStateChangeListener;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.buttons.FloatButton;
import ir.rasen.charsoo.view.widgets.pull_to_refresh.PullToRefreshListView;

//import ir.rasen.charsoo.model.post.GetTimeLinePosts;

public class FragmentHome extends Fragment implements IWebserviceResponse, IPullToRefresh, IGetNewTimeLinePost,NetworkStateChangeListener {

    public static final String TAG="FragmentHome";

    WaitDialog progressDialog;
    AdapterPostTimeLine adapterPostTimeLine;
    ListView listView;
    ArrayList<Post> results;
    ArrayList<Post> sampleResults;

    @Override
    public void doOnNetworkConnected() {
        try {
            if (LoginInfo.isLoggedIn(getActivity())) {
                if (!((MyApplication) getActivity().getApplication()).isHomeCreated)
                    recursivelyCheckForTimeLinePosts();
            }
        }catch(Exception e){};
    }


    //pull_to_refresh_lib
    //private PullToRefreshListView pullToRefreshListView;


    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;
    BroadcastReceiver timeLineUpdateReceiver;
    BroadcastReceiver updatePostLastThreeComments;
    PullToRefreshList pullToRefreshListView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home,
                container, false);

        NetworkConnectivityReciever.setNetworkStateListener(TAG,FragmentHome.this);

        try {
            sampleResults = TestUnit.getPostAdapterListItems();
        } catch (Exception e) {

        }

        results = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new WaitDialog(getActivity());

        pullToRefreshListView = new PullToRefreshList(getActivity(), (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list), FragmentHome.this);
        listView = pullToRefreshListView.getListView();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView v, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (v.getId() == listView.getId()) {
                    ((FloatButton) view.findViewById(R.id.btn_search)).onScroll(v, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });

        status = Status.FIRST_TIME;
        progressDialog.show();
        new GetTimeLinePosts_new(getActivity(), LoginInfo.getUserId(getActivity()),0,0,0,0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();

        timeLineUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                switch (bundle.getString(Params.UPDATE_TIME_LINE_TYPE)) {
                    case Params.UPATE_TIME_LINE_TYPE_SHARE:
                        updateShare(true, bundle.getInt(Params.POST_ID_INT));
                        break;
                    case Params.UPATE_TIME_LINE_TYPE_CANCEL_SHARE:
                        updateShare(false, bundle.getInt(Params.POST_ID_INT));
                        break;
                }

            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(timeLineUpdateReceiver, new IntentFilter(Params.UPATE_TIME_LINE));

        updatePostLastThreeComments = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();

                //if the comment is one of the post's last three comments
                int postId = bundle.getInt(Params.POST_ID_INT);
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).id == postId)
                        new GetTimeLinePost(getActivity(), postId, results, FragmentHome.this).execute();
                }

            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updatePostLastThreeComments, new IntentFilter(Params.UPDATE_TIME_LINE_POST_LAST_THREE_COMMENTS));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(timeLineUpdateReceiver);
    }

    private void initialize(ArrayList<Post> results) {
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        myApplication.isHomeCreated = true;

        adapterPostTimeLine = new AdapterPostTimeLine(getActivity(), results);
        listView.setAdapter(adapterPostTimeLine);
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        pullToRefreshListView.setFooterVisibility(View.VISIBLE);
        Hashtable<String,Integer> lastLoadedItems=Post.getLastLoadedTimelineItems(results);
//        new GetTimeLinePosts(getActivity(), LoginInfo.getUserId(getActivity()), results.get(results.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
        new GetTimeLinePosts_new(getActivity(),LoginInfo.getUserId(getActivity()),lastLoadedItems.get(Params.BUSINESS_POST_FOR_TIMELINE),
                lastLoadedItems.get(Params.FRIEND_SHARED_POST_FOR_TIMELINE),lastLoadedItems.get(Params.FRIEND_FOLLOW_ANNOUNCE_FOR_TIMELINE),
                lastLoadedItems.get(Params.FRIEND_REVIEW_ANNOUNCE_FOR_TIMELINE),getResources().getInteger(R.integer.lazy_load_limitation),
                FragmentHome.this).execute();
    }

    @Override
    public void getResult(Object result) {
        //listView.setEnabled(true);
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        if (result instanceof ArrayList) {
            ArrayList<Post> temp = (ArrayList<Post>) result;
            results.addAll(temp);

            pullToRefreshListView.setResultSize(results.size());

            if (status == Status.FIRST_TIME) {
                ((MyApplication) getActivity().getApplication()).homePosts = results;
                initialize(results);
            } else if (status == Status.REFRESHING) {
                adapterPostTimeLine.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
                ((MyApplication) getActivity().getApplication()).homePosts = results;
            } else if (status == Status.LOADING_MORE) {
                adapterPostTimeLine.loadMore(temp);
                ((MyApplication) getActivity().getApplication()).homePosts.addAll(temp);
                pullToRefreshListView.setFooterVisibility(View.GONE);
            }


        }
    }

    public void updateShare(boolean isShared, int postId) {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).id == postId) {
                results.get(i).isShared = isShared;
                adapterPostTimeLine.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        pullToRefreshListView.onRefreshComplete();
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode,callerStringID+">"+TAG)).show();
    }

    @Override
    public void notifyRefresh() {
        if (((MyApplication) getActivity().getApplication()).isHomeCreated) {
            status = Status.REFRESHING;
            results.clear();
            new GetTimeLinePosts_new(getActivity(), LoginInfo.getUserId(getActivity()),0,0,0,0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
        }
        else
            pullToRefreshListView.onRefreshComplete();
    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }

    @Override
    public void notifyGetNewPost(Post post) {
        adapterPostTimeLine.updatePostLastThreeComments(post);
    }



    Handler internetCheckHandler=new Handler();
    private void recursivelyCheckForTimeLinePosts(){
        internetCheckHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!((MyApplication) getActivity().getApplication()).isHomeCreated) {
                    new GetTimeLinePosts_new(getActivity(), LoginInfo.getUserId(getActivity()),0,0,0,0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
                } else {
                    ConnectivityManager cm =
                            (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting())
                        recursivelyCheckForTimeLinePosts();
                }
            }
        }, 1000);
    }

}
