package ir.rasen.charsoo;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.adapters.AdapterBusinessReview;
import ir.rasen.charsoo.classes.Review;
import ir.rasen.charsoo.dialog.DialogAddReview;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IAddReview;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.review.GetBusinessReviews;


public class ActivityBusinessReviews extends ActionBarActivity implements IWebserviceResponse, IAddReview {

    ProgressDialog progressDialog;
    int businessId;
    AdapterBusinessReview adapterBusinessReview;
    ListView listView;
    ArrayList<Review> results;
    ArrayList<Review> sampleResults;
    private View listFooterView;
    SwipeRefreshLayout swipeLayout;


    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_reviews);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.reviews));
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

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (status == Status.LOADING_MORE) {
                    swipeLayout.setRefreshing(false);
                    return;
                }

                status = Status.REFRESHING;
                results.clear();
                new GetBusinessReviews(ActivityBusinessReviews.this, businessId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessReviews.this).execute();

            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem
                    ,
                    currentVisibleItemCount
                    ,
                    currentScrollState;

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
                    if (status != Status.LOADING_MORE
                            && results.size() > 0 && results.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });

        listFooterView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
        listView.addFooterView(listFooterView, null, false);


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
        listFooterView.setVisibility(View.VISIBLE);
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
            if(Review.submitBefore(ActivityBusinessReviews.this,temp))
                (findViewById(R.id.rl_add)).setVisibility(View.GONE);

            if (status == Status.FIRST_TIME) {
                adapterBusinessReview = new AdapterBusinessReview(ActivityBusinessReviews.this, results);
                listView.setAdapter(adapterBusinessReview);
            } else {
                //it is loading more
                listFooterView.setVisibility(View.GONE);
                adapterBusinessReview.loadMore(temp);
            }
            status = Status.NONE;
        }

    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        if (errorCode != 18)
            new DialogMessage(ActivityBusinessReviews.this, ServerAnswer.getError(ActivityBusinessReviews.this, errorCode)).show();
    }

    @Override
    public void notifyAddReview(int rating, String review) {
        progressDialog.dismiss();
        results.add(0, new Review(LoginInfo.getAccessUserIdentifier(ActivityBusinessReviews.this), LoginInfo.getUserProfilePictureId(ActivityBusinessReviews.this), rating, review));
        adapterBusinessReview.notifyDataSetChanged();
        (findViewById(R.id.rl_add)).setVisibility(View.GONE);
    }

}
