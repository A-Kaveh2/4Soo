package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.HFGridView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.SearchItemPost;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.model.search.SearchBusinessesLocation;
import ir.rasen.charsoo.model.search.SearchPost;
import ir.rasen.charsoo.view.adapter.AdapterPostGrid;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;

public class ActivitySearchPostResult extends CharsooActivity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    AdapterPostGrid adapterPostGrid;
    HFGridView gridView;
    ArrayList<SearchItemPost> results;
    ArrayList<SearchItemPost> sampleResults;
    private View listFooterView;
    String searchKeyWord;
    String latitude;
    String longitude;
    int subCategoryId;




    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;      //zsdfsdfsd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gridview_header_footer);
        setTitle(getString(R.string.search_products));
        try {
            sampleResults = TestUnit.getPostAdapterGridItems();
        } catch (Exception e) {

        }

        searchKeyWord = getIntent().getExtras().getString(Params.SEARCH_KEY_WORD);

        results = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        gridView = (HFGridView) findViewById(R.id.gridView);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        gridView.addFooterView(listFooterView);

        progressDialog.show();
        new SearchPost(ActivitySearchPostResult.this,searchKeyWord,0,getResources().getInteger(R.integer.lazy_load_limitation),ActivitySearchPostResult.this).execute();


    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        listFooterView.setVisibility(View.VISIBLE);

        new SearchBusinessesLocation(ActivitySearchPostResult.this,searchKeyWord,subCategoryId,latitude,longitude,results.get(results.size()-1).postId,getResources().getInteger(R.integer.lazy_load_limitation),ActivitySearchPostResult.this).execute();

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
            ArrayList<SearchItemPost> temp = (ArrayList<SearchItemPost>) result;
            if(temp.size()== 0) {
                (findViewById(R.id.textView_no_result)).setVisibility(View.VISIBLE);
                return;
            }

            results.addAll(temp);


            if (status == Status.FIRST_TIME) {
                adapterPostGrid = new AdapterPostGrid(ActivitySearchPostResult.this,results,0, Post.GetPostType.SEARCH);

                gridView.setNumColumns(3);
                gridView.setVerticalSpacing(3);
                gridView.setHorizontalSpacing(9);
                gridView.setViewWidthIfItsZero(getWindowManager().getDefaultDisplay().getWidth());

                gridView.setAdapter(adapterPostGrid);
            } else {
                //it is loading more
                listFooterView.setVisibility(View.GONE);
                adapterPostGrid.loadMore(temp);
            }
            status = Status.NONE;
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivitySearchPostResult.this, ServerAnswer.getError(ActivitySearchPostResult.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

}
