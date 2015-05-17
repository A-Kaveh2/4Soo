package ir.rasen.charsoo.view.widget_customized;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import ir.rasen.charsoo.view.activity.ActivityProfileUser;
import ir.rasen.charsoo.view.activity.ActivityUserSetting;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.dialog.DialogExit;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.view.interface_m.IGoToRegisterBusinessActivity;
import ir.rasen.charsoo.model.DownloadImages;

/**
 * Created by android on 3/15/2015.
 */
public class DrawerLayoutUser {


    public static void Initial(final Activity activity, final DrawerLayout drawerLayout, final User user,final IGoToRegisterBusinessActivity iGoToRegisterBusinessActivity) {


        DownloadImages downloadImages = new DownloadImages(activity);
        downloadImages.download(user.profilePictureId, Image_M.MEDIUM, Image_M.ImageType.USER, (ImageViewCircle) drawerLayout.findViewById(R.id.imageView_user_picture),false);

        (drawerLayout.findViewById(R.id.imageView_drawer_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityProfileUser.class);
                activity.startActivity(intent);
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        ((TextViewFont) drawerLayout.findViewById(R.id.textView_drawer_identifier)).setText(user.userIdentifier);
        ((TextViewFont) drawerLayout.findViewById(R.id.textView_drawer_user_name)).setText(user.name);

        (drawerLayout.findViewById(R.id.ll_user_section)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });
        (drawerLayout.findViewById(R.id.ll_drawer_businesses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(activity, ActivityUserBusinesses.class);
                ((MyApplication)activity.getApplication()).userBusinesses = user.businesses;
                activity.startActivity(intent);*/
                iGoToRegisterBusinessActivity.notifyGo();
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        (drawerLayout.findViewById(R.id.ll_drawer_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication myApplication = (MyApplication) activity.getApplication();
                myApplication.setPermission(user.permissions);
                Intent intent = new Intent(activity, ActivityUserSetting.class);
                activity.startActivity(intent);
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        (drawerLayout.findViewById(R.id.ll_drawer_exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogExit(activity).show();
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        (drawerLayout.findViewById(R.id.ll_drawer_contact_us)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getResources().getString(R.string.url_contact_us)));
                activity.startActivity(browserIntent);
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        (drawerLayout.findViewById(R.id.ll_drawer_guide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getResources().getString(R.string.url_guide)));
                activity.startActivity(browserIntent);
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
    }


}