package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshList;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.object.Review;
import ir.rasen.charsoo.model.review.GetBusinessReviews;
import ir.rasen.charsoo.view.adapter.AdapterBusinessReview;
import ir.rasen.charsoo.view.dialog.DialogAddReview;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IAddReview;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityBusinessReviews extends CharsooActivity implements IWebserviceResponse, IAddReview, IPullToRefresh {

    ProgressDialog progressDialog;
    int businessId;
    AdapterBusinessReview adapterBusinessReview;
    ListView listView;
    ArrayList<Review> results;
    ArrayList<Review> sampleResults;


    //pull_to_refresh_lib
    PullToRefreshList pullToRefreshListView;

    @Override
    public void notifyRefresh() {
        status = Status.REFRESHING;
        results.clear();
        new GetBusinessReviews(ActivityBusinessReviews.this, businessId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessReviews.this).execute();
    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_reviews);
        setTitle(getResources().getString(R.string.reviews));
        try {
            sampleResults = TestUnit.getUserReviewAdapterItems();
        } catch (Exception e) {

        }

        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);
        if (getIntent().getExtras().getBoolean(Params.BUSINESS_OWNER))
            (findViewById(R.id.rl_add)).setVisibility(View.GONE);

        results = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityBusinessReviews.this);
        listView = pullToRefreshListView.getListView();


        progressDialog.show();
        new GetBusinessReviews(ActivityBusinessReviews.this, businessId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessReviews.this).execute();

        (findViewById(R.id.imageView_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogAddReview(ActivityBusinessReviews.this, businessId, ActivityBusinessReviews.this, progressDialog).show();
            }
        });

    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        pullToRefreshListView.setFooterVisibility(View.VISIBLE);
        new GetBusinessReviews(ActivityBusinessReviews.this, businessId, results.get(results.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessReviews.this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next_button, menu);*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof ArrayList) {
            ArrayList<Review> temp = (ArrayList<Review>) result;
            results.addAll(temp);

            if (Review.submitBefore(ActivityBusinessReviews.this, temp))
                (findViewById(R.id.rl_add)).setVisibility(View.GONE);

            pullToRefreshListView.setResultSize(results.size());

            if (status == Status.FIRST_TIME) {
                adapterBusinessReview = new AdapterBusinessReview(ActivityBusinessReviews.this, results);
                listView.setAdapter(adapterBusinessReview);
            } else if (status == Status.REFRESHING) {
                adapterBusinessReview.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more
                pullToRefreshListView.setFooterVisibility(View.GONE);
                adapterBusinessReview.loadMore(temp);
            }
            status = Status.NONE;
        }

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshListView.onRefreshComplete();
        if (errorCode != 18)
            new DialogMessage(ActivityBusinessReviews.this, ServerAnswer.getError(ActivityBusinessReviews.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

    @Override
    public void notifyAddReview(int rating, String review) {
        progressDialog.dismiss();
        results.add(0, new Review(LoginInfo.getAccessUserIdentifier(ActivityBusinessReviews.this), LoginInfo.getUserProfilePictureId(ActivityBusinessReviews.this), rating, review));
        adapterBusinessReview.notifyDataSetChanged();
        (findViewById(R.id.rl_add)).setVisibility(View.GONE);
    }

}
