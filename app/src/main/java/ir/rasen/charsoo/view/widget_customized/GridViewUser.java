package ir.rasen.charsoo.view.widget_customized;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.Permission;
import ir.rasen.charsoo.controller.helper.SearchItemPost;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.post.GetSharedPosts;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.view.activity.ActivityUserProfile;
import ir.rasen.charsoo.view.activity.ActivitySearchUser;
import ir.rasen.charsoo.view.activity.ActivityUserFollowingBusinesses;
import ir.rasen.charsoo.view.activity.ActivityUserFriends;
import ir.rasen.charsoo.view.activity.ActivityUserReviews;
import ir.rasen.charsoo.view.adapter.AdapterPostGrid;
import ir.rasen.charsoo.view.adapter.AdapterPostShared;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.GridViewUserListener;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.buttons.FloatButton;

/**
 * Created by android on 3/14/2015.
 */
public class GridViewUser implements IWebserviceResponse, GridViewUserListener {
    public static final String TAG = "GridViewUser";

    com.handmark.pulltorefresh.library.HFGridView gridViewHeader;
    AdapterPostGrid adapterPostGrid;
    AdapterPostShared adapterPostShared;
    public boolean isThreeColumn = true;

    FloatButton imageViewFriends, imageViewReviews, imageViewFollowingBusinesses, imageViewEdit;
    View switchGrid, switchList;
    ImageView imageViewSearch, imageViewCover, imageViewHasRequest;
    //TextViewFont textViewFriends, textViewBusinesses, textViewReviews,
    TextViewFont textViewIdentifier, textViewName, textViewAboutMe;
    ArrayList<SearchItemPost> searchItemPosts;
    View viewHeader;
    Activity activity;
    int profilePictureId;
    View listFooterView;
    boolean isLoadingMore = false;
    ArrayList<Post> posts;
    int visitedUserId;
    public boolean hasHeader;
    boolean hasRequest;
    String userIdentifier, userName, aboutMe;
    Permission userPermissions;

    public GridViewUser(Activity activity, User user, int visitedUserId, com.handmark.pulltorefresh.library.HFGridView gridViewHeader) {
        this.activity = activity;
        this.profilePictureId = user.profilePictureId;
        this.gridViewHeader = gridViewHeader;
        this.visitedUserId = visitedUserId;
//        this.hasHeader = false;
        this.userIdentifier = user.userIdentifier;
        this.userName = user.name;
        this.userPermissions = user.permissions;
        this.hasRequest = (user.friendRequestNumber > 0) ? true : false;
        this.aboutMe = user.aboutMe;
    }

    public void hideRequestAnnouncement() {
        imageViewHasRequest.setVisibility(View.GONE);
    }

    public void initialProfilePicture(String userPictureString) {
        imageViewCover.setImageBitmap(Image_M.getBitmapFromString(userPictureString));
    }

