package ir.rasen.charsoo.view.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.ui.ImageViewCircle;

public class FragmentUserSignUpeEmailID extends Fragment implements IWebserviceResponse {

    EditTextFont editText1, editText2,editText3;
    ImageViewCircle imageViewUserPicture;
    String filePath,userPictureString;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_sign_up_email_id,
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
