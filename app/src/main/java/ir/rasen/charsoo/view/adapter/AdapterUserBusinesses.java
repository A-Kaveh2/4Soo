package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.view.widget_customized.imageviews.RoundedImageView;

/**
 * Created by 'Sina KH' on 5/18/2015.
 */
public class AdapterUserBusinesses extends BaseAdapter {

    private ArrayList<Business> items;
    private Context context;
    private SimpleLoader simpleLoader;

    public AdapterUserBusinesses(Context context, ArrayList<Business> items) {
        this.context = context;
        this.items = items;
        simpleLoader = new SimpleLoader(context);
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
            view =  LayoutInflater.from(context).inflate(R.layout.item_user_businesses, viewGroup, false);
            holder.img = (RoundedImageView) view.findViewById(R.id.img_userbusinesses_pic);
            holder.name = (TextViewFont) view.findViewById(R.id.txt_userbusinesses_name);
            holder.description = (TextViewFont) view.findViewById(R.id.txt_userbusinesses_description);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        // TODO:: should be placed::
        //downloadImages.download(items.get(position).coverPictureId, Image_M.SMALL, Image_M.ImageType.BUSINESS, holder.img,true);
//        holder.description.setText(items.get(position).description);
        holder.name.setText(items.get(position).name);

        return view;
    }


    private class Holder {
        TextViewFont name;
        RoundedImageView img;
        TextViewFont description;
    }
}
