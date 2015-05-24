package ir.rasen.charsoo.view.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.dialog.PopupBlockUser;
import ir.rasen.charsoo.view.dialog.PopupDeleteCommentBlockUser;
import ir.rasen.charsoo.view.dialog.PopupEditDeleteComment;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.view.interface_m.ICommentChange;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.imageviews.ImageViewCircle;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.model.DownloadImages;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterPostComments extends BaseAdapter implements ICommentChange, IWebserviceResponse {

    //This adapter will displays comments of a post
    //In this adapter user can not delete the comment or block the comment's writer (business who owns the post can)
    //In this adapter, if the user is comment's owner, he can edit or delete the comment

    private ArrayList<Comment> comments;
    private Context context;
    DownloadImages downloadImages;
    IWebserviceResponse IWebserviceResponse;
    ProgressDialog progressDialog;
    int postOwnerBusinessId;
    private ICommentChange iCommentChange;
    boolean isUserOwner;


    public AdapterPostComments(Context context, boolean isUserOwner, int postOwnerBusinessId, ArrayList<Comment> comments, IWebserviceResponse IWebserviceResponse, ProgressDialog progressDialog) {
        this.context = context;
        this.comments = comments;
        downloadImages = new DownloadImages(context);
        this.IWebserviceResponse = IWebserviceResponse;
        this.progressDialog = progressDialog;
        this.postOwnerBusinessId = postOwnerBusinessId;
        this.iCommentChange = this;
        this.isUserOwner = isUserOwner;
    }

    public void loadMore(ArrayList<Comment> newComments) {
        this.comments.addAll(newComments);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_comment_adapter, viewGroup, false);
            holder.imageViewImage = (ImageViewCircle) view.findViewById(R.id.imageView_comment_adapter_item_image);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment_adapter_item_title);
            holder.textViewText = (TextViewFont) view.findViewById(R.id.textView_comment_adapter_item_text);
            holder.imgMore = (ImageView) view.findViewById(R.id.img_more);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();


        //download image with customized class via imageId
        downloadImages.download(comments.get(position).userProfilePictureID, Image_M.SMALL, Image_M.ImageType.USER, holder.imageViewImage,false);
        holder.textViewUserIdentifier.setText(comments.get(position).username);
        holder.textViewText.setText(comments.get(position).text);

        if (comments.get(position).userID == LoginInfo.getUserId(context) || isUserOwner)
            holder.imgMore.setVisibility(View.VISIBLE);
        else
            holder.imgMore.setVisibility(View.GONE);

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserOwner) {
                    //the business owner is watching the posts
                    new PopupBlockUser(context,postOwnerBusinessId, comments.get(position).userID, AdapterPostComments.this).show();
                    //new PopupDeleteCommentBlockUser(context,postOwnerBusinessId,comments.get(position),)
                } else {
                    PopupEditDeleteComment p = new PopupEditDeleteComment(context, comments.get(position), IWebserviceResponse, progressDialog, iCommentChange);
                    p.show();
                }

            }
        });


        holder.textViewUserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GetUserHomeInfo via comment.userID
                User.goUserHomeInfoPage(context, comments.get(position).userID);
            }
        });
        return view;
    }

    @Override
    public void notifyDeleteComment(int commentId) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).id == commentId)
                comments.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyUpdateComment(Comment comment) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).id == comment.id)
                comments.get(i).text = comment.text;
        }
        notifyDataSetChanged();
    }

    @Override
    public void getResult(Object result) {
        //get BlockUser's result
        new DialogMessage(context,context.getString(R.string.block_done)).show();
    }

    @Override
    public void getError(Integer errorCode) {
        //get BlockUser's error
        new DialogMessage(context,context.getString(R.string.block_error)).show();
    }
    //Each item in this adapter has a picture and a title

    private class Holder {
        ImageViewCircle imageViewImage;
        TextViewFont textViewUserIdentifier;
        TextViewFont textViewText;
        ImageView imgMore;
    }
}
