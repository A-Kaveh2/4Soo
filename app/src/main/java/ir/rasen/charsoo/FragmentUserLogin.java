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
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.dialog.PopupCategories;
import ir.rasen.charsoo.dialog.PopupSubCategories;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.WebservicesHandler;
import ir.rasen.charsoo.interfaces.ISelectCategory;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.ui.TextViewFont;
import ir.rasen.charsoo.webservices.business.GetBusinessGategories;
import ir.rasen.charsoo.webservices.business.GetBusinessSubcategories;

public class FragmentUserLogin extends Fragment implements IWebserviceResponse {


    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login,
                container, false);

        progressDialog = new ProgressDialog(getActivity());


        return view;
    }




    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();

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
