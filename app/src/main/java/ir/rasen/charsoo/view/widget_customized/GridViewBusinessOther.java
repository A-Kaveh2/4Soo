package ir.rasen.charsoo.view.widget_customized;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.charsoo.view.activity.ActivityBusinessFollowers;
import ir.rasen.charsoo.view.activity.ActivityBusinessReviews;
import ir.rasen.charsoo.view.activity.ActivityBusinessContactInfo;
import ir.rasen.charsoo.view.activity.ActivityProfilePicture;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.adapter.AdapterPostGrid;
import ir.rasen.charsoo.view.adapter.AdapterPostShared;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.SearchItemPost;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.interface_m.IUnfollowBusiness;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.DownloadCoverImage;
import ir.rasen.charsoo.model.post.GetBusinessPosts;
import ir.rasen.charsoo.model.user.FollowBusiness;
import ir.rasen.charsoo.model.user.UnFollowBusiness;

/**
 * Created by android on 3/14/2015.
 */
public class GridViewBusinessOther implements IWebserviceResponse, IUnfollowBusiness {
    GridViewHeader gridViewHeader;
    AdapterPostGrid adapterPostGrid;
    AdapterPostShared adapterPostBusiness;
    private boolean isThreeColumn = true;
    boolean isLoadingMore = false;
    ImageView imageViewSwitch, imageViewCover, imageViewFollowers, imageViewReviews, imageViewBack,imageViewContactInfo,imageViewCirecle;
    TextViewFont textViewFollowersNumber,textViewIdentifier,textViewName;
    View listFooterView;
    View viewHeader;
    ArrayList<SearchItemPost> searchItemPosts;
    Activity activity;
    Business business;
    ArrayList<Post> posts;
    boolean hasHeader;
    ButtonFont buttonFollowStatus;
    IUnfollowBusiness iUnfollowBusiness;
    IWebserviceResponse iWebserviceResponse;

    public GridViewBusinessOther(Activity activity, Business business, GridViewHeader gridViewHeader) {
        this.activity = activity;
        this.business = business;
        this.gridViewHeader = gridViewHeader;
        iUnfollowBusiness = this;
        iWebserviceResponse = this;
    }

    public void notifyDataSetChanged(Post post) {
        posts.add(0, post);
        searchItemPosts.add(0, new SearchItemPost(post.id, post.pictureId, post.picture));
        /*adapterPostBusiness.notifyDataSetChanged();
        adapterPostGrid.notifyDataSetChanged();*/
        adapterPostGrid = new AdapterPostGrid(activity, searchItemPosts,business.id, Post.GetPostType.SHARE);
        adapterPostBusiness = new AdapterPostShared(activity, posts);

        if (isThreeColumn) {
            gridViewHeader.setAdapter(adapterPostGrid);
        } else {
            gridViewHeader.setAdapter(adapterPostBusiness);
        }
    }

