package ir.rasen.charsoo;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.Category;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.SubCategory;
import ir.rasen.charsoo.dialog.DialogForgetPassword;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.dialog.PopupCategories;
import ir.rasen.charsoo.dialog.PopupSubCategories;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.helper.WebservicesHandler;
import ir.rasen.charsoo.interfaces.ISelectCategory;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.ui.TextViewFont;
import ir.rasen.charsoo.webservices.business.GetBusinessGategories;
import ir.rasen.charsoo.webservices.business.GetBusinessSubcategories;
import ir.rasen.charsoo.webservices.user.Login;

public class FragmentUserLogin extends Fragment implements IWebserviceResponse {

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
            editTextPassword.setErrorC(Validation.getErrorMessage());
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
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode)).show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }





}
