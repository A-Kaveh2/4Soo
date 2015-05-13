package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;

/**
 * Created by android on 3/7/2015.
 */
public class AdapterCategories extends BaseAdapter {

    private ArrayList<String> items;
    private Context context;
    int selectedPosition;


    public AdapterCategories(Context context, ArrayList<String> items, int selectedPosition) {
        this.context = context;
        this.items = items;
        this.selectedPosition = selectedPosition;
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
            view =  LayoutInflater.from(context).inflate(R.layout.layout_category_item, viewGroup, false);
            holder.textViewFont = (TextViewFont) view.findViewById(R.id.textView);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        holder.textViewFont.setText(items.get(position));
        if(position == selectedPosition){
            if(position == 0)
                holder.textViewFont.setBackgroundResource(R.drawable.shape_bg_popup_selected_top_item_gray);
            else if (position == items.size())
                holder.textViewFont.setBackgroundResource(R.drawable.shape_bg_popup_selected_bottom_item_gray);
            else
                holder.textViewFont.setBackgroundResource(R.drawable.shape_bg_popup_selected_middle_item_gray);

        }


        return view;
    }


    private class Holder {
        TextViewFont textViewFont;
    }
}
