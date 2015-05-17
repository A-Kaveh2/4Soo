package ir.rasen.charsoo.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.handmark.pulltorefresh.library.GridViewWithHeaderAndFooter;
import com.handmark.pulltorefresh.library.PullToRefreshGridViewWithHeaderAndFooter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.PullToRefreshGrid;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.fragment.FragmentUser;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.ISelectBusiness;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.DrawerLayoutBusiness;
import ir.rasen.charsoo.view.widget_customized.GridViewBusiness;
import ir.rasen.charsoo.model.business.GetBusinessHomeInfo;
import ir.rasen.charsoo.model.post.GetBusinessPosts;


public class ActivityBusiness extends Activity implements ISelectBusiness, IWebserviceResponse,IPullToRefresh {


    private DrawerLayout mDrawerLayout;
    ProgressDialog progressDialog;
    int selectedBusinessId, businessProfilePictureId;
    DrawerLayoutBusiness drawerLayoutBusiness;
    GridViewWithHeaderAndFooter gridView;
    Business business;
    GridViewBusiness gridViewBusiness;
    ArrayList<Post> posts;

    //pull_to_refresh_lib
    PullToRefreshGrid pullToRefreshGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        selectedBusinessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshGridView = new PullToRefreshGrid(ActivityBusiness.this,(PullToRefreshGridViewWithHeaderAndFooter) findViewById(R.id.gridView_HF) ,ActivityBusiness.this);
        gridView = pullToRefreshGridView.getGridViewHeaderFooter();
        drawerLayoutBusiness = new DrawerLayoutBusiness();

        progressDialog.show();
        new GetBusinessHomeInfo(ActivityBusiness.this, selectedBusinessId, LoginInfo.getUserId(ActivityBusiness.this), ActivityBusiness.this).execute();


    }


    @Override
    public void notifySelectBusiness(int businessId) {
        gridView.removeHeaderView(gridView.getHeaderView());
        gridView.setVisibility(View.GONE);
        progressDialog.show();

        new GetBusinessHomeInfo(ActivityBusiness.this, businessId, LoginInfo.getUserId(ActivityBusiness.this), ActivityBusiness.this).execute();
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof Business) {
            //this is GetBusinessHomeInfo's result
            progressDialog.dismiss();
            gridView.setVisibility(View.VISIBLE);
            business = (Business) result;
            ((MyApplication) getApplication()).business = new Business();
            ((MyApplication) getApplication()).business = business;

            drawerLayoutBusiness.Initial(this, mDrawerLayout, ((MyApplication) getApplication()).userBusinesses, selectedBusinessId, business.profilePictureId, ActivityBusiness.this);


            gridViewBusiness = new GridViewBusiness(this, business, gridView, mDrawerLayout);
            gridViewBusiness.InitialGridViewBusiness(new ArrayList<Post>());

            new GetBusinessPosts(ActivityBusiness.this, LoginInfo.getUserId(ActivityBusiness.this), business.id, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusiness.this).execute();
        }
        if (result instanceof ArrayList) {
            //this is GetBusinessPosts' result
            posts = (ArrayList<Post>) result;
            pullToRefreshGridView.setResultSize(posts.size());
            if (pullToRefreshGridView.isRefreshing()) {
                pullToRefreshGridView.onRefreshComplete();
/*                gridView.removeHeaderView(gridView.getHeaderView());*/
            }
            gridViewBusiness.InitialGridViewBusiness(posts);
        }

    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityBusiness.this, ServerAnswer.getError(ActivityBusiness.this, errorCode)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Params.ACTION_ADD_POST) {
                gridViewBusiness.notifyDataSetChanged(((MyApplication) getApplication()).post);
            } else if (requestCode == Params.ACTION_EDIT_BUSINESS) {
                if (data.getStringExtra(Params.TYPE).equals(Business.ChangeType.EDIT.name())) {
                    String picture = data.getStringExtra(Params.PROFILE_PICTURE);
                    if (picture != null) {
                        drawerLayoutBusiness.changeProfilePicture(picture);
                        gridViewBusiness.changeProfilePicture(picture);
                    }
                } else if (data.getStringExtra(Params.TYPE).equals(Business.ChangeType.DELETE.name())) {
                    Intent i = getIntent();
                    i.putExtra(Params.BUSINESS_ID, data.getIntExtra(Params.BUSINESS_ID, 0));
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        }

    }

    @Override
    public void notifyRefresh() {
        posts.clear();
        new GetBusinessPosts(ActivityBusiness.this, LoginInfo.getUserId(ActivityBusiness.this), business.id, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusiness.this).execute();
    }

    @Override
    public void notifyLoadMore() {

    }
}
