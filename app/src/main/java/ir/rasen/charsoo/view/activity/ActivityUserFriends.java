package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.adapter.AdapterUserFriends;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.PullToRefreshList;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.friend.GetUserFriends;


public class ActivityUserFriends extends ActionBarActivity implements IWebserviceResponse, IPullToRefresh {

    ProgressDialog progressDialog;
    int visitedUserId;
    AdapterUserFriends adapterFriends;
    ListView listView;
    ArrayList<BaseAdapterItem> friends;
    ArrayList<BaseAdapterItem> sampleFriends;

    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        friends.clear();
        new GetUserFriends(ActivityUserFriends.this, visitedUserId, ActivityUserFriends.this).execute();

    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    //pull_to_refresh_lib
    PullToRefreshList pullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.friends));
        boolean hasRequest = false;
        try {
            sampleFriends = TestUnit.getBaseAdapterItems(getResources());
            hasRequest = getIntent().getBooleanExtra(Params.HAS_REQUEST, false);
        } catch (Exception e) {

        }

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);
        if (visitedUserId != LoginInfo.getUserId(this) || !hasRequest)
            (findViewById(R.id.btn_friend_requests)).setVisibility(View.GONE);

        friends = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityUserFriends.this);
        listView = pullToRefreshListView.getListView();

        progressDialog.show();
        new GetUserFriends(ActivityUserFriends.this, visitedUserId, ActivityUserFriends.this).execute();

        (findViewById(R.id.btn_friend_requests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityUserFriends.this, ActivityUserFriendRequests.class);
                intent1.putExtra(Params.VISITED_USER_ID, visitedUserId);
                startActivity(intent1);
            }
        });

    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...

        //this webservice doesn't support load more yet.
        /*status = Status.LOADING_MORE;
        pullToRefreshListView.setFooterVisibility(View.VISIBLE);
        new GetUserFriends(ActivityFriends.this, visitedUserId, ActivityFriends.this).execute();*/
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
            friends.addAll(temp);
            pullToRefreshListView.setResultSize(friends.size());

            if (status == Status.FIRST_TIME) {
                adapterFriends = new AdapterUserFriends(ActivityUserFriends.this, visitedUserId, friends);
                listView.setAdapter(adapterFriends);
            } else if (status == Status.REFRESHING) {
                adapterFriends.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more
                pullToRefreshListView.setFooterVisibility(View.GONE);
                adapterFriends.loadMore(temp);
            }
            status = Status.NONE;

        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserFriends.this, ServerAnswer.getError(ActivityUserFriends.this, errorCode)).show();
    }
}
