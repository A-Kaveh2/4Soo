package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Permission;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.user.UpdateSetting;


public class ActivityUserSetting extends ActionBarActivity implements IWebserviceResponse {


    ProgressDialog progressDialog;
    MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.settings));


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        final CheckBox checkBoxFriends, checkBoxReviews, checkBoxBusinesses;
        checkBoxBusinesses = (CheckBox) findViewById(R.id.checkBox_businesses);
        checkBoxFriends = (CheckBox) findViewById(R.id.checkBox_friends);
        checkBoxReviews = (CheckBox) findViewById(R.id.checkBox_reviews);

        MyApplication myApplication = (MyApplication)getApplication();
        Permission permission = myApplication.getPermission();
        checkBoxBusinesses.setChecked(permission.followedBusiness);
        checkBoxFriends.setChecked(permission.friends);
        checkBoxReviews.setChecked(permission.reviews);


        (findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                new UpdateSetting(ActivityUserSetting.this, LoginInfo.getUserId(ActivityUserSetting.this), new Permission(checkBoxBusinesses.isChecked(), checkBoxFriends.isChecked(), checkBoxReviews.isChecked()), ActivityUserSetting.this).execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_setting, menu);*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof ResultStatus)
            new DialogMessage(ActivityUserSetting.this, getResources().getString(R.string.dialog_update_success)).show();
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserSetting.this, ServerAnswer.getError(ActivityUserSetting.this, errorCode)).show();
    }
}
