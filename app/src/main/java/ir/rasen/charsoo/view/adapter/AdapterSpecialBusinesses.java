package ir.rasen.charsoo.view.adapter;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.view.widgets.MaterialProgressBar;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;
import ir.rasen.charsoo.view.widgets.imageviews.SquareImageView;

public class AdapterSpecialBusinesses extends BaseAdapter {
    private Activity activity;
    ArrayList<Business> items;
    //private int screedWidth;
    SimpleLoader simpleLoader;

    // Constructor
    public AdapterSpecialBusinesses(Activity activity, ArrayList<Business> businesses) {
        this.activity = activity;
        items = businesses;
        //screedWidth = activity.getResources().getDisplayMetrics().widthPixels;
        simpleLoader = new SimpleLoader(activity);
    }

    public void loadMore(ArrayList<Business> newItems) {
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View view, ViewGroup parent) {
        final Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(activity).inflate(R.layout.item_business_preview, parent, false);
            holder.imageView = (RoundedSquareImageView) view.findViewById(R.id.business_img);
            holder.name = (TextViewFont) view.findViewById(R.id.business_name);
            holder.id = (TextViewFont) view.findViewById(R.id.business_id);
            holder.progressBar = (MaterialProgressBar) view.findViewById(R.id.business_pb);
            holder.img1 = (SquareImageView) view.findViewById(R.id.business_prev_img1);
            holder.img2 = (SquareImageView) view.findViewById(R.id.business_prev_img2);
            holder.img3 = (SquareImageView) view.findViewById(R.id.business_prev_img3);
        } else
            holder = (Holder) view.getTag();

        if(holder!=null) {
            Business business = items.get(position);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Business.goBusinessHomeInfoPage(activity,items.get(position).id);
                }
            });

            if (business.coverPictureId == 0 && business.coverPicture != null && !business.coverPicture.equals(""))
                holder.imageView.setImageBitmap(Image_M.getBitmapFromString(business.coverPicture));
            else
                simpleLoader.loadImage(business.coverPictureId, Image_M.MEDIUM, Image_M.ImageType.POST, holder.imageView, holder.progressBar);
            holder.name.setText(
                    business.name != null ? business.name : ""
            );
            holder.id.setText(
                    business.businessIdentifier!=null ? business.businessIdentifier : ""
            );
            simpleLoader.loadImage(business.coverPictureId, Image_M.MEDIUM, Image_M.ImageType.POST, holder.img1);
            simpleLoader.loadImage(business.coverPictureId, Image_M.MEDIUM, Image_M.ImageType.POST, holder.img2);
            simpleLoader.loadImage(business.coverPictureId, Image_M.MEDIUM, Image_M.ImageType.POST, holder.img3);
        }

        return view;
    }

    private class Holder {
        RoundedSquareImageView imageView;
        MaterialProgressBar progressBar;
        TextViewFont name, id;
        SquareImageView img1, img2, img3;
    }

}
