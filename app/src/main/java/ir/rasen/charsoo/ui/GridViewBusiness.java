package ir.rasen.charsoo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.internal.dr;

import java.util.ArrayList;

import ir.rasen.charsoo.ActivityBusiness;
import ir.rasen.charsoo.ActivityBusinessFollowers;
import ir.rasen.charsoo.ActivityBusinessReviews;
import ir.rasen.charsoo.ActivityContactInfo;
import ir.rasen.charsoo.ActivityEntrance;
import ir.rasen.charsoo.FragmentHome;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.adapters.AdapterPostBusiness;
import ir.rasen.charsoo.adapters.AdapterPostGrid;
import ir.rasen.charsoo.adapters.AdapterPostShared;
import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.SearchItemPost;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.interfaces.IAddPost;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.DownloadCoverImage;
import ir.rasen.charsoo.webservices.post.GetBusinessPosts;
import ir.rasen.charsoo.webservices.post.GetTimeLinePosts;

/**
 * Created by android on 3/14/2015.
 */
public class GridViewBusiness implements IWebserviceResponse {
    GridViewWithHeaderAndFooter gridViewHeader;
    AdapterPostGrid adapterPostGrid;
    AdapterPostBusiness adapterPostBusiness;
    private boolean isThreeColumn = true;
    boolean isLoadingMore = false;
    ImageView imageViewMore, imageViewSwitch, imageViewCover, imageViewFollowers, imageViewReviews, imageViewContactInfo,imageViewCirecle;
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

    public GridViewBusiness(Activity activity, Business business, GridViewWithHeaderAndFooter gridViewHeader, DrawerLayout drawerLayout) {
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
            textViewFollowersNumber = (TextViewFont) viewHeader.findViewById(R.id.textView_followers_number22);
            textViewIdentifier = (TextViewFont) viewHeader.findViewById(R.id.textView_business_identifier);
            textViewName = (TextViewFont) viewHeader.findViewById(R.id.textView_business_name);


            textViewIdentifier.setText(String.valueOf(business.businessIdentifier));
            textViewName.setText(String.valueOf(business.name));
            textViewFollowersNumber.setText(String.valueOf(business.followersNumber));

            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (screenWidth / 3) * 2);
            imageViewCover.setLayoutParams(params);
            downloadCoverImage = new DownloadCoverImage(activity);
            downloadCoverImage.download(business.profilePictureId, imageViewCover);



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
                    Intent intent = new Intent(activity, ActivityContactInfo.class);
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

    private void prepareGridThreeColumn(GridViewWithHeaderAndFooter gridViewHeader) {
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
