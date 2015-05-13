package ir.rasen.charsoo.view.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.adapter.AdapterPostTimeLine;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshList;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.post.GetTimeLinePosts;

public class FragmentHome extends Fragment implements IWebserviceResponse,IPullToRefresh {

    ProgressDialog progressDialog;
    AdapterPostTimeLine adapterPostTimeLine;
    ListView listView;
    ArrayList<Post> results;
    ArrayList<Post> sampleResults;


    //pull_to_refresh_lib
    //private PullToRefreshListView pullToRefreshListView;


    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        results.clear();
        new GetTimeLinePosts(getActivity(), LoginInfo.getUserId(getActivity()), 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;
    BroadcastReceiver timeLineUpdateReceiver;
    PullToRefreshList pullToRefreshListView;

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

        pullToRefreshListView = new PullToRefreshList(getActivity(), (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list), FragmentHome.this);
        listView = pullToRefreshListView.getListView();


        status = Status.FIRST_TIME;
        progressDialog.show();
        new GetTimeLinePosts(getActivity(), LoginInfo.getUserId(getActivity()), 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();

        timeLineUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                switch (bundle.getString(Params.UPATE_TIME_LINE_TYPE)) {
                    case Params.UPATE_TIME_LINE_TYPE_SHARE:
                        updateShare(true, bundle.getInt(Params.POST_ID));
                        break;
                    case Params.UPATE_TIME_LINE_TYPE_CANCEL_SHARE:
                        updateShare(false, bundle.getInt(Params.POST_ID));
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
        new GetTimeLinePosts(getActivity(), LoginInfo.getUserId(getActivity()), results.get(results.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), FragmentHome.this).execute();
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
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode)).show();
    }


}
