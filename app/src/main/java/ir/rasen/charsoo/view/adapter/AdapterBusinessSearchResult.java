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
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.view.widgets.MaterialProgressBar;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterBusinessSearchResult extends BaseAdapter {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    SimpleLoader simpleLoader;

    public AdapterBusinessSearchResult(Context context, ArrayList<BaseAdapterItem> items) {
        this.context = context;
        this.items = items;
        simpleLoader = new SimpleLoader(context);
    }

    public void loadMore(ArrayList<BaseAdapterItem> newItem){
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
        Holder holder;

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_business, viewGroup, false);
            holder.img = (RoundedSquareImageView) view.findViewById(R.id.business_img);
            holder.name = (TextViewFont) view.findViewById(R.id.business_name);
            holder.id = (TextViewFont) view.findViewById(R.id.business_id);
            holder.pb = (MaterialProgressBar) view.findViewById(R.id.business_pb);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Business.goBusinessHomeInfoPage(context,items.get(position).getId());
            }
        });
        //download image with customized class via imageId
        simpleLoader.loadImage(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.img, holder.pb);
        holder.name.setText(items.get(position).getTitle());
        // temporary:
        holder.id.setVisibility(View.GONE);

        return view;
    }



    private class Holder {
        RoundedSquareImageView img;
        TextViewFont name, id;
        MaterialProgressBar pb;
    }
}
