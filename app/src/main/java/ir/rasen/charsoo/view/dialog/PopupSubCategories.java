package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.view.adapter.AdapterCategories;
import ir.rasen.charsoo.controller.object.SubCategory;
import ir.rasen.charsoo.view.interface_m.ISelectCategory;


public class PopupSubCategories extends MyPopup {
    Context context;



    @SuppressLint("NewApi")
    public PopupSubCategories(final Context context, final ArrayList<SubCategory> subCategories, int selectedItemPosition, final ISelectCategory iSelectCategory) {
        super(context);

        this.context = context;
        int height = getRowHeight();
        int width = getRowWidth();

        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                iSelectCategory.notifySelectSubcategory(i);
                dismiss();
            }
        });

        ArrayList<String> subCategoriesStr = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            subCategoriesStr.add(subCategory.name);
        }

        AdapterCategories adapter = new AdapterCategories(context,subCategoriesStr,selectedItemPosition);
        listView.setAdapter(adapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);

        LinearLayout ll_body = getBody();
        ll_body.addView(listView);
    }




}
