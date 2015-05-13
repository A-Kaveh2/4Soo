package ir.rasen.charsoo.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.ActivityBusiness;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.adapters.AdapterCategories;
import ir.rasen.charsoo.classes.Category;
import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.interfaces.IReportPost;
import ir.rasen.charsoo.interfaces.ISelectCategory;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.TextViewFont;
import ir.rasen.charsoo.webservices.business.BlockUser;


public class PopupCategories extends MyPopup {
    Context context;



    @SuppressLint("NewApi")
    public PopupCategories(final Context context,final ArrayList<Category> categories,int selectedItemPosition,final ISelectCategory iSelectCategory) {
        super(context);

        this.context = context;
        int height = getRowHeight();
        int width = getRowWidth();

        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                iSelectCategory.notifySelectCategory(i);
                dismiss();
            }
        });

        ArrayList<String> categoriesStr = new ArrayList<>();
        for (Category category : categories) {
            categoriesStr.add(category.name);
        }

        AdapterCategories adapter = new AdapterCategories(context,categoriesStr,selectedItemPosition);
        listView.setAdapter(adapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);

        LinearLayout ll_body = getBody();
        ll_body.addView(listView);
    }




}
