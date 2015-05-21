package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import ir.rasen.charsoo.view.interface_m.ISelectBusiness;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.GridViewBusiness;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.NoActionBarActivity;


public class ActivityBusiness extends NoActionBarActivity implements ISelectBusiness, IWebserviceResponse, IPullToRefresh {

    ProgressDialog progressDialog;
    int selectedBusinessId, businessProfilePictureId;
    HFGridView gridView;
    Business business;
    GridViewBusiness gridViewBusiness;
    ArrayList<Post> posts;
    BroadcastReceiver deletePost;

    //pull_to_refresh_lib
    PullToRefreshGrid pullToRefreshGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        selectedBusinessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshGridView = new PullToRefreshGrid(ActivityBusiness.this, (PullToRefreshGridViewWithHeaderAndFooter) findViewById(R.id.gridView_HF), ActivityBusiness.this);
        gridView = pullToRefreshGridView.getGridViewHeaderFooter();

        progressDialog.show();
        new GetBusinessHomeInfo(ActivityBusiness.this, selectedBusinessId, LoginInfo.getUserId(ActivityBusiness.this), ActivityBusiness.this).execute();

        deletePost = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //if the user delete a post from ActivityPost
                gridViewBusiness.notifyDeletePost(intent.getIntExtra(Params.POST_ID, 0));
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(deletePost, new IntentFilter(Params.DELETE_POST_FROM_ACTIVITY));


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

            gridViewBusiness = new GridViewBusiness(this, business, gridView);
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
                        gridViewBusiness.changeProfilePicture(picture);
                    }
                } else if (data.getStringExtra(Params.TYPE).equals(Business.ChangeType.DELETE.name())) {
                    Intent i = getIntent();
                    i.putExtra(Params.BUSINESS_ID, data.getIntExtra(Params.BUSINESS_ID, 0));
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
            else if (requestCode == Params.ACTION_EDIT_POST) {
                Post updatedPost = ((MyApplication) getApplication()).post;
                for(int i = 0;i<posts.size();i++){
                    if(posts.get(i).id == updatedPost.id){
                        Post p = posts.get(i);
                        p.title = updatedPost.title;
                        p.description = updatedPost.description;
                        p.hashtagList = updatedPost.hashtagList;
                        p.price = updatedPost.price;
                        p.code = updatedPost.code;
                        break;
                    }
                }
                gridViewBusiness.notifyDatasetChanged();
            }
        }

    }

    @Override
    public void notifyRefresh() {
        if (posts != null) {
            posts.clear();
            new GetBusinessPosts(ActivityBusiness.this, LoginInfo.getUserId(ActivityBusiness.this), business.id, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusiness.this).execute();
        } else
            pullToRefreshGridView.onRefreshComplete();
    }

    @Override
    public void notifyLoadMore() {

    }

}
