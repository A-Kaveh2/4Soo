package ir.rasen.charsoo.view.widget_customized;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.view.activity.ActivityPostAddEdit;
import ir.rasen.charsoo.view.activity.ActivityBusinessRegisterEdit;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.interface_m.IChangeBusiness;
import ir.rasen.charsoo.view.interface_m.ISelectBusiness;
import ir.rasen.charsoo.model.DownloadImages;

/**
 * Created by android on 3/15/2015.
 */
public class DrawerLayoutBusiness implements IChangeBusiness {

    private int selectedBusinessId;
    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<User.UserBusinesses> items;
    ArrayList<String> businessNamesList;
    TextViewFont textViewIdentifier;
    ISelectBusiness selectBusiness;
    String businessIdentifier;
    Activity activity;
    IChangeBusiness iChangeBusiness;
    DrawerLayout mDrawerLayout;

    public void changeProfilePicture(String picture){
        ((ImageView)mDrawerLayout.findViewById(R.id.imageView_business_picture)).setImageBitmap(Image_M.getBitmapFromString(picture));
    }
    public void Initial(final Activity activity, DrawerLayout drawerLayout, ArrayList<User.UserBusinesses> businessListItems, final int displayedBusinessId,final int displayBusinessProfilePictureId, ISelectBusiness iSelectBusiness) {
        //businessHashTable: <businessIdentifier,businessId>
        items = businessListItems;
        selectedBusinessId = displayedBusinessId;
        this.selectBusiness = iSelectBusiness;
        this.activity = activity;
        this.mDrawerLayout = drawerLayout;
        this.iChangeBusiness = iChangeBusiness;

        for (User.UserBusinesses userBusiness: items)
            if(userBusiness.businessId == displayedBusinessId)
                businessIdentifier = userBusiness.businessIdentifier;


        ( drawerLayout.findViewById(R.id.imageView_drawer_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*PopupEditDeleteBusiness p = new PopupEditDeleteBusiness(activity,selectedBusinessId,businessIdentifier, iChangeBusiness);
                p.show();*/
                Intent intent = new Intent(activity, ActivityBusinessRegisterEdit.class);
                intent.putExtra(Params.BUSINESS_ID,displayedBusinessId);
                intent.putExtra(Params.BUSINESS_IDENTIFIER,businessIdentifier);
                activity.startActivityForResult(intent,Params.ACTION_EDIT_BUSINESS);
            }
        });

        DownloadImages downloadImages = new DownloadImages(activity);
        downloadImages.download(displayBusinessProfilePictureId,Image_M.MEDIUM, Image_M.ImageType.BUSINESS,(ImageView)drawerLayout.findViewById(R.id.imageView_business_picture),true);

        textViewIdentifier = (TextViewFont) drawerLayout.findViewById(R.id.textView_drawer_identifier);
        textViewIdentifier.setText(businessIdentifier);

        ( drawerLayout.findViewById(R.id.ll_drawer_new_post)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityPostAddEdit.class);
                intent.putExtra(Params.BUSINESS_ID,displayedBusinessId);
                activity.startActivityForResult(intent,Params.ACTION_ADD_POST);
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        businessNamesList = new ArrayList<>();
        for (User.UserBusinesses business : businessListItems) {
            businessNamesList.add(business.businessIdentifier);
        }
        listView = (ListView) drawerLayout.findViewById(R.id.listView_drawer);
        adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1, android.R.id.text1, businessNamesList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectBusiness.notifySelectBusiness(items.get(i).businessId);
                selectedBusinessId = items.get(i).businessId;
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                ((TextViewFont) mDrawerLayout.findViewById(R.id.textView_drawer_identifier)).setText(items.get(i).businessIdentifier);
            }
        });
    }


    @Override
    public void notifyDeleteBusiness(int businessId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).businessId == businessId) {
                items.remove(i);
                businessNamesList.remove(i);
            }
        }
        adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, android.R.id.text1, businessNamesList);
        listView.setAdapter(adapter);
        textViewIdentifier.setText(businessNamesList.get(0));
        selectBusiness.notifySelectBusiness(items.get(0).businessId);
    }


}
