package ir.rasen.charsoo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ir.rasen.charsoo.ActivityPostAddEdit;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.dialog.DialogDeletePostConfirmation;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.PersianDate;
import ir.rasen.charsoo.helper.TextProcessor;
import ir.rasen.charsoo.interfaces.IDeletePost;
import ir.rasen.charsoo.ui.TextViewFont;
import ir.rasen.charsoo.webservices.DownloadImages;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterPostBusiness extends BaseAdapter implements IDeletePost {

    private ArrayList<Post> items;
    private Context context;
    DownloadImages downloadImages;
    private int screedWidth;
    private IDeletePost iDeletePost;
    private boolean isUserOwner;

    public AdapterPostBusiness(Context context, ArrayList<Post> items,boolean isUserOwner) {
        this.context = context;
        this.items = items;
        downloadImages = new DownloadImages(context);
        screedWidth = context.getResources().getDisplayMetrics().widthPixels;
        iDeletePost = this;
        this.isUserOwner = isUserOwner;
    }

    public void loadMore(ArrayList<Post> newItem){
        this.items.addAll(newItem);
        notifyDataSetChanged();
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

        final Holder holder;

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_post_adapter_list_business_post, viewGroup, false);
            holder.llAnnouncementSection = (LinearLayout) view.findViewById(R.id.ll_announcement);
            holder.imageViewProfileImage = (ImageView) view.findViewById(R.id.imageView_profile_picture);
            holder.textViewBusinessIdentifier = (TextViewFont) view.findViewById(R.id.textView_business_identifier);
            holder.textViewDate = (TextViewFont) view.findViewById(R.id.textView_date);

            holder.imageViewPost = (ImageView) view.findViewById(R.id.imageView_post);
            //to display post picture as square
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screedWidth);
            holder.imageViewPost.setLayoutParams(params);

            holder.imageViewEdit = (ImageView) view.findViewById(R.id.imageView_edit);
            holder.imageViewComment = (ImageView) view.findViewById(R.id.imageView_comment);
            holder.imageViewDelete = (ImageView) view.findViewById(R.id.imageView_delete);
            holder.textViewLikeNumber = (TextViewFont) view.findViewById(R.id.textView_like_number);
            holder.textViewCommentNumber = (TextViewFont) view.findViewById(R.id.textView_comment_number);
            holder.textViewShareNumber = (TextViewFont) view.findViewById(R.id.textView_share_number);
            holder.textViewDescription = (TextViewFont) view.findViewById(R.id.textView_description);
            holder.textViewComment1 = (TextViewFont) view.findViewById(R.id.textView_comment1);
            holder.textViewComment1UserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment1_user_identifier);
            holder.textViewComment2 = (TextViewFont) view.findViewById(R.id.textView_comment2);
            holder.textViewComment2UserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment2_user_identifier);
            holder.textViewComment3 = (TextViewFont) view.findViewById(R.id.textView_comment3);
            holder.textViewComment3UserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment3_user_identifier);
            holder.textViewTitle = (TextViewFont) view.findViewById(R.id.textView_title);
            holder.textViewPrice = (TextViewFont) view.findViewById(R.id.textView_price);
            view.setTag(holder);

        } else
            holder = (Holder) view.getTag();

        holder.llAnnouncementSection.setVisibility(View.GONE);

        //all post's types have these three fields
        downloadImages.download(items.get(position).businessProfilePictureId, Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.imageViewProfileImage,true);
        holder.textViewDate.setText(PersianDate.getCreationDate(context, items.get(position).creationDate));
        holder.textViewBusinessIdentifier.setText(items.get(position).businessUserName);

        if (items.get(position).picture!= null && !items.get(position).picture.equals("") && items.get(position).pictureId == 0 )
            holder.imageViewPost.setImageBitmap(Image_M.getBitmapFromString(items.get(position).picture));
        else
        downloadImages.download(items.get(position).pictureId, Image_M.LARGE, Image_M.ImageType.POST, holder.imageViewPost,false);
        holder.textViewLikeNumber.setText(String.valueOf(items.get(position).likeNumber));
        holder.textViewCommentNumber.setText(String.valueOf(items.get(position).commentNumber));
        holder.textViewShareNumber.setText(String.valueOf(items.get(position).shareNumber));
        holder.textViewDescription.setText(items.get(position).description);
        holder.textViewTitle.setText(TextProcessor.removeHashtags(items.get(position).title));
        holder.textViewPrice.setText(items.get(position).price);

        ArrayList<Comment> lastThreeComments = items.get(position).lastThreeComments;
        if (lastThreeComments.size() > 0) {
            holder.textViewComment1UserIdentifier.setText(items.get(position).lastThreeComments.get(0).username);
            holder.textViewComment1.setText(items.get(position).lastThreeComments.get(0).text);
            holder.textViewComment1UserIdentifier.setVisibility(View.VISIBLE);
            holder.textViewComment1.setVisibility(View.VISIBLE);
        }
        if (lastThreeComments.size() > 1) {
            holder.textViewComment2UserIdentifier.setText(items.get(position).lastThreeComments.get(1).username);
            holder.textViewComment2.setText(items.get(position).lastThreeComments.get(1).text);
            holder.textViewComment2UserIdentifier.setVisibility(View.VISIBLE);
            holder.textViewComment2.setVisibility(View.VISIBLE);
        }
        if (lastThreeComments.size() > 2) {
            holder.textViewComment3UserIdentifier.setText(items.get(position).lastThreeComments.get(2).username);
            holder.textViewComment3.setText(items.get(position).lastThreeComments.get(2).text);
            holder.textViewComment3UserIdentifier.setVisibility(View.VISIBLE);
            holder.textViewComment3.setVisibility(View.VISIBLE);
        }

        holder.textViewComment1UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context,items.get(position).lastThreeComments.get(0).userID);

            }
        });
        holder.textViewComment2UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context,items.get(position).lastThreeComments.get(1).userID);
            }
        });
        holder.textViewComment3UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context,items.get(position).lastThreeComments.get(2).userID);
            }
        });

        holder.imageViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment.openCommentActivity(context,isUserOwner,items.get(position).id,items.get(position).businessID);
            }
        });
        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityPostAddEdit.class);
                intent.putExtra(Params.BUSINESS_ID,items.get(position).businessID);
                intent.putExtra(Params.POST_ID,items.get(position).id);
                context.startActivity(intent);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDeletePostConfirmation d= new DialogDeletePostConfirmation(context,items.get(position).businessID,items.get(position).id,iDeletePost);
                d.show();
            }
        });


        return view;
    }


    @Override
    public void notifyDeletePost(int postId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id == postId)
                items.remove(i);
        }
        notifyDataSetChanged();
    }

    private class Holder {

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
        ImageView imageViewEdit;
        ImageView imageViewComment;
        ImageView imageViewDelete;
        LinearLayout llAnnouncementSection;
    }
}
