package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Review;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.dialog.PopupEditDeleteReview;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.ImageViewCircle;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterBusinessReview extends BaseAdapter  {

    private ArrayList<Review> reviews;
    private Context context;
    SimpleLoader simpleLoader;
    ListView listView;


    //this adapter will displays users who wrote reviews on a business
    public AdapterBusinessReview(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        simpleLoader = new SimpleLoader(context);
    }
    public void loadMore(ArrayList<Review> newItem){
        this.reviews.addAll(newItem);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final Holder holder;

        //review is watched by business shouldn't be able to select
        if(listView == null) {
            listView = (ListView) viewGroup;
            listView.setSelector(new ColorDrawable(0x00ffffff));
        }

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_business_review_adapter, viewGroup, false);
            holder.imageViewImage = (ImageViewCircle) view.findViewById(R.id.imageView_review_adapter_item_image_user);
            holder.textViewIdentifier = (TextViewFont) view.findViewById(R.id.textView_review_adapter_item_title);
            holder.textViewText = (TextViewFont) view.findViewById(R.id.textView_review_adapter_item_text);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_review);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        //download business profile picture with customized class via imageId
        simpleLoader.loadImage(reviews.get(position).userPicutreId, Image_M.SMALL, Image_M.ImageType.USER, holder.imageViewImage);
        holder.textViewIdentifier.setText(reviews.get(position).userName);
        holder.textViewText.setText(reviews.get(position).text);
        holder.ratingBar.setRating(reviews.get(position).rate);

//        if(visitedUserId == LoginInfo.getUserId(context))
//            holder.imgMore.setVisibility(View.VISIBLE);
//        holder.imgMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupEditDeleteReview p = new PopupEditDeleteReview(context, reviews.get(position), iWebserviceResponse, progressDialog, iReviewChange);
//                p.show();
//            }
//        });
        //if(reviews.get(position).userID)
        holder.textViewIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GetBusinessHomeInfo via comment.businessID
                User.goUserHomeInfoPage(context,reviews.get(position).userID);
            }
        });

        holder.imageViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.goUserHomeInfoPage(context,reviews.get(position).userID);
            }
        });
        return view;
    }
    //Each item in this adapter has a picture and a title

    private class Holder {
        ImageViewCircle imageViewImage;
        TextViewFont textViewIdentifier;
        TextViewFont textViewText;
        RatingBar ratingBar;
        ImageView imgMore;
    }
}
