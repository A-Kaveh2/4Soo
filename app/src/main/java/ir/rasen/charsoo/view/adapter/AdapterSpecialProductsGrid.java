package ir.rasen.charsoo.view.adapter;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.view.widgets.MaterialProgressBar;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.SquareImageView;

public class AdapterSpecialProductsGrid extends BaseAdapter {
    private Activity activity;
    ArrayList<Post> items;
    //private int screedWidth;
    SimpleLoader simpleLoader;

    // Constructor
    public AdapterSpecialProductsGrid(Activity activity, ArrayList<Post> Posts) {
        this.activity = activity;
        items = Posts;
        //screedWidth = activity.getResources().getDisplayMetrics().widthPixels;
        simpleLoader = new SimpleLoader(activity);
    }

    public void loadMore(ArrayList<Post> newItems) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_special_product_grid, parent, false);
            holder.imageView = (SquareImageView) view.findViewById(R.id.special_product_img);
            holder.progressBar = (MaterialProgressBar) view.findViewById(R.id.special_product_pb);
            holder.title = (TextViewFont) view.findViewById(R.id.special_product_title);
        } else
            holder = (Holder) view.getTag();

        if(holder!=null) {
            Post post = items.get(position);
            if (post.pictureId == 0 && post.picture != null && !post.picture.equals(""))
                holder.imageView.setImageBitmap(Image_M.getBitmapFromString(post.picture));
            else
                simpleLoader.loadImage(post.pictureId, Image_M.MEDIUM, Image_M.ImageType.POST, holder.imageView, holder.progressBar);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Post post = items.get(position);
                    if (post.getPostType == Post.GetPostType.SHARE)
                        Post.goPostPageFromUserHome(activity, post.id, post.businessID, post.getPostType);
                    else
                        Post.goPostPage(activity, post.id, post.businessID, post.getPostType);

                }
            });
            if (post.title != null && post.title.length() > 0)
                holder.title.setText(post.title);
        }

        return view;
    }

    private class Holder {
        ImageView imageView;
        MaterialProgressBar progressBar;
        TextViewFont title;
    }

}
