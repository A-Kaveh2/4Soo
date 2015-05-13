package ir.rasen.charsoo.view.adapter;

/**
 * Created by android on 3/14/2015.
 */

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.SearchItemPost;
import ir.rasen.charsoo.view.widget_customized.GridViewHeader;
import ir.rasen.charsoo.view.widget_customized.GridViewWithHeaderAndFooter;
import ir.rasen.charsoo.webservices.DownloadImages;

public class AdapterPostGrid extends BaseAdapter {
    private Activity activity;
    ArrayList<SearchItemPost> items;
    private int screedWidth;
    DownloadImages downloadImages;
    GridViewWithHeaderAndFooter gridViewHF;
    GridViewHeader gridView;
    int businessIdForBusinessPosts;
    Post.GetPostType getPostType;

    // Constructor
    public AdapterPostGrid(Activity activity, ArrayList<SearchItemPost> posts, int businessIdForBusinessPosts, Post.GetPostType getPostType) {
        this.activity = activity;
        items = posts;
        screedWidth = activity.getResources().getDisplayMetrics().widthPixels;
        downloadImages = new DownloadImages(activity);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (gridView == null) {
            try {
                gridViewHF = (GridViewWithHeaderAndFooter) parent;
                gridViewHF.setVerticalSpacing(3);
                gridViewHF.setHorizontalSpacing(9);
                gridViewHF.setNumColumns(3);
            } catch (Exception e) {
                gridView = (GridViewHeader) parent;
                gridView.setVerticalSpacing(3);
                gridView.setHorizontalSpacing(9);
                gridView.setNumColumns(3);
            }

        }
        if (convertView == null) {
            imageView = new ImageView(activity);
            imageView.setLayoutParams(new GridView.LayoutParams((screedWidth / 3), (screedWidth / 3)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            try {
                imageView = (ImageView) convertView;
            } catch (Exception e) {
                //new DialogMessage(context, context.getString(R.string.try_again)).show();
            }
        }

        if (items.get(position).postPictureId == 0 && items.get(position).postPicture != null && !items.get(position).postPicture.equals(""))
            imageView.setImageBitmap(Image_M.getBitmapFromString(items.get(position).postPicture));
        else
            downloadImages.download(items.get(position).postPictureId, Image_M.MEDIUM, Image_M.ImageType.POST, imageView, false);
        if (imageView != null)
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getPostType == Post.GetPostType.SHARE)
                        Post.goPostPageFromUserHome(activity, items.get(position).postId, businessIdForBusinessPosts, getPostType);
                    else
                        Post.goPostPage(activity, items.get(position).postId, businessIdForBusinessPosts, getPostType);
                }
            });

        return imageView;
    }
}
