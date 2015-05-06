package ir.rasen.charsoo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import ir.rasen.charsoo.adapters.AdapterPostGrid;
import ir.rasen.charsoo.adapters.AdapterPostTimeLine;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.ui.DrawerLayoutUser;
import ir.rasen.charsoo.ui.GridViewHeader;
import ir.rasen.charsoo.ui.GridViewUser;


public class ActivityTestUser extends Activity {

    private DrawerLayout mDrawerLayout;


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //DrawerLayoutUser.Initial(this, mDrawerLayout, TestUnit.getUser());
        //final GridViewHeader listView = (GridViewHeader) findViewById(R.id.listView);
        //GridViewUser.InitialGridViewUser(this, 2022, TestUnit.getPostAdapterSharedListItems(), listView, mDrawerLayout);


    }


}
