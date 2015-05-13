package ir.rasen.charsoo.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.FriendshipRelation;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.GridViewHeader;
import ir.rasen.charsoo.view.widget_customized.GridViewUserOther;
import ir.rasen.charsoo.model.post.GetSharedPosts;
import ir.rasen.charsoo.model.user.GetUserHomeInfo;


public class ActivityUserOther extends Activity implements IWebserviceResponse {

    private GridViewHeader gridView;
    private int visitedUserId;
    ProgressDialog progressDialog;
    private User user;
    GridViewUserOther gridViewUser;
    SwipeRefreshLayout swipeLayout;

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;
    ArrayList<Post> posts;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_other);

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);

        //set progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        gridView = (GridViewHeader) findViewById(R.id.gridView);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (status == Status.LOADING_MORE) {
                    swipeLayout.setRefreshing(false);
                    return;
                }

                status = Status.REFRESHING;
                gridView.removeHeaderView(gridView.getHeaderView());
                gridView.setVisibility(View.GONE);
                new GetUserHomeInfo(ActivityUserOther.this, visitedUserId, LoginInfo.getUserId(ActivityUserOther.this), ActivityUserOther.this).execute();
            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        progressDialog.show();
        new GetUserHomeInfo(this, visitedUserId, LoginInfo.getUserId(this), this).execute();


    }


    @Override
    public void getResult(Object result) {
        if (result instanceof User) {
            progressDialog.dismiss();
            if(swipeLayout.isRefreshing())
                swipeLayout.setRefreshing(false);
            //GetUserHomeInfo result
            user = (User) result;
            gridView.setVisibility(View.VISIBLE);
            gridViewUser = new GridViewUserOther(ActivityUserOther.this, user, gridView);
            gridViewUser.InitialGridViewUser(new ArrayList<Post>());
            if (user.friendshipRelationStatus == FriendshipRelation.Status.FRIEND)
                new GetSharedPosts(ActivityUserOther.this, visitedUserId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityUserOther.this).execute();
        } else if (result instanceof ArrayList) {
            //GetSharedPosts result
            gridViewUser.InitialGridViewUser((ArrayList<Post>) result);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserOther.this, ServerAnswer.getError(ActivityUserOther.this, errorCode)).show();
    }
}
