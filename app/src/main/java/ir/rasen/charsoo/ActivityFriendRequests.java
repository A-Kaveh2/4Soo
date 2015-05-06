package ir.rasen.charsoo;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.Footer;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.adapters.AdapterFriendshipRequest;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.friend.GetUserFriendRequests;
import ir.rasen.charsoo.webservices.friend.GetUserFriends;


public class ActivityFriendRequests extends ActionBarActivity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    int visitedUserId;
    AdapterFriendshipRequest adapterFriendshipRequest;
    ListView listView;
    ArrayList<BaseAdapterItem> requests;
    ArrayList<BaseAdapterItem> sampleRequests;


    //pull_to_refresh_lib
    private PullToRefreshListView pullToRefreshListView;
    private Footer footer;


    private enum Status {FIRST_TIME, LOADING_MORE,REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview_pull_to_refresh);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.friend_requests));
        try {
            sampleRequests = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);

        requests = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                status = Status.REFRESHING;
                requests.clear();
                new GetUserFriendRequests(ActivityFriendRequests.this, LoginInfo.getUserId(ActivityFriendRequests.this),ActivityFriendRequests.this).execute();
            }
        });
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

                if (!pullToRefreshListView.isRefreshing()
                        && requests.size() > 0 && requests.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                    footer.setVisibility(View.VISIBLE);
                    //loadMoreData();
                }
            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        registerForContextMenu(listView);
        footer = new Footer(this);
        listView.addFooterView(footer.getFooterView(), null, false);

        progressDialog.show();
        new GetUserFriendRequests(ActivityFriendRequests.this,visitedUserId,ActivityFriendRequests.this).execute();
    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        //listFooterView.setVisibility(View.VISIBLE);
        footer.setVisibility(View.GONE);
        new GetUserFriendRequests(ActivityFriendRequests.this,visitedUserId,ActivityFriendRequests.this).execute();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next_button, menu);*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();


        if (result instanceof ArrayList) {
            ArrayList<BaseAdapterItem> temp = (ArrayList<BaseAdapterItem>) result;
            requests.addAll(temp);


            if (status == Status.FIRST_TIME) {
                adapterFriendshipRequest = new AdapterFriendshipRequest(ActivityFriendRequests.this,requests);
                listView.setAdapter(adapterFriendshipRequest);
            } else  if (status == Status.REFRESHING){
                adapterFriendshipRequest.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            }
            else {
                //it is loading more
                adapterFriendshipRequest.loadMore(temp);
                footer.setVisibility(View.GONE);
            }
            status = Status.NONE;
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityFriendRequests.this, ServerAnswer.getError(ActivityFriendRequests.this, errorCode)).show();
    }
}
