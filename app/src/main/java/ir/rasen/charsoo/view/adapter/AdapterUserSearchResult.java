package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.view.widget_customized.ImageViewCircle;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.model.DownloadImages;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterUserSearchResult extends BaseAdapter {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    DownloadImages downloadImages;
    ListView listView;


    public AdapterUserSearchResult(Context context, ArrayList<BaseAdapterItem> items) {
        this.context = context;
        this.items = items;
        downloadImages = new DownloadImages(context);
    }
    public void notifyRemoveAllItems(){
        items.clear();
        notifyDataSetChanged();
    }
    public void loadMore(ArrayList<BaseAdapterItem> newItem){
        //this.items.addAll(newItem);
        int i = items.size();
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

        if(listView == null) {
            listView = (ListView) viewGroup;
            listView.setSelector(new ColorDrawable(0x00ffffff));
        }

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_base_adapter_cirecle_image_un_selectable, viewGroup, false);
            holder.imageViewImage = (ImageViewCircle) view.findViewById(R.id.imageView_base_adapter_item_image);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_base_adapter_item_title);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        holder.textViewUserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context,items.get(position).getId());
            }
        });
        holder.imageViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context, items.get(position).getId());
            }
        });

        //download image with customized class via imageId
        downloadImages.download(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.USER, holder.imageViewImage,false);
        holder.textViewUserIdentifier.setText(items.get(position).getTitle());

        return view;
    }



    private class Holder {
        ImageViewCircle imageViewImage;
        TextViewFont textViewUserIdentifier;
    }
}
