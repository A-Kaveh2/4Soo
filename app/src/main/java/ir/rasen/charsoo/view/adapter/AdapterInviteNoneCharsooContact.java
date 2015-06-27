package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.view.interface_m.IFragInviteSelectionListener;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.checkbox.CheckBox;


public class AdapterInviteNoneCharsooContact extends BaseAdapter {

    private ArrayList<ContactEntry> items;
    private Context context;
    SimpleLoader simpleLoader;
    private Hashtable<Integer,Boolean> isItemChecked;
    IFragInviteSelectionListener delegate;
    int prevProsition=0;


    public AdapterInviteNoneCharsooContact(Context context, ArrayList<ContactEntry> items, IFragInviteSelectionListener delegate) {
        this.delegate=delegate;
        this.context = context;
        this.items = items;
        simpleLoader = new SimpleLoader(context);
        isItemChecked=new Hashtable<>();


    }

    public void loadMore(ArrayList<ContactEntry> newItem) {
        this.items.addAll(newItem);
        notifyDataSetChanged();
    }

    public void resetItems(ArrayList<ContactEntry> newItem){
        this.items=new ArrayList<>(newItem);
        notifyDataSetChanged();
    }

    public void setUncheckedViewAt(int position){
        try{
            isItemChecked.put(position,false);
            notifyDataSetChanged();
        }catch(Exception ed){

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

        final Holder holder=new Holder();
        View v=view;

        boolean flag=false;
        if (v==null){
            v = LayoutInflater.from(context).inflate(R.layout.item_none_charsoo_contact, viewGroup, false);
            flag=true;
        }
        if (!isItemChecked.containsKey(position)) {
            isItemChecked.put(position, false);
        }
//        if (view == null) {
//            holder = new Holder();
//            view = LayoutInflater.from(context).inflate(R.layout.item_none_charsoo_contact, viewGroup, false);
//
//
//            holder.imageViewProfileImage = (ImageView) view.findViewById(R.id.imageView_base_adapter_item_image);
//            holder.textViewContactName = (TextViewFont) view.findViewById(R.id.textView_ContactName);
//            holder.textViewContaceData = (TextViewFont) view.findViewById(R.id.textView_ContactData);
//            holder.checkBoxSelect = (CheckBox) view.findViewById(R.id.checkBox_SelectContactToInvite);
////            holder.checkBoxSelect.setChecked(false);
//            view.setTag(holder);
//
//        } else
//            holder = (Holder) view.getTag();
        holder.imageViewProfileImage = (ImageView) v.findViewById(R.id.imageView_base_adapter_item_image);
        holder.textViewContactName = (TextViewFont) v.findViewById(R.id.textView_ContactName);
        holder.textViewContaceData = (TextViewFont) v.findViewById(R.id.textView_ContactData);
        holder.checkBoxSelect = (CheckBox) v.findViewById(R.id.checkBox_SelectContactToInvite);

        holder.checkBoxSelect.post(new Runnable() {
            @Override
            public void run() {
                holder.checkBoxSelect.setChecked(isItemChecked.get(position));
            }
        });
        if (items.get(position).contactPhoto != null)
            holder.imageViewProfileImage.setImageBitmap(items.get(position).contactPhoto);
//        else;
//            holder.imageViewProfileImage.setImageDrawable(items.get(position).contactPhotoDrawable);

        holder.checkBoxSelect.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(ir.rasen.charsoo.view.widgets.material_library.views.CheckBox view, boolean check) {
                if (isItemChecked.get(position))
                    isItemChecked.put(position, false);
                else
                    isItemChecked.put(position, true);
                delegate.onItemCheckBoxClicked(position);
            }
        });

        holder.textViewContactName.setText(items.get(position).fullName);
        holder.textViewContaceData.setText(items.get(position).contactData);


        if (position>prevProsition){
            prevProsition=position;
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        v.startAnimation(animation);}
        return v;
    }






    private class Holder {

        ImageView imageViewProfileImage;
        TextViewFont textViewContactName;
        TextViewFont textViewContaceData;
        CheckBox checkBoxSelect;
        boolean isChecked = false;

        GestureDetector gestureDetector;

    }


}
