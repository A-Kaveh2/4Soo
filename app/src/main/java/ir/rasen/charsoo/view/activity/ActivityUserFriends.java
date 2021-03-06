package ir.rasen.charsoo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshList;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.friend.GetUserFriends;
import ir.rasen.charsoo.model.user.GetUserHomeInfo;
import ir.rasen.charsoo.view.adapter.AdapterUsersFromBAItems;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;
import ir.rasen.charsoo.view.widgets.pull_to_refresh.PullToRefreshListView;


public class ActivityUserFriends extends CharsooActivity implements IWebserviceResponse, IPullToRefresh {

    WaitDialog progressDialog;
    int visitedUserId;
    AdapterUsersFromBAItems adapterFriends;
    ListView listView;
    ArrayList<BaseAdapterItem> friends;
    ArrayList<BaseAdapterItem> sampleFriends;

    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        new GetUserFriends(ActivityUserFriends.this, visitedUserId, ActivityUserFriends.this).execute();
        if (visitedUserId == LoginInfo.getUserId(this)){
            new GetUserHomeInfo(this,visitedUserId,visitedUserId,ActivityUserFriends.this).execute();
        }
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
        setTitle(getString(R.string.friends));
        boolean hasRequest = false;
        try {
            sampleFriends = TestUnit.getBaseAdapterItems(getResources());
            hasRequest = getIntent().getBooleanExtra(Params.HAS_REQUEST, false);
        } catch (Exception e) {

        }
        friends = new ArrayList<>();
        status = Status.FIRST_TIME;
        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);
        (findViewById(R.id.btn_friend_requests)).setVisibility(View.GONE);
        if (visitedUserId == LoginInfo.getUserId(this))
        {
            //help: checkout getResult Method of this
            new GetUserHomeInfo(this,visitedUserId,visitedUserId,ActivityUserFriends.this).execute();
        }




        progressDialog = new WaitDialog(this);
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
                startActivityForResult(intent1, Params.ACTION_NEW_FRIENDS);
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
                adapterFriends = new AdapterUsersFromBAItems(ActivityUserFriends.this, visitedUserId, friends, AdapterUsersFromBAItems.Mode.USERS);
                listView.setAdapter(adapterFriends);
            } else if (status == Status.REFRESHING) {
                friends.clear();
                if (adapterFriends==null)
                    adapterFriends = new AdapterUsersFromBAItems(ActivityUserFriends.this, visitedUserId, friends, AdapterUsersFromBAItems.Mode.USERS);
                adapterFriends.notifyDataSetChanged();
                friends.addAll(temp);
                adapterFriends.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more
                pullToRefreshListView.setFooterVisibility(View.GONE);
                adapterFriends.loadMore(temp);
            }
            status = Status.NONE;

        }
        else if (result instanceof User){
            User tempUser=(User)result;
            // hasRequest = (tempUser.friendRequestNumber> 0) ? true : false;
            if (tempUser.friendRequestNumber> 0)
                (findViewById(R.id.btn_friend_requests)).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshListView.onRefreshComplete();
        new DialogMessage(ActivityUserFriends.this, ServerAnswer.getError(ActivityUserFriends.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Params.ACTION_NEW_FRIENDS) {
                if(data.getExtras().getBoolean(Params.NEW_FIREND)){
                    friends.addAll(0,((MyApplication)getApplication()).newFriends);
                    adapterFriends.notifyDataSetChanged();
                }
                if(data.getExtras().getBoolean(Params.HAS_REMAINIG_FRIEND_REQUESTS)){
                    // hasRequest=false;
                    (findViewById(R.id.btn_friend_requests)).setVisibility(View.VISIBLE);
                }
                else
                {
                    //  hasRequest=true;
                    (findViewById(R.id.btn_friend_requests)).setVisibility(View.GONE);
                }
            }
        }
    }
}
