package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TextProcessor;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;
import ir.rasen.charsoo.controller.object.Category;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.SubCategory;
import ir.rasen.charsoo.model.business.GetBusinessGategories;
import ir.rasen.charsoo.model.business.GetBusinessSubcategories;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.ISelectCategory;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;

/**
 * Created by hossein-pc on 6/14/2015.
 */
public class FragmentBusinessRegisterPageTwo extends Fragment implements IWebserviceResponse,ISelectCategory {
    public static final String TAG="FragmentBusinessRegisterPageTwo";



    public static final String LOADING_CATEGORY_LIST="Loading_Category_List";
    public static final String LOADING_SUBCATEGORY_LIST = "Loading_Subcategory_List";


    private ProgressDialog progressDialog;
    private EditTextFont editTextDescription, editTextHashtags;
    private Spinner spinnerCategory,spinnerSubcategory;
//    private SpinnerAdapter spinnerAdapterCategory,spinnerAdapterSubcategory;

    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;
    ISelectCategory iSelectCategory;
    boolean isSubcategorySelected = false;
    Hashtable<String,Category> categoryHashtable;
    Hashtable<String,SubCategory> subcategoryHashtable;
    public Category selectedCategory;
    public SubCategory selectedSubcategory;
    public String inputDescription;

    int selectedCategoryPosition;
    int selectedSubcategoryPosition;

    boolean isCategoriesLoaded=false;

    boolean doRefreshSubcategoryList=true;
    String currentlyLoading;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            iGetCallForTakePicture = (IGetCallForTakePicture) activity;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_register_page_two,
                container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        spinnerCategory = (Spinner) view.findViewById(R.id.spinner_Category);
        spinnerSubcategory=(Spinner) view.findViewById(R.id.spinner_Subcategory);

        if (selectedCategory==null){
            if (categoryHashtable==null){
                categoryHashtable=new Hashtable<>();
                spinnerCategory.setEnabled(false);
                progressDialog.show();
                new GetBusinessGategories(getActivity(), FragmentBusinessRegisterPageTwo.this).execute();
            }
            else{
                setCategorySpinnerAdapter(categories,getActivity());
            }

        }
        else{
            doRefreshSubcategoryList=false;
            final int i=selectedCategoryPosition;
            setCategorySpinnerAdapter(categories, getActivity());
            spinnerCategory.post(new Runnable() {
                @Override
                public void run() {
                    doRefreshSubcategoryList=false;
                    spinnerCategory.setSelection(i);
                }
            });
        }



