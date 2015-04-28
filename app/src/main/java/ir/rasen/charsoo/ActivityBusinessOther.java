package ir.rasen.charsoo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.interfaces.ISelectBusiness;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.DrawerLayoutBusiness;
import ir.rasen.charsoo.ui.GridViewBusiness;
import ir.rasen.charsoo.ui.GridViewBusinessOther;
import ir.rasen.charsoo.ui.GridViewHeader;
import ir.rasen.charsoo.ui.GridViewWithHeaderAndFooter;
import ir.rasen.charsoo.webservices.business.GetBusinessHomeInfo;
import ir.rasen.charsoo.webservices.post.GetBusinessPosts;
import ir.rasen.charsoo.webservices.user.GetUserHomeInfo;


public class ActivityBusinessOther extends Activity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    int selectedBusinessId;
    GridViewHeader gridView;
    Business business;
    GridViewBusinessOther gridViewBusiness;
    ArrayList<Post> posts;
    SwipeRefreshLayout swipeLayout;
    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}
    private Status status;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_other);

        selectedBusinessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);

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
                new GetBusinessHomeInfo(ActivityBusinessOther.this, selectedBusinessId, LoginInfo.getUserId(ActivityBusinessOther.this), ActivityBusinessOther.this).execute();
            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        progressDialog.show();

        new GetBusinessHomeInfo(ActivityBusinessOther.this, selectedBusinessId, LoginInfo.getUserId(ActivityBusinessOther.this), ActivityBusinessOther.this).execute();
    }




    @Override
    public void getResult(Object result) {
        if (result instanceof Business) {
            //this is GetBusinessHomeInfo's result
            if(swipeLayout.isRefreshing())
                swipeLayout.setRefreshing(false);
            gridView.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            gridView.setVisibility(View.VISIBLE);
            business = (Business) result;
            gridViewBusiness = new GridViewBusinessOther(ActivityBusinessOther.this, business, gridView);
            gridViewBusiness.InitialGridViewBusiness(new ArrayList<Post>());
            new GetBusinessPosts(ActivityBusinessOther.this, LoginInfo.getUserId(ActivityBusinessOther.this), business.id, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessOther.this).execute();
        }
        if (result instanceof ArrayList) {
            //this is GetBusinessPosts' result
            posts = (ArrayList<Post>) result;
            gridViewBusiness.InitialGridViewBusiness(posts);
        }

    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityBusinessOther.this, ServerAnswer.getError(ActivityBusinessOther.this, errorCode)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Params.ACTION_ADD_POST) {
                gridViewBusiness.notifyDataSetChanged(((MyApplication)getApplication()).post);
            }
        }

    }
}
