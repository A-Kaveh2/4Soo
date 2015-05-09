package ir.rasen.charsoo;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.adapters.AdapterFollowers;
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
import ir.rasen.charsoo.webservices.comment.GetAllCommentNotifications;


public class ActivityBusinessFollowers extends ActionBarActivity implements IWebserviceResponse, IPullToRefresh {

    ProgressDialog progressDialog;
    int businessId;
    AdapterFollowers adapterFollowers;
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

    /*SwipeRefreshLayout swipeLayout;
    private View  listFooterView;
*/
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

       /* swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (status == Status.LOADING_MORE) {
                    swipeLayout.setRefreshing(false);
                    return;
                }

                status = Status.REFRESHING;

                followers.clear();
                new GetBusinessFollowers(ActivityBusinessFollowers.this, businessId, ActivityBusinessFollowers.this).execute();

            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityBusinessFollowers.this);
        listView = pullToRefreshListView.getListView();


       /* listView = (ListView) findViewById(R.id.listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem,currentVisibleItemCount,currentScrollState;

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
                    if (!swipeLayout.isRefreshing() && status != Status.LOADING_MORE
                            && followers.size() > 0 && followers.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        //loadMoreData();
                    }
                }
            }
        });

        listFooterView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
        listView.addFooterView(listFooterView, null, false);

*/
        progressDialog.show();
        new GetBusinessFollowers(ActivityBusinessFollowers.this, businessId, ActivityBusinessFollowers.this).execute();

        (findViewById(R.id.btn_blocked_users)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBusinessFollowers.this, ActivityBlockedUsers.class);
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
                adapterFollowers = new AdapterFollowers(ActivityBusinessFollowers.this, followers);
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
