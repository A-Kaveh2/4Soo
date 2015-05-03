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

import java.util.ArrayList;

import ir.rasen.charsoo.adapters.AdapterFriends;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.ButtonFont;
import ir.rasen.charsoo.webservices.friend.GetUserFriends;
import ir.rasen.charsoo.webservices.user.GetFollowingBusinesses;


public class ActivityFriends extends ActionBarActivity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    int visitedUserId;
    AdapterFriends adapterFriends;
    ListView listView;
    ArrayList<BaseAdapterItem> friends;
    ArrayList<BaseAdapterItem> sampleFriends;
    private View listFooterView;
    SwipeRefreshLayout swipeLayout;

    private enum Status {FIRST_TIME, LOADING_MORE,REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview_swip);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DeepSkyBlue)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            sampleFriends = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);


        friends = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (status == Status.LOADING_MORE) {
                    swipeLayout.setRefreshing(false);
                    return;
                }

                status = Status.REFRESHING;
                friends.clear();
                new GetUserFriends(ActivityFriends.this,visitedUserId,ActivityFriends.this).execute();

            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                            && friends.size() > 0 && friends.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        //loadMoreData();
                    }
                }
            }
        });

        listFooterView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
        listView.addFooterView(listFooterView, null, false);


        progressDialog.show();
        new GetUserFriends(ActivityFriends.this,visitedUserId,ActivityFriends.this).execute();

        (findViewById(R.id.btn_friend_requests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;

        listFooterView.setVisibility(View.VISIBLE);

        new GetUserFriends(ActivityFriends.this,visitedUserId,ActivityFriends.this).execute();
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
        if(swipeLayout.isRefreshing())
            swipeLayout.setRefreshing(false);
        if (result instanceof ArrayList) {
            ArrayList<BaseAdapterItem> temp = (ArrayList<BaseAdapterItem>) result;
            friends.addAll(temp);


            if (status == Status.FIRST_TIME) {
                adapterFriends =new AdapterFriends(ActivityFriends.this,visitedUserId,friends);
                listView.setAdapter(adapterFriends);
            } else {
                //it is loading more
                listFooterView.setVisibility(View.GONE);
                adapterFriends.loadMore(temp);
            }
            status = Status.NONE;

        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityFriends.this, ServerAnswer.getError(ActivityFriends.this, errorCode)).show();
    }
}
