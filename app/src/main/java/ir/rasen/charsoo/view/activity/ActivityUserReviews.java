package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.object.Review;
import ir.rasen.charsoo.model.review.GetUserReviews;
import ir.rasen.charsoo.view.adapter.AdapterUserReview;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityUserReviews extends CharsooActivity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    int visitedUserId;
    AdapterUserReview adapterUserReview;
    ListView listView;
    ArrayList<Review> results;
    ArrayList<Review> sampleResults;
    private View listFooterView;

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview);
        setTitle(getString(R.string.reviews));
        try {
            sampleResults = TestUnit.getUserReviewAdapterItems();
        } catch (Exception e) {

        }

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);

        results = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

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
        new GetUserReviews(ActivityUserReviews.this,visitedUserId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityUserReviews.this).execute();


    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        listFooterView.setVisibility(View.VISIBLE);

        new GetUserReviews(ActivityUserReviews.this,visitedUserId ,results.get(results.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), ActivityUserReviews.this).execute();

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

            if (status == Status.FIRST_TIME) {
                adapterUserReview = new AdapterUserReview(ActivityUserReviews.this,visitedUserId,results,ActivityUserReviews.this,progressDialog);
                listView.setAdapter(adapterUserReview);
            } else {
                //it is loading more
                listFooterView.setVisibility(View.GONE);
                adapterUserReview.loadMore(temp);
            }
            status = Status.NONE;
        }

    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserReviews.this, ServerAnswer.getError(ActivityUserReviews.this, errorCode)).show();
    }

}
