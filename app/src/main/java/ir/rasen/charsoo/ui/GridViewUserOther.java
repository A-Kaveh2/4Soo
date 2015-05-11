package ir.rasen.charsoo.ui;

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

import ir.rasen.charsoo.ActivityUserFollowingBusinesses;
import ir.rasen.charsoo.ActivityUserFriends;
import ir.rasen.charsoo.ActivityProfilePicture;
import ir.rasen.charsoo.ActivityUserReviews;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.adapters.AdapterPostGrid;
import ir.rasen.charsoo.adapters.AdapterPostShared;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.FriendshipRelation;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.SearchItemPost;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.interfaces.ICancelFriendship;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.DownloadCoverImage;
import ir.rasen.charsoo.webservices.friend.RequestCancelFriendship;
import ir.rasen.charsoo.webservices.friend.RequestFriendship;
import ir.rasen.charsoo.webservices.post.GetSharedPosts;

/**
 * Created by android on 3/14/2015.
 */
public class GridViewUserOther implements IWebserviceResponse,ICancelFriendship {
    GridViewHeader gridViewHeader;
    AdapterPostGrid adapterPostGrid;
    AdapterPostShared adapterPostShared;
    private boolean isThreeColumn = true;

    ImageView imageViewSwitch, imageViewCover,imageViewCirecle, imageViewFriends, imageViewReviews, imageViewFollowingBusinesses;
    TextViewFont textViewFriends, textViewBusinesses, textViewReviews,textViewIdentifier,textViewName;
    ButtonFont buttonFriendStatus;
    ArrayList<SearchItemPost> searchItemPosts;
    View viewHeader;
    Activity context;
    View listFooterView;
    boolean isLoadingMore = false;
    ArrayList<Post> posts;
    User user;
    boolean headerInitialized = false;
    ICancelFriendship iCancelFriendshipl;
    IWebserviceResponse iWebserviceResponse;


    public GridViewUserOther(final Activity context, final User displayedUser, GridViewHeader gViewHeader) {
        this.context = context;
        this.user = displayedUser;
        this.gridViewHeader = gViewHeader;
        this.iCancelFriendshipl = this;
        this.iWebserviceResponse = this;
        viewHeader = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_user_grid_header_another, null);
        imageViewSwitch = (ImageView) viewHeader.findViewById(R.id.imageView_switch);
        imageViewCirecle = (ImageView) viewHeader.findViewById(R.id.imageView_cirecle);
        imageViewCover = (ImageView) viewHeader.findViewById(R.id.imageView_cover);

        imageViewFriends = (ImageView) viewHeader.findViewById(R.id.imageView_friends);
        imageViewReviews = (ImageView) viewHeader.findViewById(R.id.imageView_reviews);
        imageViewFollowingBusinesses = (ImageView) viewHeader.findViewById(R.id.imageView_businesses);

        textViewBusinesses = (TextViewFont) viewHeader.findViewById(R.id.textView_businesses);
        textViewFriends = (TextViewFont) viewHeader.findViewById(R.id.textView_friends);
        textViewReviews = (TextViewFont) viewHeader.findViewById(R.id.textView_reviews);
        textViewIdentifier = (TextViewFont) viewHeader.findViewById(R.id.textView_user_identifier);
        textViewName = (TextViewFont) viewHeader.findViewById(R.id.textView_user_name);
        buttonFriendStatus = (ButtonFont) viewHeader.findViewById(R.id.btn_friend_satus);

        imageViewSwitch.setVisibility(View.GONE);
        imageViewCirecle.setVisibility(View.GONE);

        //TODO change user.name to user.userIdentifier



        textViewIdentifier.setText(user.userIdentifier);
        textViewName.setText(user.name);

