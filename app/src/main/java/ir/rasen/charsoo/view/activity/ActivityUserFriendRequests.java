package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshList;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.model.friend.GetUserFriendRequests;
import ir.rasen.charsoo.view.adapter.AdapterUserFriendshipRequest;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityUserFriendRequests extends CharsooActivity implements IWebserviceResponse, IPullToRefresh {

    ProgressDialog progressDialog;
    int visitedUserId;
    AdapterUserFriendshipRequest adapterFriendshipRequest;
    ListView listView;
    ArrayList<BaseAdapterItem> requests;
    ArrayList<BaseAdapterItem> sampleRequests;


    //pull_to_refresh_lib
    PullToRefreshList pullToRefreshListView;

    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        requests.clear();
        new GetUserFriendRequests(ActivityUserFriendRequests.this, LoginInfo.getUserId(ActivityUserFriendRequests.this), ActivityUserFriendRequests.this).execute();
    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }


    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview_pull_to_refresh);
        setTitle(getString(R.string.friend_requests));
        try {
            sampleRequests = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);

        requests = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityUserFriendRequests.this);
        listView = pullToRefreshListView.getListView();

        progressDialog.show();
        new GetUserFriendRequests(ActivityUserFriendRequests.this, visitedUserId, ActivityUserFriendRequests.this).execute();

        //notify FragmentUser to hide request announcement
        Intent intent = new Intent(Params.REMOVE_REQUEST_ANNOUNCEMENT);
        LocalBroadcastManager.getInstance(ActivityUserFriendRequests.this).sendBroadcast(intent);
    }


    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        Toast.makeText(ActivityUserFriendRequests.this, "Load more data", Toast.LENGTH_LONG).show();
        //this webservice doesn't support load more by the now!

       /* status = Status.LOADING_MORE;
         pullToRefreshListView.setFooterVisibility(View.VISIBLE);
        new GetUserFriendRequests(ActivityFriendRequests.this,visitedUserId,ActivityFriendRequests.this).execute();*/
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
            onBackPressed();
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

            pullToRefreshListView.setResultSize(requests.size());

            if (status == Status.FIRST_TIME) {
                adapterFriendshipRequest = new AdapterUserFriendshipRequest(ActivityUserFriendRequests.this, requests);
                listView.setAdapter(adapterFriendshipRequest);
            } else if (status == Status.REFRESHING) {
                adapterFriendshipRequest.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more
                adapterFriendshipRequest.loadMore(temp);
                pullToRefreshListView.setFooterVisibility(View.GONE);
            }
            status = Status.NONE;
        }
    }



    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshListView.onRefreshComplete();
        new DialogMessage(ActivityUserFriendRequests.this, ServerAnswer.getError(ActivityUserFriendRequests.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

    @Override
    public void onBackPressed() {
        backToParent();
    }

    public void backToParent(){
        Intent i = getIntent();
        if(adapterFriendshipRequest.getAcceptedUsers().size() != 0) {
            ((MyApplication) getApplication()).newFriends = adapterFriendshipRequest.getAcceptedUsers();
            i.putExtra(Params.NEW_FIREND, true);
        }
        else {
            ((MyApplication) getApplication()).newFriends = new ArrayList<>();
            i.putExtra(Params.NEW_FIREND, false);
        }
        ArrayList<BaseAdapterItem> acceptedUsers=adapterFriendshipRequest.getAcceptedUsers(),items=adapterFriendshipRequest.getRemainingFriendRequests();
        boolean hasRemainingRequests=false;
        for (int j = 0; j < items.size(); j++) {
            if (!acceptedUsers.contains(items.get(j)))
            {
                hasRemainingRequests=true;
                break;
            }
        }
        i.putExtra(Params.HAS_REMAINIG_FRIEND_REQUESTS_STRING,hasRemainingRequests);
        setResult(RESULT_OK, i);
        finish();
    }

}
