package ir.rasen.charsoo.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.helper.WorkTime;
import ir.rasen.charsoo.view.widget_customized.ButtonFont;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.activity.ActivityBusinessWorkTime;

public class FragmentBusinessRegisterContactInfo extends Fragment {


    EditTextFont editTextPhone, editTextMobile, editTextWebsite, editTextEmail;
    ButtonFont buttonWorkTime;
    WorkTime workTime;
    private boolean isEditing;
    private Business editingBusiness;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_business_contact_info,
                container, false);

        editTextEmail = (EditTextFont) view.findViewById(R.id.edt_email);
        editTextMobile = (EditTextFont) view.findViewById(R.id.edt_mobile);
        editTextWebsite = (EditTextFont) view.findViewById(R.id.edt_wesite);
        editTextPhone = (EditTextFont) view.findViewById(R.id.edt_phone);
        buttonWorkTime = (ButtonFont) view.findViewById(R.id.btn_work_time);

        buttonWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityBusinessWorkTime.class);
                if (isEditing) {
                    intent.putExtra(Params.IS_EDITTING, true);
                } else {
                    if (workTime != null) {
                        ((MyApplication) getActivity().getApplication()).workTime = workTime;
                        intent.putExtra(Params.IS_EDITTING, true);
                    } else
                        intent.putExtra(Params.IS_EDITTING, false);
                }
                startActivityForResult(intent, Params.ACTION_WORK_TIME);
            }
        });

        //if the user is editing the business
        isEditing = getArguments().getBoolean(Params.IS_EDITTING);
        if (isEditing) {
            editingBusiness = ((MyApplication) getActivity().getApplication()).business;
            editTextPhone.setText(editingBusiness.phone);
            editTextMobile.setText(editingBusiness.mobile);
            editTextWebsite.setText(editingBusiness.webSite);
            editTextEmail.setText(editingBusiness.email);

            buttonWorkTime.setBackgroundResource(R.drawable.selector_button_register);
            buttonWorkTime.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
            ((MyApplication) getActivity().getApplication()).workTime = editingBusiness.workTime;
        }

        return view;
    }

    public boolean isVerified() {
        if (!editTextPhone.getText().toString().equals("") && !Validation.validatePhone(getActivity(), editTextPhone.getText().toString()).isValid()) {
            editTextPhone.setError(Validation.getErrorMessage());
            return false;
        }
        if (!editTextMobile.getText().toString().equals("") && !Validation.validateMobile(getActivity(), editTextMobile.getText().toString()).isValid()) {
            editTextMobile.setError(Validation.getErrorMessage());
            return false;
        }
        if (!editTextWebsite.getText().toString().equals("") && !Validation.validateWebsite(getActivity(), editTextWebsite.getText().toString()).isValid()) {
            editTextWebsite.setError(Validation.getErrorMessage());
            return false;
        }
        if (!editTextEmail.getText().toString().equals("") && !Validation.validateEmail(getActivity(), editTextEmail.getText().toString()).isValid()) {
            editTextEmail.setError(Validation.getErrorMessage());
            return false;
        }


        if (!isEditing && workTime == null) {
            new DialogMessage(getActivity(), getString(R.string.work_time_do)).show();
            return false;
        }

        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        myApplication.business.phone = editTextPhone.getText().toString();
        myApplication.business.mobile = editTextMobile.getText().toString();
        myApplication.business.webSite = editTextWebsite.getText().toString();
        myApplication.business.email = editTextEmail.getText().toString();
        if (workTime != null)
            myApplication.business.workTime = workTime;

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Params.ACTION_WORK_TIME && resultCode == Activity.RESULT_OK) {
            workTime = ((MyApplication) getActivity().getApplication()).workTime;
            buttonWorkTime.setBackgroundResource(R.drawable.selector_button_register);
            buttonWorkTime.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_white_24dp), null);
        }
    }
}
