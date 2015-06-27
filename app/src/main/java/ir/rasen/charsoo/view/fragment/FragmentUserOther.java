package ir.rasen.charsoo.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.FriendshipRelation;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshGrid;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.post.GetSharedPosts;
import ir.rasen.charsoo.model.user.GetUserHomeInfo;
import ir.rasen.charsoo.view.activity.ActivityUserOther;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.GridViewUserOther;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.pull_to_refresh.HFGridView;
import ir.rasen.charsoo.view.widgets.pull_to_refresh.PullToRefreshGridViewWithHeaderAndFooter;

public class FragmentUserOther extends Fragment implements IWebserviceResponse, IPullToRefresh {

    private HFGridView gridView;
    private int visitedUserId;
    WaitDialog progressDialog;
    private User user;
    GridViewUserOther gridViewUser;
    ArrayList<Post> posts = new ArrayList<>();
    ActivityUserOther parentActivity;

    @Override
    public void notifyRefresh() {
        if (posts != null) {
            status = Status.REFRESHING;
            new GetUserHomeInfo(parentActivity, visitedUserId, LoginInfo.getUserId(parentActivity), FragmentUserOther.this).execute();
        }
        else
            pullToRefreshGridView.onRefreshComplete();
    }

    @Override
    public void notifyLoadMore() {

    }

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    //pull_to_refresh_lib
    PullToRefreshGrid pullToRefreshGridView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_other,
                container, false);

        if (posts==null)
            posts=new ArrayList<>();

        parentActivity=(ActivityUserOther)getActivity();
        visitedUserId = parentActivity.getIntent().getExtras().getInt(Params.VISITED_USER_ID);

        //set progress dialog
        progressDialog = new WaitDialog(parentActivity);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshGridView = new PullToRefreshGrid(parentActivity, (PullToRefreshGridViewWithHeaderAndFooter) view.findViewById(R.id.gridView_HF), FragmentUserOther.this);
        gridView = pullToRefreshGridView.getGridViewHeaderFooter();

        progressDialog.show();
        new GetUserHomeInfo(parentActivity, visitedUserId, LoginInfo.getUserId(parentActivity), this).execute();

        return view;

    }


    @Override
    public void getResult(Object result) {
        if (result instanceof User) {
            progressDialog.dismiss();

            //GetUserHomeInfo result
            user = (User) result;
            gridView.setVisibility(View.VISIBLE);

            boolean beThreeColumn = gridViewUser == null ? true : gridViewUser.isThreeColumn;
            if (gridViewUser == null){
                gridViewUser = new GridViewUserOther(parentActivity, user, gridView);
                gridViewUser.InitialGridViewUser(new ArrayList<Post>(), beThreeColumn);
            }
            else
                gridViewUser.refreshUserData(user);
//            if (pullToRefreshGridView.isRefreshing())
//                gridView.removeHeaderView(gridView.getHeaderView());
            if (user.friendshipRelationStatus == FriendshipRelation.Status.FRIEND)
                new GetSharedPosts(parentActivity, visitedUserId, 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentUserOther.this).execute();
            else {
                if (pullToRefreshGridView.isRefreshing())
                    pullToRefreshGridView.onRefreshComplete();
            }
        } else if (result instanceof ArrayList) {
            //GetSharedPosts result
            if(pullToRefreshGridView.isRefreshing())
                posts.clear();
            posts = (ArrayList<Post>) result;
            pullToRefreshGridView.setResultSize(posts.size());
            if (pullToRefreshGridView.isRefreshing()) {
                pullToRefreshGridView.onRefreshComplete();
            }
            gridViewUser.InitialGridViewUser(posts, gridViewUser.isThreeColumn);
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshGridView.onRefreshComplete();
        new DialogMessage(parentActivity, ServerAnswer.getError(parentActivity, errorCode,callerStringID+">"+parentActivity.getLocalClassName())).show();
    }
}
