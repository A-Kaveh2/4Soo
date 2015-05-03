package ir.rasen.charsoo.ui;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

import ir.rasen.charsoo.ActivityEntrance;
import ir.rasen.charsoo.ActivityFollowingBusinesses;
import ir.rasen.charsoo.ActivityFriendRequests;
import ir.rasen.charsoo.ActivityFriends;
import ir.rasen.charsoo.ActivitySearchUser;
import ir.rasen.charsoo.ActivityUserReviews;
import ir.rasen.charsoo.FragmentUser;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.adapters.AdapterPostGrid;
import ir.rasen.charsoo.adapters.AdapterPostShared;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.SearchItemPost;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.DownloadCoverImage;
import ir.rasen.charsoo.webservices.post.GetBusinessPosts;
import ir.rasen.charsoo.webservices.post.GetSharedPosts;

/**
 * Created by android on 3/14/2015.
 */
public class GridViewUser implements IWebserviceResponse {
    GridViewHeader gridViewHeader;
    AdapterPostGrid adapterPostGrid;
    AdapterPostShared adapterPostShared;
    private boolean isThreeColumn = true;

    ImageView imageViewSearch, imageViewMore, imageViewSwitch, imageViewCover, imageViewCirecle, imageViewFriends, imageViewReviews, imageViewFollowingBusinesses;
    TextViewFont textViewFriends, textViewBusinesses, textViewReviews, textViewRequests, textViewIdentifier, textViewName;
    ArrayList<SearchItemPost> searchItemPosts;
    View viewHeader;
    Activity activity;
    int profilePictureId;
    DrawerLayout drawerLayout;
    View listFooterView;
    boolean isLoadingMore = false;
    ArrayList<Post> posts;
    int visitedUserId;
    boolean hasHeader, hasRequest;
    String userIdentifier, userName;


    public GridViewUser(Activity activity, String userIdentifier, String userName, int profilePictureId, boolean hasRequest, int visitedUserId, GridViewHeader gridViewHeader, DrawerLayout drawerLayout) {
        this.activity = activity;
        this.profilePictureId = profilePictureId;
        this.gridViewHeader = gridViewHeader;
        this.drawerLayout = drawerLayout;
        this.visitedUserId = visitedUserId;
        this.hasHeader = false;
        this.hasRequest = hasRequest;
        this.userIdentifier = userIdentifier;
        this.userName = userName;

    }


    public void initialProfilePicture(String userPictureString) {
        imageViewCover.setImageBitmap(Image_M.getBitmapFromString(userPictureString));
    }

    public void InitialGridViewUser(ArrayList<Post> postList) {

        this.posts = postList;


        searchItemPosts = new ArrayList<>();
        for (Post post : posts)
            searchItemPosts.add(new SearchItemPost(post.id, post.pictureId, post.picture));

        adapterPostGrid = new AdapterPostGrid(activity, searchItemPosts, 0, Post.GetPostType.SHARE);

        adapterPostShared = new AdapterPostShared(activity, posts);


        if (!hasHeader) {
            viewHeader = (activity).getLayoutInflater().inflate(R.layout.layout_user_grid_header, null);
            imageViewMore = (ImageView) viewHeader.findViewById(R.id.imageView_more);
            imageViewSearch = (ImageView) viewHeader.findViewById(R.id.imageView_search);
            imageViewSwitch = (ImageView) viewHeader.findViewById(R.id.imageView_switch);
            imageViewCirecle = (ImageView) viewHeader.findViewById(R.id.imageView_cirecle);
            imageViewCover = (ImageView) viewHeader.findViewById(R.id.imageView_cover);

            imageViewFriends = (ImageView) viewHeader.findViewById(R.id.imageView_friends);
            imageViewReviews = (ImageView) viewHeader.findViewById(R.id.imageView_reviews);
            imageViewFollowingBusinesses = (ImageView) viewHeader.findViewById(R.id.imageView_businesses);

            textViewBusinesses = (TextViewFont) viewHeader.findViewById(R.id.textView_businesses);
            textViewFriends = (TextViewFont) viewHeader.findViewById(R.id.textView_friends);
            textViewReviews = (TextViewFont) viewHeader.findViewById(R.id.textView_reviews);
            textViewRequests = (TextViewFont) viewHeader.findViewById(R.id.textView_friend_requests);
            textViewIdentifier = (TextViewFont) viewHeader.findViewById(R.id.textView_user_identifier);
            textViewName = (TextViewFont) viewHeader.findViewById(R.id.textView_user_name);
            textViewIdentifier.setText(String.valueOf(userIdentifier));
            textViewName.setText(String.valueOf(userName));


            if (!hasRequest)
                textViewRequests.setVisibility(View.GONE);
            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (screenWidth / 3) * 2);
            imageViewCover.setLayoutParams(params);

            DownloadCoverImage downloadCoverImage = new DownloadCoverImage(activity);
            downloadCoverImage.download(profilePictureId, imageViewCover);

            imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    else
                        drawerLayout.openDrawer(Gravity.RIGHT);
                }
            });

            imageViewSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivitySearchUser.class);
                    intent.putExtra(Params.SEARCH_KEY_WORD, "key");
                    activity.startActivity(intent);
                }
            });

            imageViewFollowingBusinesses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityFollowingBusinesses.class);
                    intent1.putExtra(Params.VISITED_USER_ID, visitedUserId);
                    activity.startActivity(intent1);
                }
            });
            imageViewFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityFriends.class);
                    intent1.putExtra(Params.VISITED_USER_ID, visitedUserId);
                    activity.startActivity(intent1);
                }
            });

           /* textViewRequests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityFriendRequests.class);
                    intent1.putExtra(Params.VISITED_USER_ID, visitedUserId);
                    activity.startActivity(intent1);
                }
            });*/
            imageViewReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityUserReviews.class);
                    intent1.putExtra(Params.VISITED_USER_ID, visitedUserId);
                    activity.startActivity(intent1);
                }
            });


            imageViewSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isThreeColumn) {
                        gridViewHeader.setNumColumns(1);
                        gridViewHeader.setAdapter(adapterPostShared);
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
            gridViewHeader.addFooterView(listFooterView);
        } else {
            listFooterView.setVisibility(View.GONE);
        }

        gridViewHeader.setBackgroundColor(Color.parseColor("#ffffff"));


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

        if (posts.size() == 0) {
            imageViewSwitch.setVisibility(View.GONE);
            imageViewCirecle.setVisibility(View.GONE);
        } else {
            imageViewSwitch.setVisibility(View.VISIBLE);
            imageViewCirecle.setVisibility(View.VISIBLE);
        }
        prepareGridThreeColumn(gridViewHeader);
        gridViewHeader.setAdapter(adapterPostGrid);
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
        new GetSharedPosts(activity, visitedUserId, posts.get(posts.size() - 1).id, activity.getResources().getInteger(R.integer.lazy_load_limitation), GridViewUser.this).execute();
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
                adapterPostShared.loadMore(posts);


        }
    }

    @Override
    public void getError(Integer errorCode) {
        new DialogMessage(activity, ServerAnswer.getError(activity, errorCode)).show();
    }

    public void hideLoader() {
        listFooterView.setVisibility(View.GONE);
    }
}
