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
import ir.rasen.charsoo.model.search.SearchBusinessesLocation;
import ir.rasen.charsoo.view.adapter.AdapterBusinessSearchResult;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;


public class UNUSED_ActivitySearchBusinessResult extends CharsooActivity implements IWebserviceResponse {

    WaitDialog progressDialog;
    int visitedUserId;
    AdapterBusinessSearchResult adapterBusinessSearchResult;
    ListView listView;
    ArrayList<BaseAdapterItem> results;
    ArrayList<BaseAdapterItem> sampleResults;
    private View listFooterView;
    String searchKeyWord;
    String latitude;
    String longitude;
    int subCategoryId;




    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview);
        setTitle(getResources().getString(R.string.search_businesses));
        try {
            sampleResults = TestUnit.getBaseAdapterItems(getResources());
        } catch (Exception e) {

        }

        searchKeyWord = getIntent().getExtras().getString(Params.SEARCH_KEY_WORD);
        latitude = getIntent().getExtras().getString(Params.LATITUDE);
        longitude = getIntent().getExtras().getString(Params.LONGITUDE);
        subCategoryId = getIntent().getExtras().getInt(Params.SUB_CATEGORY_ID);


        results = new ArrayList<>();
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
        new SearchBusinessesLocation(UNUSED_ActivitySearchBusinessResult.this,searchKeyWord,subCategoryId,latitude,longitude,0,getResources().getInteger(R.integer.lazy_load_limitation),UNUSED_ActivitySearchBusinessResult.this).execute();


    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        listFooterView.setVisibility(View.VISIBLE);
        new SearchBusinessesLocation(UNUSED_ActivitySearchBusinessResult.this,searchKeyWord,subCategoryId,latitude,longitude,results.get(results.size()-1).getId(),getResources().getInteger(R.integer.lazy_load_limitation),UNUSED_ActivitySearchBusinessResult.this).execute();

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
            if(temp.size()== 0) {
                (findViewById(R.id.textView_no_result)).setVisibility(View.VISIBLE);
                return;
            }
            results.addAll(temp);


            if (status == Status.FIRST_TIME) {
                adapterBusinessSearchResult = new AdapterBusinessSearchResult(UNUSED_ActivitySearchBusinessResult.this,results);
                listView.setAdapter(adapterBusinessSearchResult);
            } else {
                //it is loading more

                adapterBusinessSearchResult.loadMore(temp);
            }
            listFooterView.setVisibility(View.GONE);
            status = Status.NONE;
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(UNUSED_ActivitySearchBusinessResult.this, ServerAnswer.getError(UNUSED_ActivitySearchBusinessResult.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

}
