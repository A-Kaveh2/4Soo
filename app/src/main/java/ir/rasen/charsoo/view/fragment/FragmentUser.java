package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.HFGridView;
import com.handmark.pulltorefresh.library.PullToRefreshGridViewWithHeaderAndFooter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshGrid;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.post.GetSharedPosts;
import ir.rasen.charsoo.model.user.GetUserHomeInfo;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IChangeTabs;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IUpdateUserProfile;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.GridViewUser;

public class FragmentUser extends Fragment implements IWebserviceResponse, IUpdateUserProfile, IPullToRefresh {

    private HFGridView gridView;
    private int visitedUserId;
    ProgressDialog progressDialog;
    private User user;
    GridViewUser gridViewUser;
    static IUpdateUserProfile iUpdateUserProfile;
    ArrayList<Post> sharedPosts;
    BroadcastReceiver cancelShareReceiver, removeRequestAnnouncement, updateUserProfilePicture;
    //PullToRefreshGridViewWithHeaderAndFooter pullToRefreshGridViewWithHeaderAndFooter;

    //pull_to_refresh_lib
    PullToRefreshGrid pullToRefreshGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_user,
                    container, false);

            visitedUserId = LoginInfo.getUserId(getActivity());
            iUpdateUserProfile = this;


            //set progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.please_wait));

            pullToRefreshGridView = new PullToRefreshGrid(getActivity(), (PullToRefreshGridViewWithHeaderAndFooter) view.findViewById(R.id.gridView_HF), FragmentUser.this);
            gridView = pullToRefreshGridView.getGridViewHeaderFooter();

            recursivelyCallHandler();

        } catch (Exception e) {
            String s = e.getMessage();
        }

        cancelShareReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int postId = intent.getIntExtra(Params.POST_ID, 0);

                //remove canceled post
                cancelShare(postId);

                //update time line
                Intent intentUpdateTimeLine = new Intent(Params.UPATE_TIME_LINE);
                intentUpdateTimeLine.putExtra(Params.UPATE_TIME_LINE_TYPE, Params.UPATE_TIME_LINE_TYPE_CANCEL_SHARE);
                intentUpdateTimeLine.putExtra(Params.POST_ID, postId);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentUpdateTimeLine);
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cancelShareReceiver, new IntentFilter(Params.CANCEL_USER_SHARE_POST));


        removeRequestAnnouncement = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                gridViewUser.hideRequestAnnouncement();
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(removeRequestAnnouncement, new IntentFilter(Params.REMOVE_REQUEST_ANNOUNCEMENT));

        updateUserProfilePicture = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                notifyUpdateUserProfile(bundle.getString(Params.USER_PICUTE));
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUserProfilePicture, new IntentFilter(Params.UPDATE_USER_PROFILE_PCITURE));

        return view;
    }

    Handler handler = new Handler();

    public void recursivelyCallHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                if (((MyApplication) getActivity().getApplication()).isSearchCreated) {
                    if (((MyApplication) getActivity().getApplication()).isUserCreated)
                        initializeUser();
                    else {
                        new GetUserHomeInfo(getActivity(), visitedUserId, LoginInfo.getUserId(getActivity()), FragmentUser.this).execute();
                    }
                } else
                    recursivelyCallHandler();
            }
        }, 500);
    }

    private void initializeUser() {
/*        if (!(getActivity() instanceof IGoToRegisterBusinessActivity))
            return;
        boolean hasRequest = false;*/

        boolean beThreeColumn = gridViewUser == null ? true : gridViewUser.isThreeColumn;
        gridViewUser = new GridViewUser(getActivity(), user, visitedUserId, gridView);
        if (((MyApplication) getActivity().getApplication()).isUserCreated) {
            try {
                gridViewUser.InitialGridViewUser(sharedPosts, beThreeColumn);
                gridViewUser.hideLoader();
            } catch (Exception e) {

            }
        } else {
            try {

                gridViewUser.InitialGridViewUser(new ArrayList<Post>(), beThreeColumn);
            } catch (Exception e) {

            }
            new GetSharedPosts(getActivity(), visitedUserId, 0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentUser.this).execute();
            ((MyApplication) getActivity().getApplication()).isUserCreated = true;
        }
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof User) {
            //GetUserHomeInfo result
            user = (User) result;
            ((MyApplication) getActivity().getApplication()).userIdentifier = user.userIdentifier;
            ((MyApplication) getActivity().getApplication()).userProfilePictureId = user.profilePictureId;
            ((MyApplication) getActivity().getApplication()).userBusinesses = user.businesses;
            ((MyApplication) getActivity().getApplication()).setPermission(user.permissions);
            initializeUser();

            if (!LoginInfo.hasBusiness(getActivity()) && user.businesses.size() != 0) {
                if (getActivity() instanceof IChangeTabs)
                    ((IChangeTabs) getActivity()).notifyMakeFourTabs();

                LoginInfo.submitBusiness(getActivity());
            }

        } else if (result instanceof ArrayList) {
            progressDialog.dismiss();


            //GetSharedPosts result
            sharedPosts = (ArrayList<Post>) result;
            pullToRefreshGridView.setResultSize(sharedPosts.size());
            if (pullToRefreshGridView.isRefreshing()) {
                pullToRefreshGridView.onRefreshComplete();
                gridView.removeHeaderView(gridView.getHeaderView());
            }
            gridViewUser.InitialGridViewUser(sharedPosts, gridViewUser.isThreeColumn);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode)).show();
    }


    @Override
    public void notifyUpdateUserProfile(String userPictureString) {
        gridViewUser.initialProfilePicture(userPictureString);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Params.ACTION_ACTIVITY_POST) {

            }
        }
    }

    public void cancelShare(int postId) {
        for (int i = 0; i < sharedPosts.size(); i++) {
            if (sharedPosts.get(i).id == postId) {
                sharedPosts.remove(i);
                break;
            }
        }
        gridViewUser.InitialGridViewUser(sharedPosts, gridViewUser.isThreeColumn);
    }

    @Override
    public void notifyRefresh() {
        if (sharedPosts != null) {
            sharedPosts.clear();
            ((MyApplication) getActivity().getApplication()).isUserCreated = false;
            new GetUserHomeInfo(getActivity(), visitedUserId, LoginInfo.getUserId(getActivity()), FragmentUser.this).execute();
        } else
            pullToRefreshGridView.onRefreshComplete();
    }

    @Override
    public void notifyLoadMore() {
        //GetUserHomeInfo doesn't support load more yet.
    }
}
