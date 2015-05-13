package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.adapters.AdapterBusinessSearchResult;
import ir.rasen.charsoo.adapters.AdapterBusinessFollowers;
import ir.rasen.charsoo.adapters.AdapterUserFollowingBusinesses;
import ir.rasen.charsoo.adapters.AdapterUserFriendshipRequest;
import ir.rasen.charsoo.adapters.AdapterPostTimeLine;
import ir.rasen.charsoo.adapters.AdapterUserSearchResult;
import ir.rasen.charsoo.adapters.BaseAdapter_M;
import ir.rasen.charsoo.adapters.AdapterBusinessPostComments;
import ir.rasen.charsoo.adapters.AdapterBusinessReview;
import ir.rasen.charsoo.adapters.AdapterCommentNotification;
import ir.rasen.charsoo.adapters.AdapterUserFriends;
import ir.rasen.charsoo.adapters.AdapterPostComments;
import ir.rasen.charsoo.adapters.AdapterUserReview;
import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.dialog.DialogEditComment;
import ir.rasen.charsoo.dialog.PopupEditDeleteComment;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IBaseAdapterOnClickTask;
import ir.rasen.charsoo.interfaces.ICommentChange;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;


public class ActivityTest extends ActionBarActivity implements IBaseAdapterOnClickTask, IWebserviceResponse,ICommentChange {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        progressDialog = new ProgressDialog(this);


        ListView listView = (ListView) findViewById(R.id.listView);
        GridView gridView = (GridView)findViewById(R.id.gridView);

        String type = getIntent().getExtras().getString("type");
        switch (type) {
            case "button_adapter_base":
                try {
                    BaseAdapter_M baseAdapter_m = new BaseAdapter_M(this, TestUnit.getBaseAdapterItems(this.getResources()),true, this,false);
                    listView.setAdapter(baseAdapter_m);
                } catch (Exception e) {
                    Toast.makeText(ActivityTest.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            case "button_business_post_adapter_comment":
                AdapterBusinessPostComments adapterBusinessPostComments = new AdapterBusinessPostComments(ActivityTest.this,1, TestUnit.getCommentAdapterItems(), ActivityTest.this,progressDialog);
                listView.setAdapter(adapterBusinessPostComments);
                break;
            case "button_post_adapter_comment":
                AdapterPostComments adapterPostComments = new AdapterPostComments(ActivityTest.this,true,1, TestUnit.getCommentAdapterItems(), ActivityTest.this,progressDialog);
                listView.setAdapter(adapterPostComments);
                break;
            case "button_adapter_comment_notification":
                AdapterCommentNotification adapterCommentNotification = new AdapterCommentNotification(ActivityTest.this, TestUnit.getCommentNotificationAdapterItems(ActivityTest.this));
                listView.setAdapter(adapterCommentNotification);
                break;
            case "button_adapter_user_reviews":
                AdapterUserReview adapterUserReview = new AdapterUserReview(ActivityTest.this,3, TestUnit.getUserReviewAdapterItems(), ActivityTest.this,progressDialog);
                listView.setAdapter(adapterUserReview);
                break;
            case "button_dialog":
                DialogEditComment d = new DialogEditComment(ActivityTest.this, TestUnit.getCommentAdapterItems().get(0),ActivityTest.this,progressDialog,ActivityTest.this);
                d.show();
                break;
            case "button_popup":
                PopupEditDeleteComment p = new PopupEditDeleteComment(ActivityTest.this, TestUnit.getCommentAdapterItems().get(0),ActivityTest.this,progressDialog,ActivityTest.this);
                p.show();
                break;
            case "button_adapter_business_reviews":
                AdapterBusinessReview adapterBusinessReview = new AdapterBusinessReview(ActivityTest.this,TestUnit.getUserReviewAdapterItems());
                listView.setAdapter(adapterBusinessReview);
                break;
            case "button_friends":
                try {
                    AdapterUserFriends adapterFriends = new AdapterUserFriends(ActivityTest.this,3, TestUnit.getBaseAdapterItems(ActivityTest.this.getResources()));
                    listView.setAdapter(adapterFriends);
                }
                catch (Exception e){

                }
                break;
            case "button_followers":
                try {
                    AdapterBusinessFollowers adapterFollowers = new AdapterBusinessFollowers(ActivityTest.this, TestUnit.getBaseAdapterItems(ActivityTest.this.getResources()));
                    listView.setAdapter(adapterFollowers);
                }
                catch (Exception e){

                }
                break;
            case "button_following_businesses":
                try {
                    AdapterUserFollowingBusinesses adapterFollowingBusinesses = new AdapterUserFollowingBusinesses(ActivityTest.this,3,TestUnit.getBaseAdapterItems(ActivityTest.this.getResources()),progressDialog);
                    listView.setAdapter(adapterFollowingBusinesses);
                }
                catch (Exception e){

                }
                break;
            case "button_user_search_result":
                try {
                    AdapterUserSearchResult adapterUserSearchResult = new AdapterUserSearchResult(ActivityTest.this,TestUnit.getBaseAdapterItems(ActivityTest.this.getResources()));
                    listView.setAdapter(adapterUserSearchResult);
                }
                catch (Exception e){

                }
                break;
            case "button_business_search_result":
                try {
                    AdapterBusinessSearchResult adapterBusinessSearchResult = new AdapterBusinessSearchResult(ActivityTest.this,TestUnit.getBaseAdapterItems(ActivityTest.this.getResources()));
                    listView.setAdapter(adapterBusinessSearchResult);
                }
                catch (Exception e){

                }
                break;
            case "button_friends_request":
                try {
                    AdapterUserFriendshipRequest adapterFriendshipRequest = new AdapterUserFriendshipRequest(ActivityTest.this,TestUnit.getBaseAdapterItems(ActivityTest.this.getResources()));
                    listView.setAdapter(adapterFriendshipRequest);
                } catch (Exception e) {

                }
                break;
            case "button_time_line_posts":
                AdapterPostTimeLine adapterPostTimeLine = new AdapterPostTimeLine(ActivityTest.this,TestUnit.getPostAdapterListItems());
                gridView.setAdapter(adapterPostTimeLine);
                break;
            case "button_grid_posts":
               /* AdapterPostGrid adapterPostGrid = new AdapterPostGrid(ActivityTest.this,TestUnit.getPostAdapterGridItems());
                listView.setAdapter(adapterPostGrid);*/
                break;

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickTask(BaseAdapterItem baseAdapterItem) {
        Toast.makeText(ActivityTest.this, baseAdapterItem.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        String s = "ok";
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        String s = "not ok";
    }

    @Override
    public void notifyDeleteComment(int commentId) {

    }

    @Override
    public void notifyUpdateComment(Comment comment) {

    }
}
