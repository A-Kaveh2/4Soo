package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.view.interface_m.IBaseAdapterOnClickTask;
import ir.rasen.charsoo.view.widget_customized.imageviews.ImageViewCircle;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.model.DownloadImages;

/**
 * Created by android on 3/7/2015.
 */
public class BaseAdapter_M extends BaseAdapter {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    DownloadImages downloadImages;
    boolean isBusinessWatching;

    //we are using IBaseAdapterOnClickTask because BaseAdapter_M will be used to display followers,friends,searchUser,searchBusiness
    // and every usage needs a different onClick listener
    IBaseAdapterOnClickTask iBaseAdapterOnClickTask;
    boolean isRounderCorner;
    public BaseAdapter_M(Context context, ArrayList<BaseAdapterItem> items, boolean isBusinessWatching, IBaseAdapterOnClickTask iBaseAdapterOnClickTask,boolean isRounderCorner) {
        this.context = context;
        this.items = items;
        downloadImages = new DownloadImages(context);
        this.iBaseAdapterOnClickTask = iBaseAdapterOnClickTask;
        this.isBusinessWatching = isBusinessWatching;
        this.isRounderCorner = isRounderCorner;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_base_adapter_cirecle_image, viewGroup, false);
            holder.imageViewImage = (ImageViewCircle) view.findViewById(R.id.imageView_base_adapter_item_image);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_base_adapter_item_title);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        holder.textViewUserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //business can not watch user home info
                if (!isBusinessWatching)
                    //return selected item
                    iBaseAdapterOnClickTask.onClickTask(items.get(position));
            }
        });

        //download image with customized class via imageId
        downloadImages.download(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.POST, holder.imageViewImage,isRounderCorner);
        holder.textViewUserIdentifier.setText(items.get(position).getTitle());

        return view;
    }
    //Each item in this adapter has a picture and a title

    private class Holder {
        ImageViewCircle imageViewImage;
        TextViewFont textViewUserIdentifier;
    }
}
