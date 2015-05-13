package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.Footer;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.adapter.AdapterBlockedUsers;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.ActionBar_M;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.business.GetBlockedUsers;


public class ActivityBusinessBlockedUsers extends ActionBarActivity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    int businessId;
    AdapterBlockedUsers adapterBlockedUsers;
    ListView listView;
    ArrayList<BaseAdapterItem> blockedUsers;
    ArrayList<BaseAdapterItem> sampleBlockedUsers;


    private Footer footer;

    private enum Status {FIRST_TIME, LOADING_MORE, NONE,REFRESHING}

    private Status status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview);

        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.blocked));
        try {
            sampleBlockedUsers = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }

        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);
        blockedUsers = new ArrayList<>();
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
                            && blockedUsers.size() > 0 && blockedUsers.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        //loadMoreData();
                    }
                }
            }
        });

        footer = new Footer(this);
        listView.addFooterView(footer.getFooterView(), null, false);

        progressDialog.show();
        new GetBlockedUsers(ActivityBusinessBlockedUsers.this, businessId,0,getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessBlockedUsers.this).execute();


    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        footer.setVisibility(View.VISIBLE);
        new GetBlockedUsers(ActivityBusinessBlockedUsers.this, businessId,blockedUsers.get(blockedUsers.size()-1).getId(),getResources().getInteger(R.integer.lazy_load_limitation), ActivityBusinessBlockedUsers.this).execute();
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
            blockedUsers.addAll(temp);


            if (status == Status.FIRST_TIME || status == Status.REFRESHING) {
                adapterBlockedUsers = new AdapterBlockedUsers(ActivityBusinessBlockedUsers.this, businessId, blockedUsers);
                listView.setAdapter(adapterBlockedUsers);
            } else {
                //it is loading more
                footer.setVisibility(View.GONE);
                adapterBlockedUsers.loadMore(temp);
            }
            status = Status.NONE;
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityBusinessBlockedUsers.this, ServerAnswer.getError(ActivityBusinessBlockedUsers.this, errorCode)).show();
    }
}
