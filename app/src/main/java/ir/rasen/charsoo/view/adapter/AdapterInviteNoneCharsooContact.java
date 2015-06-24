package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        if (!isItemChecked.containsKey(position)) {
            isItemChecked.put(position, false);
        }
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_none_charsoo_contact, viewGroup, false);


            holder.imageViewProfileImage = (ImageView) view.findViewById(R.id.imageView_base_adapter_item_image);
            holder.textViewContactName = (TextViewFont) view.findViewById(R.id.textView_ContactName);
            holder.textViewContaceData = (TextViewFont) view.findViewById(R.id.textView_ContactData);
            holder.checkBoxSelect = (CheckBox) view.findViewById(R.id.checkBox_SelectContactToInvite);
//            holder.checkBoxSelect.setChecked(false);
            view.setTag(holder);

        } else
            holder = (Holder) view.getTag();
        holder.checkBoxSelect.setChecked(isItemChecked.get(position));
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

        try {


        } catch (Exception e) {
            String s = e.getMessage();
        }
        return view;
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
