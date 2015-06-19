package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AbsListView;

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
import ir.rasen.charsoo.view.widgets.GridViewBusiness;
import ir.rasen.charsoo.view.widgets.buttons.FloatButton;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;
import ir.rasen.charsoo.view.widgets.pull_to_refresh.HFGridView;
import ir.rasen.charsoo.view.widgets.pull_to_refresh.PullToRefreshGridViewWithHeaderAndFooter;


public class ActivityBusiness extends CharsooActivity implements ISelectBusiness, IWebserviceResponse, IPullToRefresh {
    ProgressDialog progressDialog;
    int selectedBusinessId, businessProfilePictureId;
    HFGridView gridView;
    Business business;
    GridViewBusiness gridViewBusiness;
    ArrayList<Post> posts;
    BroadcastReceiver deletePost;

    //pull_to_refresh_lib
    private PullToRefreshGrid pullToRefreshGridView;

    private FloatButton btnAddPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        selectedBusinessId = getIntent().getExtras().getInt(Params.BUSINESS_ID_STRING);

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
                gridViewBusiness.notifyDeletePost(intent.getIntExtra(Params.POST_ID_INT, 0));
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(deletePost, new IntentFilter(Params.DELETE_POST_FROM_ACTIVITY));

        btnAddPost = (FloatButton) findViewById(R.id.btn_new_post);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityBusiness.this, ActivityPostAddEdit.class);
                intent.putExtra(Params.BUSINESS_ID_STRING, business.id);
                startActivityForResult(intent, Params.ACTION_ADD_POST);
            }
        });

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

            boolean beThreeColumn = gridViewBusiness == null ? true : gridViewBusiness.isThreeColumn;
            if (gridViewBusiness == null) {
                gridViewBusiness = new GridViewBusiness(this, business, gridView);
                gridViewBusiness.InitialGridViewBusiness(new ArrayList<Post>(), beThreeColumn);
            } else
                gridViewBusiness.refreshBusinessData(business);
            if (posts == null)
                posts = new ArrayList<>();
            new GetBusinessPosts(ActivityBusiness.this, LoginInfo.getUserId(ActivityBusiness.this), business.id, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusiness.this).execute();
        }
        if (result instanceof ArrayList) {
            //this is GetBusinessPosts' result
            if (pullToRefreshGridView.isRefreshing())
                posts.clear();
            posts = (ArrayList<Post>) result;
            pullToRefreshGridView.setResultSize(posts.size());
            if (pullToRefreshGridView.isRefreshing()) {
                pullToRefreshGridView.onRefreshComplete();
/*                gridView.removeHeaderView(gridView.getHeaderView());*/
            }
            gridViewBusiness.InitialGridViewBusiness(posts, gridViewBusiness.isThreeColumn);
        }

    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshGridView.onRefreshComplete();
        new DialogMessage(ActivityBusiness.this, ServerAnswer.getError(ActivityBusiness.this, errorCode, callerStringID + ">" + this.getLocalClassName())).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Params.ACTION_ADD_POST) {
                Post p = ((MyApplication) getApplication()).post;
                gridViewBusiness.notifyDataSetChanged(p);
            } else if (requestCode == Params.ACTION_EDIT_BUSINESS) {
                if (data.getStringExtra(Params.TYPE).equals(Business.ChangeType.EDIT.name())) {
                    String picture = data.getStringExtra(Params.PROFILE_PICTURE);
                    if (picture != null) {
                        gridViewBusiness.changeProfilePicture(picture);
                    }
                } else if (data.getStringExtra(Params.TYPE).equals(Business.ChangeType.DELETE.name())) {
                    Intent i = getIntent();
                    i.putExtra(Params.BUSINESS_ID_STRING, data.getIntExtra(Params.BUSINESS_ID_STRING, 0));
                    setResult(RESULT_OK, i);
                    finish();
                }
            } else if (requestCode == Params.ACTION_EDIT_POST) {
                Post updatedPost = ((MyApplication) getApplication()).post;
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).id == updatedPost.id) {
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
        if (posts == null)
            posts = new ArrayList<>();
        new GetBusinessHomeInfo(ActivityBusiness.this, selectedBusinessId, LoginInfo.getUserId(ActivityBusiness.this), ActivityBusiness.this).execute();
    }

    @Override
    public void notifyLoadMore() {

    }

    public void onClick(View view) {
        // to set null onClick, please don't clean this function
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        btnAddPost.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }
}