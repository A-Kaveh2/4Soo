package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.views.CheckBox;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Permission;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.model.user.UpdateSetting;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;


public class ActivityUserSetting extends CharsooActivity implements IWebserviceResponse {


    ProgressDialog progressDialog;
    MyApplication myApplication;
    private Permission permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        setTitle(getString(R.string.settings));


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        final CheckBox checkBoxFriends, checkBoxReviews, checkBoxBusinesses;
        checkBoxBusinesses = (CheckBox) findViewById(R.id.checkBox_businesses);
        checkBoxFriends = (CheckBox) findViewById(R.id.checkBox_friends);
        checkBoxReviews = (CheckBox) findViewById(R.id.checkBox_reviews);

        myApplication = (MyApplication)getApplication();
        permission = myApplication.getPermission();
        checkBoxBusinesses.post(new Runnable() {
            @Override
            public void run() {
                checkBoxBusinesses.setChecked(permission.followedBusiness);
            }
        });
        checkBoxFriends.post(new Runnable() {
            @Override
            public void run() {
                checkBoxFriends.setChecked(permission.friends);
            }
        });
        checkBoxReviews.post(new Runnable() {
            @Override
            public void run() {
                checkBoxReviews.setChecked(permission.reviews);
            }
        });


        (findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                permission = new Permission(checkBoxBusinesses.isCheck(), checkBoxFriends.isCheck(), checkBoxReviews.isCheck());
                new UpdateSetting(ActivityUserSetting.this, LoginInfo.getUserId(ActivityUserSetting.this), permission, ActivityUserSetting.this).execute();
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
        if (result instanceof ResultStatus) {
            myApplication.setPermission(permission);
            Toast.makeText(ActivityUserSetting.this, getResources().getString(R.string.dialog_update_success), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserSetting.this, ServerAnswer.getError(ActivityUserSetting.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }
}
