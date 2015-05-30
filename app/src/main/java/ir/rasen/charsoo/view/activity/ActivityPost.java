package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.MyGestureDetector;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PersianDate;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TextProcessor;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.DownloadImages;
import ir.rasen.charsoo.model.post.CancelShare;
import ir.rasen.charsoo.model.post.GetPost;
import ir.rasen.charsoo.model.post.Like;
import ir.rasen.charsoo.model.post.Share;
import ir.rasen.charsoo.model.post.Unlike;
import ir.rasen.charsoo.view.dialog.DialogDeletePostConfirmation;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.dialog.PopupCancelSharePost;
import ir.rasen.charsoo.view.dialog.PopupReportCancelSharePost;
import ir.rasen.charsoo.view.dialog.PopupReportPostActivity;
import ir.rasen.charsoo.view.interface_m.IDeletePost;
import ir.rasen.charsoo.view.interface_m.IReportPost;
import ir.rasen.charsoo.view.interface_m.IUpdateTimeLine;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityPost extends CharsooActivity implements IWebserviceResponse, IReportPost, IDeletePost, IUpdateTimeLine {


    ProgressDialog progressDialog;
    ImageView imageViewProfileImage;
    TextViewFont textViewBusinessIdentifier;
    TextViewFont textViewDate;

    //complete post section
    LinearLayout llCompleteSection;
    ImageView imageViewPost;
    TextViewFont textViewLikeNumber;
    TextViewFont textViewCommentNumber;
    TextViewFont textViewShareNumber;
    TextViewFont textViewDescription;
    TextViewFont textViewComment1UserIdentifier;
    TextViewFont textViewComment2UserIdentifier;
    TextViewFont textViewComment3UserIdentifier;
    TextViewFont textViewComment1;
    TextViewFont textViewComment2;
    TextViewFont textViewComment3;
    TextViewFont textViewPrice;
    TextViewFont textViewTitle;
    ImageView imageViewLike;
    ImageView imageViewComment;
    ImageView imageViewShare;
    ImageView imageViewMore;
    ImageView imageViewPostLike;
    TextViewFont textViewCode;
    LinearLayout llPriceSection, llCodeSection;
    Post post;
    DownloadImages downloadImages;
    Post.GetPostType getPostType;
    int postId, businessId;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle(getString(R.string.product));
        postId = getIntent().getExtras().getInt(Params.POST_ID);
        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);
        final String postType = getIntent().getExtras().getString(Params.POST_TYPE);
        if (postType.equals(Post.GetPostType.BUSINESS.name()))
            getPostType = Post.GetPostType.BUSINESS;
        else if (postType.equals(Post.GetPostType.SHARE.name()))
            getPostType = Post.GetPostType.SHARE;
        else if (postType.equals(Post.GetPostType.SEARCH.name()))
            getPostType = Post.GetPostType.SEARCH;


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        downloadImages = new DownloadImages(this);

        imageViewProfileImage = (ImageView) findViewById(R.id.imageView_profile_picture);
        textViewBusinessIdentifier = (TextViewFont) findViewById(R.id.textView_business_identifier);
        textViewDate = (TextViewFont) findViewById(R.id.textView_date);
        llCompleteSection = (LinearLayout) findViewById(R.id.ll_complete_post_section);

        imageViewPost = (ImageView) findViewById(R.id.imageView_post);
        int screedWidth = getResources().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screedWidth);
        imageViewPost.setLayoutParams(params);
        imageViewPostLike = (ImageView)findViewById(R.id.imageView_post_like);
        imageViewLike = (ImageView) findViewById(R.id.imageView_like);
        imageViewComment = (ImageView) findViewById(R.id.imageView_comment);
        imageViewShare = (ImageView) findViewById(R.id.imageView_share);
        imageViewMore = (ImageView) findViewById(R.id.imageView_more);

        textViewLikeNumber = (TextViewFont) findViewById(R.id.textView_like_number);
        textViewCommentNumber = (TextViewFont) findViewById(R.id.textView_comment_number);
        textViewShareNumber = (TextViewFont) findViewById(R.id.textView_share_number);
        textViewDescription = (TextViewFont) findViewById(R.id.textView_description);
        textViewComment1 = (TextViewFont) findViewById(R.id.textView_comment1);
        textViewComment1UserIdentifier = (TextViewFont) findViewById(R.id.textView_comment1_user_identifier);
        textViewComment2 = (TextViewFont) findViewById(R.id.textView_comment2);
        textViewComment2UserIdentifier = (TextViewFont) findViewById(R.id.textView_comment2_user_identifier);
        textViewComment3 = (TextViewFont) findViewById(R.id.textView_comment3);
        textViewComment3UserIdentifier = (TextViewFont) findViewById(R.id.textView_comment3_user_identifier);
        textViewTitle = (TextViewFont) findViewById(R.id.textView_title);
        textViewPrice = (TextViewFont) findViewById(R.id.textView_price);
        textViewCode = (TextViewFont) findViewById(R.id.textView_code);
        llPriceSection = (LinearLayout) findViewById(R.id.ll_price_section);
        llCodeSection = (LinearLayout) findViewById(R.id.ll_code_section);

        textViewBusinessIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Business.goBusinessHomeInfoPage(ActivityPost.this, post.businessID);

            }
        });
        imageViewProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Business.goBusinessHomeInfoPage(ActivityPost.this, post.businessID);
            }
        });
        textViewComment1UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(ActivityPost.this, post.lastThreeComments.get(0).userID);
            }
        });
        textViewComment2UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(ActivityPost.this, post.lastThreeComments.get(1).userID);
            }
        });
        textViewComment3UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(ActivityPost.this, post.lastThreeComments.get(2).userID);

            }
        });

        imageViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPostType == Post.GetPostType.BUSINESS)
                    Comment.openCommentActivity(ActivityPost.this, true, post.id, post.businessID);
                else
                    Comment.openCommentActivity(ActivityPost.this, false, post.id, post.businessID);
            }
        });
        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPostType == Post.GetPostType.BUSINESS) {
                    //it is edit
                    Intent intent = new Intent(ActivityPost.this, ActivityPostAddEdit.class);
                    intent.putExtra(Params.BUSINESS_ID, businessId);
                    intent.putExtra(Params.POST_ID, postId);
                    startActivityForResult(intent, Params.ACTION_EDIT_POST);
                } else {
                    if (post.isLiked) {
                        //unlike the post

                        new Unlike(ActivityPost.this, LoginInfo.getUserId(ActivityPost.this), post.id).execute();
                        post.isLiked = false;
                        imageViewLike.setImageResource(R.drawable.ic_favorite_grey);
                    } else {
                        //like the post

                        new Like(ActivityPost.this, LoginInfo.getUserId(ActivityPost.this), post.id).execute();
                        post.isLiked = true;
                        imageViewLike.setImageResource(R.drawable.ic_favorite_red);
                    }
                }

            }
        });



        imageViewPost.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(post == null || getPostType == Post.GetPostType.BUSINESS)
                    return false;
                return gestureDetector.onTouchEvent(event);
            }
        });


        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPostType == Post.GetPostType.BUSINESS) {
                    //it is delete
                    DialogDeletePostConfirmation d = new DialogDeletePostConfirmation(ActivityPost.this, businessId, postId, ActivityPost.this);
                    d.show();
                } else {
                    if (post.isShared) {
                        //cancel share the post

                        new CancelShare(ActivityPost.this, LoginInfo.getUserId(ActivityPost.this), post.id,ActivityPost.this).execute();

                        post.isShared = false;
                        imageViewShare.setImageResource(R.drawable.ic_reply_grey);
                    } else {
                        //share the post

                        new Share(ActivityPost.this, LoginInfo.getUserId(ActivityPost.this), post.id, ActivityPost.this).execute();

                        post.isShared = true;
                        imageViewShare.setImageResource(R.drawable.ic_reply_blue);

                        //if post is reported before, then imageViewMore is gone
                        if (post.isReported)
                            imageViewMore.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!post.isReported && post.isShared)
                    new PopupReportCancelSharePost(ActivityPost.this,  LoginInfo.getUserId(ActivityPost.this), post.id,0,null, ActivityPost.this,ActivityPost.this).show();
                else if (post.isReported && post.isShared)
                    new PopupCancelSharePost(ActivityPost.this,  LoginInfo.getUserId(ActivityPost.this), post.id, ActivityPost.this).show();
                else if (!post.isReported && !post.isShared)
                    new PopupReportPostActivity(ActivityPost.this, LoginInfo.getUserId(ActivityPost.this), post.id,0,null, ActivityPost.this).show();
            }
        });


        progressDialog.show();
        int iiid=LoginInfo.getUserId(this);
        new GetPost(this, LoginInfo.getUserId(this), businessId, postId, getPostType, this).execute();

        if (getPostType == Post.GetPostType.BUSINESS) {
            imageViewLike.setBackgroundResource(R.drawable.selector_edit_button_grey);
            imageViewShare.setBackgroundResource(R.drawable.selector_delete_button);
            imageViewMore.setVisibility(View.GONE);
        } else {
            imageViewLike.setBackgroundResource(R.drawable.selector_post_button_like);
            imageViewShare.setBackgroundResource(R.drawable.selector_post_button_share1);
            imageViewMore.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_activity, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Params.ACTION_EDIT_POST) {
                post = ((MyApplication) getApplication()).post;
                initialize();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            progressDialog.show();
            new GetPost(this, LoginInfo.getUserId(this), businessId, postId, getPostType, this).execute();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof Post) {
            post = (Post) result;
            gestureDetector = new GestureDetector(ActivityPost.this, new MyGestureDetector(ActivityPost.this,post.id,post.isLiked, imageViewLike, imageViewPostLike));
            initialize();
        }

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityPost.this, ServerAnswer.getError(ActivityPost.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

    public void initialize() {
        downloadImages.download(post.businessProfilePictureId, Image_M.SMALL, Image_M.ImageType.BUSINESS, imageViewProfileImage, true);
        textViewDate.setText(PersianDate.getCreationDate(ActivityPost.this, post.creationDate));
        textViewBusinessIdentifier.setText(post.businessUserName);

        downloadImages.download(post.pictureId, Image_M.LARGE, Image_M.ImageType.POST, imageViewPost, false);
        textViewLikeNumber.setText(String.valueOf(post.likeNumber));
        textViewCommentNumber.setText(String.valueOf(post.commentNumber));
        textViewShareNumber.setText(String.valueOf(post.shareNumber));
        textViewDescription.setText(post.description);
        textViewTitle.setText(TextProcessor.removeHashtags(post.title));
        if (post.price != null && !post.price.equals("") && !post.price.equals("null")) {
            textViewPrice.setText(post.price);
            llPriceSection.setVisibility(View.VISIBLE);
        } else//dfadf
            llPriceSection.setVisibility(View.GONE);
        if (post.code != null && !post.code.equals("") && !post.code.equals("null")) {
            textViewCode.setText(post.code);
            llCodeSection.setVisibility(View.VISIBLE);
        } else
            llCodeSection.setVisibility(View.GONE);

        ArrayList<Comment> lastThreeComments = post.lastThreeComments;
        if (lastThreeComments.size() > 0) {
            textViewComment1UserIdentifier.setText(post.lastThreeComments.get(0).username);
            textViewComment1.setText(post.lastThreeComments.get(0).text);
            textViewComment1UserIdentifier.setVisibility(View.VISIBLE);
            textViewComment1.setVisibility(View.VISIBLE);
        }
        if (lastThreeComments.size() > 1) {
            textViewComment2UserIdentifier.setText(post.lastThreeComments.get(1).username);
            textViewComment2.setText(post.lastThreeComments.get(1).text);
            textViewComment2UserIdentifier.setVisibility(View.VISIBLE);
            textViewComment2.setVisibility(View.VISIBLE);
        }
        if (lastThreeComments.size() > 2) {
            textViewComment3UserIdentifier.setText(post.lastThreeComments.get(2).username);
            textViewComment3.setText(post.lastThreeComments.get(2).text);
            textViewComment3UserIdentifier.setVisibility(View.VISIBLE);
            textViewComment3.setVisibility(View.VISIBLE);
        }
        if (post.isLiked && getPostType != Post.GetPostType.BUSINESS)
            imageViewLike.setBackgroundResource(R.drawable.ic_favorite_red);
        if (post.isShared)
            imageViewShare.setBackgroundResource(R.drawable.ic_reply_blue);

        if (!post.isShared && post.isReported)
            imageViewMore.setVisibility(View.GONE);
        else
            imageViewMore.setVisibility(View.VISIBLE);
    }




    @Override
    public void notifyDeletePost(int postId) {
        Intent intent = new Intent(Params.DELETE_POST_FROM_ACTIVITY);
        intent.putExtra(Params.POST_ID,postId);
        LocalBroadcastManager.getInstance(ActivityPost.this).sendBroadcast(intent);

        finish();
    }

    @Override
    public void notifyUpdateTimeLineShare(int postId) {
        Intent intent = new Intent(Params.UPATE_TIME_LINE);
        intent.putExtra(Params.UPDATE_TIME_LINE_TYPE, Params.UPATE_TIME_LINE_TYPE_SHARE);
        intent.putExtra(Params.POST_ID, postId);
        LocalBroadcastManager.getInstance(ActivityPost.this).sendBroadcast(intent);
    }

    @Override
    public void notifyUpdateTimeLineCancelShare(int postId) {
        //it goes to the FragmentUser and the goes to FragmentHome
        if (getPostType == Post.GetPostType.SHARE) {
            Intent intent = new Intent(Params.CANCEL_USER_SHARE_POST);
            intent.putExtra(Params.POST_ID, postId);
            LocalBroadcastManager.getInstance(ActivityPost.this).sendBroadcast(intent);
            //finish();
        } else {
            //update time line
            Intent intentUpdateTimeLine = new Intent(Params.UPATE_TIME_LINE);
            intentUpdateTimeLine.putExtra(Params.UPDATE_TIME_LINE_TYPE, Params.UPATE_TIME_LINE_TYPE_CANCEL_SHARE);
            intentUpdateTimeLine.putExtra(Params.POST_ID, postId);
            LocalBroadcastManager.getInstance(ActivityPost.this).sendBroadcast(intentUpdateTimeLine);
        }
    }

    @Override
    public void notifyReportPost(int position, ImageView imageViewMore) {
        post.isReported = true;
        if (!post.isShared)
            this.imageViewMore.setVisibility(View.GONE);
    }


}
