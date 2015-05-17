package ir.rasen.charsoo.view.widget_customized;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.*;

import java.util.ArrayList;
import ir.rasen.charsoo.view.activity.ActivityBusinessFollowers;
import ir.rasen.charsoo.view.activity.ActivityBusinessReviews;
import ir.rasen.charsoo.view.activity.ActivityBusinessContactInfo;
import ir.rasen.charsoo.view.activity.ActivityProfilePicture;
import ir.rasen.charsoo.view.activity.ActivityBusinessRegisterEdit;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.adapter.AdapterPostBusiness;
import ir.rasen.charsoo.view.adapter.AdapterPostGrid;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.SearchItemPost;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.DownloadCoverImage;
import ir.rasen.charsoo.model.post.GetBusinessPosts;

/**
 * Created by android on 3/14/2015.
 */
public class GridViewBusiness implements IWebserviceResponse {
    com.handmark.pulltorefresh.library.GridViewWithHeaderAndFooter gridViewHeader;
    AdapterPostGrid adapterPostGrid;
    AdapterPostBusiness adapterPostBusiness;
    private boolean isThreeColumn = true;
    boolean isLoadingMore = false;
    ImageView imageViewMore, imageViewSwitch, imageViewCover, imageViewFollowers, imageViewReviews, imageViewContactInfo,imageViewCirecle,imageViewBack,imageViewEdit;
    TextViewFont textViewFollowersNumber,textViewIdentifier,textViewName;
    View listFooterView;
    View viewHeader;
    ArrayList<SearchItemPost> searchItemPosts;
    Activity  activity;
    Business business;
    IWebserviceResponse iWebserviceResponse;
    ArrayList<Post> posts;
    DrawerLayout drawerLayout;
    boolean hasHeader;
    DownloadCoverImage downloadCoverImage;

    public GridViewBusiness(Activity activity, Business business, com.handmark.pulltorefresh.library.GridViewWithHeaderAndFooter gridViewHeader, DrawerLayout drawerLayout) {
        this.activity = activity;
        this.business = business;
        this.gridViewHeader = gridViewHeader;
        this.drawerLayout = drawerLayout;
    }

    public void changeProfilePicture(String picture){
        imageViewCover.setImageBitmap(Image_M.getBitmapFromString(picture));
    }
    public void notifyDataSetChanged(Post post) {
        posts.add(0, post);
        if (posts.size() == 0) {
            imageViewSwitch.setVisibility(View.GONE);
            imageViewCirecle.setVisibility(View.GONE);
        }
        else {
            imageViewSwitch.setVisibility(View.VISIBLE);
            imageViewCirecle.setVisibility(View.VISIBLE);
        }

        searchItemPosts.add(0, new SearchItemPost(post.id, post.pictureId, post.picture));
        /*adapterPostBusiness.notifyDataSetChanged();
        adapterPostGrid.notifyDataSetChanged();*/
        adapterPostGrid = new AdapterPostGrid(activity, searchItemPosts, business.id, Post.GetPostType.BUSINESS);
        adapterPostBusiness = new AdapterPostBusiness(activity, posts, true);

        imageViewSwitch.setVisibility(View.VISIBLE);
        imageViewCirecle.setVisibility(View.VISIBLE);

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
        adapterPostGrid = new AdapterPostGrid(activity, searchItemPosts, business.id, Post.GetPostType.BUSINESS);
        adapterPostBusiness = new AdapterPostBusiness(activity, posts, true);


        if (!hasHeader) {
            viewHeader = ((Activity) activity).getLayoutInflater().inflate(R.layout.layout_business_grid_header, null);

            imageViewMore = (ImageView) viewHeader.findViewById(R.id.imageView_more);
            imageViewSwitch = (ImageView) viewHeader.findViewById(R.id.imageView_switch);
            imageViewCirecle = (ImageView) viewHeader.findViewById(R.id.imageView_cirecle);
            imageViewCover = (ImageView) viewHeader.findViewById(R.id.imageView_cover);
            imageViewFollowers = (ImageView) viewHeader.findViewById(R.id.imageView_followers);
            imageViewReviews = (ImageView) viewHeader.findViewById(R.id.imageView_reviews);
            imageViewContactInfo = (ImageView) viewHeader.findViewById(R.id.imageView_conatct_info);
            imageViewBack = (ImageView) viewHeader.findViewById(R.id.imageView_back);
            imageViewEdit = (ImageView) viewHeader.findViewById(R.id.imageView_edit);

            textViewFollowersNumber = (TextViewFont) viewHeader.findViewById(R.id.textView_followers_number22);
            textViewIdentifier = (TextViewFont) viewHeader.findViewById(R.id.textView_business_identifier_header);
            textViewName = (TextViewFont) viewHeader.findViewById(R.id.textView_business_name);



            textViewIdentifier.setText(String.valueOf(business.businessIdentifier));
            textViewName.setText(String.valueOf(business.name));
            textViewFollowersNumber.setText(String.valueOf(business.followersNumber));

            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (screenWidth / 3) * 2);
            imageViewCover.setLayoutParams(params);
            downloadCoverImage = new DownloadCoverImage(activity);
            downloadCoverImage.download(business.profilePictureId, imageViewCover, Image_M.ImageType.BUSINESS);

            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityBusinessRegisterEdit.class);
                    intent.putExtra(Params.BUSINESS_ID,business.id);
                    intent.putExtra(Params.BUSINESS_IDENTIFIER,business.businessIdentifier);
                    activity.startActivityForResult(intent, Params.ACTION_EDIT_BUSINESS);
                }
            });
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

            imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    else
                        drawerLayout.openDrawer(Gravity.RIGHT);
                }
            });
            imageViewFollowers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityBusinessFollowers.class);
                    intent1.putExtra(Params.BUSINESS_ID, business.id);
                    intent1.putExtra(Params.USER_ID, LoginInfo.getUserId(activity));
                    activity.startActivity(intent1);
                }
            });
            imageViewReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityBusinessReviews.class);
                    intent1.putExtra(Params.BUSINESS_ID, business.id);
                    intent1.putExtra(Params.BUSINESS_OWNER, true);
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
            imageViewBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
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


        if (postList.size() == 0) {
            imageViewSwitch.setVisibility(View.GONE);
            imageViewCirecle.setVisibility(View.GONE);
        }
        else {
            imageViewSwitch.setVisibility(View.VISIBLE);
            imageViewCirecle.setVisibility(View.VISIBLE);
        }

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
        new GetBusinessPosts(activity, LoginInfo.getUserId(activity), business.id, posts.get(posts.size() - 1).id, activity.getResources().getInteger(R.integer.lazy_load_limitation), GridViewBusiness.this).execute();
    }

    private void prepareGridThreeColumn(com.handmark.pulltorefresh.library.GridViewWithHeaderAndFooter gridViewHeader) {
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
        }


    }

    @Override
    public void getError(Integer errorCode) {
        new DialogMessage(activity, ServerAnswer.getError(activity, errorCode)).show();
    }
}