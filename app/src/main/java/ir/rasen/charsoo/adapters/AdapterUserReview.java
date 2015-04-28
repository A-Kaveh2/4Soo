package ir.rasen.charsoo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.Review;
import ir.rasen.charsoo.dialog.PopupEditDeleteReview;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.interfaces.IReviewChange;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.TextViewFont;
import ir.rasen.charsoo.webservices.DownloadImages;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterUserReview extends BaseAdapter implements  IReviewChange {

    private ArrayList<Review> reviews;
    private Context context;
    DownloadImages downloadImages;
    IWebserviceResponse iWebserviceResponse;
    IReviewChange iReviewChange;
    ProgressDialog progressDialog;
    int visitedUserId;

    public AdapterUserReview(Context context,int visitedUserId, ArrayList<Review> reviews, IWebserviceResponse iWebserviceResponse, ProgressDialog progressDialog) {
        this.context = context;
        this.reviews = reviews;
        downloadImages = new DownloadImages(context);
        this.iWebserviceResponse = iWebserviceResponse;
        iReviewChange = this;
        this.progressDialog = progressDialog;
        this.visitedUserId = visitedUserId;
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

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_user_review_adapter, viewGroup, false);
            holder.imageViewImage = (ImageView) view.findViewById(R.id.imageView_review_adapter_item_image_user);
            holder.textViewIdentifier = (TextViewFont) view.findViewById(R.id.textView_review_adapter_item_title);
            holder.textViewText = (TextViewFont) view.findViewById(R.id.textView_review_adapter_item_text);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_review);
            holder.imgMore = (ImageView)view.findViewById(R.id.img_more);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        //download business profile picture with customized class via imageId
        downloadImages.download(reviews.get(position).businessPicutreId, Image_M.SMALL, holder.imageViewImage,true);
        holder.textViewIdentifier.setText(reviews.get(position).businessUserName);
        holder.textViewText.setText(reviews.get(position).text);
        holder.ratingBar.setRating(reviews.get(position).rate);

        if(visitedUserId == LoginInfo.getUserId(context))
            holder.imgMore.setVisibility(View.VISIBLE);
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupEditDeleteReview p = new PopupEditDeleteReview(context,reviews.get(position),iWebserviceResponse,progressDialog,iReviewChange);
                p.show();
            }
        });


        holder.textViewIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GetBusinessHomeInfo via comment.businessID
                Business.goBusinessHomeInfoPage(context,reviews.get(position).businessID);
            }
        });
        return view;
    }

    @Override
    public void notifyDeleteReview(int reviewId) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).id == reviewId)
                reviews.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyUpdateReview(Review review) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).id == review.id) {
                reviews.get(i).text = review.text;
                reviews.get(i).rate = review.rate;
            }
        }
        notifyDataSetChanged();
    }
    //Each item in this adapter has a picture and a title

    private class Holder {
        ImageView imageViewImage;
        TextViewFont textViewIdentifier;
        TextViewFont textViewText;
        RatingBar ratingBar;
        ImageView imgMore;
    }
}
