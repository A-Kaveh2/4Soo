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
public class FragmentUserRegisterPageTwo extends Fragment {

    public static final String TAG="SecondPage";

    EditTextFont editTextEmail, editTextPassword,editTextPhoneNumber;
    ProgressDialog progressDialog;

//        TextViewFont persianLicenseTextView,englishLicenseTextView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register_page_two,
                container, false);



        editTextEmail = (EditTextFont) view.findViewById(R.id.editText_email);
        editTextPassword = (EditTextFont) view.findViewById(R.id.editText_password);
        editTextPhoneNumber = (EditTextFont) view.findViewById(R.id.editText_PhoneNumber);


        editTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                boolean handeled = true;
                if (actionID == EditorInfo.IME_ACTION_NEXT) {
                    if (editTextPassword.getText().toString().isEmpty()) {
                        editTextPassword.requestFocus();
                    } else if (editTextPhoneNumber.getText().toString().isEmpty()) {
                        editTextPhoneNumber.requestFocus();
                    } else {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);
                        ((ActivityUserRegister) getActivity()).onDoneButtonPressed(TAG);
                    }
                }
                return handeled;
            }
        });

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                boolean handeled = true;
                if (actionID == EditorInfo.IME_ACTION_NEXT) {
                    if (editTextPhoneNumber.getText().toString().isEmpty()) {
                        editTextPhoneNumber.requestFocus();
                    } else if (editTextEmail.getText().toString().isEmpty()) {
                        editTextEmail.requestFocus();
                    } else {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);
                        ((ActivityUserRegister) getActivity()).onDoneButtonPressed(TAG);
                    }
                }
                return handeled;
            }
        });

        editTextPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                boolean handeled = true;
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editTextPhoneNumber.getWindowToken(), 0);
                    ((ActivityUserRegister) getActivity()).onDoneButtonPressed(TAG);
                }
                return handeled;
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editTextEmail.setHint("");
                } else {
                    editTextEmail.setHint(getActivity().getString(R.string.email_field_hint));
//                    if (editTextName.getText().toString().equals("")) {
//                        editTextUserIdentifier.setHint(getActivity().getString(R.string.identifier_field_hint));
//                    }
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editTextPassword.setHint("");
                } else {
                    editTextPassword.setHint(getActivity().getString(R.string.password_field_hint));
//                    if (editTextName.getText().toString().equals("")) {
//                        editTextUserIdentifier.setHint(getActivity().getString(R.string.identifier_field_hint));
//                    }
                }
            }
        });

        editTextPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editTextPhoneNumber.setHint("");
                } else {
                    editTextPhoneNumber.setHint(getActivity().getString(R.string.mobile));
//                    if (editTextName.getText().toString().equals("")) {
//                        editTextUserIdentifier.setHint(getActivity().getString(R.string.identifier_field_hint));
//                    }
                }
            }
        });

                /*String ss=getActivity().getString(R.string.licenseAgreementEnglish);
                SpannableString licenseInEnglish = new SpannableString(ss);
                ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View textView) {
//                                navigate to license in persian
                        }
                };
                int leftIndex=ss.indexOf("Privacy");
                int rightIndex=ss.length();
                licenseInEnglish.setSpan(clickableSpan, leftIndex, rightIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                licenseInEnglish.setSpan(new StyleSpan(Typeface.NORMAL), 0, ss.length(), 0);
                licenseInEnglish.setSpan(new StyleSpan(Typeface.BOLD), leftIndex, rightIndex, 0);
                licenseInEnglish.setSpan(new StyleSpan(Typeface.ITALIC), leftIndex, rightIndex, 0);
                englishLicenseTextView=(TextViewFont) view.findViewById(R.id.licensesInEnglish);
                englishLicenseTextView.setText(licenseInEnglish);
                englishLicenseTextView.setMovementMethod(LinkMovementMethod.getInstance());*/
//                persianLicenseTextView=(TextViewFont) view.findViewById(R.id.licensesInPersian);
//                persianLicenseTextView.setText(licenseInEnglish);
//                persianLicenseTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return view;

    }

    private boolean checkInputData(){

        if (!Validation.validateEmail(getActivity(), editTextEmail.getText().toString()).isValid()) {
            editTextEmail.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validatePassword(getActivity(), editTextPassword.getText().toString()).isValid()) {
            editTextPassword.setError(Validation.getErrorMessage());
            return false;
        }
        if (!editTextPhoneNumber.getText().toString().equals("") && !Validation.validateMobile(getActivity(), editTextPhoneNumber.getText().toString()).isValid()) {
            editTextPhoneNumber.setError(Validation.getErrorMessage());
            return false;
        }
        return true;
    }

    public String[] getInputData(){

        if (checkInputData()){
            String[] s=new String[3];
            s[0]=editTextEmail.getText().toString();
            s[1]=editTextPassword.getText().toString();
            s[2]=editTextPhoneNumber.getText().toString();
            return s;
        }
        else
            return null;
    }

}
