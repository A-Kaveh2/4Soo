package ir.rasen.charsoo.view.adapter;

/**
 * Created by android on 3/14/2015.
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
import ir.rasen.charsoo.controller.helper.SearchItemPost;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.view.widget_customized.MaterialProgressBar;
import ir.rasen.charsoo.view.widget_customized.imageviews.SquareImageView;
import ir.rasen.charsoo.view.widget_customized.pull_to_refresh.HFGridView;

public class AdapterPostGrid extends BaseAdapter {
    private Activity activity;
    ArrayList<SearchItemPost> items;
    private int screedWidth;
    SimpleLoader simpleLoader;
    //GridViewHeaderFooter gridViewHF;
    HFGridView gridView;
    int businessIdForBusinessPosts;
    Post.GetPostType getPostType;

    // Constructor
    public AdapterPostGrid(Activity activity, ArrayList<SearchItemPost> posts, int businessIdForBusinessPosts, Post.GetPostType getPostType) {
        this.activity = activity;
        items = posts;
        screedWidth = activity.getResources().getDisplayMetrics().widthPixels;
        simpleLoader = new SimpleLoader(activity);
        this.businessIdForBusinessPosts = businessIdForBusinessPosts;
        this.getPostType = getPostType;
    }

    public void loadMore(ArrayList<SearchItemPost> newItem) {
        this.items.addAll(newItem);
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
       /* if (gridView == null) {
            try {
                gridView = (com.handmark.pulltorefresh.library.HFGridView) parent;
                gridView.setVerticalSpacing(3);
                gridView.setHorizontalSpacing(9);
                gridView.setNumColumns(3);
            } catch (Exception e) {

            }

        }*/
        final Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(activity).inflate(R.layout.item_post_adapter_grid, parent, false);
            holder.imageView = (SquareImageView) view.findViewById(R.id.img_grid_post);
            holder.progressBar = (MaterialProgressBar) view.findViewById(R.id.pb_grid_post);
        } else
            holder = (Holder) view.getTag();

        if (items.get(position).postPictureId == 0 && items.get(position).postPicture != null && !items.get(position).postPicture.equals(""))
            holder.imageView.setImageBitmap(Image_M.getBitmapFromString(items.get(position).postPicture));
        else
            simpleLoader.loadImage(items.get(position).postPictureId, Image_M.MEDIUM, Image_M.ImageType.POST, holder.imageView, holder.progressBar);
        if (holder.imageView != null)
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getPostType == Post.GetPostType.SHARE)
                        Post.goPostPageFromUserHome(activity, items.get(position).postId, businessIdForBusinessPosts, getPostType);
                    else
                        Post.goPostPage(activity, items.get(position).postId, businessIdForBusinessPosts, getPostType);
                }
            });

        return view;
    }

    private class Holder {
        ImageView imageView;
        MaterialProgressBar progressBar;
    }

    public void removePostByIntID(int postID_int){
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).postId == postID_int) {
                items.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }
}
