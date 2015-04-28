package ir.rasen.charsoo;

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
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.interfaces.IGoToRegisterBusinessActivity;
import ir.rasen.charsoo.interfaces.IChangeTabs;
import ir.rasen.charsoo.interfaces.IUpdateUserProfile;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.DrawerLayoutUser;
import ir.rasen.charsoo.ui.GridViewHeader;
import ir.rasen.charsoo.ui.GridViewUser;
import ir.rasen.charsoo.webservices.post.GetSharedPosts;
import ir.rasen.charsoo.webservices.user.GetUserHomeInfo;

public class FragmentUser extends Fragment implements IWebserviceResponse, IUpdateUserProfile {

    private DrawerLayout mDrawerLayout;
    private GridViewHeader gridView;
    private int visitedUserId;
    ProgressDialog progressDialog;
    private User user;
    GridViewUser gridViewUser;
    static IUpdateUserProfile iUpdateUserProfile;
    ArrayList<Post> sharedPosts;
    BroadcastReceiver cancelShareReceiver;

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


            mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
            gridView = (GridViewHeader) view.findViewById(R.id.gridView);

            recursivelyCallHandler();

        } catch (Exception e) {
            String s = e.getMessage();
        }

        cancelShareReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int postId = intent.getIntExtra(Params.POST_ID,0);

                //remove canceled post
                cancelShare(postId);

                //update time line
                Intent intentUpdateTimeLine = new Intent(Params.UPATE_TIME_LINE);
                intentUpdateTimeLine.putExtra(Params.UPATE_TIME_LINE_TYPE, Params.UPATE_TIME_LINE_TYPE_CANCEL_SHARE);
                intentUpdateTimeLine.putExtra(Params.POST_ID,postId);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentUpdateTimeLine);
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cancelShareReceiver, new IntentFilter(Params.CANCEL_USER_SHARE_POST));


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
        if (!(getActivity() instanceof IGoToRegisterBusinessActivity))
            return;
        DrawerLayoutUser.Initial(getActivity(), mDrawerLayout, user, (IGoToRegisterBusinessActivity) getActivity());
        boolean hasRequest = false;

        gridViewUser = new GridViewUser(getActivity(), user.userIdentifier, user.name, user.profilePictureId, (user.friendRequestNumber > 0) ? true : false, visitedUserId, gridView, mDrawerLayout);
        if (((MyApplication) getActivity().getApplication()).isUserCreated) {
            try {
                gridViewUser.InitialGridViewUser(sharedPosts);
                gridViewUser.hideLoader();
            } catch (Exception e) {

            }
        } else {
            try {
                gridViewUser.InitialGridViewUser(new ArrayList<Post>());
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
            ((MyApplication) getActivity().getApplication()).userBusinesses = user.businesses;
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
            gridViewUser.InitialGridViewUser(sharedPosts);
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

    public void cancelShare(int postId){
        for (int i = 0; i < sharedPosts.size(); i++) {
            if (sharedPosts.get(i).id == postId) {
                sharedPosts.remove(i);
                break;
            }
        }
        gridViewUser.InitialGridViewUser(sharedPosts);
    }
}
