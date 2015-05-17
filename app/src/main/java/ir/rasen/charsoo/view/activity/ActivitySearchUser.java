package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.adapter.AdapterUserSearchResult;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.ActionBar_M;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.search.SearchUser;


public class ActivitySearchUser extends ActionBarActivity implements IWebserviceResponse {

    ProgressDialog progressDialog;
    int visitedUserId;
    AdapterUserSearchResult adapterUserSearchResult;
    ListView listView;
    ArrayList<BaseAdapterItem> results;
    ArrayList<BaseAdapterItem> sampleResults;
    private View listFooterView;
    //EditTextFont editTextSearch;
    String searchKeyWord;




    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.search_users));
        //editTextSearch = (EditTextFont)findViewById(R.id.edt_search);

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

        searchKeyWord = getIntent().getExtras().getString(Params.SEARCH_KEY_WORD);
        search();

        //editTextSearch is now gone. The search scenario changed.
       /* editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!Validation.validateIdentifier(ActivitySearchUser.this,editTextSearch.getText().toString()).isValid()){
                        editTextSearch.setError(Validation.getErrorMessage());
                        return false;
                    }
                    search();
                    return true;
                }
                return false;
            }
        });
        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editTextSearch.getRight() - editTextSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(!Validation.validateIdentifier(ActivitySearchUser.this,editTextSearch.getText().toString()).isValid()){
                            editTextSearch.setError(Validation.getErrorMessage());
                            return false;
                        }
                        search();
                    }
                }
                return false;
            }
        });*/

    }

    private void search(){
        //searchKeyWord = editTextSearch.getText().toString();
        progressDialog.show();
        if(results.size() != 0) {
            results.clear();
            adapterUserSearchResult.notifyRemoveAllItems();
        }
        /*InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);*/
        status = Status.FIRST_TIME;
        new SearchUser(ActivitySearchUser.this,searchKeyWord,0,getResources().getInteger(R.integer.lazy_load_limitation),ActivitySearchUser.this).execute();
    }
    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        listFooterView.setVisibility(View.VISIBLE);

        new SearchUser(ActivitySearchUser.this,searchKeyWord,results.get(results.size()-1).getId(),getResources().getInteger(R.integer.lazy_load_limitation),ActivitySearchUser.this).execute();

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
                adapterUserSearchResult = new AdapterUserSearchResult(ActivitySearchUser.this, results);
                listView.setAdapter(adapterUserSearchResult);
            } else {
                //it is loading more
                listFooterView.setVisibility(View.GONE);
                adapterUserSearchResult.loadMore(temp);
            }
            status = Status.NONE;
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivitySearchUser.this, ServerAnswer.getError(ActivitySearchUser.this, errorCode)).show();
    }

}