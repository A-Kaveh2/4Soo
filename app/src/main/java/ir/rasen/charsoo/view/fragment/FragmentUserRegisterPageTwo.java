package ir.rasen.charsoo.view.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.view.activity.ActivityUserRegister;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterPageTwo extends Fragment {

        public static final String TAG="SecondPage";

        EditTextFont editTextEmail, editTextPassword,editTextPhoneNumber;
        ProgressDialog progressDialog;

        TextViewFont persianLicenseTextView,englishLicenseTextView;
//        ActivityRegisterListener listener;

//
//            public void setListener(ActivityRegisterListener lsr){
//                    listener=lsr;
//                    }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_user_register_page_two,
                    container, false);



//            (view.findViewById(R.id.btn_register)).setOnClickListener(this);
//            (view.findViewById(R.id.btn_Cancel)).setOnClickListener(this);


            editTextEmail = (EditTextFont) view.findViewById(R.id.editText_email);
            editTextPassword = (EditTextFont) view.findViewById(R.id.editText_password);
            editTextPhoneNumber = (EditTextFont) view.findViewById(R.id.editText_PhoneNumber);
                editTextPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                                boolean handeled = false;
                                if (actionID == EditorInfo.IME_ACTION_DONE) {
                                        ((ActivityUserRegister)getActivity()).onDoneButtonPressed(TAG);
                                }
                                return handeled;
                        }
                });

                String ss=getActivity().getString(R.string.licenseAgreementEnglish);
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
                englishLicenseTextView.setMovementMethod(LinkMovementMethod.getInstance());
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
