package ir.rasen.charsoo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.dialog.DialogForgetPassword;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.helper.WebservicesHandler;
import ir.rasen.charsoo.ui.TextViewFont;
import ir.rasen.charsoo.ui.TextViewFontActionBarTitle;
import ir.rasen.charsoo.webservices.user.Login;


public class ActivityLogin extends ActionBarActivity implements View.OnClickListener, IWebserviceResponse {


    EditTextFont editTextEmail, editTextPassword;
    Validation validation;
    ProgressDialog progressDialog;
    MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.login));


        //for the test I need to disable automatically going to ActivityMain
      /*  if (LoginInfo.isLoggedIn(this)) {
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            return;
        }*/

        myApplication = (MyApplication) getApplication();
        myApplication.setCurrentWebservice(WebservicesHandler.Webservices.NONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        //edit texts
        editTextEmail = (EditTextFont) findViewById(R.id.edt_login_email);
        editTextPassword = (EditTextFont) findViewById(R.id.edt_login_password);

        //views which do an action
        findViewById(R.id.btn_login_login).setOnClickListener(this);
        findViewById(R.id.btn_login_forget).setOnClickListener(this);
        findViewById(R.id.btn_login_register).setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next_button, menu);*/
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_login:
                if (!Validation.validateEmail(ActivityLogin.this, editTextEmail.getText().toString()).isValid()) {
                    editTextEmail.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validatePassword(ActivityLogin.this, editTextPassword.getText().toString()).isValid()) {
                    editTextPassword.setErrorC(Validation.getErrorMessage());
                    return;
                }
                progressDialog.show();
                new Login(ActivityLogin.this, editTextEmail.getText().toString(), editTextPassword.getText().toString(), ActivityLogin.this).execute();
                myApplication.setCurrentWebservice(WebservicesHandler.Webservices.LOGIN);
                break;
            case R.id.btn_login_forget:
                new DialogForgetPassword(ActivityLogin.this, ActivityLogin.this, progressDialog).show();
                myApplication.setCurrentWebservice(WebservicesHandler.Webservices.FORGET_PASSWORD);
                break;
            case R.id.btn_login_register:
                //The main function commented here because of the test
              /*  Intent intent = new Intent(ActivityLogin.this, ActivityRegisterUser.class);
                startActivity(intent);*/
                Intent intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       /* if (item.getItemId() == R.id.action_charsoo) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        }*/
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (!(result instanceof ResultStatus))
            return;
        if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.LOGIN) {
            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
            startActivity(intent);
            myApplication.setCurrentWebservice(WebservicesHandler.Webservices.NONE);
        } else if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.FORGET_PASSWORD) {
            new DialogMessage(ActivityLogin.this, getResources().getString(R.string.forgot_password_sent)).show();
            myApplication.setCurrentWebservice(WebservicesHandler.Webservices.NONE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityLogin.this, ServerAnswer.getError(ActivityLogin.this, errorCode)).show();
    }
}
