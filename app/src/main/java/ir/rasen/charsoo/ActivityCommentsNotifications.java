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

import ir.rasen.charsoo.adapters.AdapterCommentNotification;
import ir.rasen.charsoo.classes.CommentNotification;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.comment.GetAllCommentNotifications;


public class ActivityCommentsNotifications extends ActionBarActivity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    int businessId;
    AdapterCommentNotification adapterCommentNotification;
    ListView listView;
    ArrayList<CommentNotification> commentNotifications;
    SwipeRefreshLayout swipeLayout;
    private View  listFooterView;

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview_swip);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.comments));

        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);
        commentNotifications = new ArrayList<>();
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
                commentNotifications.clear();
                new GetAllCommentNotifications(ActivityCommentsNotifications.this, businessId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityCommentsNotifications.this).execute();

            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView = (ListView) findViewById(R.id.listView);
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
                            && commentNotifications.size() > 0 && commentNotifications.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });

        listFooterView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
        listView.addFooterView(listFooterView, null, false);


        progressDialog.show();
        new GetAllCommentNotifications(ActivityCommentsNotifications.this, LoginInfo.getUserId(ActivityCommentsNotifications.this), 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityCommentsNotifications.this).execute();


    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;

            listFooterView.setVisibility(View.VISIBLE);

        new GetAllCommentNotifications(ActivityCommentsNotifications.this, businessId, commentNotifications.get(commentNotifications.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), ActivityCommentsNotifications.this).execute();

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
            ArrayList<CommentNotification> temp = (ArrayList<CommentNotification>) result;
            commentNotifications.addAll(temp);
            if(status == Status.REFRESHING)
                swipeLayout.setRefreshing(false);

            if (status == Status.FIRST_TIME || status == Status.REFRESHING) {
                adapterCommentNotification = new AdapterCommentNotification(ActivityCommentsNotifications.this, commentNotifications);
                listView.setAdapter(adapterCommentNotification);
            } else {
                //it is loading more

                listFooterView.setVisibility(View.GONE);
                adapterCommentNotification.loadMore(temp);
            }
            status = Status.NONE;

        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityCommentsNotifications.this, ServerAnswer.getError(ActivityCommentsNotifications.this, errorCode)).show();
    }
}
