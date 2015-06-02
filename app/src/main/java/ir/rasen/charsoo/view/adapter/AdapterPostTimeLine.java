package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.MyGestureDetector;
import ir.rasen.charsoo.controller.helper.PersianDate;
import ir.rasen.charsoo.controller.helper.TextProcessor;
import ir.rasen.charsoo.controller.helper.downloadImage.ImageDownloader;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.DownloadImages;
import ir.rasen.charsoo.model.post.Like;
import ir.rasen.charsoo.model.post.Share;
import ir.rasen.charsoo.model.post.Unlike;
import ir.rasen.charsoo.view.dialog.DialogCancelShareConfirmationTimeLine;
import ir.rasen.charsoo.view.dialog.PopupReportPostAdapter;
import ir.rasen.charsoo.view.interface_m.IReportPost;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.view.widget_customized.buttons.ButtonFont;


/**
 * Created by android on 3/7/2015.
 */
public class AdapterPostTimeLine extends BaseAdapter implements IReportPost {

    private ArrayList<Post> items;
    private Context context;
    DownloadImages downloadImages;
    private int screedWidth;
    private IReportPost iReportPost;
    GridView gridView;


    public AdapterPostTimeLine(Context context, ArrayList<Post> items) {
        this.context = context;
        this.items = items;
        downloadImages = new DownloadImages(context);
        screedWidth = context.getResources().getDisplayMetrics().widthPixels;
        iReportPost = this;

    }

    public void loadMore(ArrayList<Post> newItem) {
        //this.items.addAll(newItem);
        notifyDataSetChanged();
    }

