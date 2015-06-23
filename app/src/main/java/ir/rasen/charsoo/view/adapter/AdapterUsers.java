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
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.widgets.MaterialProgressBar;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.ImageViewCircle;

public class AdapterUsers extends BaseAdapter {
    private Activity activity;
    ArrayList<User> items;
    //private int screedWidth;
    SimpleLoader simpleLoader;

    // Constructor
    public AdapterUsers(Activity activity, ArrayList<User> useres) {
        this.activity = activity;
        items = useres;
        //screedWidth = activity.getResources().getDisplayMetrics().widthPixels;
        simpleLoader = new SimpleLoader(activity);
    }

    public void loadMore(ArrayList<User> newItems) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_user, parent, false);
            holder.imageView = (ImageViewCircle) view.findViewById(R.id.user_img);
            holder.name = (TextViewFont) view.findViewById(R.id.user_name);
            holder.id = (TextViewFont) view.findViewById(R.id.user_id);
            holder.progressBar = (MaterialProgressBar) view.findViewById(R.id.user_pb);

        } else
            holder = (Holder) view.getTag();

        if(holder!=null) {
            User user = items.get(position);
            if (user.coverPictureId == 0 && user.coverPicture != null && !user.coverPicture.equals(""))
                holder.imageView.setImageBitmap(Image_M.getBitmapFromString(user.coverPicture));
            else
                simpleLoader.loadImage(user.coverPictureId, Image_M.MEDIUM, Image_M.ImageType.POST, holder.imageView, holder.progressBar);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User.goUserHomeInfoPage(activity, items.get(position).id);
                }
            });
            holder.name.setText(
                    user.name != null ? user.name : ""
            );
            holder.id.setText(
                    user.userIdentifier != null ? user.userIdentifier : ""
            );
        }

        return view;
    }

    private class Holder {
        ImageViewCircle imageView;
        MaterialProgressBar progressBar;
        TextViewFont name, id;
    }

}
