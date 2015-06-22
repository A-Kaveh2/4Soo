package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.buttons.ButtonFont;


public class AdapterOfferFriendsToAdd extends BaseAdapter {

    private ArrayList<ContactEntry> items;
    private Context context;
    SimpleLoader simpleLoader;


    public AdapterOfferFriendsToAdd(Context context, ArrayList<ContactEntry> items) {
        this.context = context;
        this.items = items;
        simpleLoader = new SimpleLoader(context);

    }

    public void loadMore(ArrayList<ContactEntry> newItem) {
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

        try {
            final Holder holder;

            if (view == null) {
                holder = new Holder();
                view = LayoutInflater.from(context).inflate(R.layout.item_charsoo_contact, viewGroup, false);


                holder.imageViewProfileImage = (ImageView) view.findViewById(R.id.imageView_profile_picture);
                holder.textViewContactName=(TextViewFont) view.findViewById(R.id.textView_ContactName);
                holder.textViewContaceData=(TextViewFont) view.findViewById(R.id.textView_ContactData);
                holder.buttonFontRequestFriendship=(ButtonFont) view.findViewById(R.id.btn_requestFriendship);
                view.setTag(holder);

            } else
                holder = (Holder) view.getTag();

            simpleLoader.loadImage(items.get(position).charsooPictureIntId, Image_M.SMALL, Image_M.ImageType.USER, holder.imageViewProfileImage);

            holder.buttonFontRequestFriendship.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.textViewContactName.setText(items.get(position).charsooFullName);
            holder.textViewContaceData.setText(items.get(position).charsooStringId);

        } catch (Exception e) {
            String s = e.getMessage();
        }
        return view;
    }






    private class Holder {

        ImageView imageViewProfileImage;
        TextViewFont textViewContactName;
        TextViewFont textViewContaceData;
        ButtonFont buttonFontRequestFriendship;


        GestureDetector gestureDetector;

    }


}
