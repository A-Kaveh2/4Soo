package ir.rasen.charsoo;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.Category;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.SubCategory;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.helper.ImageHelper;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TextProcessor;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.helper.WebservicesHandler;
import ir.rasen.charsoo.interfaces.IGetCallForTakePicture;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.webservices.DownloadImages;
import ir.rasen.charsoo.webservices.business.GetBusinessGategories;
import ir.rasen.charsoo.webservices.business.GetBusinessSubcategories;

public class FragmentRegisterBusinessBaseInfo extends Fragment implements IWebserviceResponse {

    private ProgressDialog progressDialog;
    private EditTextFont editTextName, editTextIdentifier, editTextDescription,editTextHashtags;
    private Spinner spinnerCategory, spinnerSubCategory;
    private String name, identifier, description;
    private int categoryId, subCategoryId;
    private MyApplication myApplication;
    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;
    private ImageView imageViewPicture;
    private  IGetCallForTakePicture iGetCallForTakePicture;
    private boolean isEditing;
    private Business editingBusiness;


    public void setPicture(Bitmap bitmap){
        imageViewPicture.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap,20));
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
        View view = inflater.inflate(R.layout.fragment_register_business_base_info,
                container, false);



        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        editTextDescription = (EditTextFont) view.findViewById(R.id.edt_description);
        editTextHashtags = (EditTextFont) view.findViewById(R.id.edt_hashtags);
        editTextIdentifier = (EditTextFont) view.findViewById(R.id.edt_identifier);
        editTextName = (EditTextFont) view.findViewById(R.id.edt_name);
        spinnerCategory = (Spinner) view.findViewById(R.id.spinner_category);
        spinnerSubCategory = (Spinner) view.findViewById(R.id.spinner_sub_category);
        imageViewPicture = (ImageView)view.findViewById(R.id.imageView_picture);
        imageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iGetCallForTakePicture.notifyCallForTakePicture();
            }
        });

        spinnerCategory.setEnabled(false);
        spinnerSubCategory.setEnabled(false);

        //process hashtags
        editTextHashtags.addTextChangedListener(new TextWatcher() {
            String oldText;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                oldText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().equals(oldText))
                    return;
                TextProcessor.processEdtHashtags(editTextHashtags.getText().toString(), editTextHashtags, getActivity());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        myApplication = (MyApplication) getActivity().getApplication();
        myApplication.setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_CATEGORY);

        progressDialog.show();
        new GetBusinessGategories(getActivity(), FragmentRegisterBusinessBaseInfo.this).execute();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                progressDialog.show();
                myApplication.setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);

                progressDialog.show();
                new GetBusinessSubcategories(getActivity(), categories.get(position).id
                        , FragmentRegisterBusinessBaseInfo.this).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //if the user is editing the business
        isEditing = getArguments().getBoolean(Params.IS_EDITTING);
        if(isEditing){
            editingBusiness = ((MyApplication)getActivity().getApplication()).business;
            DownloadImages downloadImages = new DownloadImages(getActivity());
            downloadImages.download(editingBusiness.profilePictureId,Image_M.MEDIUM,imageViewPicture,true);
            editTextName.setText(editingBusiness.name);
            editTextIdentifier.setText(editingBusiness.businessIdentifier);
            editTextIdentifier.setEnabled(false);
            editTextDescription.setText(editingBusiness.description);
            String hashtags = "";
            for (String hashtag: editingBusiness.hashtagList){
                hashtags += "#"+hashtag;
            }

            editTextHashtags.setText(hashtags+" ");
        }
        return view;
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof ArrayList) {
            //get business categories
            if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.GET_BUSINESS_CATEGORY) {
                categories = (ArrayList<Category>) result;
                categories.add(0,new Category(0,getString(R.string.category)));

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.layout_spinner_back, Category.getCategoryListString(categories));
                dataAdapter.setDropDownViewResource(R.layout.layout_spinner_back_drop_down);
                spinnerCategory.setAdapter(dataAdapter);

                //get subcategory for first category
                myApplication.setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);
                spinnerCategory.setEnabled(true);

                if(isEditing){
                    for (int i = 0;i<categories.size();i++)
                        if (editingBusiness.category.equals(categories.get(i).name)){
                            //this is the editingBusiness' category
                            spinnerCategory.setSelection(i);
                            break;
                        }
                }
            } else if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY) {
                subCategories = (ArrayList<SubCategory>) result;
                subCategories.add(0,new SubCategory(0,getString(R.string.subcategory)));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.layout_spinner_back, SubCategory.getSubCategoryListString(subCategories));
                dataAdapter.setDropDownViewResource(R.layout.layout_spinner_back_drop_down);
                spinnerSubCategory.setAdapter(dataAdapter);
                spinnerSubCategory.setEnabled(true);

                if(isEditing){
                    for (int i = 0;i<subCategories.size();i++)
                        if (editingBusiness.subcategory.equals(subCategories.get(i).name)){
                            //this is the editingBusiness' category
                            spinnerSubCategory.setSelection(i);
                            break;
                        }
                }
            }
        }
    }


    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode)).show();
    }

    public boolean isVerified() {

        if(!Validation.validateName(getActivity(),editTextName.getText().toString()).isValid()){
            editTextName.setError(Validation.getErrorMessage());
            return false;
        }
        if(!Validation.validateIdentifier(getActivity(),editTextIdentifier.getText().toString()).isValid()){
            editTextIdentifier.setError(Validation.getErrorMessage());
            return false;
        }
        if(spinnerCategory.getSelectedItemPosition() == 0 || spinnerSubCategory.getSelectedItemPosition() == 0){
            new DialogMessage(getActivity(), getString(R.string.err_choose_spinner_item)).show();
            return false;
        }

        //initial MyApplication.business to pass the data
        ((MyApplication)getActivity().getApplication()).business.name = editTextName.getText().toString();
        ((MyApplication)getActivity().getApplication()).business.businessIdentifier = editTextIdentifier.getText().toString();
        ((MyApplication)getActivity().getApplication()).business.categoryID = categories.get(spinnerCategory.getSelectedItemPosition()).id;
        ((MyApplication)getActivity().getApplication()).business.subCategoryID = categories.get(spinnerSubCategory.getSelectedItemPosition()).id;
        ((MyApplication)getActivity().getApplication()).business.description = editTextDescription.getText().toString();
        ((MyApplication)getActivity().getApplication()).business.hashtagList = TextProcessor.getHashtags(editTextHashtags.getText().toString());

        return true;
    }
}
