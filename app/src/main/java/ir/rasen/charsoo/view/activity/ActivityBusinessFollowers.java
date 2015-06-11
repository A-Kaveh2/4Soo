package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshList;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.model.business.GetBusinessFollowers;
import ir.rasen.charsoo.view.adapter.AdapterBusinessFollowers;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityBusinessFollowers extends CharsooActivity implements IWebserviceResponse, IPullToRefresh {

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
        setTitle(getResources().getString(R.string.followers));
        //for the test
        try {
            sampleFollowers = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }
        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID_STRING);
        int userId = getIntent().getExtras().getInt(Params.USER_ID_INT);
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
                intent.putExtra(Params.BUSINESS_ID_STRING, businessId);
                startActivity(intent);
            }
        });
    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...

      /*  status = Status.LOADING_MORE;
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
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshListView.onRefreshComplete();
        new DialogMessage(ActivityBusinessFollowers.this, ServerAnswer.getError(ActivityBusinessFollowers.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }
}
