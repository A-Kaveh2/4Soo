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

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.adapters.AdapterPostComments;
import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.PullToRefreshList;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.interfaces.IPullToRefresh;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.webservices.comment.GetPostAllComments;
import ir.rasen.charsoo.webservices.comment.SendComment;


public class ActivityComments extends ActionBarActivity implements IWebserviceResponse, IPullToRefresh {

    int postId, postOwnerBusinessId;
    AdapterPostComments adapterPostComments;
    ListView listView;
    ArrayList<Comment> comments;
    //for the test
    ArrayList<Comment> sampleComments;
    ProgressDialog progressDialog;
    String commentText;

    @Override
    public void notifyRefresh() {
        comments.clear();
        status = Status.REFRESHING;
        new GetPostAllComments(ActivityComments.this, postId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityComments.this).execute();
    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    EditTextFont editTextComment;
    private Status status;
    boolean isUserOwner;

    //pull_to_refresh_lib
    PullToRefreshList pullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.comments));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        //for the test
        try {
            sampleComments = TestUnit.getCommentAdapterItems();
        } catch (Exception e) {

        }

        postId = getIntent().getExtras().getInt(Params.POST_ID);
        postOwnerBusinessId = getIntent().getExtras().getInt(Params.POST_OWNER_BUSINESS_ID);
        isUserOwner = getIntent().getExtras().getBoolean(Params.IS_OWNER);

        comments = new ArrayList<>();
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
                comments.clear();
                status = Status.REFRESHING;
                new GetPostAllComments(ActivityComments.this, postId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityComments.this).execute();

            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView = (ListView) findViewById(R.id.listView);
        listView.setSelector(new ColorDrawable(0x00ffffff));
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
                    if (!swipeLayout.isRefreshing() && status != Status.LOADING_MORE
                            && comments.size() > 0 && comments.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });

        listFooterView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
        listView.addFooterView(listFooterView, null, false);*/

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityComments.this);
        listView = pullToRefreshListView.getListView();

        progressDialog.show();
        new GetPostAllComments(ActivityComments.this, postId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityComments.this).execute();

        editTextComment = (EditTextFont) findViewById(R.id.edt_comment);

        (findViewById(R.id.imageView_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentText = editTextComment.getText().toString();
                if (!Validation.validateComment(ActivityComments.this, commentText).isValid()) {
                    editTextComment.setError(Validation.getErrorMessage());
                    return;
                }
                new SendComment(ActivityComments.this, LoginInfo.getUserId(ActivityComments.this), postId, commentText, ActivityComments.this).execute();
            }
        });

    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        pullToRefreshListView.setFooterVisibility(View.VISIBLE);
        new GetPostAllComments(ActivityComments.this, postId, comments.get(comments.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), ActivityComments.this).execute();

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
            ArrayList<Comment> temp = (ArrayList<Comment>) result;
            comments.addAll(temp);

            pullToRefreshListView.setResultSize(comments.size());


            if (status == Status.FIRST_TIME) {
                adapterPostComments = new AdapterPostComments(ActivityComments.this, isUserOwner, postOwnerBusinessId, comments, ActivityComments.this, progressDialog);
                listView.setAdapter(adapterPostComments);
            } else if (status == Status.REFRESHING) {
                adapterPostComments.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more
                pullToRefreshListView.setFooterVisibility(View.GONE);
                adapterPostComments.loadMore(temp);
            }
            status = Status.NONE;

        } else if (result instanceof Integer) {
            comments.add(0, new Comment((Integer) result, LoginInfo.getUserId(ActivityComments.this), LoginInfo.getAccessUserIdentifier(ActivityComments.this), commentText));
            adapterPostComments.notifyDataSetChanged();
            editTextComment.setText("");
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityComments.this, ServerAnswer.getError(ActivityComments.this, errorCode)).show();
    }
}
