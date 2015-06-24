package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.view.fragment.FragmentUserRegisterOfferFriendInvite;
import ir.rasen.charsoo.view.interface_m.IInviteFriendByAppListener;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.checkbox.CheckBox;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;

/**
 * Created by hossein-pc on 6/22/2015.
 */
public class AdapterInviteFriendsByApp extends BaseAdapter {

    private ArrayList<PackageInfoCustom> items;
    private Context context;
    private ArrayList<Boolean> isItemChecked;
    private Hashtable<Integer,String> appsTag;
    IInviteFriendByAppListener delegate;

    public AdapterInviteFriendsByApp(Context context, ArrayList<PackageInfoCustom> items,IInviteFriendByAppListener delegate) {
        this.context = context;
        this.items = items;
        isItemChecked=new ArrayList<>();
        appsTag=new Hashtable<>();
        this.delegate=delegate;
    }

    public void loadMore(ArrayList<PackageInfoCustom> newItem) {
        if (newItem!=null) {
            this.items.addAll(newItem);
            notifyDataSetChanged();
        }
    }

    public void resetItems(ArrayList<PackageInfoCustom> newItem) {
        if (newItem!=null) {
            this.items.addAll(newItem);
            notifyDataSetChanged();
        }
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
            view = LayoutInflater.from(context).inflate(R.layout.item_application_to_invite, viewGroup, false);


            holder.imageViewAppIcone=(RoundedSquareImageView) view.findViewById(R.id.imageView_ApplicationIcone);
            holder.textViewAppName=(TextViewFont) view.findViewById(R.id.textViewFont_ApplicationName);

            view.setTag(holder);

        } else
            holder = (Holder) view.getTag();

        holder.textViewAppName.setText(items.get(position).appname);
        holder.imageViewAppIcone.setImageBitmap(items.get(position).icon);
        appsTag.put(position, items.get(position).appname);
        holder.textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onItemClicked(appsTag.get(position));
            }
        });
        holder.imageViewAppIcone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onItemClicked(appsTag.get(position));
            }
        });
//        try {
//
//
//        } catch (Exception e) {
//            String s = e.getMessage();
//        }
        return view;
    }






    public class Holder{
        public RoundedSquareImageView imageViewAppIcone;
        public TextViewFont textViewAppName;

    }


}
