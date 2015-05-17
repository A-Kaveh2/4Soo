package ir.rasen.charsoo.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.handmark.pulltorefresh.library.GridViewWithHeaderAndFooter;
import com.handmark.pulltorefresh.library.PullToRefreshGridViewWithHeaderAndFooter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.PullToRefreshGrid;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.FriendshipRelation;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.GridViewHeader;
import ir.rasen.charsoo.view.widget_customized.GridViewUserOther;
import ir.rasen.charsoo.model.post.GetSharedPosts;
import ir.rasen.charsoo.model.user.GetUserHomeInfo;


public class ActivityUserOther extends Activity implements IWebserviceResponse,IPullToRefresh {

    private GridViewWithHeaderAndFooter gridView;
    private int visitedUserId;
    ProgressDialog progressDialog;
    private User user;
    GridViewUserOther gridViewUser;
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        posts.clear();
        new GetUserHomeInfo(ActivityUserOther.this, visitedUserId, LoginInfo.getUserId(ActivityUserOther.this), ActivityUserOther.this).execute();
    }

    @Override
    public void notifyLoadMore() {

    }

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    //pull_to_refresh_lib
    PullToRefreshGrid pullToRefreshGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_other);

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);

        //set progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshGridView = new PullToRefreshGrid(ActivityUserOther.this,(PullToRefreshGridViewWithHeaderAndFooter) findViewById(R.id.gridView_HF) ,ActivityUserOther.this);
        gridView = pullToRefreshGridView.getGridViewHeaderFooter();

        progressDialog.show();
        new GetUserHomeInfo(this, visitedUserId, LoginInfo.getUserId(this), this).execute();


    }


    @Override
    public void getResult(Object result) {
        if (result instanceof User) {
            progressDialog.dismiss();

            //GetUserHomeInfo result
            user = (User) result;
            gridView.setVisibility(View.VISIBLE);
            gridViewUser = new GridViewUserOther(ActivityUserOther.this, user, gridView);
            if (pullToRefreshGridView.isRefreshing())
                gridView.removeHeaderView(gridView.getHeaderView());
            gridViewUser.InitialGridViewUser(new ArrayList<Post>());
            if (user.friendshipRelationStatus == FriendshipRelation.Status.FRIEND)
                new GetSharedPosts(ActivityUserOther.this, visitedUserId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityUserOther.this).execute();
        } else if (result instanceof ArrayList) {
            //GetSharedPosts result
            posts = (ArrayList<Post>) result;
            pullToRefreshGridView.setResultSize(posts.size());
            if (pullToRefreshGridView.isRefreshing()) {
                pullToRefreshGridView.onRefreshComplete();
            }
            gridViewUser.InitialGridViewUser(posts);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserOther.this, ServerAnswer.getError(ActivityUserOther.this, errorCode)).show();
    }
}