    public void updatePostLastThreeComments(Post post) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id == post.id) {
                items.get(i).lastThreeComments = post.lastThreeComments;
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        /*if (gridView == null) {
            gridView = (GridView) viewGroup;
            gridView.setSelector(new ColorDrawable(0x00ffffff));
        }*/
        final Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_post_adapter_list, viewGroup, false);

            holder.imageViewProfileImage = (ImageView) view.findViewById(R.id.imageView_profile_picture);
            holder.imageViewProfileImageShared = (ImageView) view.findViewById(R.id.imageView_profile_picture_shared);
            holder.textViewBusinessIdentifier = (TextViewFont) view.findViewById(R.id.textView_business_identifier);
            holder.textViewDate = (TextViewFont) view.findViewById(R.id.textView_date);

            //complete section
            holder.llCompleteSection = (LinearLayout) view.findViewById(R.id.ll_complete_post_section);
            holder.imageViewPost = (ImageView) view.findViewById(R.id.imageView_post);
            holder.imageViewPostLike = (ImageView) view.findViewById(R.id.imageView_post_like);

            holder.imageViewLike = (ImageView) view.findViewById(R.id.imageView_like);
            holder.imageViewComment = (ImageView) view.findViewById(R.id.imageView_comment);
            holder.imageViewShare = (ImageView) view.findViewById(R.id.imageView_share);
            holder.imageViewMore = (ImageView) view.findViewById(R.id.imageView_more);
            holder.textViewLikeNumber = (TextViewFont) view.findViewById(R.id.textView_like_number);
            holder.textViewCommentNumber = (TextViewFont) view.findViewById(R.id.textView_comment_number);
            holder.textViewShareNumber = (TextViewFont) view.findViewById(R.id.textView_share_number);
            holder.textViewDescription = (TextViewFont) view.findViewById(R.id.textView_description);
            holder.textViewComment1 = (TextViewFont) view.findViewById(R.id.textView_comment1);
            holder.textViewComment1UserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment1_user_identifier);
            holder.textViewComment2 = (TextViewFont) view.findViewById(R.id.textView_comment2);
            holder.textViewComment2UserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment2_user_identifier);
            holder.textViewComment3 = (TextViewFont) view.findViewById(R.id.textView_comment3);
            holder.textViewTitle = (TextViewFont) view.findViewById(R.id.textView_title);
            holder.textViewPrice = (TextViewFont) view.findViewById(R.id.textView_price);
            holder.textViewComment3UserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment3_user_identifier);
            holder.textViewCode = (TextViewFont) view.findViewById(R.id.textView_code);
            holder.llPriceSection = (LinearLayout) view.findViewById(R.id.ll_price_section);
            holder.llCodeSection = (LinearLayout) view.findViewById(R.id.ll_code_section);

            //announcement parts
            holder.llAnnouncementSection = (LinearLayout) view.findViewById(R.id.ll_announcement);
            holder.textViewAnnouncementUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_announcement_userIdentifier_title);
            holder.textViewAnnouncementBusinessStaticPart = (TextViewFont) view.findViewById(R.id.textView_announcement_business_static_part);
            holder.textViewAnnouncementBusinessIdentifier = (TextViewFont) view.findViewById(R.id.textView_announcement_business_identifier);
            holder.btnView = (ButtonFont) view.findViewById(R.id.btn_announcement_view);

            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();


        //all post's types have these three fields
        //downloadImages.download(items.get(position).businessProfilePictureId, Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.imageViewProfileImage, true);

        holder.textViewDate.setText(PersianDate.getCreationDate(context, items.get(position).creationDate));
        holder.textViewBusinessIdentifier.setText(items.get(position).businessUserName);
        holder.textViewBusinessIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Business.goBusinessHomeInfoPage(context, items.get(position).businessID);

            }
        });
        holder.imageViewProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Business.goBusinessHomeInfoPage(context, items.get(position).businessID);
            }
        });

        if (items.get(position).type == Post.Type.Complete) {
            //this post is not an announcement
            ImageDownloader imageDownloader = new ImageDownloader(context, items.get(position).businessProfilePictureId, Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.imageViewProfileImage);
            imageDownloader.DownloadImage();

            holder.llAnnouncementSection.setVisibility(View.GONE);
            holder.llCompleteSection.setVisibility(View.VISIBLE);

            //downloadImages.download(items.get(position).pictureId, Image_M.LARGE, Image_M.ImageType.POST, holder.imageViewPost, false);
            imageDownloader = new ImageDownloader(context, items.get(position).pictureId, Image_M.LARGE, Image_M.ImageType.POST, holder.imageViewPost);
            imageDownloader.DownloadImage();


            holder.textViewLikeNumber.setText(String.valueOf(items.get(position).likeNumber));
            holder.textViewCommentNumber.setText(String.valueOf(items.get(position).commentNumber));
            holder.textViewShareNumber.setText(String.valueOf(items.get(position).shareNumber));
            holder.textViewDescription.setText(TextProcessor.removeHashtags(items.get(position).description));
            holder.textViewTitle.setText(TextProcessor.removeHashtags(items.get(position).title));
            if (items.get(position).price != null && !items.get(position).price.equals("") && !items.get(position).price.equals("null")) {
                holder.textViewPrice.setText(items.get(position).price);
                holder.llPriceSection.setVisibility(View.VISIBLE);
            } else
                holder.llPriceSection.setVisibility(View.GONE);
            if (items.get(position).code != null && !items.get(position).code.equals("") && !items.get(position).code.equals("null")) {
                holder.textViewCode.setText(items.get(position).code);
                holder.llCodeSection.setVisibility(View.VISIBLE);
            } else
                holder.llCodeSection.setVisibility(View.GONE);


            ArrayList<Comment> lastThreeComments = items.get(position).lastThreeComments;
            if (lastThreeComments.size() == 0) {
                holder.textViewComment1UserIdentifier.setVisibility(View.GONE);
                holder.textViewComment1.setVisibility(View.GONE);

                holder.textViewComment2UserIdentifier.setVisibility(View.GONE);
                holder.textViewComment2.setVisibility(View.GONE);

                holder.textViewComment3UserIdentifier.setVisibility(View.GONE);
                holder.textViewComment3.setVisibility(View.GONE);

                view.findViewById(R.id.ll_comments).setVisibility(View.GONE);
            } else
                view.findViewById(R.id.ll_comments).setVisibility(View.VISIBLE);

            if (lastThreeComments.size() == 1) {
                holder.textViewComment1UserIdentifier.setText(items.get(position).lastThreeComments.get(0).username);
                holder.textViewComment1.setText(items.get(position).lastThreeComments.get(0).text);
                holder.textViewComment1UserIdentifier.setVisibility(View.VISIBLE);
                holder.textViewComment1.setVisibility(View.VISIBLE);

                holder.textViewComment2UserIdentifier.setVisibility(View.GONE);
                holder.textViewComment2.setVisibility(View.GONE);

                holder.textViewComment3UserIdentifier.setVisibility(View.GONE);
                holder.textViewComment3.setVisibility(View.GONE);
            }
            if (lastThreeComments.size() == 2) {
                holder.textViewComment1UserIdentifier.setText(items.get(position).lastThreeComments.get(0).username);
                holder.textViewComment1.setText(items.get(position).lastThreeComments.get(0).text);
                holder.textViewComment1UserIdentifier.setVisibility(View.VISIBLE);
                holder.textViewComment1.setVisibility(View.VISIBLE);

                holder.textViewComment2UserIdentifier.setText(items.get(position).lastThreeComments.get(1).username);
                holder.textViewComment2.setText(items.get(position).lastThreeComments.get(1).text);
                holder.textViewComment2UserIdentifier.setVisibility(View.VISIBLE);
                holder.textViewComment2.setVisibility(View.VISIBLE);

                holder.textViewComment3UserIdentifier.setVisibility(View.GONE);
                holder.textViewComment3.setVisibility(View.GONE);
            }
            if (lastThreeComments.size() == 3) {
                holder.textViewComment1UserIdentifier.setText(items.get(position).lastThreeComments.get(0).username);
                holder.textViewComment1.setText(items.get(position).lastThreeComments.get(0).text);
                holder.textViewComment1UserIdentifier.setVisibility(View.VISIBLE);
                holder.textViewComment1.setVisibility(View.VISIBLE);

                holder.textViewComment2UserIdentifier.setText(items.get(position).lastThreeComments.get(1).username);
                holder.textViewComment2.setText(items.get(position).lastThreeComments.get(1).text);
                holder.textViewComment2UserIdentifier.setVisibility(View.VISIBLE);
                holder.textViewComment2.setVisibility(View.VISIBLE);

                holder.textViewComment3UserIdentifier.setText(items.get(position).lastThreeComments.get(2).username);
                holder.textViewComment3.setText(items.get(position).lastThreeComments.get(2).text);
                holder.textViewComment3UserIdentifier.setVisibility(View.VISIBLE);
                holder.textViewComment3.setVisibility(View.VISIBLE);
            }

            if (items.get(position).isLiked)
                holder.imageViewLike.setImageResource(R.drawable.ic_favorite_blue);
            else
                holder.imageViewLike.setImageResource(R.drawable.ic_like);
            if (items.get(position).isShared)
                holder.imageViewShare.setImageResource(R.drawable.ic_reply_blue);
            else
                holder.imageViewShare.setImageResource(R.drawable.ic_share);
            if (items.get(position).isReported)
                holder.imageViewMore.setVisibility(View.GONE);
            else
                holder.imageViewMore.setVisibility(View.VISIBLE);

            holder.textViewComment1UserIdentifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User.goUserHomeInfoPage(context, items.get(position).lastThreeComments.get(0).userID);

                }
            });
            holder.textViewComment2UserIdentifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User.goUserHomeInfoPage(context, items.get(position).lastThreeComments.get(1).userID);

                }
            });
            holder.textViewComment3UserIdentifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User.goUserHomeInfoPage(context, items.get(position).lastThreeComments.get(2).userID);

                }
            });

            holder.imageViewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Comment.openCommentActivity(context, false, items.get(position).id, items.get(position).businessID);
                }
            });
            holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (items.get(position).isLiked) {
                        //unlike the post

                        new Unlike(context, LoginInfo.getUserId(context), items.get(position).id).execute();
                        items.get(position).isLiked = false;
                        holder.imageViewLike.setImageResource(R.drawable.ic_like);
                    } else {
                        //like the post

                        new Like(context, LoginInfo.getUserId(context), items.get(position).id).execute();
                        items.get(position).isLiked = true;
                        holder.imageViewLike.setImageResource(R.drawable.ic_favorite_blue);
                    }

                }
            });


            holder.gestureDetector = new GestureDetector(context, new MyGestureDetector(context, items.get(position).id, items.get(position).isLiked, holder.imageViewLike, holder.imageViewPostLike));

            holder.imageViewPost.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.gestureDetector.onTouchEvent(event);
                }
            });

            holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (items.get(position).isShared) {
                        //cancel share the post
                        new DialogCancelShareConfirmationTimeLine(context,items.get(position),holder.imageViewShare).show();
                        /*new CancelShare(context, LoginInfo.getUserId(context), items.get(position).id, null).execute();

                        items.get(position).isShared = false;
                        holder.imageViewShare.setImageResource(R.drawable.ic_reply_grey);*/
                    } else {
                        //share the post

                        new Share(context, LoginInfo.getUserId(context), items.get(position).id, null).execute();

                        items.get(position).isShared = true;
                        holder.imageViewShare.setImageResource(R.drawable.ic_reply_blue);
                    }
                }
            });

            holder.imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupReportPostAdapter p = new PopupReportPostAdapter(context, LoginInfo.getUserId(context), items.get(position).id, position, holder.imageViewMore, iReportPost);
                    p.show();
                }
            });

        } else {
            //this post is an announcement
            holder.llCompleteSection.setVisibility(View.GONE);
            holder.llAnnouncementSection.setVisibility(View.VISIBLE);

            ImageDownloader imageDownloader = new ImageDownloader(context, items.get(position).businessProfilePictureId, Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.imageViewProfileImageShared);
            imageDownloader.DownloadImage();

            holder.textViewAnnouncementUserIdentifier.setText(items.get(position).userName);
            holder.textViewAnnouncementUserIdentifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User.goUserHomeInfoPage(context, items.get(position).userId);
                }
            });

            holder.textViewAnnouncementBusinessIdentifier.setText(items.get(position).businessUserName);
            holder.textViewAnnouncementBusinessIdentifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Business.goBusinessHomeInfoPage(context, items.get(position).businessID);
                }
            });

            if (items.get(position).type == Post.Type.Follow)
                holder.textViewAnnouncementBusinessStaticPart.setText(context.getResources().getString(R.string.follow_announcement));
            else if (items.get(position).type == Post.Type.Review)
                holder.textViewAnnouncementBusinessStaticPart.setText(context.getResources().getString(R.string.review_announcement));

            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Business.goBusinessHomeInfoPage(context, items.get(position).businessID);
                }
            });

        }


        return view;
    }

    @Override
    public void notifyReportPost(int position, ImageView imageViewMore) {
        items.get(position).isReported = true;
        imageViewMore.setVisibility(View.GONE);
    }


    private class Holder {

        ImageView imageViewProfileImage;
        ImageView imageViewProfileImageShared;
        TextViewFont textViewBusinessIdentifier;
        TextViewFont textViewDate;
        ButtonFont btnView;

        //complete post section
        LinearLayout llCompleteSection;
        ImageView imageViewPost;
        ImageView imageViewPostLike;
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
        TextViewFont textViewCode;
        LinearLayout llPriceSection, llCodeSection;

        //announcement parts
        LinearLayout llAnnouncementSection;
        TextViewFont textViewAnnouncementUserIdentifier;
        TextViewFont textViewAnnouncementBusinessIdentifier;
        TextViewFont textViewAnnouncementBusinessStaticPart;

        GestureDetector gestureDetector;

    }

}
