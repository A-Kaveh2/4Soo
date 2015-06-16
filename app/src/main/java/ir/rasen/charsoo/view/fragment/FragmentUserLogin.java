package ir.rasen.charsoo.view.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.view.dialog.DialogForgetPassword;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.model.user.Login;

public class FragmentUserLogin extends Fragment implements IWebserviceResponse {
    public static final String TAG ="FragmentUserLogin";
    EditTextFont editTextEmail, editTextPassword;
    Validation validation;
    MyApplication myApplication;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login,
                container, false);

        progressDialog = new ProgressDialog(getActivity());

        myApplication = (MyApplication) getActivity().getApplication();
        myApplication.setCurrentWebservice(WebservicesHandler.Webservices.NONE);

        view.findViewById(R.id.btn_login_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        view.findViewById(R.id.btn_login_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forget();
            }
        });

        return view;
    }


    private void forget(){
        new DialogForgetPassword(getActivity(), FragmentUserLogin.this, progressDialog).show();
        myApplication.setCurrentWebservice(WebservicesHandler.Webservices.FORGET_PASSWORD);
    }

    public void login(){
        if (!Validation.validateEmail(getActivity(), editTextEmail.getText().toString()).isValid()) {
            editTextEmail.setError(Validation.getErrorMessage());
            return;
        }
        if (!Validation.validatePassword(getActivity(), editTextPassword.getText().toString()).isValid()) {
            editTextPassword.setError(Validation.getErrorMessage());
            return;
        }
        progressDialog.show();
        new Login(getActivity(), editTextEmail.getText().toString(), editTextPassword.getText().toString(), FragmentUserLogin.this).execute();
        myApplication.setCurrentWebservice(WebservicesHandler.Webservices.LOGIN);

    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (!(result instanceof ResultStatus))
            return;
        if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.LOGIN) {
            Intent intent = new Intent(getActivity(), ActivityMain.class);
            startActivity(intent);
            myApplication.setCurrentWebservice(WebservicesHandler.Webservices.NONE);
        } else if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.FORGET_PASSWORD) {
            new DialogMessage(getActivity(), getResources().getString(R.string.forgot_password_sent)).show();
            myApplication.setCurrentWebservice(WebservicesHandler.Webservices.NONE);
        }

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode,callerStringID+">"+TAG)).show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }





}
