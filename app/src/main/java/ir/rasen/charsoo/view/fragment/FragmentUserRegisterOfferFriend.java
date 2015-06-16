package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.view.activity.ActivityUserRegister;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriend extends Fragment {

    public static final String TAG="SecondPage";

//    adapter

//        TextViewFont persianLicenseTextView,englishLicenseTextView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register_offer_friends,
                container, false);




        return view;

    }



}
