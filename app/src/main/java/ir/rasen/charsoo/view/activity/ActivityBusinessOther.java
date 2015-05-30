package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.handmark.pulltorefresh.library.HFGridView;
import com.handmark.pulltorefresh.library.PullToRefreshGridViewWithHeaderAndFooter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshGrid;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.model.business.GetBusinessHomeInfo;
import ir.rasen.charsoo.model.post.GetBusinessPosts;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.GridViewBusinessOther;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.NoActionBarActivity;

public class ActivityBusinessOther extends NoActionBarActivity implements IWebserviceResponse, IPullToRefresh {

    ProgressDialog progressDialog;
    int selectedBusinessId;
    private HFGridView gridView;
    Business business;
    GridViewBusinessOther gridViewBusiness;
    ArrayList<Post> posts;

    @Override
    public void notifyRefresh() {
        if (posts != null) {
            status = Status.REFRESHING;
            posts.clear();
            new GetBusinessHomeInfo(ActivityBusinessOther.this, selectedBusinessId, LoginInfo.getUserId(ActivityBusinessOther.this), ActivityBusinessOther.this).execute();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_other);

        selectedBusinessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshGridView = new PullToRefreshGrid(ActivityBusinessOther.this, (PullToRefreshGridViewWithHeaderAndFooter) findViewById(R.id.gridView_HF), ActivityBusinessOther.this);
        gridView = pullToRefreshGridView.getGridViewHeaderFooter();

        progressDialog.show();
        new GetBusinessHomeInfo(ActivityBusinessOther.this, selectedBusinessId, LoginInfo.getUserId(ActivityBusinessOther.this), ActivityBusinessOther.this).execute();
    }


    @Override
    public void getResult(Object result) {
        if (result instanceof Business) {
            //this is GetBusinessHomeInfo's result

            gridView.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            gridView.setVisibility(View.VISIBLE);
            business = (Business) result;

            if (pullToRefreshGridView.isRefreshing())
                gridView.removeHeaderView(gridView.getHeaderView());

            boolean beThreeColumn = gridViewBusiness == null ? true : gridViewBusiness.isThreeColumn;
            gridViewBusiness = new GridViewBusinessOther(ActivityBusinessOther.this, business, gridView);
            gridViewBusiness.InitialGridViewBusiness(new ArrayList<Post>(), beThreeColumn);
            new GetBusinessPosts(ActivityBusinessOther.this, LoginInfo.getUserId(ActivityBusinessOther.this), business.id, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessOther.this).execute();
        }
        if (result instanceof ArrayList) {
            //this is GetBusinessPosts' result
            posts = (ArrayList<Post>) result;
            pullToRefreshGridView.setResultSize(posts.size());
            if (pullToRefreshGridView.isRefreshing()) {
                pullToRefreshGridView.onRefreshComplete();
            }
            gridViewBusiness.InitialGridViewBusiness(posts, gridViewBusiness.isThreeColumn);
        }

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityBusinessOther.this, ServerAnswer.getError(ActivityBusinessOther.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Params.ACTION_ADD_POST) {
                gridViewBusiness.notifyDataSetChanged(((MyApplication) getApplication()).post);
            }
        }

    }
}
