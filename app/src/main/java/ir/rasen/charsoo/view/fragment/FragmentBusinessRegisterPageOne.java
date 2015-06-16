package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ImageHelper;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TextProcessor;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.Category;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.SubCategory;
import ir.rasen.charsoo.model.business.GetBusinessGategories;
import ir.rasen.charsoo.model.business.GetBusinessStrIdAvailability;
import ir.rasen.charsoo.model.business.GetBusinessSubcategories;
import ir.rasen.charsoo.view.activity.ActivityBusinessRegister;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IGetCallForTakePicture;
import ir.rasen.charsoo.view.interface_m.ISelectCategory;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;

/**
 * Created by hossein-pc on 6/14/2015.
 */
public class FragmentBusinessRegisterPageOne extends Fragment implements IWebserviceResponse {
    public static final String TAG="FragmentBusinessRegisterPageOne";

    private ProgressDialog progressDialog;
    private EditTextFont editTextName, editTextIdentifier;


    private ImageView imageViewPicture;
    private IGetCallForTakePicture iGetCallForTakePicture;
    Bitmap selectedProfilePicture;


    public void setPicture(Bitmap bitmap) {
        selectedProfilePicture=bitmap.copy(Bitmap.Config.ARGB_8888,false);
        imageViewPicture.setImageBitmap(ImageHelper.getRoundedCornerBitmap(selectedProfilePicture, 20));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            iGetCallForTakePicture = (IGetCallForTakePicture) activity;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_register_page_one,
                container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));


        editTextIdentifier = (EditTextFont) view.findViewById(R.id.editText_identifier);
        editTextName = (EditTextFont) view.findViewById(R.id.editText_Name);



        /*textViewCategories.setTextSize(editTextIdentifier.getTextSize()-10);
        textViewSubcategories.setTextSize(editTextIdentifier.getTextSize()-10);
*/
        imageViewPicture = (ImageView) view.findViewById(R.id.imageView_picture);
        if (selectedProfilePicture!=null)
            imageViewPicture.setImageBitmap(ImageHelper.getRoundedCornerBitmap(selectedProfilePicture, 20));
        imageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iGetCallForTakePicture.notifyCallForTakePicture();
            }
        });


        return view;
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();

    }


    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode, callerStringID + ">" + TAG)).show();
    }

    public boolean checkInputData() {

        if ((!editTextName.getText().toString().isEmpty()) && (!Validation.validateName(getActivity(), editTextName.getText().toString()).isValid())) {
            editTextName.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateIdentifier(getActivity(), editTextIdentifier.getText().toString()).isValid()) {
            editTextIdentifier.setError(Validation.getErrorMessage());
            return false;
        }
        new GetBusinessStrIdAvailability(getActivity(),editTextIdentifier.getText().toString(),(ActivityBusinessRegister)getActivity()).execute();

        return true;
    }



    public Hashtable<String,String> getInputData(){
        if (checkInputData()) {
            Hashtable<String,String> result= new Hashtable<>();
            result.put(Params.FULL_NAME, editTextName.getText().toString());
            result.put(Params.STRING_IDENTIFIER,editTextIdentifier.getText().toString());
            return result;
        }
        else
            return null;
    }


    public void setErrorStringIdIsNotAvailable(){
        editTextName.setError(getActivity().getString(R.string.txt_StringIdentifierIsNotAvailable));
    }
}
