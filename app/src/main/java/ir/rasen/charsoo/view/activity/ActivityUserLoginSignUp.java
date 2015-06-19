import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;

/*package ir.rasen.charsoo.view.activity;

/**
 * Created by android on 3/28/2015.
 */


/*import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.fragment.FragmentUserLogin;
import ir.rasen.charsoo.view.fragment.FragmentUserSignUpeEmailID;
import ir.rasen.charsoo.view.fragment.FragmentUserSignUpeNamePassword;
import ir.rasen.charsoo.view.fragment.FragmentUserSignUpePicture;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;

public class ActivityUserLoginSignUp extends CharsooActivity implements IWebserviceResponse {

    FragmentUserLogin fragmentUserLogin;
    FragmentUserSignUpeEmailID fragmentEmailID;
    FragmentUserSignUpeNamePassword fragmentNamePassword;
    FragmentUserSignUpePicture fragmentPicture;
    ProgressDialog progressDialog;

    private enum Fragments {LOGIN, REGISTER_EMAIL_ID, REGISTER_NAME_PASSWORD, REGISTER_PICTURE}

    private Fragments fragmentCurrent, fragmentCurrentSignUp;
    FragmentManager fm;
    FragmentTransaction ft;

    LinearLayout llIndicatorSignUp, llIndicatorLogin;
    MenuItem menuItemRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        llIndicatorLogin = (LinearLayout) findViewById(R.id.ll_indicator_base);
        llIndicatorSignUp = (LinearLayout) findViewById(R.id.ll_indicator_sign_up);

        fragmentUserLogin = new FragmentUserLogin();
        fragmentEmailID = new FragmentUserSignUpeEmailID();
        fragmentNamePassword = new FragmentUserSignUpeNamePassword();
        fragmentPicture = new FragmentUserSignUpePicture();

        fragmentCurrent = Fragments.LOGIN;
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragmentContainer, fragmentUserLogin);


        (findViewById(R.id.rl_sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = fm.beginTransaction();

                //do nothing if one of sign up fragments is displaying
                if (fragmentCurrent == Fragments.LOGIN)
                    return;

                switch (fragmentCurrentSignUp) {
                    case REGISTER_EMAIL_ID:
                        ft.replace(R.id.fragmentContainer, fragmentEmailID);
                        fragmentCurrent = Fragments.REGISTER_EMAIL_ID;

                    case REGISTER_NAME_PASSWORD:
                        ft.replace(R.id.fragmentContainer, fragmentNamePassword);
                        fragmentCurrent = Fragments.REGISTER_NAME_PASSWORD;
                    case REGISTER_PICTURE:
                        ft.replace(R.id.fragmentContainer, fragmentPicture);
                        fragmentCurrent = Fragments.REGISTER_PICTURE;
                }
                fragmentCurrentSignUp = fragmentCurrent;
                ft.commit();

            }
        });
        (findViewById(R.id.rl_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //do nothing is the current fragment is login
                if (fragmentCurrent != Fragments.LOGIN) {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fragmentContainer, fragmentUserLogin);
                    ft.commit();
                    fragmentCurrentSignUp = fragmentCurrent;
                    fragmentCurrent = Fragments.LOGIN;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (fragmentCurrent) {
            case LOGIN:
                //do nothing
                break;
            case REGISTER_EMAIL_ID:
                //do nothing
                break;
            case REGISTER_NAME_PASSWORD:
                fragmentCurrent = Fragments.REGISTER_EMAIL_ID;
                fragmentCurrentSignUp = fragmentCurrent;
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragmentEmailID);
                ft.commit();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case REGISTER_PICTURE:
                fragmentCurrent = Fragments.REGISTER_NAME_PASSWORD;
                fragmentCurrentSignUp = fragmentCurrent;
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragmentNamePassword);
                ft.commit();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tik_button, menu);
        menuItemRegister = menu.findItem(R.id.action_next);
        menuItemRegister.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;

        } else if (item.getItemId() == R.id.action_tik) {
            register();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserLoginSignUp.this, ServerAnswer.getError(ActivityUserLoginSignUp.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

    private void nextFragment() {
        switch (fragmentCurrent) {
            case REGISTER_EMAIL_ID:
                fragmentCurrent = Fragments.REGISTER_NAME_PASSWORD;
                fragmentCurrentSignUp = fragmentCurrent;
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragmentNamePassword);
                ft.commit();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case REGISTER_NAME_PASSWORD:
                fragmentCurrent = Fragments.REGISTER_PICTURE;
                fragmentCurrentSignUp = fragmentCurrent;
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragmentPicture);
                ft.commit();
                menuItemRegister.setVisible(true);
                break;
        }
    }

    private void register() {

    }
}*/