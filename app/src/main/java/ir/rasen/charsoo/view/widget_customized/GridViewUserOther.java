package ir.rasen.charsoo.view.widget_customized;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.HFGridView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.FriendshipRelation;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.SearchItemPost;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.DownloadCoverImage;
import ir.rasen.charsoo.model.friend.RequestCancelFriendship;
import ir.rasen.charsoo.model.friend.RequestFriendship;
import ir.rasen.charsoo.model.post.GetSharedPosts;
import ir.rasen.charsoo.view.activity.ActivityUserFollowingBusinesses;
import ir.rasen.charsoo.view.activity.ActivityUserFriends;
import ir.rasen.charsoo.view.activity.ActivityUserReviews;
import ir.rasen.charsoo.view.adapter.AdapterPostGrid;
import ir.rasen.charsoo.view.adapter.AdapterPostShared;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.ICancelFriendship;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.buttons.ButtonFont;
import ir.rasen.charsoo.view.widget_customized.buttons.FloatButton;

/**
 * Created by android on 3/14/2015.
 */
public class GridViewUserOther implements IWebserviceResponse,ICancelFriendship {
    HFGridView gridViewHeader;
    AdapterPostGrid adapterPostGrid;
    AdapterPostShared adapterPostShared;
    public boolean isThreeColumn = true;

    FloatButton imageViewFriends, imageViewReviews, imageViewFollowingBusinesses;
    ImageView imageViewCover, imageViewBack;
    View switchGrid, switchList;
    //TextViewFont textViewFriends, textViewBusinesses, textViewReviews,
    TextViewFont textViewIdentifier,textViewName;
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

    Activity activity;

    public GridViewUserOther(final Activity context, final User displayedUser, com.handmark.pulltorefresh.library.HFGridView gViewHeader) {
        this.context = context;
        this.user = displayedUser;
        this.gridViewHeader = gViewHeader;
        this.iCancelFriendshipl = this;
        this.iWebserviceResponse = this;

    }

    public void InitialGridViewUser(ArrayList<Post> postList, boolean beThreeColumn) {

        this.posts = postList;
        searchItemPosts = new ArrayList<>();
        for (Post post : posts)
            searchItemPosts.add(new SearchItemPost(post.id, post.pictureId, post.picture));

        adapterPostGrid = new AdapterPostGrid(context, searchItemPosts,0, Post.GetPostType.SHARE);
        adapterPostShared = new AdapterPostShared(context, posts);

        if (!headerInitialized) {
            viewHeader = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_user_grid_header_another, null);

            viewHeader.findViewById(R.id.ll_action_bar).setOnClickListener(null);
            imageViewCover = (ImageView) viewHeader.findViewById(R.id.imageView_cover);
            imageViewBack = (ImageView) viewHeader.findViewById(R.id.imageView_back);

            imageViewFriends = (FloatButton) viewHeader.findViewById(R.id.imageView_friends);
            imageViewReviews = (FloatButton) viewHeader.findViewById(R.id.imageView_reviews);
            imageViewFollowingBusinesses = (FloatButton) viewHeader.findViewById(R.id.imageView_businesses);

            switchGrid = viewHeader.findViewById(R.id.btn_switch_grid);
            switchList = viewHeader.findViewById(R.id.btn_switch_list);
            //textViewBusinesses = (TextViewFont) viewHeader.findViewById(R.id.textView_businesses);
            //textViewFriends = (TextViewFont) viewHeader.findViewById(R.id.textView_friends);
            //textViewReviews = (TextViewFont) viewHeader.findViewById(R.id.textView_reviews);
            textViewIdentifier = (TextViewFont) viewHeader.findViewById(R.id.textView_user_identifier);
            textViewName = (TextViewFont) viewHeader.findViewById(R.id.textView_user_name);
            buttonFriendStatus = (ButtonFont) viewHeader.findViewById(R.id.btn_friend_satus);

            //TODO change user.name to user.userIdentifier


            textViewIdentifier.setText(user.userIdentifier);
            textViewName.setText(user.name);

            String text = buttonFriendStatus.getText().toString();
            switch (user.friendshipRelationStatus) {
                case FRIEND:
                    buttonFriendStatus.setBackgroundResource(R.drawable.selector_button_shape_green);
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


            text = buttonFriendStatus.getText().toString();
            buttonFriendStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (user.friendshipRelationStatus) {
                        case FRIEND:
                            //cancel friendship
                            new RequestCancelFriendship(context, LoginInfo.getUserId(context), user.id, iCancelFriendshipl).execute();
                            break;
                        case NOT_FRIEND:
                            //send friendship request
                            new RequestFriendship(context, LoginInfo.getUserId(context), user.id, iWebserviceResponse).execute();
                            break;
                        case REQUEST_REJECTED:
                            //send request again
                            new RequestFriendship(context, LoginInfo.getUserId(context), user.id, iWebserviceResponse).execute();
                            break;
                        case REQUEST_SENT:
                            //do nothing
                            break;
                    }
                }
            });

            DownloadCoverImage downloadCoverImage = new DownloadCoverImage(context);
            downloadCoverImage.download(user.profilePictureId, imageViewCover, Image_M.ImageType.USER);

            imageViewBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.finish();
                }
            });
            imageViewFollowingBusinesses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user.friendshipRelationStatus != FriendshipRelation.Status.FRIEND)
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
                    if (user.friendshipRelationStatus != FriendshipRelation.Status.FRIEND)
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
                    if (user.friendshipRelationStatus != FriendshipRelation.Status.FRIEND)
                        return;
                    if (user.permissions.reviews) {
                        Intent intent1 = new Intent(context, ActivityUserReviews.class);
                        intent1.putExtra(Params.VISITED_USER_ID, user.id);
                        context.startActivity(intent1);
                    } else
                        new DialogMessage(context, context.getString(R.string.err_permission_reviews)).show();
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
                    gridViewHeader.setNumColumns(1);
                    gridViewHeader.setAdapter(adapterPostShared);
                    //now it has one column
                    isThreeColumn = false;
                    // now it has three column
                    switchGrid.setBackgroundColor(activity.getResources().getColor(R.color.material_blue_light));
                    switchList.setBackgroundColor(activity.getResources().getColor(R.color.material_gray_light));
                }
            });

            gridViewHeader.addHeaderView(viewHeader);
            listFooterView = ((LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
            gridViewHeader.addFooterView(listFooterView);

            headerInitialized = true;
        }
        else {
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
                            && posts.size() > 0 && posts.size() % context.getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                        loadMoreData();
                    }
                }
            }
        });



        this.isThreeColumn = beThreeColumn;

        //if gridview is displaying the post or user has not any posts
        if (postList.size() != 0 || (headerInitialized && postList.size()==0))
            listFooterView.setVisibility(View.GONE);
        if(isThreeColumn) {
            gridViewHeader.setAdapter(adapterPostGrid);
            prepareGridThreeColumn(gridViewHeader);
        } else {
            gridViewHeader.setAdapter(adapterPostShared);
        }

    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
        new GetSharedPosts(context, user.id, posts.get(posts.size() - 1).id, context.getResources().getInteger(R.integer.lazy_load_limitation), GridViewUserOther.this).execute();
    }

    private void prepareGridThreeColumn(HFGridView gridViewHeader) {
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
            isLoadingMore=false;
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
