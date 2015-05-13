package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.CommentNotification;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.view.widget_customized.ImageViewCircle;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.model.DownloadImages;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterCommentNotification extends BaseAdapter {

    private ArrayList<CommentNotification> commentNotifications;
    private Context context;
    DownloadImages downloadImages;


    public AdapterCommentNotification(Context context, ArrayList<CommentNotification> commentNotifications) {
        this.context = context;
        this.commentNotifications = commentNotifications;
        downloadImages = new DownloadImages(context);
    }

    @Override
    public int getCount() {
        return commentNotifications.size();
    }

    @Override
    public Object getItem(int position) {
        return commentNotifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void loadMore(ArrayList<CommentNotification> newCommentNotifications){
        this.commentNotifications.addAll(newCommentNotifications);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final Holder holder;

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_comment_notification_adapter, viewGroup, false);
            holder.imageViewImageUser = (ImageViewCircle) view.findViewById(R.id.imageView_comment_adapter_item_image_user);
            holder.imageViewImagePost = (ImageView) view.findViewById(R.id.imageView_comment_adapter_item_image_post);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_comment_adapter_item_title);
            holder.textViewText = (TextViewFont) view.findViewById(R.id.textView_comment_adapter_item_text);

            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        holder.imageViewImagePost.setImageBitmap(Image_M.getBitmapFromString(commentNotifications.get(position).postPicture));
        holder.imageViewImageUser.setImageBitmap(Image_M.getBitmapFromString(commentNotifications.get(position).userPicture));
        holder.textViewUserIdentifier.setText(commentNotifications.get(position).userName);
        holder.textViewText.setText(commentNotifications.get(position).text);


        holder.imageViewImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open ActivityCommentNotification
                Toast.makeText(context, "Open post info page", Toast.LENGTH_LONG).show();
            }
        });

        holder.textViewUserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GetUserHomeInfo via commentNotification.userID
                Toast.makeText(context, "Open UserHomeInfo", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }


    private class Holder {
        ImageViewCircle imageViewImageUser;
        ImageView imageViewImagePost;
        TextViewFont textViewUserIdentifier;
        TextViewFont textViewText;
    }
}