        switch (user.friendshipRelationStatus) {
            case FRIEND:
                buttonFriendStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
                buttonFriendStatus.setText(context.getString(R.string.friend));
                break;
            case NOT_FRIEND:
                buttonFriendStatus.setBackgroundResource(R.drawable.selector_button_shape_blue);
                buttonFriendStatus.setText(context.getString(R.string.friendy));
                break;
            case REQUEST_REJECTED:
                buttonFriendStatus.setBackgroundResource(R.drawable.selector_button_shape_red);
                buttonFriendStatus.setText(context.getString(R.string.friendy));
                break;
            case REQUEST_SENT:
                buttonFriendStatus.setEnabled(false);
                buttonFriendStatus.setBackgroundResource(R.drawable.shape_button_gray);
                buttonFriendStatus.setText(context.getString(R.string.wating_for_comfirm));
                break;
        }
        buttonFriendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (user.friendshipRelationStatus) {
                    case FRIEND:
                        //cancel friendship
                        new RequestCancelFriendship(context, LoginInfo.getUserId(context),user.id,iCancelFriendshipl).execute();
                        break;
                    case NOT_FRIEND:
                        //send friendship request
                        new RequestFriendship(context,LoginInfo.getUserId(context),user.id,iWebserviceResponse).execute();
                        break;
                    case REQUEST_REJECTED:
                        //send request again
                        new RequestFriendship(context,LoginInfo.getUserId(context),user.id,iWebserviceResponse).execute();
                        break;
                    case REQUEST_SENT:
                        //do nothing
                        break;
                }
            }
        });

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (screenWidth / 3) * 2);
        imageViewCover.setLayoutParams(params);
        DownloadCoverImage downloadCoverImage = new DownloadCoverImage(context);
        downloadCoverImage.download(user.profilePictureId, imageViewCover, Image_M.ImageType.USER);

        imageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityProfilePicture.class);
                intent.putExtra(Params.USER_IDENTIFIER, displayedUser.userIdentifier);
                intent.putExtra(Params.PROFILE_PICTURE_ID, displayedUser.profilePictureId);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.slide_enter_down,
                        R.anim.slide_exit_down);
            }
        });

        imageViewFollowingBusinesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.friendshipRelationStatus != FriendshipRelation.Status.FRIEND)
                    return;
                if (user.permissions.followedBusiness) {
                    Intent intent1 = new Intent(context, ActivityUserFollowingBusinesses.class);
                    intent1.putExtra(Params.VISITED_USER_ID, user.id);
                    context.startActivity(intent1);
                } else
                    new DialogMessage(context, context.getString(R.string.err_permission_businesses)).show();
            }
        });
        imageViewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.friendshipRelationStatus != FriendshipRelation.Status.FRIEND)
                    return;
                if (user.permissions.friends) {
                    Intent intent1 = new Intent(context, ActivityUserFriends.class);
                    intent1.putExtra(Params.VISITED_USER_ID, user.id);
                    context.startActivity(intent1);
                } else
                    new DialogMessage(context, context.getString(R.string.err_permission_friends)).show();
            }
        });

        imageViewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.friendshipRelationStatus != FriendshipRelation.Status.FRIEND)
                   return;
                if (user.permissions.reviews) {
                    Intent intent1 = new Intent(context, ActivityUserReviews.class);
                    intent1.putExtra(Params.VISITED_USER_ID, user.id);
                    context.startActivity(intent1);
                } else
                    new DialogMessage(context, context.getString(R.string.err_permission_reviews)).show();
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
        listFooterView = ((LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        gridViewHeader.addFooterView(listFooterView);


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
                            && posts.size() > 0 && posts.size() % context.getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });
        headerInitialized = true;
    }

    public void InitialGridViewUser(ArrayList<Post> postList) {

        if (postList.size() == 0) {
            imageViewSwitch.setVisibility(View.GONE);
            imageViewCirecle.setVisibility(View.GONE);
        }
        else {
            imageViewSwitch.setVisibility(View.VISIBLE);
            imageViewCirecle.setVisibility(View.VISIBLE);
        }

        //if gridview is displaying the post or user has not any posts
        if (postList.size() != 0 || (headerInitialized && postList.size()==0))
            listFooterView.setVisibility(View.GONE);
        this.posts = postList;
        searchItemPosts = new ArrayList<>();
        for (Post post : posts)
            searchItemPosts.add(new SearchItemPost(post.id, post.pictureId, post.picture));

        adapterPostGrid = new AdapterPostGrid(context, searchItemPosts,0, Post.GetPostType.SHARE);
        adapterPostShared = new AdapterPostShared(context, posts);

        prepareGridThreeColumn(gridViewHeader);
        gridViewHeader.setAdapter(adapterPostGrid);


    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
        new GetSharedPosts(context, user.id, posts.get(posts.size() - 1).id, context.getResources().getInteger(R.integer.lazy_load_limitation), GridViewUserOther.this).execute();
    }

    private void prepareGridThreeColumn(GridViewHeader gridViewHeader) {
        gridViewHeader.setNumColumns(3);
        gridViewHeader.setVerticalSpacing(3);
        gridViewHeader.setHorizontalSpacing(9);
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof ResultStatus) {

            //RequestFriendship's result
            buttonFriendStatus.setEnabled(false);
            buttonFriendStatus.setBackgroundResource(R.drawable.shape_button_gray);
            buttonFriendStatus.setText(context.getString(R.string.wating_for_comfirm));
            user.friendshipRelationStatus = FriendshipRelation.Status.REQUEST_SENT;
        }
        else if (result instanceof ArrayList) {
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
        new DialogMessage(context, ServerAnswer.getError(context, errorCode)).show();
    }

    @Override
    public void notifyDeleteFriend(int userId) {
        buttonFriendStatus.setBackgroundResource(R.drawable.selector_button_shape_blue);
        buttonFriendStatus.setEnabled(true);
        buttonFriendStatus.setCompoundDrawablesWithIntrinsicBounds(null, null,null, null);
        buttonFriendStatus.setText(context.getString(R.string.friendy));
        user.friendshipRelationStatus = FriendshipRelation.Status.NOT_FRIEND;
    }
}
