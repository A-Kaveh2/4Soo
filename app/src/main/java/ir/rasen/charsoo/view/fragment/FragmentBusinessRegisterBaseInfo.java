package ir.rasen.charsoo.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

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
import ir.rasen.charsoo.model.business.GetBusinessSubcategories;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.dialog.PopupCategories;
import ir.rasen.charsoo.view.dialog.PopupSubCategories;
import ir.rasen.charsoo.view.interface_m.IGetCallForTakePicture;
import ir.rasen.charsoo.view.interface_m.ISelectCategory;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;

public class FragmentBusinessRegisterBaseInfo extends Fragment implements IWebserviceResponse,ISelectCategory {
    public static final String TAG="FragmentBusinessRegisterBaseInfo";

    private ProgressDialog progressDialog;
    private EditTextFont editTextName, editTextIdentifier, editTextDescription, editTextHashtags;
    TextViewFont textViewCategories,textViewSubcategories;
    private MyApplication myApplication;
    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;
    private ImageView imageViewPicture;
    private IGetCallForTakePicture iGetCallForTakePicture;
    private boolean isEditing;
    private Business editingBusiness;
    int selectedCategoryPosition, selectedSubCategoryPosition;
    ISelectCategory iSelectCategory;
    boolean isSubcategorySelected = false;

    public void setPicture(Bitmap bitmap) {
        imageViewPicture.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 20));
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
        iSelectCategory = this;
        isEditing = getArguments().getBoolean(Params.IS_EDITTING);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        editTextDescription = (EditTextFont) view.findViewById(R.id.edt_description);
        editTextHashtags = (EditTextFont) view.findViewById(R.id.edt_hashtags);
        editTextIdentifier = (EditTextFont) view.findViewById(R.id.edt_identifier);
        editTextName = (EditTextFont) view.findViewById(R.id.edt_name);

        textViewCategories = (TextViewFont) view.findViewById(R.id.textView_category);
        textViewSubcategories = (TextViewFont) view.findViewById(R.id.textView_sub_category);
        textViewCategories.setEnabled(false);
        textViewSubcategories.setEnabled(false);

        /*textViewCategories.setTextSize(editTextIdentifier.getTextSize()-10);
        textViewSubcategories.setTextSize(editTextIdentifier.getTextSize()-10);
*/
        imageViewPicture = (ImageView) view.findViewById(R.id.imageView_picture);
        imageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iGetCallForTakePicture.notifyCallForTakePicture();
            }
        });


        textViewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing)
                    new PopupCategories(getActivity(), categories, selectedCategoryPosition, iSelectCategory).show();
                else
                    new PopupCategories(getActivity(), categories, -1, iSelectCategory).show();
            }
        });
        textViewSubcategories.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isEditing)
                    new PopupSubCategories(getActivity(), subCategories, selectedSubCategoryPosition, iSelectCategory).show();
                else
                    new PopupSubCategories(getActivity(), subCategories, -1, iSelectCategory).show();
                return false;
            }
        });

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
        new GetBusinessGategories(getActivity(), FragmentBusinessRegisterBaseInfo.this).execute();

        Business b = ((MyApplication) getActivity().getApplication()).business;

        //if the user is editing the business
        if (isEditing) {
            editingBusiness = ((MyApplication) getActivity().getApplication()).business;
            SimpleLoader simpleLoader = new SimpleLoader(getActivity());
            simpleLoader.loadImage(editingBusiness.profilePictureId, Image_M.MEDIUM, Image_M.ImageType.BUSINESS, imageViewPicture);
            editTextName.setText(editingBusiness.name);
            editTextIdentifier.setText(editingBusiness.businessIdentifier);
            editTextIdentifier.setEnabled(false);
            editTextDescription.setText(editingBusiness.description);
            String hashtags = "";
            for (String hashtag : editingBusiness.hashtagList) {
                hashtags += "#" + hashtag;
            }

            editTextHashtags.setText(hashtags + " ");
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
                textViewCategories.setEnabled(true);
                editTextIdentifier.requestFocus();

                myApplication.setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);
                if (isEditing) {
                    for (int i = 0; i < categories.size(); i++)
                        if (editingBusiness.category.equals(categories.get(i).name)) {
                            //this is the editingBusiness' category
                            selectedCategoryPosition = i;
                            textViewCategories.setText(categories.get(i).name);
                            notifySelectCategory(i);
                            break;
                        }

                }
            } else if (myApplication.getCurrentWebservice() == WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY) {
                subCategories = (ArrayList<SubCategory>) result;
                //subCategories.add(0, new SubCategory(0, getString(R.string.subcategory)));
                textViewSubcategories.setEnabled(true);
                editTextIdentifier.requestFocus();
                if (isEditing) {
                    for (int i = 0; i < subCategories.size(); i++)
                        if (editingBusiness.subcategory.equals(subCategories.get(i).name)) {
                            //this is the editingBusiness' category
                            selectedSubCategoryPosition = i;
                            textViewSubcategories.setText(subCategories.get(i).name);
                            isSubcategorySelected = true;
                            break;
                        }
                }
            }
        }
    }


    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode,callerStringID+">"+TAG)).show();
    }

    public boolean isVerified() {

        if (!Validation.validateName(getActivity(), editTextName.getText().toString()).isValid()) {
            editTextName.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateIdentifier(getActivity(), editTextIdentifier.getText().toString()).isValid()) {
            editTextIdentifier.setError(Validation.getErrorMessage());
            return false;
        }
        if (!isSubcategorySelected) {
            new DialogMessage(getActivity(), getString(R.string.err_choose_spinner_item)).show();
            return false;
        }

        //initial MyApplication.business to pass the data
        ((MyApplication) getActivity().getApplication()).business.name = editTextName.getText().toString();
        ((MyApplication) getActivity().getApplication()).business.businessIdentifier = editTextIdentifier.getText().toString();
        ((MyApplication) getActivity().getApplication()).business.categoryID = categories.get(selectedCategoryPosition).id;
        ((MyApplication) getActivity().getApplication()).business.subCategoryID = subCategories.get(selectedSubCategoryPosition).id;
        ((MyApplication) getActivity().getApplication()).business.description = editTextDescription.getText().toString();
        ((MyApplication) getActivity().getApplication()).business.hashtagList = TextProcessor.getHashtags(editTextHashtags.getText().toString());

        return true;
    }

    @Override
    public void notifySelectCategory(int categoryListPosition) {
        //if user select another category, he should select a subcategory either.
        if(categoryListPosition != selectedCategoryPosition) {
            isSubcategorySelected = false;
            textViewSubcategories.setEnabled(false);
            textViewSubcategories.setText(getString(R.string.subcategory));
        }

        this.selectedCategoryPosition = categoryListPosition;
        textViewCategories.setText(categories.get(categoryListPosition).name);
        progressDialog.show();
        ((MyApplication) getActivity().getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);
        new GetBusinessSubcategories(getActivity(), categories.get(categoryListPosition).id, FragmentBusinessRegisterBaseInfo.this).execute();
    }

    @Override
    public void notifySelectSubcategory(int subcategoryListPosition) {
        this.selectedSubCategoryPosition = subcategoryListPosition;
        textViewSubcategories.setText(subCategories.get(subcategoryListPosition).name);
        isSubcategorySelected = true;
    }
}
