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
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.model.comment.GetPostAllComments;
import ir.rasen.charsoo.model.comment.SendComment;
import ir.rasen.charsoo.view.adapter.AdapterPostComments;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityComments extends CharsooActivity implements IWebserviceResponse, IPullToRefresh {

    int postId, postOwnerBusinessId;
    AdapterPostComments adapterPostComments;
    ListView listView;
    ArrayList<Comment> comments;
    //for the test
    ArrayList<Comment> sampleComments;
    ProgressDialog progressDialog;
    String commentText;

    @Override
    public void notifyRefresh() {
        comments.clear();
        status = Status.REFRESHING;
        new GetPostAllComments(ActivityComments.this, postId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityComments.this).execute();
    }

    @Override
    public void notifyLoadMore() {
        loadMoreData();
    }

    private enum Status {FIRST_TIME, LOADING_MORE, REFRESHING, NONE}

    EditTextFont editTextComment;
    private Status status;
    boolean isUserOwner;

    //pull_to_refresh_lib
    PullToRefreshList pullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setTitle(getString(R.string.comments));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        //for the test
        try {
            sampleComments = TestUnit.getCommentAdapterItems();
        } catch (Exception e) {

        }

        postId = getIntent().getExtras().getInt(Params.POST_ID);
        postOwnerBusinessId = getIntent().getExtras().getInt(Params.POST_OWNER_BUSINESS_ID);
        isUserOwner = getIntent().getExtras().getBoolean(Params.IS_OWNER);

        comments = new ArrayList<>();
        status = Status.FIRST_TIME;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        pullToRefreshListView = new PullToRefreshList(this, (PullToRefreshListView) findViewById(R.id.pull_refresh_list), ActivityComments.this);
        listView = pullToRefreshListView.getListView();

        progressDialog.show();
        new GetPostAllComments(ActivityComments.this, postId, 0, getResources().getInteger(R.integer.lazy_load_limitation), ActivityComments.this).execute();

        editTextComment = (EditTextFont) findViewById(R.id.edt_comment);

        (findViewById(R.id.imageView_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentText = editTextComment.getText().toString();
                if (!Validation.validateComment(ActivityComments.this, commentText).isValid()) {
                    editTextComment.setError(Validation.getErrorMessage());
                    return;
                }
                new SendComment(ActivityComments.this, LoginInfo.getUserId(ActivityComments.this), postId, commentText, ActivityComments.this).execute();
            }
        });

    }


    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        status = Status.LOADING_MORE;
        pullToRefreshListView.setFooterVisibility(View.VISIBLE);
        new GetPostAllComments(ActivityComments.this, postId, comments.get(comments.size() - 1).id, getResources().getInteger(R.integer.lazy_load_limitation), ActivityComments.this).execute();

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
            ArrayList<Comment> temp = (ArrayList<Comment>) result;
            comments.addAll(temp);

            pullToRefreshListView.setResultSize(comments.size());


            if (status == Status.FIRST_TIME) {
                adapterPostComments = new AdapterPostComments(ActivityComments.this, isUserOwner, postId, postOwnerBusinessId, comments, progressDialog);
                listView.setAdapter(adapterPostComments);
            } else if (status == Status.REFRESHING) {
                adapterPostComments.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            } else {
                //it is loading more
                pullToRefreshListView.setFooterVisibility(View.GONE);
                adapterPostComments.loadMore(temp);
            }
            status = Status.NONE;

        } else if (result instanceof Integer) {
            //comments.add(0, new Comment((Integer) result, LoginInfo.getUserId(ActivityComments.this), LoginInfo.getAccessUserIdentifier(ActivityComments.this), commentText));
            comments.add(0, Comment.getSentComment(ActivityComments.this, (Integer) result, commentText));
            adapterPostComments.notifyDataSetChanged();
            editTextComment.setText("");
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        pullToRefreshListView.onRefreshComplete();
        new DialogMessage(ActivityComments.this, ServerAnswer.getError(ActivityComments.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }
}