    public void InitialGridViewBusiness(ArrayList<Post> postList) {

        searchItemPosts = new ArrayList<>();
        this.posts = postList;
        iWebserviceResponse = this;
        for (Post post : posts)
            searchItemPosts.add(new SearchItemPost(post.id, post.pictureId, post.picture));
        adapterPostGrid = new AdapterPostGrid(activity, searchItemPosts,business.id, Post.GetPostType.SHARE);
        adapterPostBusiness = new AdapterPostShared(activity, posts);


        if (!hasHeader) {
            viewHeader = ( activity).getLayoutInflater().inflate(R.layout.layout_business_grid_header_another, null);
            imageViewSwitch = (ImageView) viewHeader.findViewById(R.id.imageView_switch);
            imageViewCirecle = (ImageView) viewHeader.findViewById(R.id.imageView_cirecle);
            imageViewCover = (ImageView) viewHeader.findViewById(R.id.imageView_cover);
            imageViewFollowers = (ImageView) viewHeader.findViewById(R.id.imageView_followers);
            imageViewReviews = (ImageView) viewHeader.findViewById(R.id.imageView_reviews);
            imageViewContactInfo = (ImageView) viewHeader.findViewById(R.id.imageView_conatct_info);
            imageViewBack = (ImageView) viewHeader.findViewById(R.id.imageView_back);

            textViewFollowersNumber = (TextViewFont) viewHeader.findViewById(R.id.textView_followers_number22);
            buttonFollowStatus = (ButtonFont) viewHeader.findViewById(R.id.btn_follow_satus);
            textViewIdentifier = (TextViewFont) viewHeader.findViewById(R.id.textView_business_identifier);
            textViewName = (TextViewFont) viewHeader.findViewById(R.id.textView_business_name);

            textViewIdentifier.setText(String.valueOf(business.businessIdentifier));
            textViewName.setText(String.valueOf(business.name));
            textViewFollowersNumber.setText(String.valueOf(business.followersNumber));

            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (screenWidth / 3) * 2);
            imageViewCover.setLayoutParams(params);
            DownloadCoverImage downloadCoverImage = new DownloadCoverImage(activity);
            downloadCoverImage.download(business.profilePictureId, imageViewCover, Image_M.ImageType.BUSINESS);

            imageViewCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityProfilePicture.class);
                    intent.putExtra(Params.USER_IDENTIFIER, business.businessIdentifier);
                    intent.putExtra(Params.PROFILE_PICTURE_ID, business.profilePictureId);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_enter_down,
                            R.anim.slide_exit_down);
                }
            });

            if (business.isFollowing) {
                buttonFollowStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, activity.getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
                buttonFollowStatus.setText(activity.getString(R.string.followed_business_page));
            } else {
                buttonFollowStatus.setBackgroundResource(R.drawable.selector_button_shape_blue);
                buttonFollowStatus.setText(activity.getString(R.string.follow));
            }

            buttonFollowStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (business.isFollowing) {
                        new UnFollowBusiness(activity, LoginInfo.getUserId(activity), business.id, iUnfollowBusiness).execute();
                    } else {
                        new FollowBusiness(activity, LoginInfo.getUserId(activity), business.id, iWebserviceResponse).execute();
                    }
                }
            });
            imageViewBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });

            imageViewFollowers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityBusinessFollowers.class);
                    intent1.putExtra(Params.BUSINESS_ID, business.id);
                    intent1.putExtra(Params.USER_ID, business.userID);

                    activity.startActivity(intent1);
                }
            });
            imageViewReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityBusinessReviews.class);
                    intent1.putExtra(Params.BUSINESS_ID, business.id);
                    intent1.putExtra(Params.BUSINESS_OWNER, false);
                    activity.startActivity(intent1);
                }
            });
            imageViewContactInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityBusinessContactInfo.class);
                    MyApplication myApplication = (MyApplication) ((Activity) activity).getApplication();
                    myApplication.business = business;
                    activity.startActivity(intent);
                }
            });

            imageViewSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isThreeColumn) {
                        gridViewHeader.setNumColumns(1);
                        gridViewHeader.setAdapter(adapterPostBusiness);
                        //now it has one column
                        isThreeColumn = false;
                        imageViewSwitch.setImageResource(R.drawable.selector_header_swtich_grid);
                    } else {
                        prepareGridThreeColumn(gridViewHeader);
                        gridViewHeader.setAdapter(adapterPostGrid);
                        // now it has three column
                        isThreeColumn = true;
                        imageViewSwitch.setImageResource(R.drawable.selector_header_swtich_list);
                    }
                }
            });


            gridViewHeader.addHeaderView(viewHeader);
            hasHeader = true;

            listFooterView = ((LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
            //listFooterView.setVisibility(View.GONE);
            gridViewHeader.addFooterView(listFooterView);
        } else {
            listFooterView.setVisibility(View.GONE);
        }
        gridViewHeader.setBackgroundColor(Color.parseColor("#ffffff"));


        prepareGridThreeColumn(gridViewHeader);
        gridViewHeader.setAdapter(adapterPostGrid);

        gridViewHeader.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    if (!isLoadingMore
                            && posts.size() > 0 && posts.size() % activity.getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });

    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
        new GetBusinessPosts(activity, LoginInfo.getUserId(activity), business.id, posts.get(posts.size() - 1).id, activity.getResources().getInteger(R.integer.lazy_load_limitation), GridViewBusinessOther.this).execute();
    }

    private void prepareGridThreeColumn(GridViewHeader gridViewHeader) {
        gridViewHeader.setNumColumns(3);
        gridViewHeader.setVerticalSpacing(3);
        gridViewHeader.setHorizontalSpacing(9);
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof ArrayList) {
            //GetBusinessPosts' result
            ArrayList<Post> posts = (ArrayList<Post>) result;
            listFooterView.setVisibility(View.GONE);
            if (isThreeColumn)
                adapterPostGrid.loadMore(SearchItemPost.getItems(posts));
            else
                adapterPostBusiness.loadMore(posts);

            if (posts.size() == 0) {
                imageViewSwitch.setVisibility(View.GONE);
                imageViewCirecle.setVisibility(View.GONE);
            }
            else {
                imageViewSwitch.setVisibility(View.VISIBLE);
                imageViewCirecle.setVisibility(View.VISIBLE);
            }

        } else if (result instanceof ResultStatus) {
            //FollowBusiness' result
            buttonFollowStatus.setBackgroundResource(R.drawable.selector_button_shape_orange);
            buttonFollowStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, activity.getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
            buttonFollowStatus.setText(activity.getString(R.string.followed_business_page));
            business.isFollowing = true;
        }


    }

    @Override
    public void getError(Integer errorCode) {
        new DialogMessage(activity, ServerAnswer.getError(activity, errorCode)).show();
    }

    @Override
    public void notifyUnfollowBusiness(int businessId) {
        buttonFollowStatus.setBackgroundResource(R.drawable.selector_button_shape_blue);
        buttonFollowStatus.setText(activity.getString(R.string.follow));
        buttonFollowStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        business.isFollowing = false;
    }
}
