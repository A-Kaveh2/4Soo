package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import ir.rasen.charsoo.view.widget_customized.pull_to_refresh.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshList;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.CommentNotification;
import ir.rasen.charsoo.model.comment.GetAllCommentNotifications;
import ir.rasen.charsoo.view.adapter.AdapterCommentNotification;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityCommentsNotifications extends CharsooActivity implements IWebserviceResponse, IPullToRefresh {

    ProgressDialog progressDialog;
    int businessId;
    AdapterCommentNotification adapterCommentNotification;
    ListView listView;
    ArrayList<CommentNotification> commentNotifications;

    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        commentNotifications.clear();
        new GetAllCommentNotifications(ActivityCommentsNotifications.this, businessId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityCommentsNotifications.this).execute();
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
        setContentView(R.layout.layout_listview_pull_to_refresh);
        setTitle(getString(R.string.comments));

        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID_STRING);
        commentNotifications = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityCommentsNotifications.this);
        listView = pullToRefreshListView.getListView();

        progressDialog.show();
        new GetAllCommentNotifications(ActivityCommentsNotifications.this, LoginInfo.getUserId(ActivityCommentsNotifications.this), 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityCommentsNotifications.this).execute();

    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        pullToRefreshListView.setFooterVisibility(View.VISIBLE);
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

            pullToRefreshListView.setResultSize(commentNotifications.size());

            if (status == Status.FIRST_TIME || status == Status.REFRESHING) {
                adapterCommentNotification = new AdapterCommentNotification(ActivityCommentsNotifications.this, commentNotifications);
                listView.setAdapter(adapterCommentNotification);
            } else if (status == Status.REFRESHING) {
                adapterCommentNotification.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more
                pullToRefreshListView.setFooterVisibility(View.GONE);
                adapterCommentNotification.loadMore(temp);
            }
            status = Status.NONE;

        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshListView.onRefreshComplete();
        new DialogMessage(ActivityCommentsNotifications.this, ServerAnswer.getError(ActivityCommentsNotifications.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }
}
