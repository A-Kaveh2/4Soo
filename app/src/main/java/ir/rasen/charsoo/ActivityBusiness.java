package ir.rasen.charsoo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.interfaces.IChangeBusiness;
import ir.rasen.charsoo.interfaces.ISelectBusiness;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.DrawerLayoutBusiness;
import ir.rasen.charsoo.ui.GridViewBusiness;
import ir.rasen.charsoo.ui.GridViewWithHeaderAndFooter;
import ir.rasen.charsoo.webservices.business.GetBusinessHomeInfo;
import ir.rasen.charsoo.webservices.post.GetBusinessPosts;


public class ActivityBusiness extends Activity implements ISelectBusiness, IWebserviceResponse{

    private DrawerLayout mDrawerLayout;
    ProgressDialog progressDialog;
    int selectedBusinessId, businessProfilePictureId;
    DrawerLayoutBusiness drawerLayoutBusiness;
    GridViewWithHeaderAndFooter gridView;
    Business business;
    GridViewBusiness gridViewBusiness;
    ArrayList<Post> posts;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        selectedBusinessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        drawerLayoutBusiness = new DrawerLayoutBusiness();
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);

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

            drawerLayoutBusiness.Initial(this, mDrawerLayout, ((MyApplication) getApplication()).userBusinesses, selectedBusinessId, business.profilePictureId, ActivityBusiness.this);


            gridViewBusiness = new GridViewBusiness(this, business, gridView, mDrawerLayout);
            gridViewBusiness.InitialGridViewBusiness(new ArrayList<Post>());
            new GetBusinessPosts(ActivityBusiness.this, LoginInfo.getUserId(ActivityBusiness.this), business.id, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusiness.this).execute();
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
                    drawerLayoutBusiness.changeProfilePicture(picture);
                    gridViewBusiness.changeProfilePicture(picture);
                } else if (data.getStringExtra(Params.TYPE).equals(Business.ChangeType.DELETE.name())) {
                    Intent i = getIntent();
                    i.putExtra(Params.BUSINESS_ID, data.getIntExtra(Params.BUSINESS_ID, 0));
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        }

    }

}