        if (selectedSubcategory==null){
            if (subcategoryHashtable==null){
                subcategoryHashtable=new Hashtable<>();
                setSubcategorySpinnerAdapter(new ArrayList<SubCategory>(),getActivity());
            }
        }
        else {
            final int i=selectedSubcategoryPosition;
            setSubcategorySpinnerAdapter(subCategories, getActivity());
            spinnerSubcategory.post(new Runnable() {
                @Override
                public void run() {
                    spinnerSubcategory.setSelection(i);
                }
            });
        }

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategoryPosition=i;
                selectedCategory = categoryHashtable.get(adapterView.getItemAtPosition(i).toString());
                if (doRefreshSubcategoryList) {
                    spinnerSubcategory.setEnabled(false);
                    try {
                        if (isCategoriesLoaded) {
                            currentlyLoading = LOADING_SUBCATEGORY_LIST;
                            new GetBusinessSubcategories(getActivity(), selectedCategory.id, FragmentBusinessRegisterPageTwo.this).execute();
                            progressDialog.show();
                            ((ArrayAdapter<String>) spinnerSubcategory.getAdapter()).clear();
                            spinnerSubcategory.setAdapter(null);
                        }
                    } catch (Exception e) {

                    }
                }else{
                    doRefreshSubcategoryList=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    selectedSubcategoryPosition=i;
                    selectedSubcategory = subcategoryHashtable.get(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        iSelectCategory = this;



        editTextDescription = (EditTextFont) view.findViewById(R.id.edt_description);
        editTextHashtags = (EditTextFont) view.findViewById(R.id.edt_hashtags);

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

//        myApplication = (MyApplication) getActivity().getApplication();
//        myApplication.setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_CATEGORY);



        return view;
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof ArrayList) {
            //get business categories
            if (!isCategoriesLoaded){//(currentlyLoading.equals(LOADING_CATEGORY_LIST)) {
                categories=(ArrayList<Category>) result;
                setCategorySpinnerAdapter(categories,getActivity());
                spinnerCategory.setEnabled(true);
                isCategoriesLoaded=true;
//                getSubcategoryList();
            } else if (currentlyLoading.equals(LOADING_SUBCATEGORY_LIST)) {
                subCategories = (ArrayList<SubCategory>) result;
                setSubcategorySpinnerAdapter(subCategories,getActivity());
                spinnerSubcategory.setEnabled(true);
            }
        }
    }


    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode, callerStringID + ">" + TAG)).show();
    }

    public boolean isVerified() {

        if (spinnerCategory.getSelectedItemPosition()==0){
            new DialogMessage(getActivity(), getActivity().getString(R.string.txt_PleaseSelectCategory)).show();
            return false;
        }

        if (spinnerSubcategory.getSelectedItemPosition()==0){
            new DialogMessage(getActivity(), getActivity().getString(R.string.txt_PleaseSelectSubcategory)).show();
            return false;
        }

        return true;
    }

    @Override
    public void notifySelectCategory(int categoryListPosition) {
        //if user select another category, he should select a subcategory either.
        /*if(categoryListPosition != selectedCategoryPosition) {
            isSubcategorySelected = false;
            textViewSubcategories.setEnabled(false);
            textViewSubcategories.setText(getString(R.string.subcategory));
        }*/

        progressDialog.show();
        ((MyApplication) getActivity().getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_BUSINESS_SUB_CATEGORY);
//        new GetBusinessSubcategories(getActivity(), categories.get(categoryListPosition).id, FragmentBusinessEditBaseInfo.this).execute();
    }

    @Override
    public void notifySelectSubcategory(int subcategoryListPosition) {
//        this.selectedSubCategoryPosition = subcategoryListPosition;
//        /*textViewSubcategories.setText(subCategories.get(subcategoryListPosition).name);
//*/        isSubcategorySelected = true;
    }


    public ArrayList<String> getHashtagList(){
        return TextProcessor.getHashtags(editTextHashtags.getText().toString());
    }

    public String getInputDescription(){
        return editTextDescription.getText().toString();
    }

    private void setCategorySpinnerAdapter(ArrayList<Category> cats,Context context){
        categoryHashtable.clear();
        for (Category ct: cats){
            categoryHashtable.put(ct.name,ct);
        }


        List<String> categoriesList = new ArrayList<>();
        categoriesList.add(context.getString(R.string.txt_Category));
        categoriesList.addAll(new ArrayList<>(categoryHashtable.keySet()));
        String[] items = new String[categoriesList.size()];
        items = categoriesList.toArray(items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);

        spinnerCategory.setAdapter(adapter);
    }

    private void setSubcategorySpinnerAdapter(ArrayList<SubCategory> subCats,Context context){
        subcategoryHashtable.clear();
        for (SubCategory subct: subCats){
            SubCategory ss=subct;
            subcategoryHashtable.put(subct.name, subct);
        }


        List<String> subCategoriesList = new ArrayList<>();
        subCategoriesList.add(context.getString(R.string.txt_Subcategory));
        subCategoriesList.addAll(new ArrayList<String>(subcategoryHashtable.keySet()));
        String[] items = new String[subCategoriesList.size()];
        items = subCategoriesList.toArray(items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerSubcategory.setAdapter(adapter);
    }

    public void setSpinners(Context c){

        /*setCategorySpinnerAdapter(categories, c);
        setSubcategorySpinnerAdapter(subCategories,c);
        spinnerCategory.setSelection(selectedCategoryPosition);
        spinnerSubcategory.setSelection(selectedSubcategoryPosition);*/
    }
}
