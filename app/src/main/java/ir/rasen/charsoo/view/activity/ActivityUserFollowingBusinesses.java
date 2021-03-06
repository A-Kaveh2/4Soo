package ir.rasen.charsoo.view.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.model.user.GetFollowingBusinesses;
import ir.rasen.charsoo.view.adapter.AdapterUserFollowingBusinesses;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;


public class ActivityUserFollowingBusinesses extends CharsooActivity implements IWebserviceResponse {

    WaitDialog progressDialog;
    int visitedUserId;
    AdapterUserFollowingBusinesses adapterFollowingBusinesses;
    ListView listView;
    ArrayList<BaseAdapterItem> businesses;
    ArrayList<BaseAdapterItem> sampleBusinesses;
    private View listFooterView;

    //for the test


    private enum Status {FIRST_TIME, LOADING_MORE, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview);
        setTitle(getString(R.string.businesses));
        try {
            sampleBusinesses = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }

        visitedUserId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);

        businesses = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new WaitDialog(this);
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
                            && businesses.size() > 0 && businesses.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        //loadMoreData();
                    }
                }
            }
        });

        listFooterView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
        listView.addFooterView(listFooterView, null, false);


        progressDialog.show();
        new GetFollowingBusinesses(ActivityUserFollowingBusinesses.this,visitedUserId,ActivityUserFollowingBusinesses.this).execute();

    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;

        listFooterView.setVisibility(View.VISIBLE);

        new GetFollowingBusinesses(ActivityUserFollowingBusinesses.this,visitedUserId,ActivityUserFollowingBusinesses.this).execute();


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
            ArrayList<BaseAdapterItem> temp = (ArrayList<BaseAdapterItem>) result;
            businesses.addAll(temp);


            if (status == Status.FIRST_TIME) {
                adapterFollowingBusinesses = new AdapterUserFollowingBusinesses(ActivityUserFollowingBusinesses.this,visitedUserId,businesses,progressDialog);
                listView.setAdapter(adapterFollowingBusinesses);
            } else {
                //it is loading more
                listFooterView.setVisibility(View.GONE);
                adapterFollowingBusinesses.loadMore(temp);
            }
            status = Status.NONE;

        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserFollowingBusinesses.this, ServerAnswer.getError(ActivityUserFollowingBusinesses.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }
}
