package ir.rasen.charsoo;

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

import ir.rasen.charsoo.adapters.AdapterBusinessFollowers;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.PullToRefreshList;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IPullToRefresh;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.business.GetBusinessFollowers;


public class ActivityBusinessFollowers extends ActionBarActivity implements IWebserviceResponse, IPullToRefresh {

    ProgressDialog progressDialog;
    int businessId;
    AdapterBusinessFollowers adapterFollowers;
    ListView listView;
    ArrayList<BaseAdapterItem> followers;
    //for the test
    ArrayList<BaseAdapterItem> sampleFollowers;

    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        followers.clear();
        new GetBusinessFollowers(ActivityBusinessFollowers.this, businessId, ActivityBusinessFollowers.this).execute();
    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }

    private enum Status {
        FIRST_TIME, LOADING_MORE, REFRESHING, NONE
    }

    private Status status;
    PullToRefreshList pullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_followers);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.followers));
        //for the test
        try {
            sampleFollowers = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }
        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);
        int userId = getIntent().getExtras().getInt(Params.USER_ID);
        if (userId != LoginInfo.getUserId(this))
            (findViewById(R.id.btn_blocked_users)).setVisibility(View.GONE);
        followers = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityBusinessFollowers.this);
        listView = pullToRefreshListView.getListView();

        progressDialog.show();
        new GetBusinessFollowers(ActivityBusinessFollowers.this, businessId, ActivityBusinessFollowers.this).execute();

        (findViewById(R.id.btn_blocked_users)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBusinessFollowers.this, ActivityBusinessBlockedUsers.class);
                intent.putExtra(Params.BUSINESS_ID, businessId);
                startActivity(intent);
            }
        });
    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        //this webservice doesn't support load more yet.
       /* status = Status.LOADING_MORE;
        pullToRefreshListView.setFooterVisibility(View.VISIBLE);
        new GetBusinessFollowers(ActivityBusinessFollowers.this, businessId, ActivityBusinessFollowers.this).execute();*/
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
            followers.addAll(temp);

            pullToRefreshListView.setResultSize(followers.size());

            if (status == Status.FIRST_TIME) {
                adapterFollowers = new AdapterBusinessFollowers(ActivityBusinessFollowers.this, followers);
                listView.setAdapter(adapterFollowers);
            } else if (status == Status.REFRESHING) {
                adapterFollowers.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more

                pullToRefreshListView.setFooterVisibility(View.GONE);
                adapterFollowers.loadMore(temp);
            }
            status = Status.NONE;

        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityBusinessFollowers.this, ServerAnswer.getError(ActivityBusinessFollowers.this, errorCode)).show();
    }
}
