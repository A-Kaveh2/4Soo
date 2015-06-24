package ir.rasen.charsoo.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.imageviews.ImageViewCircle;

public class FragmentUserSignUpeEmailID extends Fragment implements IWebserviceResponse {
    public static final String TAG="FragmentUserSignUpeEmailID";

    EditTextFont editText1, editText2,editText3;
    ImageViewCircle imageViewUserPicture;
    String filePath,userPictureString;
    private WaitDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_sign_up_email_id,
                container, false);

        progressDialog = new WaitDialog(getActivity());


        return view;
    }




    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();


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
