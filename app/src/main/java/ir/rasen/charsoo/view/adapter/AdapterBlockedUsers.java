package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.view.widget_customized.ImageViewCircle;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.webservices.DownloadImages;
import ir.rasen.charsoo.webservices.business.UnblockUser;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterBlockedUsers extends BaseAdapter {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    DownloadImages downloadImages;
    ListView listView;
    int businessId;


    public AdapterBlockedUsers(Context context,int businessId, ArrayList<BaseAdapterItem> items) {
        this.context = context;
        this.items = items;
        downloadImages = new DownloadImages(context);
        this.businessId = businessId;
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

        if(listView == null) {
            listView = (ListView) viewGroup;
            listView.setSelector(new ColorDrawable(0x00ffffff));
        }

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_blocked_user, viewGroup, false);
            holder.imageViewImage = (ImageViewCircle) view.findViewById(R.id.imageView_base_adapter_item_image);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_base_adapter_item_title);
            holder.imageViewUnblock = (ImageView) view.findViewById(R.id.img_unblock);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        //download image with customized class via imageId
        downloadImages.download(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.USER, holder.imageViewImage,false);
        holder.textViewUserIdentifier.setText(items.get(position).getTitle());
        holder.imageViewUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UnblockUser(context,businessId, items.get(position).getId()).execute();
                items.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }



    private class Holder {
        ImageViewCircle imageViewImage;
        TextViewFont textViewUserIdentifier;
        ImageView imageViewUnblock;
    }
}
