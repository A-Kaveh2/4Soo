package ir.rasen.charsoo.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ir.rasen.charsoo.view.activity.ActivityPostAddEdit;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.dialog.DialogDeletePostConfirmation;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PersianDate;
import ir.rasen.charsoo.controller.helper.TextProcessor;
import ir.rasen.charsoo.view.interface_m.IDeletePost;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.model.DownloadImages;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterPostBusiness extends BaseAdapter {

    private ArrayList<Post> items;
    private Activity activity;
    DownloadImages downloadImages;
    private int screedWidth;
    private IDeletePost iDeletePost;
    private boolean isUserOwner;

    public AdapterPostBusiness(Activity activity, ArrayList<Post> items, boolean isUserOwner, IDeletePost iDeletePost) {
        this.activity = activity;
        this.items = items;
        downloadImages = new DownloadImages(activity);
        screedWidth = this.activity.getResources().getDisplayMetrics().widthPixels;
        this.iDeletePost = iDeletePost;
        this.isUserOwner = isUserOwner;
    }

    public void loadMore(ArrayList<Post> newItem) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_post_adapter_list_business_post, viewGroup, false);
            holder.llAnnouncementSection = (LinearLayout) view.findViewById(R.id.ll_announcement);
            holder.imageViewProfileImage = (ImageView) view.findViewById(R.id.imageView_profile_picture);
            holder.textViewBusinessIdentifier = (TextViewFont) view.findViewById(R.id.textView_business_identifier);
            holder.textViewDate = (TextViewFont) view.findViewById(R.id.textView_date);

            holder.imageViewPost = (ImageView) view.findViewById(R.id.imageView_post);

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
            holder.textViewCode = (TextViewFont) view.findViewById(R.id.textView_code);
            holder.llPriceSection = (LinearLayout) view.findViewById(R.id.ll_price_section);
            holder.llCodeSection = (LinearLayout) view.findViewById(R.id.ll_code_section);


            view.setTag(holder);

        } else
            holder = (Holder) view.getTag();

        holder.llAnnouncementSection.setVisibility(View.GONE);

        //all post's types have these three fields
        downloadImages.download(items.get(position).businessProfilePictureId, Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.imageViewProfileImage, true);
        holder.textViewDate.setText(PersianDate.getCreationDate(activity, items.get(position).creationDate));
        holder.textViewBusinessIdentifier.setText(items.get(position).businessUserName);

        if (items.get(position).picture != null && !items.get(position).picture.equals("") && items.get(position).pictureId == 0)
            holder.imageViewPost.setImageBitmap(Image_M.getBitmapFromString(items.get(position).picture));
        else
            downloadImages.download(items.get(position).pictureId, Image_M.LARGE, Image_M.ImageType.POST, holder.imageViewPost, false);
        holder.textViewLikeNumber.setText(String.valueOf(items.get(position).likeNumber));
        holder.textViewCommentNumber.setText(String.valueOf(items.get(position).commentNumber));
        holder.textViewShareNumber.setText(String.valueOf(items.get(position).shareNumber));
        holder.textViewDescription.setText(items.get(position).description);
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
                User.goUserHomeInfoPage(activity, items.get(position).lastThreeComments.get(0).userID);

            }
        });
        holder.textViewComment2UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(activity, items.get(position).lastThreeComments.get(1).userID);
            }
        });
        holder.textViewComment3UserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(activity, items.get(position).lastThreeComments.get(2).userID);
            }
        });

        holder.imageViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment.openCommentActivity(activity, isUserOwner, items.get(position).id, items.get(position).businessID);
            }
        });
        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityPostAddEdit.class);
                intent.putExtra(Params.BUSINESS_ID, items.get(position).businessID);
                intent.putExtra(Params.POST_ID, items.get(position).id);
                activity.startActivityForResult(intent, Params.ACTION_EDIT_POST);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDeletePostConfirmation d = new DialogDeletePostConfirmation(activity, items.get(position).businessID, items.get(position).id, iDeletePost);
                d.show();
            }
        });


        return view;
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

        TextViewFont textViewCode;
        LinearLayout llPriceSection, llCodeSection;
    }
}