    public void InitialGridViewUser(ArrayList<Post> postList, boolean beThreeColumn, boolean hasHeader) {
        this.isThreeColumn = beThreeColumn;
        this.hasHeader = hasHeader;
        this.posts = postList;

        ((ActivityMain) activity).initPopupWindowUser();

        searchItemPosts = new ArrayList<>();
        for (Post post : posts)
            searchItemPosts.add(new SearchItemPost(post.id, post.pictureId, post.picture));

        adapterPostGrid = new AdapterPostGrid(activity, searchItemPosts, 0, Post.GetPostType.SHARE);

        adapterPostShared = new AdapterPostShared(activity, posts, GridViewUser.this);


        if (!hasHeader) {
            viewHeader = (activity).getLayoutInflater().inflate(R.layout.layout_user_grid_header, null);

            viewHeader.findViewById(R.id.ll_action_bar).setOnClickListener(null);
            imageViewSearch = (ImageView) viewHeader.findViewById(R.id.imageView_search);
            switchGrid = viewHeader.findViewById(R.id.btn_switch_grid);
            switchList = viewHeader.findViewById(R.id.btn_switch_list);
            imageViewCover = (ImageView) viewHeader.findViewById(R.id.imageView_cover);

            imageViewFriends = (FloatButton) viewHeader.findViewById(R.id.imageView_friends);
            imageViewHasRequest = (ImageView) viewHeader.findViewById(R.id.imageView_has_request);
            imageViewReviews = (FloatButton) viewHeader.findViewById(R.id.imageView_reviews);
            imageViewFollowingBusinesses = (FloatButton) viewHeader.findViewById(R.id.imageView_businesses);
            imageViewEdit = (FloatButton) viewHeader.findViewById(R.id.imageView_edit);

            //textViewBusinesses = (TextViewFont) viewHeader.findViewById(R.id.textView_businesses);
            //textViewFriends = (TextViewFont) viewHeader.findViewById(R.id.textView_friends);
            //textViewReviews = (TextViewFont) viewHeader.findViewById(R.id.textView_reviews);

            textViewIdentifier = (TextViewFont) viewHeader.findViewById(R.id.textView_user_identifier);
            textViewName = (TextViewFont) viewHeader.findViewById(R.id.textView_user_name);
            textViewAboutMe = (TextViewFont) viewHeader.findViewById(R.id.textView_user_about_me);

            textViewIdentifier.setText(String.valueOf(userIdentifier));

            SpannableStringBuilder builder = new SpannableStringBuilder();

            String userId = userIdentifier + "@";
            SpannableString redSpannable = new SpannableString(userId);
            redSpannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.button_on_dark)), 0, userId.length(), 0);


            builder.append(userName);
            builder.append("(");
            builder.append(redSpannable);
            builder.append(")");

            textViewName.setText(builder, TextView.BufferType.SPANNABLE);

            if (!aboutMe.equals("null"))
                textViewAboutMe.setText(aboutMe);


            if (!hasRequest)
                imageViewHasRequest.setVisibility(View.GONE);

            SimpleLoader simpleLoader = new SimpleLoader(activity);
            simpleLoader.loadImage(profilePictureId, Image_M.LARGE, Image_M.ImageType.USER, imageViewCover);

            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityUserProfile.class);
                    activity.startActivity(intent);
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
                    Intent intent1 = new Intent(activity, ActivityUserFollowingBusinesses.class);
                    intent1.putExtra(Params.VISITED_USER_ID, visitedUserId);
                    activity.startActivity(intent1);
                }
            });
            imageViewFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(activity, ActivityUserFriends.class);
                    intent1.putExtra(Params.VISITED_USER_ID, visitedUserId);
                    intent1.putExtra(Params.HAS_REQUEST, hasRequest);
                    activity.startActivityForResult(intent1, 0);
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

            switchList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridViewHeader.setNumColumns(1);
                    gridViewHeader.setAdapter(adapterPostShared);
                    //now it has one column
                    isThreeColumn = false;
                    switchList.setBackgroundColor(activity.getResources().getColor(R.color.material_blue_light));
                    switchGrid.setBackgroundColor(activity.getResources().getColor(R.color.material_gray_light));
                }
            });
            switchGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepareGridThreeColumn(gridViewHeader);
                    gridViewHeader.setAdapter(adapterPostGrid);
                    // now it has three column
                    switchGrid.setBackgroundColor(activity.getResources().getColor(R.color.material_blue_light));
                    switchList.setBackgroundColor(activity.getResources().getColor(R.color.material_gray_light));
                }
            });

            gridViewHeader.addHeaderView(viewHeader);
            this.hasHeader = true;

            listFooterView = ((LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
            gridViewHeader.addFooterView(listFooterView);
        } else {
            if (listFooterView != null) {
                listFooterView.setVisibility(View.GONE);
            }
        }


        gridViewHeader.setBackgroundColor(Color.parseColor("#ffffff"));


        gridViewHeader.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem,
                    currentVisibleItemCount,
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
            //imageViewCirecle.setVisibility(View.GONE);
        } else {
            //if (imageViewCirecle != null) {
            //    imageViewCirecle.setVisibility(View.VISIBLE);
            //}
        }

        /*if(isThreeColumn) {
            prepareGridThreeColumn(gridViewHeader);
            gridViewHeader.setAdapter(adapterPostGrid);
        } else {
            gridViewHeader.setNumColumns(1);
            gridViewHeader.setAdapter(adapterPostShared);
        }*/
        if (isThreeColumn) {
//            gridViewHeader.setNumColumns(3);
//            gridViewHeader.setVerticalSpacing(3);
//            gridViewHeader.setHorizontalSpacing(9);
            //        gridViewHeader.setAdapter(adapterPostShared);
            //now it has one column
            //        isThreeColumn = false;
            //        switchList.setBackgroundColor(activity.getResources().getColor(R.color.material_blue_light));
            //        switchGrid.setBackgroundColor(activity.getResources().getColor(R.color.material_gray_light));
//            gridViewHeader.setNumColumns(3);
//            gridViewHeader.setVerticalSpacing(3);
//            gridViewHeader.setHorizontalSpacing(9);
            prepareGridThreeColumn(gridViewHeader);
            gridViewHeader.setAdapter(adapterPostGrid);
            // now it has three column
            switchGrid.setBackgroundColor(activity.getResources().getColor(R.color.material_blue_light));
            switchList.setBackgroundColor(activity.getResources().getColor(R.color.material_gray_light));
        } else {
            gridViewHeader.setNumColumns(1);
            gridViewHeader.setAdapter(adapterPostShared);
            switchList.setBackgroundColor(activity.getResources().getColor(R.color.material_blue_light));
            switchGrid.setBackgroundColor(activity.getResources().getColor(R.color.material_gray_light));
        }
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
        new GetSharedPosts(activity, visitedUserId, posts.get(posts.size() - 1).id, activity.getResources().getInteger(R.integer.lazy_load_limitation), GridViewUser.this).execute();
    }

    private void prepareGridThreeColumn(com.handmark.pulltorefresh.library.HFGridView gridViewHeader) {
        gridViewHeader.setNumColumns(3);
        gridViewHeader.setVerticalSpacing(3);
        gridViewHeader.setHorizontalSpacing(9);
        gridViewHeader.setViewWidthIfItsZero(activity.getWindowManager().getDefaultDisplay().getWidth());
        isThreeColumn = true;
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
            isLoadingMore = false;


        }
    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {
        new DialogMessage(activity, ServerAnswer.getError(activity, errorCode, callerStringID + ">" + TAG)).show();
    }

    public void hideLoader() {
        listFooterView.setVisibility(View.GONE);
    }

    @Override
    public void notifyOnShareCanceled(int postID_int) {
        if (adapterPostGrid != null)
            adapterPostGrid.removePostByIntID(postID_int);
    }


}
