package ir.rasen.charsoo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.CommentNotification;
import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.dialog.PopupCancelFriendship;
import ir.rasen.charsoo.dialog.PopupEditDeleteReview;
import ir.rasen.charsoo.dialog.PopupUnfollowBusiness;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.interfaces.ICancelFriendship;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.ImageViewCircle;
import ir.rasen.charsoo.ui.TextViewFont;
import ir.rasen.charsoo.webservices.DownloadImages;
import ir.rasen.charsoo.webservices.friend.RequestCancelFriendship;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterFriends extends BaseAdapter implements ICancelFriendship {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    DownloadImages downloadImages;
    ICancelFriendship iCancelFriendship;
    //IWebserviceResponse iWebserviceResponse;
    //ProgressDialog progressDialog;
    int visitedUserId;

    public AdapterFriends(Context context,int visitedUserId, ArrayList<BaseAdapterItem> items) {
        this.context = context;
        this.items = items;
        downloadImages = new DownloadImages(context);
        iCancelFriendship = this;
        //this.iWebserviceResponse = iWebserviceResponse;
        //this.progressDialog = progressDialog;
        this.visitedUserId = visitedUserId;
    }

    public void loadMore(ArrayList<BaseAdapterItem> newItem){
        //this.items.addAll(newItem);
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
        Holder holder;

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_friend, viewGroup, false);
            holder.imageViewImage = (ImageViewCircle) view.findViewById(R.id.imageView_base_adapter_item_image);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_base_adapter_item_title);
            holder.imgRemove = (ImageView) view.findViewById(R.id.img_remove);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        if (visitedUserId == LoginInfo.getUserId(context))
            holder.imgRemove.setVisibility(View.VISIBLE);

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RequestCancelFriendship(context,LoginInfo.getUserId(context),items.get(position).getId(),iCancelFriendship).execute();
                items.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.textViewUserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context,items.get(position).getId());
            }
        });

        //download image with customized class via imageId
        downloadImages.download(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.USER, holder.imageViewImage,false);
        holder.textViewUserIdentifier.setText(items.get(position).getTitle());

        return view;
    }

    @Override
    public void notifyDeleteFriend(int userId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == userId)
                items.remove(i);
        }
        notifyDataSetChanged();
    }
    //Each item in this adapter has a picture and a title

    private class Holder {
        ImageViewCircle imageViewImage;
        TextViewFont textViewUserIdentifier;
        ImageView imgRemove;
    }
}
