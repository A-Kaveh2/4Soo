package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.model.user.Login;
import ir.rasen.charsoo.view.dialog.DialogForgetPassword;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;

public class ActivityLogin extends CharsooActivity implements View.OnClickListener, IWebserviceResponse {

    EditTextFont editTextEmail, editTextPassword;
    Validation validation;
    ProgressDialog progressDialog;
    MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(getString(R.string.login));

        //for the test I need to disable automatically going to ActivityMain
        if ((LoginInfo.isLoggedIn(this))&&(!Params.isTestVersion)) {
            Intent intent = new Intent(this, ActivityMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

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
                    ((TextInputLayout) findViewById(R.id.til_email)).setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validatePassword(ActivityLogin.this, editTextPassword.getText().toString()).isValid()) {
                    ((TextInputLayout) findViewById(R.id.til_password)).setError(Validation.getErrorMessage());
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
                /*Intent intent = new Intent(ActivityLogin.this, NOT_USED___ActivityUserRegister.class);
                startActivity(intent);*/
                Intent intent = new Intent(this, ActivityUserRegister.class);
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
            finish();
        } else if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.FORGET_PASSWORD) {
            new DialogMessage(ActivityLogin.this, R.string.forgot_password, getResources().getString(R.string.forgot_password_sent)).show();
            myApplication.setCurrentWebservice(WebservicesHandler.Webservices.NONE);
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityLogin.this, ServerAnswer.getError(ActivityLogin.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }
}
