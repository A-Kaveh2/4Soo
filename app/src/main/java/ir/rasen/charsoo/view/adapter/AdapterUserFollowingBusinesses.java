package ir.rasen.charsoo.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.view.dialog.PopupUnfollowBusiness;
import ir.rasen.charsoo.view.interface_m.IUnfollowBusiness;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedImageView;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterUserFollowingBusinesses extends BaseAdapter implements IUnfollowBusiness {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    SimpleLoader simpleLoader;
    IUnfollowBusiness iUnfollowBusiness;
    //IWebserviceResponse iWebserviceResponse;
    WaitDialog progressDialog;
    int visitedUserId;

    public AdapterUserFollowingBusinesses(Context context, int visitedUserId, ArrayList<BaseAdapterItem> items, WaitDialog progressDialog) {
        this.context = context;
        this.items = items;
        simpleLoader = new SimpleLoader(context);
        iUnfollowBusiness = this;
        //this.iWebserviceResponse = iWebserviceResponse;
        this.progressDialog = progressDialog;
        this.visitedUserId = visitedUserId;
    }

    public void loadMore(ArrayList<BaseAdapterItem> newItems){
        this.items.addAll(newItems);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_base_adapter_squar_image_selectable, viewGroup, false);
            holder.imageViewImage = (RoundedImageView) view.findViewById(R.id.imageView_base_adapter_item_image);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_base_adapter_item_title);
            holder.imgMore = (ImageView) view.findViewById(R.id.img_more);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        //the user is visiting his following businesses
        if (visitedUserId == LoginInfo.getUserId(context))
            holder.imgMore.setVisibility(View.VISIBLE);
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupUnfollowBusiness p = new PopupUnfollowBusiness(context, items.get(position).getId(), progressDialog, iUnfollowBusiness);
                p.show();
            }
        });

        holder.textViewUserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Business.goBusinessHomeInfoPage(context,items.get(position).getId());
            }
        });

        //download image with customized class via imageId
        simpleLoader.loadImage(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.imageViewImage);
        holder.textViewUserIdentifier.setText(items.get(position).getTitle());

        return view;
    }


    @Override
    public void notifyUnfollowBusiness(int businessId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == businessId)
                items.remove(i);
        }
        notifyDataSetChanged();
        progressDialog.dismiss();
    }

    //Each item in this adapter has a picture and a title

    private class Holder {
        ImageView imageViewImage;
        TextViewFont textViewUserIdentifier;
        ImageView imgMore;
    }
}
